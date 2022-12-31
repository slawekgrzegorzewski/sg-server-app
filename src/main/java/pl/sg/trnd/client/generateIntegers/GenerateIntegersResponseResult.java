package pl.sg.trnd.client.generateIntegers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateIntegersResponseResult(GenerateIntegersResponseRandom random, int bitUsed, int bitsLeft,
                                             int requestsLeft, int advisoryDelay) {
    public GenerateIntegersResponseResult(
            @JsonProperty GenerateIntegersResponseRandom random,
            @JsonProperty int bitUsed,
            @JsonProperty int bitsLeft,
            @JsonProperty int requestsLeft,
            @JsonProperty int advisoryDelay) {
        this.random = random;
        this.bitUsed = bitUsed;
        this.bitsLeft = bitsLeft;
        this.requestsLeft = requestsLeft;
        this.advisoryDelay = advisoryDelay;
    }
}
