package pl.sg.syr.generator;

import org.apache.poi.ss.usermodel.Sheet;
import pl.sg.syr.model.Country;
import pl.sg.syr.model.SYR;

import java.time.Year;
import java.util.List;

public interface SyrGeneratorStrategy {
    static SyrGeneratorStrategy getStrategy(Year year, Sheet sheet, List<Country> countries) {
        if (year.isBefore(Year.of(2017))) {
            return new UpTo2016SYRGenerator(sheet, countries);
        } else if (year.isAfter(Year.of(2016)) && year.isBefore(Year.of(2019))) {
            return new Between2017And2018SYRGenerator(sheet, countries);
        } else {
            return new From2019SYRGenerator(sheet, countries);
        }
    }

    Year getYear();

    List<String> getListOfCountries();

    List<SYR> generate();
}
