package pl.sg.checker.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.PageFetcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class Checker {
    private static final String PAGE = "http://www.szkockiewrzosowisko.pl/";
    private final PageFetcher fetcher;
    private final PageElementExtractor pageElementExtractor;
    private final PageVersionsService pageVersionsService;

    private String serverAddress = "https://grzegorzewski.org:8443";


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
                            String pageUrl = PAGE + element.attr("src").replace(" ", "%20");
                            try {
                                element.attr("src",
                                        serverAddress + "/image-proxy/" + encodeValue(pageUrl));
                            } catch (UnsupportedEncodingException e) {
                                System.out.println("Error");
                            }
                        })
                )
                .ifPresent(this.pageVersionsService::updateVersion);
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(value.getBytes(StandardCharsets.UTF_8)));
    }

}
