package pl.sg.pjm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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

    private final Path videosLocation;
    private final TranslatedVersesRepository translatedVersesRepository;


    public TranslatedBiblesFetcher(
            @Value("${pjm.videos-location}") Path videosLocation,
            TranslatedVersesRepository translatedVersesRepository) {
        this.videosLocation = videosLocation;
        this.translatedVersesRepository = translatedVersesRepository;
    }

    @Scheduled(cron = "${pjm.fetch}", zone = "Europe/Warsaw")
    public void fetchAllTransactions() throws IOException {
        try (Stream<Path> walk = Files.walk(videosLocation)) {
            walk
                    .filter(Files::isRegularFile)
                    .forEach(path ->
                            {
                                System.out.println(path);
                                try {
                                    Files.deleteIfExists(path);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        }
        Downloader.download(videosLocation);
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
        translatedVersesRepository.saveAll(newTranslatedVerses);
    }
}
