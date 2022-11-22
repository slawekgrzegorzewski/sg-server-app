package pl.sg.application.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenBearerAuth {

    boolean inQuery() default false;

    String[] all() default {};

    String[] any() default {};

    boolean domainAdmin() default false;

    boolean domainMember() default true;
}
