package pl.sg.trnd.client.generateIntegers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateIntegersResponse(String jsonrpc, GenerateIntegersResponseResult result, int id) {
    public GenerateIntegersResponse(
            @JsonProperty String jsonrpc,
            @JsonProperty GenerateIntegersResponseResult result,
            @JsonProperty int id) {
        this.jsonrpc = jsonrpc;
        this.result = result;
        this.id = id;
    }
}
