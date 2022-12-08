package pl.sg.checker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EditElementsAttributeStep extends CheckerStep<EditElementsAttributeStep> {
    private String pageContentVariable;
    private String resultVariable;
    @Column(length = 1000)
    private String cssQuery;
    private String attribute;
    private String attributeVariableNameInExpression;
    private String operationExpression;

    public EditElementsAttributeStep() {
    }

    public String getPageContentVariable() {
        return pageContentVariable;
    }

    public EditElementsAttributeStep setPageContentVariable(String pageContentVariable) {
        this.pageContentVariable = pageContentVariable;
        return this;
    }

    public String getResultVariable() {
        return resultVariable;
    }

    public EditElementsAttributeStep setResultVariable(String resultVariable) {
        this.resultVariable = resultVariable;
        return this;
    }

    public String getCssQuery() {
        return cssQuery;
    }

    public EditElementsAttributeStep setCssQuery(String cssQuery) {
        this.cssQuery = cssQuery;
        return this;
    }

    public String getAttribute() {
        return attribute;
    }

    public EditElementsAttributeStep setAttribute(String attribute) {
        this.attribute = attribute;
        return this;
    }

    public String getOperationExpression() {
        return operationExpression;
    }

    public EditElementsAttributeStep setOperationExpression(String operationExpression) {
        this.operationExpression = operationExpression;
        return this;
    }

    public String getAttributeVariableNameInExpression() {
        return attributeVariableNameInExpression;
    }

    public EditElementsAttributeStep setAttributeVariableNameInExpression(String attributeVariableNameInExpression) {
        this.attributeVariableNameInExpression = attributeVariableNameInExpression;
        return this;
    }
}
