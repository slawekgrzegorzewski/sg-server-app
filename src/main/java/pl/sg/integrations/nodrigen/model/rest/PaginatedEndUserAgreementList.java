package pl.sg.integrations.nodrigen.model.rest;

import java.util.List;

public class PaginatedEndUserAgreementList {

    private final int count;
    private final String next;
    private final String previous;
    private final List<EndUserAgreement> results;

    public PaginatedEndUserAgreementList(int count, String next, String previous, List<EndUserAgreement> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<EndUserAgreement> getResults() {
        return results;
    }
}
