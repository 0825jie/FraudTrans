import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private long timestamp;
    private double amount;
    private int user;
    private String merchant;
    private Config.Result result;
    Map<Config.Risk, Double> risks;

    public Transaction(String[] transaction) {
        this.user = Integer.valueOf(transaction[0]);
        this.timestamp = Long.valueOf(transaction[1]);
        this.merchant = transaction[2];
        this.amount = Double.valueOf(transaction[3]);
        this.risks = new HashMap<>();
    }

    public Transaction(int user, int timestamp, double amount, String merchant) {
        this.user = user;
        this.timestamp = timestamp;
        this.amount = amount;
        this.merchant = merchant;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMerchant() {
        return merchant;
    }

    public void insertRisk(Config.Risk risk, double score) {
        risks.put(risk, score);
    }

    public double getAmount() {
        return amount;
    }

    public int getUser() {
        return user;
    }

    public void setResult(Config.Result result) {
        this.result = result;
    }

    public String[] result() {
        String[] res = new String[5];
        res[0] = String.valueOf(user);
        res[1] = String.valueOf(timestamp);
        res[2] = String.valueOf(merchant);
        res[3] = String.valueOf(amount);
        res[4] = String.valueOf(result);
        return res;
    }


}
