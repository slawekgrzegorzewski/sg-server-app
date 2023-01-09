package pl.sg.syr.generator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jetbrains.annotations.NotNull;
import pl.sg.syr.model.Country;
import pl.sg.syr.model.CountrySYR;
import pl.sg.syr.model.SYR;
import pl.sg.syr.model.SecretCountriesSYR;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class From2019SYRGenerator extends SheetBasedSyrGeneratorStrategy {

    public From2019SYRGenerator(Sheet sheet, List<Country> countries) {
        super(sheet, countries);
    }

    @Override
    public List<SYR> generate() {
        List<SYR> result = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            SYR newSyr = null;
            if (isSecretCountriesRow(row)) {
                newSyr = new SecretCountriesSYR()
                        .setYear(Year.parse(sheet.getSheetName()))
                        .setNumberOfCountries(extractNoOfCountries(row.getCell(0).getStringCellValue()))
                        .setPeak(getNumberFromCell(row, 2))
                        .setAverage(getNumberFromCell(row, 4))
                        .setPercentIncrease(getNumberFromCell(row, 5))
                        .setBaptized(getNumberFromCell(row, 6))
                        .setAveragePioneers(getNumberFromCell(row, 7))
                        .setNumberOfCongregations(getNumberFromCell(row, 8))
                        .setMemorialAttendance(getNumberFromCell(row, 9));
                result.add(newSyr);
                break;
            } else {
                newSyr = new CountrySYR()
                        .setYear(Year.parse(sheet.getSheetName()))
                        .setCountry(findCountry(row))
                        .setPopulation(getNumberFromCell(row, 1))
                        .setPeak(getNumberFromCell(row, 2))
                        .setRatio1PublisherTo(getNumberFromCell(row, 3))
                        .setAverage(getNumberFromCell(row, 4))
                        .setPercentIncrease(getNumberFromCell(row, 5))
                        .setBaptized(getNumberFromCell(row, 6))
                        .setAveragePioneers(getNumberFromCell(row, 7))
                        .setNumberOfCongregations(getNumberFromCell(row, 8))
                        .setMemorialAttendance(getNumberFromCell(row, 9));
                result.add(newSyr);
            }
        }
        return result;
    }

    @NotNull
    private static Integer getNumberFromCell(Row row, int cell) {
        return Optional.ofNullable(row.getCell(cell)).map(Cell::getNumericCellValue).map(Double::intValue).orElse(0);
    }
}
