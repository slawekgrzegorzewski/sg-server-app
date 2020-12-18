package pl.sg.checker.model;

import javax.persistence.Entity;

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
