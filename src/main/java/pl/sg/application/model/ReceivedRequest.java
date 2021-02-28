package pl.sg.application.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ReceivedRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_sequence")
    private Integer id;
    String remoteAddress;
    String method;
    String requestURI;
    @Column(length = 200_000)
    String headers;
    @Column(length = 200_000)
    String body;
    String login;
    Date requestTime = new Date();

    public ReceivedRequest() {
    }

    public Integer getId() {
        return id;
    }

    public ReceivedRequest setId(Integer id) {
        this.id = id;
        return this;
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
