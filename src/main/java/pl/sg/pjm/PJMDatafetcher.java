package pl.sg.pjm;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.pjm.service.TranslatedBiblesFetcher;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@DgsComponent
public class PJMDatafetcher {
    private static final Logger LOG = LoggerFactory.getLogger(PJMDatafetcher.class);
    private final ScheduledTaskRegistrar scheduledTaskRegistrar;
    private final TranslatedBiblesFetcher translatedBiblesFetcher;

    public PJMDatafetcher(ScheduledTaskRegistrar scheduledTaskRegistrar, TranslatedBiblesFetcher translatedBiblesFetcher) {
        this.translatedBiblesFetcher = translatedBiblesFetcher;
        this.scheduledTaskRegistrar = scheduledTaskRegistrar;
    }

    @DgsMutation
    @TokenBearerAuth(any = "PJM")
    public String triggerTranslatedPJMVersesCheck() {
        LOG.info("Scheduling fetching translated PJM verses");
        scheduledTaskRegistrar.addOneTimeTask(() -> {
            try {
                LOG.info("Starting fetching translated PJM verses");
                translatedBiblesFetcher.fetchTranslatedVerses();
                LOG.info("Finished fetching translated PJM verses");
            } catch (IOException e) {
                LOG.info("Error during fetching translated PJM verses");
                throw new RuntimeException(e);
            }
        }, Duration.of(1, ChronoUnit.SECONDS));
        LOG.info("Scheduled fetching translated PJM verses");
        return "OK";
    }
}