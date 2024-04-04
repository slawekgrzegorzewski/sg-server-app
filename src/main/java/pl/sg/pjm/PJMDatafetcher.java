package pl.sg.pjm;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.pjm.service.TranslatedBiblesFetcher;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@DgsComponent
public class PJMDatafetcher {
    private final ScheduledTaskRegistrar scheduledTaskRegistrar;
    private final TranslatedBiblesFetcher translatedBiblesFetcher;

    public PJMDatafetcher(ScheduledTaskRegistrar scheduledTaskRegistrar, TranslatedBiblesFetcher translatedBiblesFetcher) {
        this.translatedBiblesFetcher = translatedBiblesFetcher;
        this.scheduledTaskRegistrar = scheduledTaskRegistrar;
    }

    @DgsMutation
    @TokenBearerAuth(any = "PJM")
    public String triggerTranslatedPJMVersesCheck() {
        scheduledTaskRegistrar.addOneTimeTask(() -> {
            try {
                translatedBiblesFetcher.fetchTranslatedVerses();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Duration.of(1, ChronoUnit.SECONDS));

        return "OK";
    }
}