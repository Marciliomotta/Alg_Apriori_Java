package Model;

import java.util.Set;

public class AssociationRule {
    private final Set<String> antecedent;
    private final Set<String> consequent;
    private final double confidence;

    public AssociationRule(Set<String> antecedent, Set<String> consequent, double confidence) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return antecedent + " => " + consequent + " (Confiança: " + confidence + ")";
    }
}
