
import java.util.Random;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    private static final int NUM_USERS = 2000;
    private static final int NUM_MERCHANTS = 500;
    private static final String[] MERCHANT_NAMES = Util.generateMerchantNames(NUM_MERCHANTS);

    // check each transaction
    public static boolean isValid(String[] trans) {
        if (trans == null || trans.length != 4) {
            return false;
        }
        // use id cannot be null
        if (Integer.valueOf(trans[0]) == null) {
            return false;
        }
        // timestamp cannot be null or negative
        if (Long.valueOf(trans[1]) == null || Long.valueOf(trans[1]) < 0) {
            return false;
        }
        // merchant cannot be null
        if (trans[2] == null || trans[2].trim().length() <= 0) {
            return false;
        }

        if (Double.valueOf(trans[3]) == null) {
            return false;
        }
        return true;
    }

    public static List<String[]> generateData(int numEntries) {
        List<String[]> dataList = new ArrayList<>();

        for (int i = 0; i < numEntries; i++) {
            int userId = ThreadLocalRandom.current().nextInt(0, NUM_USERS + 1);
            long timestamp = generateRandomTimestamp();
            String merchant = MERCHANT_NAMES[ThreadLocalRandom.current().nextInt(0, NUM_MERCHANTS)];
            double amount = generateRandomAmount();
            String[] entry = { String.valueOf(userId), String.valueOf(timestamp), merchant, String.valueOf(amount) };
            dataList.add(entry);
        }

        return dataList;
    }

    public static long generateRandomTimestamp() {
        long currentTimestamp = System.currentTimeMillis();
        long sevenDaysAgo = currentTimestamp - (7 * 24 * 60 * 60 * 1000); // 7 days ago in milliseconds
        return ThreadLocalRandom.current().nextLong(sevenDaysAgo, currentTimestamp);
    }

    // make sure 80% amount less than 500, 90% less than 1000, 98% less than 2000
    public static double generateRandomAmount() {
        double rand = Math.random();
        double amount;
        if (rand < 0.8) {
            amount = Math.round(ThreadLocalRandom.current().nextDouble(1, 500) * 100.0) / 100.0; // 80% within 500
        } else if (rand < 0.9) {
            amount = Math.round(ThreadLocalRandom.current().nextDouble(500, 1000) * 100.0) / 100.0; // 10% within 1000
        } else {
            amount = Math.round(ThreadLocalRandom.current().nextDouble(1000, 2000) * 100.0) / 100.0; // 8% within 2000
        }
        // Round 5% of amounts
        if (Math.random() < 0.05) {
            amount = Math.round(amount);
        }
        return amount;
    }

    private static String[] generateMerchantNames(int numMerchants) {
        String[] names = new String[numMerchants];
        for (int i = 0; i < numMerchants; i++) {
            names[i] = generateRandomString();
        }
        return names;
    }
    private static String generateRandomString() {
        // letter 'a'
        int leftLimit = 97;
        // letter 'z'
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
