package pl.sg.syr.generator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import pl.sg.syr.model.Country;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public abstract class SheetBasedSyrGeneratorStrategy implements SyrGeneratorStrategy {
    protected static List<String> SECRET_COUNTRIES_DISCRIMINATORS = List.of("innych krajów", "inne kraje");
    protected final Sheet sheet;
    protected final List<Country> countries;

    public SheetBasedSyrGeneratorStrategy(Sheet sheet, List<Country> countries) {
        this.sheet = sheet;
        this.countries = countries;
    }

    @Override
    public Year getYear() {
        return Year.parse(sheet.getSheetName());
    }

    @Override
    public List<String> getListOfCountries() {
        List<String> countries = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            String countryName = row.getCell(0).getStringCellValue();
            if (countryName.contains(" inne ") || countryName.contains(" innych ")) {
                break;
            }
            countries.add(countryName);
        }
        return countries;
    }

    public Sheet getSheet() {
        return sheet;
    }

    protected int extractNoOfCountries(String stringCellValue) {
        for (String d : SECRET_COUNTRIES_DISCRIMINATORS) {
            stringCellValue = stringCellValue.replace(d, "");
        }
        return Integer.parseInt(stringCellValue.trim());
    }

    protected boolean isSecretCountriesRow(Row row) {
        String cellValue = row.getCell(0).getStringCellValue();
        return SECRET_COUNTRIES_DISCRIMINATORS
                .stream()
                .anyMatch(cellValue::contains);
    }

    protected Country findCountry(Row row) {
        return countries.stream()
                .filter(c -> c.isEqual(row.getCell(0).getStringCellValue()))
                .findFirst()
                .get();
    }
}
