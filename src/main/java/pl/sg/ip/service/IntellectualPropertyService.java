package pl.sg.ip.service;

import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.api.IntellectualPropertyData;

import java.util.List;

public interface IntellectualPropertyService {
    List<IntellectualProperty> getAll(int domainId);
    IntellectualProperty create(int domainId, IntellectualPropertyData createData);

    void update(int domainId, int intellectualPropertyId, IntellectualPropertyData createData);
}
