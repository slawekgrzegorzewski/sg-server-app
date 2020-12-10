package pl.sg.checker.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.PageFetcher;

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

    @Scheduled(fixedDelay = 3600 * 1000)
    public void scheduleFixedDelayTask() {
        this.fetcher.getPage(PAGE)
                .flatMap(p -> this.pageElementExtractor.getElement(p, "a[href*=\"aktualnosci\"]")
                        .map(element -> PAGE + element.attr("href"))
                        .flatMap(this.fetcher::getPage))
                .map(pageContent ->
                    this.pageElementExtractor.visitElements(pageContent, "[src]", element -> {
                        element.attr("src", PAGE + element.attr("src"));
                    })
                )
                .ifPresent(this.pageVersionsService::updateVersion);
    }

}
