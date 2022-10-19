package pl.sg.accountant.controller;

import pl.sg.accountant.transport.PiggyBank;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PiggyBankController {

    List<PiggyBank> getAll(Domain domain);

    Integer create(pl.sg.accountant.model.billings.PiggyBank piggyBank);

    String update(pl.sg.accountant.model.billings.PiggyBank piggyBank);
}
