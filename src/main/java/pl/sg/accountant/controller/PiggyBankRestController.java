package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.service.AccountsException;
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
    @GetMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<List<PiggyBankTO>> getAll(@RequestUser ApplicationUser user) {
        List<PiggyBankTO> result = piggyBanksService.findByUser(user).stream()
                .map(pb -> mapper.map(pb, PiggyBankTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<Integer> create(@RequestBody PiggyBankTO piggyBankTO, @RequestUser ApplicationUser user) {
        PiggyBank piggyBank = mapper.map(piggyBankTO, PiggyBank.class);
        piggyBank.setApplicationUser(user);
        return piggyBanksService.create(piggyBank)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<String> update(@RequestBody PiggyBankTO piggyBankTO, @RequestUser ApplicationUser user) throws AccountsException {
        PiggyBank piggyBank = piggyBanksService.getById(piggyBankTO.getId());
        mapper.map(piggyBankTO, piggyBank);
        piggyBank.setApplicationUser(user);
        piggyBanksService.update(piggyBank);
        return ResponseEntity.ok("OK!");
    }
}
