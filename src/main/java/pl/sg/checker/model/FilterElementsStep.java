package pl.sg.checker.model;

import jakarta.persistence.Entity;

@Entity
public class FilterElementsStep extends CheckerStep<FilterElementsStep> {
    private String filterExpression;
    private String elementVariableNameInExpression;
    private String elementsVariable;
    private String resultVariable;

    public FilterElementsStep() {
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public FilterElementsStep setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
        return this;
    }

    public String getElementVariableNameInExpression() {
        return elementVariableNameInExpression;
    }

    public FilterElementsStep setElementVariableNameInExpression(String elementVariableNameInExpression) {
        this.elementVariableNameInExpression = elementVariableNameInExpression;
        return this;
    }

    public String getElementsVariable() {
        return elementsVariable;
    }

    public FilterElementsStep setElementsVariable(String elementsVariable) {
        this.elementsVariable = elementsVariable;
        return this;
    }

    public String getResultVariable() {
        return resultVariable;
    }

    public FilterElementsStep setResultVariable(String resultVariable) {
        this.resultVariable = resultVariable;
        return this;
    }
}
