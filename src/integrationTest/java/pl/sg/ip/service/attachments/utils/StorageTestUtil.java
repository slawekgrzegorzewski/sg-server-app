package pl.sg.ip.service.attachments.utils;

import java.io.IOException;
import java.net.URISyntaxException;

public interface StorageTestUtil {
    void initStorage() throws IOException;

    void clearStorage() throws IOException;

    boolean checkFileExistenceInStorage(String fileName);

    void putResourceInStorage(String resource, String withName) throws IOException, URISyntaxException;

    String readFileContentFromStorage(String fileName) throws IOException;
}
