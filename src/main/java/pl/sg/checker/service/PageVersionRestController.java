package pl.sg.checker.service;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.checker.model.PageVersion;
import pl.sg.checker.transport.PageVersionTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/page-version")
@Validated
public class PageVersionRestController {
    private final PageVersionRepository pageVersionRepository;
    private final ModelMapper mapper;

    public PageVersionRestController(PageVersionRepository pageVersionRepository, ModelMapper mapper) {
        this.pageVersionRepository = pageVersionRepository;
        this.mapper = mapper;
    }

    @GetMapping
    @TokenBearerAuth(any = {"CHECKER_ADMIN", "CHECKER_USER"})
    public List<PageVersionTO> allAccounts() {
        return map(pageVersionRepository.findAll());
    }

    private List<PageVersionTO> map(List<PageVersion> pageVersions) {
        return pageVersions.stream().map(pg -> mapper.map(pg, PageVersionTO.class)).collect(Collectors.toList());
    }

}
