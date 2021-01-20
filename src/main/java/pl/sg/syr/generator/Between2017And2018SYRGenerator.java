package pl.sg.syr.generator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import pl.sg.syr.model.Country;
import pl.sg.syr.model.CountrySYR;
import pl.sg.syr.model.SYR;
import pl.sg.syr.model.SecretCountriesSYR;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class Between2017And2018SYRGenerator extends SheetBasedSyrGeneratorStrategy {

    public Between2017And2018SYRGenerator(Sheet sheet, List<Country> countries) {
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
                        .setPeak((int) row.getCell(2).getNumericCellValue())
                        .setPercentIncrease((int) row.getCell(4).getNumericCellValue())
                        .setBaptized((int) row.getCell(5).getNumericCellValue())
                        .setAveragePioneers((int) row.getCell(6).getNumericCellValue())
                        .setNumberOfCongregations((int) row.getCell(7).getNumericCellValue())
                        .setMemorialAttendance((int) row.getCell(8).getNumericCellValue());
                result.add(newSyr);
                break;
            } else {
                newSyr = new CountrySYR()
                        .setYear(Year.parse(sheet.getSheetName()))
                        .setCountry(findCountry(row))
                        .setPopulation((int) row.getCell(1).getNumericCellValue())
                        .setPeak((int) row.getCell(2).getNumericCellValue())
                        .setRatio1PublisherTo((int) row.getCell(3).getNumericCellValue())
                        .setPercentIncrease((int) row.getCell(4).getNumericCellValue())
                        .setBaptized((int) row.getCell(5).getNumericCellValue())
                        .setAveragePioneers((int) row.getCell(6).getNumericCellValue())
                        .setNumberOfCongregations((int) row.getCell(7).getNumericCellValue())
                        .setMemorialAttendance((int) row.getCell(8).getNumericCellValue());
                result.add(newSyr);
            }
        }
        return result;
    }
}
