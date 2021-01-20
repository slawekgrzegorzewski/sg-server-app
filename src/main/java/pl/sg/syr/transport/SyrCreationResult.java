package pl.sg.syr.transport;

import java.util.List;

public class SyrCreationResult {
    List<String> notMatchedCountries;
    List<CountryTO> knownCountries;

    public SyrCreationResult() {
    }

    public SyrCreationResult(List<String> notMatchedCountries, List<CountryTO> knownCountries) {
        this.notMatchedCountries = notMatchedCountries;
        this.knownCountries = knownCountries;
    }

    public List<String> getNotMatchedCountries() {
        return notMatchedCountries;
    }

    public SyrCreationResult setNotMatchedCountries(List<String> notMatchedCountries) {
        this.notMatchedCountries = notMatchedCountries;
        return this;
    }

    public List<CountryTO> getKnownCountries() {
        return knownCountries;
    }

    public SyrCreationResult setKnownCountries(List<CountryTO> knownCountries) {
        this.knownCountries = knownCountries;
        return this;
    }
}
