package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionRepository {

    private final List<List<String>> rawData;

    public TransactionRepository(List<List<String>> rawData) {
        this.rawData = rawData;
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        int id = 1;
        List<String> headers = rawData.get(0);  // Cabeçalhos (ex: leite, café, cerveja, etc.)
        
        for (int i = 1; i < rawData.size(); i++) {
            List<String> row = rawData.get(i);
            Set<String> items = new HashSet<>();
            for (int j = 1; j < row.size(); j++) {
                if (row.get(j).equalsIgnoreCase("sim")) {
                    items.add(headers.get(j));
                }
            }
            transactions.add(new Transaction(id++, items));
        }
        return transactions;
    }
}