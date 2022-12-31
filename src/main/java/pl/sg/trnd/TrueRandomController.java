package pl.sg.trnd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.trnd.client.RandomOrgClient;

@RestController
@RequestMapping("/trnd")
public class TrueRandomController {
    private final RandomOrgClient randomOrgClient;

    public TrueRandomController(RandomOrgClient randomOrgClient) {
        this.randomOrgClient = randomOrgClient;
    }

    @GetMapping("/lotto")
    public int[] drawLotto() {
        return randomOrgClient.generateIntegers(6, 1, 49);
    }

    @GetMapping("/euroJackPot")
    public int[][] euroJackPot() {
        return new int[][]{
                randomOrgClient.generateIntegers(5, 1, 50),
                randomOrgClient.generateIntegers(2, 1, 12)
        };
    }
}
