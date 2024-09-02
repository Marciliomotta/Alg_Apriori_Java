package Model;
import java.util.Set;

public class Transaction {
    private final int id;
    private final Set<String> items;

    public Transaction(int id, Set<String> items) {
        this.id = id;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public Set<String> getItems() {
        return items;
    }
}