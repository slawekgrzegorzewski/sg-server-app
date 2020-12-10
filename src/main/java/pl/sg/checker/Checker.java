package pl.sg.checker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.checker.service.PageVersionsService;

@Component
public class Checker {
    private static final String PAGE = "http://www.szkockiewrzosowisko.pl/";
    private final PageFetcher fetcher;
    private final PageElementExtractor pageElementExtractor;
    private final PageVersionsService pageVersionsService;

    public Checker(PageFetcher fetcher, PageElementExtractor pageElementExtractor, PageVersionsService pageVersionsService) {
        this.fetcher = fetcher;
        this.pageElementExtractor = pageElementExtractor;
        this.pageVersionsService = pageVersionsService;
    }

    @Scheduled(fixedDelay = 3600 * 1)
    public void scheduleFixedDelayTask() {
        this.fetcher.getPage(PAGE)
                .flatMap(p -> this.pageElementExtractor.getElement(p, "a[href*=\"aktualnosci\"]")
                        .map(element -> PAGE + element.attr("href"))
                        .flatMap(this.fetcher::getPage))
                .ifPresent(this.pageVersionsService::updateVersion);
    }

}
