package pl.sg.application;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String user, String role) {
        super(String.format("User %s does not have %s role", user, role));
    }
}
