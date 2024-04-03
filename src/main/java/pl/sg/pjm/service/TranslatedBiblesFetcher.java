package pl.sg.pjm.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.application.configuration.Configuration;
import pl.sg.pjm.bible.Downloader;
import pl.sg.pjm.bible.M4Chapters;
import pl.sg.pjm.bible.model.Book;
import pl.sg.pjm.bible.model.ChapterVerse;
import pl.sg.pjm.entities.TranslatedVerse;
import pl.sg.pjm.repositories.TranslatedVersesRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TranslatedBiblesFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(TranslatedBiblesFetcher.class);
    private final Path videosLocation;
    private final TranslatedVersesRepository translatedVersesRepository;
    private final String sendgridApiKey;


    public TranslatedBiblesFetcher(
            @Value("${pjm.videos-location}") Path videosLocation,
            TranslatedVersesRepository translatedVersesRepository,
            Configuration configuration) {
        this.videosLocation = videosLocation;
        this.translatedVersesRepository = translatedVersesRepository;
        this.sendgridApiKey = configuration.getSendgridApiKey();
    }

    @Scheduled(cron = "${pjm.fetch}", zone = "Europe/Warsaw")
    public void fetchAllTransactions() throws IOException {
        LOG.info("Deleting all files from " + videosLocation);
//        try (Stream<Path> walk = Files.walk(videosLocation)) {
//            walk
//                    .filter(Files::isRegularFile)
//                    .forEach(path ->
//                            {
//                                System.out.println(path);
//                                try {
//                                    Files.deleteIfExists(path);
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                    );
//        }
        LOG.info("Downloading files to " + videosLocation);
//        Downloader.download(videosLocation);

        LOG.info("Parsing all pjm files from  " + videosLocation);
        Map<Book, List<ChapterVerse>> newVerses = M4Chapters.calculateStatistics(videosLocation);

        Map<Book, List<ChapterVerse>> currentVerses = translatedVersesRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        TranslatedVerse::getBook,
                        Collectors.mapping(tv -> ChapterVerse.of(tv.getChapter(), tv.getVerse()), Collectors.toList())
                ));
        List<TranslatedVerse> newTranslatedVerses = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();

        for (Book book : Book.values()) {
            List<ChapterVerse> newVersion = new ArrayList<>(newVerses.getOrDefault(book, new ArrayList<>()));
            newVersion.removeAll(currentVerses.getOrDefault(book, List.of()));
            newVersion.stream()
                    .map(chapterVerse -> {
                        TranslatedVerse translatedVerse = new TranslatedVerse();
                        translatedVerse.setBook(book);
                        translatedVerse.setChapter(chapterVerse.getChapter());
                        translatedVerse.setVerse(chapterVerse.getVerse());
                        translatedVerse.setTranslationDate(now);
                        return translatedVerse;
                    })
                    .collect(Collectors.toCollection(() -> newTranslatedVerses));
        }
        if (newTranslatedVerses.isEmpty()) {
            LOG.info("No new translated verses");
        } else {
            LOG.info("Saving new " + newTranslatedVerses.size() + " verse(s) to DB");
            translatedVersesRepository.saveAll(newTranslatedVerses);
            sendEmail(newTranslatedVerses);
        }
    }

    private void sendEmail(List<TranslatedVerse> newTranslatedVerses) {
        Email from = new Email("admin@grzegorzewski.org");

        StringBuilder contentBuilder = new StringBuilder("<div>Nowe przetłumaczone wersety</div>").append("<div style=\"background-color: #b7e1cd;\"><ul>");
        newTranslatedVerses.stream()
                .map(tv -> "<li>" + tv.getBook() + " " + tv.getChapter() + ":" + tv.getVerse() + "</li>")
                .forEach(contentBuilder::append);
        contentBuilder.append("</ul></div>");

        Content content = new Content("text/html", contentBuilder.toString());
        Email firstTo = new Email("slawek.grz@gmail.com", "Sławek Grzegorzewski");

        Mail mail = new Mail(from, "Nowe wersety przetłumaczone na PJM", firstTo, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println("Could not send email");
        }
    }
}
