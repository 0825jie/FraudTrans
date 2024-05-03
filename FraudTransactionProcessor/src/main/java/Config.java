
public class Config {
    enum Result {
        REFUSED,
        APPROVED,
        WARN
    }

    enum Risk {
        BLACKLIST,
        TIME,
        AMOUNT,
        ROUND_AMOUNT,
        SEQUENTIAL_TRANS
    }

    public static double REFUSED_THRESHOLD = 1.5;
    public static double WARN_THRESHOLD = 1.2;

    public static double[][] TimeRiskWindow = {{1, 1.0}, {7, 0.8}, {8, 0.5}, {10, 0}, {23, 0.8}};

    public static double[][] AmountRiskWindow = {{0, 0.8}, {1, 0.5}, {3, 0}, {800, 0.3}, {1000, 0.5}, {2000, 0.7}, {10000, 1}};

}
