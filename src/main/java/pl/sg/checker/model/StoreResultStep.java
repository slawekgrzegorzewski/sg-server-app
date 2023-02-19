package pl.sg.checker.model;

import jakarta.persistence.Entity;

@Entity
public class StoreResultStep extends CheckerStep<StoreResultStep> {
    private String getResultFromVariable;

    public StoreResultStep() {
    }

    public String getGetResultFromVariable() {
        return getResultFromVariable;
    }

    public StoreResultStep setGetResultFromVariable(String getResultFromVariable) {
        this.getResultFromVariable = getResultFromVariable;
        return this;
    }
}
