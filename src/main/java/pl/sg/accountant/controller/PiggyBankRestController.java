package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.service.PiggyBanksService;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
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
    public List<PiggyBankTO> getAll(@RequestDomain Domain domain) {
        return piggyBanksService.findByDomain(domain).stream()
                .map(pb -> mapper.map(pb, PiggyBankTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Integer create(
            @RequestBodyWithDomain(
                    domainAdmin = true,
                    transportClass = PiggyBankTO.class,
                    create = true,
                    mapperName = CREATE_PIGGY_BANK
            )
            @Valid PiggyBank piggyBank) {
        return piggyBanksService.create(piggyBank);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String update(
            @RequestBodyWithDomain(
                    domainAdmin = true,
                    transportClass = PiggyBankTO.class,
                    mapperName = UPDATE_PIGGY_BANK
            )
            @Valid PiggyBank piggyBank) {
        piggyBanksService.update(piggyBank);
        return "OK!";
    }
}
