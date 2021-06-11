package pl.sg.utils;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @Override
    public Optional<String> getBody(String page) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(page))
                    .build();
            String[] body = new String[1];
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(b -> body[0] = b)
                    .join();
            return Optional.of(body[0]);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<byte[]> getImage(String page) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            URL url = new URL(page);
            BufferedImage image = ImageIO.read(url);
            ImageIO.write(image, "jpg", baos);
            return Optional.of(baos.toByteArray());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
