package pl.sg.checker;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.utils.PageFetcher;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/image-proxy")
@Validated
public class ImageProxyRestController {
    private final PageFetcher pageFetcher;

    public ImageProxyRestController(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    @GetMapping("/{pageUrlBase64}")
    public ResponseEntity<byte[]> getImage(@PathVariable("pageUrlBase64") String pageUrlBase64) {
        HttpHeaders headers = new HttpHeaders();
        String pageUrl = new String(Base64.getDecoder().decode(pageUrlBase64.getBytes(StandardCharsets.UTF_8)));
        byte[] image = pageFetcher.getImage(pageUrl).orElseThrow();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

}
