package pl.sg.accountant.transport.syr;

public class NameCountryIdMapping {
    String name;
    Integer country;

    public NameCountryIdMapping() {
    }

    public String getName() {
        return name;
    }

    public NameCountryIdMapping setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCountry() {
        return country;
    }

    public NameCountryIdMapping setCountry(Integer country) {
        this.country = country;
        return this;
    }
}
