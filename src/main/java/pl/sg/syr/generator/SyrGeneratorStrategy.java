package pl.sg.syr.generator;

import org.apache.poi.ss.usermodel.Sheet;
import pl.sg.syr.model.Country;
import pl.sg.syr.model.SYR;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface SyrGeneratorStrategy {

    Map<Year, BiFunction<Sheet, List<Country>, SyrGeneratorStrategy>> strategyGenerators = Map.of(
            Year.of(2015), SyrFormatA::new,
            Year.of(2016), SyrFormatA::new,
            Year.of(2017), SyrFormatB::new,
            Year.of(2018), SyrFormatB::new,
            Year.of(2019), SyrFormatC::new,
            Year.of(2020), SyrFormatC::new,
            Year.of(2021), SyrFormatD::new,
            Year.of(2022), SyrFormatD::new
    );

    static SyrGeneratorStrategy getStrategy(Year year, Sheet sheet, List<Country> countries) {
        return strategyGenerators.get(year).apply(sheet, countries);
    }

    Year getYear();

    List<String> getListOfCountries();

    List<SYR> generate();
}
