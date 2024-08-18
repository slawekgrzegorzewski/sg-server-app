package pl.sg.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DebugRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(DebugRestTemplateInterceptor.class);
    private final Path logsDirPath;

    public DebugRestTemplateInterceptor(Path logsDirPath) {
        this.logsDirPath = logsDirPath;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        RequestLogInfo requestLogInfo = traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response, requestLogInfo);
        Path dir = logsDirPath.resolve(
                requestLogInfo.getRequestURI().toString()
                        .replace("https://", "")
                        .replace("/", "_")
                        .replace("?", "")
                        .replace(":", "_")
                        .replace("=", "_"));
        Files.createDirectories(dir);
        Files.write(
                dir.resolve(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss")) + ".json"),
                createGson().toJson(requestLogInfo).getBytes(StandardCharsets.UTF_8));
        return response;
    }

    private Gson createGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    private RequestLogInfo traceRequest(HttpRequest request, byte[] body) throws IOException {
        return new RequestLogInfo(request.getURI(), request.getMethod(), request.getHeaders(), createGson().fromJson(new String(body, StandardCharsets.UTF_8), Object.class));
    }

    private void traceResponse(ClientHttpResponse response, RequestLogInfo requestLogInfo) throws IOException {
        if (response.getStatusCode().is2xxSuccessful()) {
            StringBuilder inputStringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
            requestLogInfo.setResponseBody(createGson().fromJson(inputStringBuilder.toString(), Object.class));
        }
        requestLogInfo.setResponseStatus(response.getStatusCode().toString());
        requestLogInfo.setResponseStatusText(response.getStatusText());
        requestLogInfo.setResponseHeaders(response.getHeaders());
    }

    private static class RequestLogInfo {
        private final URI requestURI;
        private final HttpMethod requestMethod;
        private final HttpHeaders requestHeaders;
        private final Object requestBody;
        private String responseStatus;
        private String responseStatusText;
        private HttpHeaders responseHeaders;
        private Object responseBody;

        public RequestLogInfo(URI requestURI, HttpMethod requestMethod, HttpHeaders requestHeaders, Object requestBody) {
            this.requestURI = requestURI;
            this.requestMethod = requestMethod;
            this.requestHeaders = requestHeaders;
            this.requestBody = requestBody;
        }

        public URI getRequestURI() {
            return requestURI;
        }

        public HttpMethod getRequestMethod() {
            return requestMethod;
        }

        public HttpHeaders getRequestHeaders() {
            return requestHeaders;
        }

        public Object getRequestBody() {
            return requestBody;
        }

        public String getResponseStatus() {
            return responseStatus;
        }

        public String getResponseStatusText() {
            return responseStatusText;
        }

        public HttpHeaders getResponseHeaders() {
            return responseHeaders;
        }

        public Object getResponseBody() {
            return responseBody;
        }

        public RequestLogInfo setResponseStatus(String responseStatus) {
            this.responseStatus = responseStatus;
            return this;
        }

        public RequestLogInfo setResponseStatusText(String responseStatusText) {
            this.responseStatusText = responseStatusText;
            return this;
        }

        public RequestLogInfo setResponseHeaders(HttpHeaders responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }

        public RequestLogInfo setResponseBody(Object responseBody) {
            this.responseBody = responseBody;
            return this;
        }
    }
}
