package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.accountant.service.AccountantSettingsService;
import pl.sg.accountant.transport.AccountantSettingsTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

@RestController
@RequestMapping("/accountant-settings")
@Validated
public class AccountantSettingsRestController implements AccountantSettingsController {
    private final AccountantSettingsService accountantSettingsService;
    private final ModelMapper mapper;

    public AccountantSettingsRestController(AccountantSettingsService accountantSettingsService, ModelMapper mapper) {
        this.accountantSettingsService = accountantSettingsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public AccountantSettingsTO getForDomain(@RequestDomain Domain domain) {
        return this.mapper.map(accountantSettingsService.getForDomain(domain), AccountantSettingsTO.class);
    }

    @Override
    @PatchMapping("/is-company/enable")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public AccountantSettingsTO enableIsCompany(@RequestDomain Domain domain) {
        return this.mapper.map(accountantSettingsService.enableIsCompany(domain), AccountantSettingsTO.class);
    }

    @Override
    @PatchMapping("/is-company/disable")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public AccountantSettingsTO disableIsCompany(@RequestDomain Domain domain) {
        return this.mapper.map(accountantSettingsService.disableIsCompany(domain), AccountantSettingsTO.class);
    }

}
