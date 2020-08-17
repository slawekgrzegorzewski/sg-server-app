package pl.sg.application.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestUser {
    String ID = "id";
    String LOGIN = "login";
    String EMAIL = "email";
    String FIRST_NAME = "firstName";
    String LAST_NAME = "lastName";
    String ROLES = "roles";
    String NO_FIELD = "";

    String value() default NO_FIELD;
}
