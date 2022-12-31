package pl.sg.trnd.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.sg.application.configuration.Configuration;
import pl.sg.trnd.client.generateIntegers.GenerateIntegersRequest;
import pl.sg.trnd.client.generateIntegers.GenerateIntegersRequestParams;
import pl.sg.trnd.client.generateIntegers.GenerateIntegersResponse;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class RandomOrgClient {
    private static final String JSON_RPC = "2.0";
    @Value("${random-org.url}")
    private String randomOrgUrl;
    private final Configuration configuration;

    public RandomOrgClient(Configuration configuration) {
        this.configuration = configuration;
    }

    public int[] generateIntegers(int count, int minInclusive, int maxInclusive) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<GenerateIntegersResponse> response = new RestTemplate()
                    .exchange(
                            RequestEntity.post(new URI(randomOrgUrl))
                                    .headers(headers)
                                    .body(
                                            new GenerateIntegersRequest(JSON_RPC,
                                                    new GenerateIntegersRequestParams(configuration.getRandomOrgApiKey(), count, minInclusive, maxInclusive, false))),
                            GenerateIntegersResponse.class);
            return response.getBody().result().random().data();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
