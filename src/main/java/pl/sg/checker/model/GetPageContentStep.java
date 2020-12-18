package pl.sg.checker.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class GetPageContentStep extends CheckerStep<GetPageContentStep> {
    @Column(length = 1000)
    private String pageUrlExpression;
    private String variableName;

    public GetPageContentStep() {
    }

    public GetPageContentStep(int id, String name, String description, int order, String pageUrlExpression, String variableName) {
        super(id, name, description, order);
        this.pageUrlExpression = pageUrlExpression;
        this.variableName = variableName;
    }

    public String getPageUrlExpression() {
        return pageUrlExpression;
    }

    public GetPageContentStep setPageUrlExpression(String pageUrlExpression) {
        this.pageUrlExpression = pageUrlExpression;
        return this;
    }

    public String getVariableName() {
        return variableName;
    }

    public GetPageContentStep setVariableName(String variableName) {
        this.variableName = variableName;
        return this;
    }
}
