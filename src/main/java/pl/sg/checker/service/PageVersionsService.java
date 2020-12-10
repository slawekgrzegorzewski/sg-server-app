package pl.sg.checker.service;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.model.PageVersion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        Optional<PageVersion> lastVersion = this.pageVersionRepository.getTopByOrderByVersionTime();
        List<Element> currentValue;
        if (lastVersion.isEmpty()) {
            currentValue = List.of();
        } else {
            currentValue = this.pageElementExtractor.getElements(lastVersion.get().getContent(), "p");
        }
        List<Element> newValue = this.pageElementExtractor.getElements(content, "p");
        currentValue = filterNoOfVisitsOut(currentValue);
        newValue = filterNoOfVisitsOut(newValue);

        List<Element> added = findAddedElements(currentValue, newValue);
        List<Element> removed = findRemovedElements(currentValue, newValue);
        if (added.size() > 0 || removed.size() > 0) {
            PageVersion newVersion = new PageVersion()
                    .setElementsAdded(added.stream().map(Element::html).collect(Collectors.toUnmodifiableList()))
                    .setElementsRemoved(removed.stream().map(Element::html).collect(Collectors.toUnmodifiableList()))
                    .setContent(content)
                    .setVersionTime(LocalDateTime.now());
            this.pageVersionRepository.save(newVersion);
        }
    }

    private List<Element> filterNoOfVisitsOut(List<Element> currentValue) {
        Pattern visitsPattern = Pattern.compile("Wizyt: [0-9]{6,}");
        return currentValue.stream()
                .filter(e -> !visitsPattern.matcher(e.text()).find())
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Element> findAddedElements(List<Element> currentValue, List<Element> newValue) {
        return listsDifference(currentValue, newValue);
    }

    private List<Element> findRemovedElements(List<Element> currentValue, List<Element> newValue) {
        return listsDifference(newValue, currentValue);
    }

    private List<Element> listsDifference(List<Element> currentValue, List<Element> newValue) {
        return newValue.stream()
                .filter(e -> currentValue.stream()
                        .map(Element::text)
                        .noneMatch(e.text()::equals))
                .collect(Collectors.toList());
    }
}
