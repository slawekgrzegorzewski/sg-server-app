package pl.sg.checker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PageElementExtractorImpl implements PageElementExtractor {
    @Override
    public Optional<Element> getElement(String page, String path) {
        Document doc = Jsoup.parse(page);
        Elements selection = doc.select(path);
        if (selection.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(selection.get(0));
    }

    @Override
    public Optional<String> getElementInnerHtml(String page, String path) {
        Document doc = Jsoup.parse(page);
        Elements selection = doc.select(path);
        if (selection.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(selection.html());
    }

}
