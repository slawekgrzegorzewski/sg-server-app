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
    public String triggerTranslatedPJMVersesCheck() throws IOException {
        translatedBiblesFetcher.fetchTranslatedVerses();
        return "OK";
    }
}