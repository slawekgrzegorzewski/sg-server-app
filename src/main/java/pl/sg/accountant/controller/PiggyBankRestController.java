package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.billings.PiggyBanksService;
import pl.sg.accountant.transport.PiggyBank;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_PIGGY_BANK;
import static pl.sg.Application.UPDATE_PIGGY_BANK;

@RestController
@RequestMapping("/piggy-banks")
public class PiggyBankRestController implements PiggyBankController {

    private final PiggyBanksService piggyBanksService;
    private final ModelMapper mapper;

    public PiggyBankRestController(PiggyBanksService piggyBanksService, ModelMapper mapper) {
        this.piggyBanksService = piggyBanksService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<PiggyBank> getAll(@RequestDomain Domain domain) {
        return piggyBanksService.findByDomain(domain).stream()
                .map(pb -> mapper.map(pb, PiggyBank.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Integer create(
            @RequestBodyWithDomain(
                    transportClass = PiggyBank.class,
                    create = true,
                    mapperName = CREATE_PIGGY_BANK
            )
            @Valid pl.sg.accountant.model.billings.PiggyBank piggyBank) {
        return piggyBanksService.create(piggyBank);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String update(
            @RequestBodyWithDomain(
                    transportClass = PiggyBank.class,
                    mapperName = UPDATE_PIGGY_BANK
            )
            @Valid pl.sg.accountant.model.billings.PiggyBank piggyBank) {
        piggyBanksService.update(piggyBank);
        return "OK!";
    }
}
