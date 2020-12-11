package pl.sg.checker;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
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
