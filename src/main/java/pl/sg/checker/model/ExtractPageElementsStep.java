package pl.sg.checker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ExtractPageElementsStep extends CheckerStep<ExtractPageElementsStep> {
    private String pageContentVariable;
    private String resultVariable;
    @Column(length = 1000)
    private String cssQuery;

    public ExtractPageElementsStep() {
    }

    public String getPageContentVariable() {
        return pageContentVariable;
    }

    public ExtractPageElementsStep setPageContentVariable(String pageContentVariable) {
        this.pageContentVariable = pageContentVariable;
        return this;
    }

    public String getResultVariable() {
        return resultVariable;
    }

    public ExtractPageElementsStep setResultVariable(String resultVariable) {
        this.resultVariable = resultVariable;
        return this;
    }

    public String getCssQuery() {
        return cssQuery;
    }

    public ExtractPageElementsStep setCssQuery(String cssQuery) {
        this.cssQuery = cssQuery;
        return this;
    }
}
