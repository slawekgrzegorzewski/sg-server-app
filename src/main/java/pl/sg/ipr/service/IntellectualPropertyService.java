package pl.sg.ipr.service;

import pl.sg.application.model.Domain;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.transport.IntellectualPropertyCreateData;

public interface IntellectualPropertyService {
    IntellectualProperty create(IntellectualPropertyCreateData createData, Domain domain);
}
