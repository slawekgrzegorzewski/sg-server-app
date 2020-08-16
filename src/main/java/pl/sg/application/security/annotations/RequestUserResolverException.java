package pl.sg.application.security.annotations;

public class RequestUserResolverException extends RuntimeException {
    public RequestUserResolverException(String message) {
        super(message);
    }
}
