package pl.sg.ipr.controller;

import pl.sg.ipr.api.IntellectualProperty;
import pl.sg.ipr.api.IntellectualPropertyCreateData;

import java.util.List;


public interface IntellectualPropertyController {

    List<IntellectualProperty> getAll(Integer domainId);
    IntellectualProperty create(Integer domainId, IntellectualPropertyCreateData createData);

}
