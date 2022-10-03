package pl.sg.application.model;

import java.util.Date;

public class ReceivedRequest {
    String remoteAddress;
    String method;
    String requestURI;
    String headers;
    String body;
    String login;
    Date requestTime = new Date();

    public ReceivedRequest() {
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public ReceivedRequest setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ReceivedRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public ReceivedRequest setRequestURI(String requestURI) {
        this.requestURI = requestURI;
        return this;
    }

    public String getHeaders() {
        return headers;
    }

    public ReceivedRequest setHeaders(String headers) {
        this.headers = headers;
        return this;
    }

    public String getBody() {
        return body;
    }

    public ReceivedRequest setBody(String body) {
        this.body = body;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public ReceivedRequest setLogin(String login) {
        this.login = login;
        return this;
    }

}
