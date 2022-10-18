package pl.sg.ipr.controller;

import pl.sg.accountant.transport.AccountantSettingsTO;
import pl.sg.application.model.Domain;
import pl.sg.ipr.transport.IntellectualPropertyCreateData;
import pl.sg.ipr.transport.IntellectualPropertyDTO;

public interface IntellectualPropertyController {

    IntellectualPropertyDTO create(IntellectualPropertyCreateData createData, Domain domain);
}
