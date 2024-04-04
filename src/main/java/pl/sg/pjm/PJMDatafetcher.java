package pl.sg.pjm;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.pjm.service.TranslatedBiblesFetcher;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class PJMDatafetcher {
    private static final Logger LOG = LoggerFactory.getLogger(PJMDatafetcher.class);
    private final TranslatedBiblesFetcher translatedBiblesFetcher;

    public PJMDatafetcher(TranslatedBiblesFetcher translatedBiblesFetcher) {
        this.translatedBiblesFetcher = translatedBiblesFetcher;
    }

    @DgsMutation
    @TokenBearerAuth(any = "PJM")
    public CompletableFuture<String> triggerTranslatedPJMVersesCheck() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                translatedBiblesFetcher.fetchTranslatedVerses();
                return "OK";
            } catch (IOException e) {
                return "Not OK";
            }
        });
    }
}