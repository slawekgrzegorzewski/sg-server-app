package pl.sg.checker;

import java.util.Optional;

public interface PageFetcher {
    Optional<String> getPage(String page);
}
