package pl.sg.pjm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sg.pjm.bible.model.Book;

import java.time.OffsetDateTime;

@Entity(name = "translated_verses")
public class TranslatedVerse {

    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    Long id;

    @Getter
    @Setter
    OffsetDateTime translationDate;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    Book book;

    @Getter
    @Setter
    int chapter;

    @Getter
    @Setter
    int verse;

}
