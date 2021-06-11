package pl.sg.utils;

import java.util.Optional;

public interface PageFetcher {
    Optional<String> getPage(String page);

    Optional<String> getBody(String page);

    Optional<byte[]> getImage(String url);
}
