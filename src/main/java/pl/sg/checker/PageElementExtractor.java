package pl.sg.checker;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface PageElementExtractor {
    Optional<Element> getElement(String page, String path);

    List<Element> getElements(String page, String path);

    Optional<String> getElementInnerHtml(String page, String path);

    String visitElements(String page, String path, Consumer<Element> visitor);
}
