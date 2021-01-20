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

public class UpTo2016SYRGenerator extends SheetBasedSyrGeneratorStrategy {

    public UpTo2016SYRGenerator(Sheet sheet, List<Country> countries) {
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
                        .setAverage((int) row.getCell(4).getNumericCellValue())
                        .setPercentIncrease((int) row.getCell(5).getNumericCellValue())
                        .setAveragePreviousYear((int) row.getCell(6).getNumericCellValue())
                        .setBaptized((int) row.getCell(7).getNumericCellValue())
                        .setAverageAuxiliaryPioneers((int) row.getCell(8).getNumericCellValue())
                        .setAveragePioneers((int) row.getCell(9).getNumericCellValue())
                        .setNumberOfCongregations((int) row.getCell(10).getNumericCellValue())
                        .setTotalHours((int) row.getCell(11).getNumericCellValue())
                        .setAverageBibleStudies((int) row.getCell(12).getNumericCellValue())
                        .setMemorialAttendance((int) row.getCell(13).getNumericCellValue());
                result.add(newSyr);
                break;
            } else {
                Country country = findCountry(row);
                newSyr = new CountrySYR()
                        .setYear(Year.parse(sheet.getSheetName()))
                        .setCountry(country)
                        .setPopulation((int) row.getCell(1).getNumericCellValue())
                        .setPeak((int) row.getCell(2).getNumericCellValue())
                        .setRatio1PublisherTo((int) row.getCell(3).getNumericCellValue())
                        .setAverage((int) row.getCell(4).getNumericCellValue())
                        .setPercentIncrease((int) row.getCell(5).getNumericCellValue())
                        .setAveragePreviousYear((int) row.getCell(6).getNumericCellValue())
                        .setBaptized((int) row.getCell(7).getNumericCellValue())
                        .setAverageAuxiliaryPioneers((int) row.getCell(8).getNumericCellValue())
                        .setAveragePioneers((int) row.getCell(9).getNumericCellValue())
                        .setNumberOfCongregations((int) row.getCell(10).getNumericCellValue())
                        .setTotalHours((int) row.getCell(11).getNumericCellValue())
                        .setAverageBibleStudies((int) row.getCell(12).getNumericCellValue())
                        .setMemorialAttendance((int) row.getCell(13).getNumericCellValue());
                result.add(newSyr);
            }
        }
        return result;
    }
}
