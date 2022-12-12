package pl.sg.syr.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Country {

    @Id
    
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private int id;
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> names;

    public Country() {
    }

    public Country(String countryName) {
        this.names = List.of(countryName);
    }

    public Country(List<String> countriesNames) {
        this.names = countriesNames;
    }

    public Integer getId() {
        return id;
    }

    public boolean isEqual(String countryName){
        return names.stream().anyMatch(countryName::equals);
    }

    public List<String> getNames() {
        return names;
    }

    public Country setNames(List<String> names) {
        this.names = names;
        return this;
    }
}
