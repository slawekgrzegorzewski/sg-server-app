package pl.sg.syr;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sg.accountant.transport.syr.NameCountryIdMapping;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.syr.generator.SyrGeneratorStrategy;
import pl.sg.syr.model.Country;
import pl.sg.syr.model.SYR;
import pl.sg.syr.model.SecretCountriesSYR;
import pl.sg.syr.repository.CountryRepository;
import pl.sg.syr.repository.SYRRepository;
import pl.sg.syr.transport.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/syr")
public class SyrController {

    private final SYRRepository syrRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper mapper;

    public SyrController(SYRRepository syrRepository, CountryRepository countryRepository, ModelMapper mapper) {
        this.syrRepository = syrRepository;
        this.countryRepository = countryRepository;
        this.mapper = mapper;
    }

    @PostMapping()
    @TokenBearerAuth(any = {"SYR_ADMIN"})
    @SuppressWarnings("rawtypes")
    public SyrCreationResult importReport(
            @RequestParam("uploadFile") MultipartFile file,
            @RequestParam("newCountriesMatch") String newCountriesMatch) throws IOException {

        List<SYR> syrs = syrRepository.findAll();
        List<Country> countries = countryRepository.findAll();

        Type listType = new TypeToken<ArrayList<NameCountryIdMapping>>() {
        }.getType();
        List<NameCountryIdMapping> matches = new Gson().fromJson(newCountriesMatch, listType);
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        List<SyrGeneratorStrategy> generators = IntStream.range(0, workbook.getNumberOfSheets())
                .mapToObj(workbook::getSheetAt)
                .filter(sheet -> Pattern.compile("[0-9][0-9][0-9][0-9]").matcher(sheet.getSheetName()).matches())
                .filter(sheet -> syrs.stream().noneMatch(syr -> syr.getYear() == Year.parse(sheet.getSheetName())))
                .sorted(Comparator.comparing(sheet -> Year.parse(sheet.getSheetName())))
                .map(sheet -> SyrGeneratorStrategy.getStrategy(Year.parse(sheet.getSheetName()), sheet, countries))
                .toList();
        Set<String> notKnownCountries = generators.stream()
                .map(SyrGeneratorStrategy::getListOfCountries)
                .map(findNotMatchedCountries(countries, matches))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        SyrCreationResult result;
        if (notKnownCountries.isEmpty()) {
            List<SYR> allReports = generators.stream()
                    .map(SyrGeneratorStrategy::generate)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            syrRepository.saveAll(allReports);
            result = new SyrCreationResult();
        } else {
            result = new SyrCreationResult(
                    new ArrayList<>(notKnownCountries),
                    countries.stream().map(c -> this.mapper.map(c, CountryTO.class)).collect(Collectors.toList())
            );
        }
        return result;
    }

    private Function<List<String>, Set<String>> findNotMatchedCountries(
            List<Country> existingCountries,
            List<NameCountryIdMapping> matches) {
        return (List<String> listOfCountries) -> {
            if (existingCountries.isEmpty()) {
                existingCountries.addAll(createCountries(listOfCountries));
                return Set.of();
            } else {
                return listOfCountries.stream()
                        .filter(countryName -> existingCountries.stream().noneMatch(c -> c.isEqual(countryName)))
                        .filter(countryName -> {
                            Optional<NameCountryIdMapping> match = matches.stream()
                                    .filter(m -> m.getName().equals(countryName))
                                    .findFirst();
                            if (match.isPresent()) {
                                addNameToCountry(existingCountries, match.get().getCountry(), countryName);
                                return false;
                            } else {
                                return true;
                            }
                        })
                        .collect(Collectors.toSet());
            }
        };
    }

    private List<Country> createCountries(List<String> listOfCountries) {
        List<Country> newCountries = listOfCountries.stream()
                .map(Country::new)
                .collect(Collectors.toList());
        return countryRepository.saveAll(newCountries);
    }

    private void addNameToCountry(List<Country> countries, Integer countryId, String countryName) {
        Country countryToUpdate = countries.stream()
                .filter(c -> c.getId() == countryId)
                .findFirst()
                .get();
        countryToUpdate.getNames().add(countryName);
        countryRepository.save(countryToUpdate);
    }


    @GetMapping()
    @TokenBearerAuth(any = {"SYR_ADMIN", "SYR_USER"})
    public List<SYRTO> getAll() throws IOException {
        return syrRepository.findAll()
                .stream()
                .map(s -> {
                    if (s instanceof SecretCountriesSYR) {
                        return mapper.map(s, SecretCountriesSYRTO.class);
                    } else {
                        return mapper.map(s, CountrySYRTO.class);
                    }
                })
                .collect(Collectors.toList());
    }
}
