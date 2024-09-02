package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Apriori {

    private final List<Transaction> transactions;
    private final double minSupport;

    public Apriori(List<Transaction> transactions, double minSupport) {
        this.transactions = transactions;
        this.minSupport = minSupport;
    }

    public Map<Set<String>, Double> findFrequentItemsetsWithSupport() {
        Map<Set<String>, Double> frequentItemsets = new HashMap<>();
        Map<Set<String>, Integer> candidateItemsets = generateCandidateItemsets();
        
        while (!candidateItemsets.isEmpty()) {
            Map<Set<String>, Integer> frequentCandidates = getFrequentItemsets(candidateItemsets);
            for (Map.Entry<Set<String>, Integer> entry : frequentCandidates.entrySet()) {
                double support = entry.getValue() / (double) transactions.size();
                frequentItemsets.put(entry.getKey(), support);
            }
            candidateItemsets = generateNewCandidates(frequentCandidates.keySet());
        }
        
        return frequentItemsets;
    }

    private Map<Set<String>, Integer> generateCandidateItemsets() {
        Map<Set<String>, Integer> candidateItemsets = new HashMap<>();
        for (Transaction transaction : transactions) {
            for (String item : transaction.getItems()) {
                Set<String> itemset = Set.of(item);
                candidateItemsets.put(itemset, candidateItemsets.getOrDefault(itemset, 0) + 1);
            }
        }
        return candidateItemsets;
    }

    private Map<Set<String>, Integer> getFrequentItemsets(Map<Set<String>, Integer> candidateItemsets) {
        Map<Set<String>, Integer> frequentItemsets = new HashMap<>();
        for (Map.Entry<Set<String>, Integer> entry : candidateItemsets.entrySet()) {
            if (entry.getValue() / (double) transactions.size() >= minSupport) {
                frequentItemsets.put(entry.getKey(), entry.getValue());
            }
        }
        return frequentItemsets;
    }

    private Map<Set<String>, Integer> generateNewCandidates(Set<Set<String>> frequentItemsets) {
        Map<Set<String>, Integer> newCandidates = new HashMap<>();
        for (Set<String> itemset1 : frequentItemsets) {
            for (Set<String> itemset2 : frequentItemsets) {
                Set<String> union = new HashSet<>(itemset1);
                union.addAll(itemset2);
                if (union.size() == itemset1.size() + 1) {
                    newCandidates.put(union, 0);
                }
            }
        }

        for (Transaction transaction : transactions) {
            for (Set<String> candidate : newCandidates.keySet()) {
                if (transaction.getItems().containsAll(candidate)) {
                    newCandidates.put(candidate, newCandidates.get(candidate) + 1);
                }
            }
        }

        return newCandidates;
    }

    public List<AssociationRule> generateAssociationRules() {
        Map<Set<String>, Double> frequentItemsets = findFrequentItemsetsWithSupport();
        List<AssociationRule> rules = new ArrayList<>();

        for (Set<String> itemset : frequentItemsets.keySet()) {
            if (itemset.size() > 1) {
                rules.addAll(generateRulesForItemset(itemset, frequentItemsets));
            }
        }

        return rules;
    }

    private List<AssociationRule> generateRulesForItemset(Set<String> itemset, Map<Set<String>, Double> frequentItemsets) {
        List<AssociationRule> rules = new ArrayList<>();
        int n = itemset.size();
        for (int i = 1; i < n; i++) {
            for (Set<String> subset : getSubsets(itemset, i)) {
                Set<String> antecedent = subset;
                Set<String> consequent = new HashSet<>(itemset);
                consequent.removeAll(antecedent);

                double confidence = frequentItemsets.get(itemset) / frequentItemsets.get(antecedent);
                if (confidence >= minSupport) {
                    rules.add(new AssociationRule(antecedent, consequent, confidence));
                }
            }
        }
        return rules;
    }

    private Set<Set<String>> getSubsets(Set<String> itemset, int k) {
        Set<Set<String>> subsets = new HashSet<>();
        List<String> items = new ArrayList<>(itemset);
        generateSubsets(items, new HashSet<>(), k, 0, subsets);
        return subsets;
    }

    private void generateSubsets(List<String> items, Set<String> currentSubset, int k, int index, Set<Set<String>> subsets) {
        if (currentSubset.size() == k) {
            subsets.add(new HashSet<>(currentSubset));
            return;
        }
        for (int i = index; i < items.size(); i++) {
            currentSubset.add(items.get(i));
            generateSubsets(items, currentSubset, k, i + 1, subsets);
            currentSubset.remove(items.get(i));
        }
    }
}
