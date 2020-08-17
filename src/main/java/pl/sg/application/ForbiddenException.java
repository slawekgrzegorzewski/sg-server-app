package pl.sg.application;

import java.util.Arrays;

public class ForbiddenException extends RuntimeException {

    private ForbiddenException(String format, String user, String... roles) {
        super(String.format(format, user, Arrays.toString(roles)));
    }

    public static ForbiddenException anyRoleNotMet(String user, String... roles) {
        return new ForbiddenException("User %s lacks all of following roles %s", user, roles);
    }

    public static ForbiddenException allRoleNotMet(String user, String... roles) {
        return new ForbiddenException("User %s lacks any of following roles %s", user, roles);
    }
}
