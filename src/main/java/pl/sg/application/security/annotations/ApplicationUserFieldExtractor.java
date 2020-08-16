package pl.sg.application.security.annotations;

import pl.sg.application.model.ApplicationUser;

import java.util.function.Function;

public class ApplicationUserFieldExtractor<T> {
    private final Class<T> forClass;

    public ApplicationUserFieldExtractor(Class<T> forClass) {
        this.forClass = forClass;
    }

    public T extract(ApplicationUser user, String field) {
        if (forClass.isAssignableFrom(ApplicationUser.class) && !field.equals(RequestUser.NO_FIELD)) {
            throw new RequestUserResolverException("Not supported field requested to be injected");
        }
        return (T) getExtractor(field).apply(user);
    }

    Function<ApplicationUser, ?> getExtractor(String field) {
        if (forClass.isAssignableFrom(ApplicationUser.class))
            return Function.identity();
        if (forClass.isAssignableFrom(String.class))
            return getStringExtractor(field);
        if (forClass.isAssignableFrom(Integer.class))
            return getIntegerExtractor(field);
        throw new RequestUserResolverException("Not supported field requested to be injected");
    }

    Function<ApplicationUser, String> getStringExtractor(String field) {
        switch (field) {
            case RequestUser.LOGIN:
                return ApplicationUser::getLogin;
            case RequestUser.EMAIL:
                return ApplicationUser::getEmail;
            case RequestUser.FIRST_NAME:
                return ApplicationUser::getFirstName;
            case RequestUser.LAST_NAME:
                return ApplicationUser::getLastName;
            default:
                throw new RequestUserResolverException("Not supported field requested to be injected");
        }
    }

    Function<ApplicationUser, Integer> getIntegerExtractor(String field) {
        switch (field) {
            case RequestUser.ID:
                return ApplicationUser::getId;
            default:
                throw new RequestUserResolverException("Not supported field requested to be injected");
        }
    }
}
