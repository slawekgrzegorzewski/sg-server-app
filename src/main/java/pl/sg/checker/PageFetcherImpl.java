package pl.sg.checker;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PageFetcherImpl implements PageFetcher {

    @Override
    public Optional<String> getPage(String page) {
        try {
            return Optional.of(Jsoup.connect(page).get().outerHtml());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
