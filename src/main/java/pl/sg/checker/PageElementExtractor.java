package pl.sg.checker;

import org.jsoup.nodes.Element;

import java.util.Optional;

public interface PageElementExtractor {
    Optional<Element> getElement(String page, String path);
    Optional<String> getElementInnerHtml(String page, String path);
}
