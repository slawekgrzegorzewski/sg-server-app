package pl.sg.checker.service;

import org.springframework.stereotype.Component;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.model.PageVersion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PageVersionsService {
    private final PageVersionRepository pageVersionRepository;
    private final PageElementExtractor pageElementExtractor;

    public PageVersionsService(PageVersionRepository pageVersionRepository, PageElementExtractor pageElementExtractor) {
        this.pageVersionRepository = pageVersionRepository;
        this.pageElementExtractor = pageElementExtractor;
    }

    public List<PageVersion> getAllVersions() {
        return this.pageVersionRepository.findAll();
    }

    public void updateVersion(String content) {
        Boolean isNewVersion = this.pageVersionRepository.getTopByOrderByVersionTime()
                .map(pg -> {
                    Optional<String> currentValue = this.pageElementExtractor.getElementInnerHtml(pg.getContent(), "p");
                    Optional<String> newValue = this.pageElementExtractor.getElementInnerHtml(content, "p");
                    return currentValue.flatMap(cv -> newValue.map(nv -> nv.equals(cv))).orElse(false);
                })
                .orElse(true);
        if (isNewVersion) {
            PageVersion newVersion = new PageVersion().setContent(content).setVersionTime(LocalDateTime.now());
            this.pageVersionRepository.save(newVersion);
        }
    }
}
