package Model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        List<List<String>> rawData = List.of(
            List.of("TID", "leite", "café", "cerveja", "pão", "manteiga", "arroz", "feijão"),
            List.of("1", "não", "sim", "não", "sim", "sim", "não", "não"),
            List.of("2", "sim", "não", "sim", "sim", "sim", "não", "não"),
            List.of("3", "não", "sim", "não", "sim", "sim", "não", "não"),
            List.of("4", "sim", "sim", "não", "sim", "não", "não", "não"),
            List.of("5", "não", "não", "sim", "não", "não", "não", "não"),
            List.of("6", "não", "não", "não", "sim", "não", "não", "não"),
            List.of("7", "não", "não", "não", "não", "não", "sim", "não"),
            List.of("8", "não", "não", "não", "não", "não", "sim", "sim"),
            List.of("9", "não", "não", "não", "não", "não", "sim", "sim"),
            List.of("10", "não", "não", "não", "não", "não", "sim", "não")
        );

        TransactionRepository repository = new TransactionRepository(rawData);
        List<Transaction> transactions = repository.getTransactions();

        Apriori apriori = new Apriori(transactions, 0.3);

        Map<Set<String>, Double> frequentItemsets = apriori.findFrequentItemsetsWithSupport();
        System.out.println("Conjuntos de itens frequentes com suporte:");
        for (Map.Entry<Set<String>, Double> entry : frequentItemsets.entrySet()) {
            System.out.println(entry.getKey() + " => Suporte: " + entry.getValue());
        }

        List<AssociationRule> rules = apriori.generateAssociationRules();
        System.out.println("\nRegras de Associação:");
        for (AssociationRule rule : rules) {
            System.out.println(rule);
        }
    }
}
