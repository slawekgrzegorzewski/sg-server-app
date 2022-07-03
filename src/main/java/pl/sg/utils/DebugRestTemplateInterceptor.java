package pl.sg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DebugRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(DebugRestTemplateInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        LOG.info("===========================request begin================================================");
        LOG.info("URI         : {}", request.getURI());
        LOG.info("Method      : {}", request.getMethod());
        LOG.info("Headers     : {}", request.getHeaders());
        LOG.info("Request body: {}", new String(body, "UTF-8"));
        LOG.info("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        LOG.info("============================response begin==========================================");
        LOG.info("Status code  : {}", response.getStatusCode());
        LOG.info("Status text  : {}", response.getStatusText());
        LOG.info("Headers      : {}", response.getHeaders());
        LOG.info("Response body: {}", inputStringBuilder.toString());
        LOG.info("=======================response end=================================================");
    }
}
