package pl.sg.trnd.client.generateIntegers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateIntegersRequestParams(String apiKey, int n, int min, int max, boolean replacement) {

    @Override
    @JsonProperty("apiKey")
    public String apiKey() {
        return apiKey;
    }

    @Override
    @JsonProperty("n")
    public int n() {
        return n;
    }

    @Override
    @JsonProperty("min")
    public int min() {
        return min;
    }

    @Override
    @JsonProperty("max")
    public int max() {
        return max;
    }

    @Override
    @JsonProperty("replacement")
    public boolean replacement() {
        return replacement;
    }
}
