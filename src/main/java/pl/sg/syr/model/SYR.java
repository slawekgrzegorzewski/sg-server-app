package pl.sg.syr.model;

import jakarta.persistence.*;
import pl.sg.application.database.YearStringAttributeConverter;

import jakarta.validation.constraints.NotNull;
import java.time.Year;

@Entity
public class SYR<T extends SYR> {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private int id;
    @NotNull
    @Convert(converter = YearStringAttributeConverter.class)
    private Year year;

    public SYR() {
    }

    public Integer getId() {
        return id;
    }

    public Year getYear() {
        return year;
    }

    public T setYear(Year year) {
        this.year = year;
        return (T) this;
    }
}
