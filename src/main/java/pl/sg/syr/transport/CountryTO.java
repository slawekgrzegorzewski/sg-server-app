package pl.sg.syr.transport;

import java.util.List;

public class CountryTO {

    private int id;
    List<String> names;

    public CountryTO() {
    }

    public CountryTO(String countryName) {
        this.names = List.of(countryName);
    }

    public CountryTO(List<String> countriesNames) {
        this.names = countriesNames;
    }

    public int getId() {
        return id;
    }

    public CountryTO setId(int id) {
        this.id = id;
        return this;
    }

    public List<String> getNames() {
        return names;
    }

    public CountryTO setNames(List<String> names) {
        this.names = names;
        return this;
    }
}
