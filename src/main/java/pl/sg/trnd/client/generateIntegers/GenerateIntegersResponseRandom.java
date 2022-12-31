package pl.sg.trnd.client.generateIntegers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateIntegersResponseRandom(int[] data) {
    public GenerateIntegersResponseRandom(
            @JsonProperty int[] data) {
        this.data = data;
    }


}
