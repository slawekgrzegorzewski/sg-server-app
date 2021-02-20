package pl.sg.application.security.annotations;

import pl.sg.application.transport.WithDomainTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBodyWithDomain {
    boolean domainAdmin() default false;

    boolean domainMember() default true;

    Class<? extends WithDomainTO> transportClass();

    String mapperName() default "";

    boolean create() default false;
}
