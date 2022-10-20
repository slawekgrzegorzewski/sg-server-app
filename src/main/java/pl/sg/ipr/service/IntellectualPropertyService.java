package pl.sg.ipr.service;

import pl.sg.application.model.Domain;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.api.IntellectualPropertyCreateData;

import java.util.List;

public interface IntellectualPropertyService {
    List<IntellectualProperty> getAll(int domainId);
    IntellectualProperty create(IntellectualPropertyCreateData createData, Domain domain);
}
