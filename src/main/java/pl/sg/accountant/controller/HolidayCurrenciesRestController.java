package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.HolidayCurrenciesService;
import pl.sg.accountant.transport.HolidayCurrencies;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.Valid;

import static pl.sg.Application.CREATE_HOLIDAY_CURRENCIES;
import static pl.sg.Application.UPDATE_HOLIDAY_CURRENCIES;

@RestController
@RequestMapping("/holiday-currencies")
@Validated
public class HolidayCurrenciesRestController implements HolidayCurrenciesController {
    private final HolidayCurrenciesService holidayCurrenciesService;
    private final ModelMapper mapper;

    public HolidayCurrenciesRestController(HolidayCurrenciesService holidayCurrenciesService, ModelMapper mapper) {
        this.holidayCurrenciesService = holidayCurrenciesService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping()
    @TokenBearerAuth()
    public HolidayCurrencies get(@RequestDomain Domain domain) {
        pl.sg.accountant.model.HolidayCurrencies forDomain = holidayCurrenciesService
                .getForDomain(domain)
                .orElseGet(() -> {
                    pl.sg.accountant.model.HolidayCurrencies holidayCurrencies = new pl.sg.accountant.model.HolidayCurrencies().setDomain(domain);
                    return this.holidayCurrenciesService.create(holidayCurrencies);
                });
        return mapper.map(forDomain, HolidayCurrencies.class);
    }

    @Override
    @PutMapping
    @TokenBearerAuth(domainAdmin = true)
    public HolidayCurrencies create(
            @RequestBodyWithDomain(
                    transportClass = HolidayCurrencies.class,
                    mapperName = CREATE_HOLIDAY_CURRENCIES,
                    create = true,
                    domainAdmin = true)
            @Valid pl.sg.accountant.model.HolidayCurrencies holidayCurrencies) {

        holidayCurrencies = holidayCurrenciesService.create(holidayCurrencies);
        return mapper.map(holidayCurrencies, HolidayCurrencies.class);
    }

    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String update(
            @RequestBodyWithDomain(transportClass = HolidayCurrencies.class, mapperName = UPDATE_HOLIDAY_CURRENCIES)
            @Valid pl.sg.accountant.model.HolidayCurrencies holidayCurrencies) {
        holidayCurrenciesService.update(holidayCurrencies);
        return "OK";
    }
}
