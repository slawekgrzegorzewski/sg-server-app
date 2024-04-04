package pl.sg.pjm;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.pjm.service.TranslatedBiblesFetcher;

import java.io.IOException;

@DgsComponent
public class PJMDatafetcher {
    private final TranslatedBiblesFetcher translatedBiblesFetcher;

    public PJMDatafetcher(TranslatedBiblesFetcher translatedBiblesFetcher) {
        this.translatedBiblesFetcher = translatedBiblesFetcher;
    }

    @DgsMutation
    @TokenBearerAuth(any = "PJM")
    public String triggerTranslatedPJMVersesCheck() {
        try {
            translatedBiblesFetcher.fetchTranslatedVerses();
            return "OK";
        } catch (IOException e) {
            return "Not OK";
        }
    }
}