package pl.sg.trnd.client.generateIntegers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

public class GenerateIntegersRequest {
    private final String jsonrpc;
    private final String method = "generateIntegers";
    private final GenerateIntegersRequestParams params;
    private final int id;

    public GenerateIntegersRequest(String jsonrpc, GenerateIntegersRequestParams params) {
        this.jsonrpc = jsonrpc;
        this.params = params;
        this.id = new Random().nextInt(1_000_000);
    }

    @JsonProperty("jsonrpc")
    public String getJsonrpc() {
        return jsonrpc;
    }

    @JsonProperty("method")
    public String getMethod() {
        return method;
    }

    @JsonProperty("params")
    public GenerateIntegersRequestParams getParams() {
        return params;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

}
