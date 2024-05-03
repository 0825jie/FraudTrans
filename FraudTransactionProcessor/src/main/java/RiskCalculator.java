import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class RiskCalculator {

    public static void calculateRisk(Transaction transaction, User user) {
        transaction.insertRisk(Config.Risk.BLACKLIST, checkBlockedMerchant(transaction.getMerchant(), user.getBlacklist()));
        transaction.insertRisk(Config.Risk.TIME, checkTimeframe(transaction.getTimestamp()));
        transaction.insertRisk(Config.Risk.AMOUNT, checkAmount(transaction.getAmount()));
        transaction.insertRisk(Config.Risk.ROUND_AMOUNT, checkRoundNumber(transaction.getAmount()));
        transaction.insertRisk(Config.Risk.SEQUENTIAL_TRANS, checkSequentialTrans(transaction.getTimestamp(), user.getHistory()));
        summarize(transaction, user);
    }

    // Rule 1: if user blocked this merchant, return 1.0 risk score
    private static double checkBlockedMerchant(String merchant, Set<String> blacklist) {
        return blacklist.contains(merchant) ? 1.0 : 0;
    }

    // Rule 2: check unusual timeframe
    // classify the activity into 5 windows, each window with a different risk score,
    // 1:00-7:00=>1.0, 7:00-8:00=>0.8, 8:00-10:00=>0.5, 10:00-23:00=>0, 23:00-1:00=>0.8
    private static double checkTimeframe(long timestamp) {
        double score = 0;
        Timestamp time = new Timestamp(timestamp);
        LocalDateTime localDateTime = time.toLocalDateTime();
        int hour = localDateTime.getHour();
        for (double[] timeframe : Config.TimeRiskWindow) {
            if (hour > timeframe[0]) {
                score = timeframe[1];
            }
        }
        return score;
    }

    // Rule 3: return risk score according to transaction amount.
    // too large or too small amount means larger risk.
    // for example: amount between $0 and $1, return score 0.8
    private static double checkAmount(double amount) {
        double score = 0;
        for (double[] amountFrame : Config.AmountRiskWindow) {
            if (amount > amountFrame[0]) {
                score = amountFrame[1];
            }
        }
        return score;
    }

    // Rule 4: check if the number is round, usually an authentic transaction to merchant should with decimal
    // return 0.5 if the amount is round
    private static double checkRoundNumber(double amount) {
        if (amount - (int)amount != 0) {
            return 0;
        }
        return 0.5;
    }

    // Rule 5: check if the user sends money to same merchant within a short time
    // return 0.5 if 2 transactions found within 30 mins, return score 1.0 if 3 or more transactions
    private static double checkSequentialTrans(long timestamp, TreeMap<Long, Transaction> history) {
        long before = timestamp - 30 * 60 * 1000;
        SortedMap<Long, Transaction> submap = history.subMap(before, timestamp);
        double score = submap.size() > 2 ? 1 : (submap.size() < 2 ? 0 : 0.5);
        for(Transaction pre : submap.values()) {
            pre.insertRisk(Config.Risk.SEQUENTIAL_TRANS, score);
        }
        return score;
    }

    // calculate risk for each transaction, compare with threshold to decide approved/refused/warned
    // *if it is in blacklist, mark it as refused
    // *if not in blacklist and in whitelist, mark it as approved
    private static void summarize(Transaction transaction, User user) {
        if(transaction.risks.get(Config.Risk.BLACKLIST) == 1) {
            transaction.setResult(Config.Result.REFUSED);
            return;
        }
        if(user.getBlacklist().contains(transaction.getMerchant())) {
            transaction.setResult(Config.Result.APPROVED);
        }
        double res = transaction.risks.values().stream().mapToDouble(f -> f.doubleValue()).sum();
        transaction.setResult(res > Config.REFUSED_THRESHOLD ?
                Config.Result.REFUSED : res < Config.WARN_THRESHOLD ? Config.Result.APPROVED : Config.Result.WARN);
    }

}
