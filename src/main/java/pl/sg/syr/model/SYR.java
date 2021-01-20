package pl.sg.syr.model;

import pl.sg.application.database.YearStringAttributeConverter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Year;

@Entity
public class SYR<T extends SYR> {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @Convert(converter = YearStringAttributeConverter.class)
    private Year year;

    public SYR() {
    }

    public int getId() {
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
