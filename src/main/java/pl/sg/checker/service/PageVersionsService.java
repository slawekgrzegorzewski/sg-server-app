package pl.sg.checker.service;

import org.springframework.stereotype.Component;
import pl.sg.checker.model.CheckerTask;
import pl.sg.checker.model.PageVersion;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PageVersionsService {
    private final PageVersionRepository pageVersionRepository;

    public PageVersionsService(PageVersionRepository pageVersionRepository) {
        this.pageVersionRepository = pageVersionRepository;
    }

    public Optional<PageVersion> updateVersion(CheckerTask task, List<String> content) {
        List<PageVersion> allVersions = this.pageVersionRepository.getByTask(task);
        Optional<PageVersion> lastVersion = allVersions.stream().max(Comparator.comparing(PageVersion::getVersionTime));
        List<String> currentValue;
        if (lastVersion.isEmpty()) {
            currentValue = List.of();
        } else {
            currentValue = lastVersion.get().getContent();
        }

        List<String> added = findAddedElements(currentValue, content);
        List<String> removed = findRemovedElements(currentValue, content);
        if (added.size() > 0 || removed.size() > 0) {
            PageVersion newVersion = new PageVersion()
                    .setTask(task)
                    .setElementsAdded(added)
                    .setElementsRemoved(removed)
                    .setContent(content)
                    .setVersionTime(LocalDateTime.now());
            this.pageVersionRepository.save(newVersion);
            return Optional.of(newVersion);
        }
        return Optional.empty();
    }

    private List<String> findAddedElements(List<String> currentValue, List<String> newValue) {
        return listsDifference(currentValue, newValue);
    }

    private List<String> findRemovedElements(List<String> currentValue, List<String> newValue) {
        return listsDifference(newValue, currentValue);
    }

    private List<String> listsDifference(List<String> currentValue, List<String> newValue) {
        return newValue.stream()
                .filter(str -> currentValue.stream().noneMatch(str::equals))
                .collect(Collectors.toList());
    }
}
