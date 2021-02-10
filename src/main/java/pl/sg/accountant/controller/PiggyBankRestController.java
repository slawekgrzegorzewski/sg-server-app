package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.service.PiggyBanksService;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping("/{domainId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<List<PiggyBankTO>> getAll(@RequestUser ApplicationUser user, @PathVariable int domainId) {
        List<PiggyBankTO> result = piggyBanksService.findByDomain(user, domainId).stream()
                .map(pb -> mapper.map(pb, PiggyBankTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<Integer> create(@RequestUser ApplicationUser user, @RequestBody PiggyBankTO piggyBankTO) {
        PiggyBank piggyBank = mapper.map(piggyBankTO, PiggyBank.class);
        return piggyBanksService.create(user, piggyBank)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<String> update(@RequestUser ApplicationUser user, @RequestBody PiggyBankTO piggyBankTO) {
        PiggyBank piggyBank = applyChanges(piggyBankTO, piggyBanksService.getById(user, piggyBankTO.getId()));
        piggyBanksService.update(user, piggyBank);
        return ResponseEntity.ok("OK!");
    }

    private PiggyBank applyChanges(PiggyBankTO piggyBankTO, PiggyBank piggyBank) {
        piggyBank.setName(piggyBank.getName());
        piggyBank.setDescription(piggyBank.getDescription());
        piggyBank.setBalance(piggyBankTO.getBalance());
        piggyBank.setSavings(piggyBank.isSavings());
        piggyBank.setMonthlyTopUp(piggyBank.getMonthlyTopUp());
        return piggyBank;
    }
}
