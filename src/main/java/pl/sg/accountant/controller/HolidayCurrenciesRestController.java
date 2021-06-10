package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.HolidayCurrencies;
import pl.sg.accountant.service.HolidayCurrenciesService;
import pl.sg.accountant.transport.HolidayCurrenciesTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;

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
    public HolidayCurrenciesTO get(@RequestDomain Domain domain) {
        HolidayCurrencies forDomain = holidayCurrenciesService
                .getForDomain(domain)
                .orElseGet(() -> {
                    HolidayCurrencies holidayCurrencies = new HolidayCurrencies().setDomain(domain);
                    return this.holidayCurrenciesService.create(holidayCurrencies);
                });
        return mapper.map(forDomain, HolidayCurrenciesTO.class);
    }

    @Override
    @PutMapping
    @TokenBearerAuth(domainAdmin = true)
    public HolidayCurrenciesTO create(
            @RequestBodyWithDomain(
                    transportClass = HolidayCurrenciesTO.class,
                    mapperName = CREATE_HOLIDAY_CURRENCIES,
                    create = true,
                    domainAdmin = true)
            @Valid HolidayCurrencies holidayCurrencies) {

        holidayCurrencies = holidayCurrenciesService.create(holidayCurrencies);
        return mapper.map(holidayCurrencies, HolidayCurrenciesTO.class);
    }

    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String update(
            @RequestBodyWithDomain(transportClass = HolidayCurrenciesTO.class, mapperName = UPDATE_HOLIDAY_CURRENCIES)
            @Valid HolidayCurrencies holidayCurrencies) {
        holidayCurrenciesService.update(holidayCurrencies);
        return "OK";
    }
}
