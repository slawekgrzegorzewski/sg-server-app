package pl.sg.checker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class PageElementExtractorStep extends CheckerStep<PageElementExtractorStep> {
    private String pageContentVariable;
    private String resultVariable;
    @Column(length = 1000)
    private String cssQuery;
    private int elementIndex;
    private String attribute;

    public PageElementExtractorStep() {
    }

    public String getPageContentVariable() {
        return pageContentVariable;
    }

    public PageElementExtractorStep setPageContentVariable(String pageContentVariable) {
        this.pageContentVariable = pageContentVariable;
        return this;
    }

    public String getResultVariable() {
        return resultVariable;
    }

    public PageElementExtractorStep setResultVariable(String resultVariable) {
        this.resultVariable = resultVariable;
        return this;
    }

    public String getCssQuery() {
        return cssQuery;
    }

    public PageElementExtractorStep setCssQuery(String cssQuery) {
        this.cssQuery = cssQuery;
        return this;
    }

    public int getElementIndex() {
        return elementIndex;
    }

    public PageElementExtractorStep setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
        return this;
    }

    public String getAttribute() {
        return attribute;
    }

    public PageElementExtractorStep setAttribute(String attribute) {
        this.attribute = attribute;
        return this;
    }
}
