import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class User {
    private TreeMap<Long, Transaction> history;
    private int userID;
    private Set<String> whitelist;
    private Set<String> blacklist;

    public User(int userID) {
        this.userID = userID;
        history = new TreeMap<>();
        whitelist = new HashSet<>();
        blacklist = new HashSet<>();
    }

    public void insertTransaction(Transaction trans) {
        history.put(trans.getTimestamp(), trans);
    }

    public void addBlockedMerchant(String merchant) {
        whitelist.remove(merchant);
        blacklist.add(merchant);
    }

    public Set<String> getBlacklist() {
        return blacklist;
    }

    public TreeMap<Long, Transaction> getHistory() {
        return history;
    }

    public Set<String> getWhitelist() {
        return whitelist;
    }


}
