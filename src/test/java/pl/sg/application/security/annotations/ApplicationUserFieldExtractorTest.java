package pl.sg.application.security.annotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserLogin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class ApplicationUserFieldExtractorTest {
    private static final int ID = 10;
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final ArrayList<String> ROLES = new ArrayList<>() {{
        add("a");
        add("b");
    }};
    private final static ApplicationUser applicationUser = createApplicationUser();

    private static ApplicationUser createApplicationUser() {
        ApplicationUserLogin applicationUserLogin = new ApplicationUserLogin(ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, ROLES);
        ApplicationUser applicationUser = new ApplicationUser(
                ID * ID,
                List.of(applicationUserLogin)
        );
        applicationUser.setLoggedInUser(applicationUserLogin);
        return applicationUser;
    }

    @ParameterizedTest
    @MethodSource("supportedFields")
    void shouldExtractSupportedFields(String field, Class forClass, Object expected) {
        //given
        ApplicationUserFieldExtractor extractor = new ApplicationUserFieldExtractor(forClass);
        //when
        Object extract = extractor.extract(applicationUser, field);
        //then
        Assertions.assertEquals(expected, extract);
    }

    private static Stream<Arguments> supportedFields() {
        return Stream.of(
                Arguments.of(RequestUser.ID, Integer.class, ID),
                Arguments.of(RequestUser.FIRST_NAME, String.class, FIRST_NAME),
                Arguments.of(RequestUser.LAST_NAME, String.class, LAST_NAME),
                Arguments.of(RequestUser.EMAIL, String.class, EMAIL),
                Arguments.of(RequestUser.LOGIN, String.class, LOGIN),
                Arguments.of(RequestUser.NO_FIELD, ApplicationUser.class, applicationUser)

        );
    }

    @ParameterizedTest
    @MethodSource("notSupportedFields")
    void shouldNotExtractNotSupportedFields(String field, Class forClass) {
        //given
        ApplicationUserFieldExtractor extractor = new ApplicationUserFieldExtractor(forClass);
        //when
        Executable executable = () -> extractor.extract(applicationUser, field);
        //then
        Assertions.assertThrows(RequestUserResolverException.class, executable);
    }

    private static Stream<Arguments> notSupportedFields() {
        return Stream.of(
                Arguments.of("password", String.class),
                Arguments.of("isUsing2FA", Boolean.class),
                Arguments.of("secret", String.class),
                Arguments.of("roles", List.class)
        );
    }

    @ParameterizedTest
    @MethodSource("supportedFieldsWithWrongTypes")
    void shouldNotExtractSupportedFieldsWithWrongTypes(String field, Class forClass) {
        //given
        ApplicationUserFieldExtractor extractor = new ApplicationUserFieldExtractor(forClass);
        //when
        Executable executable = () -> extractor.extract(applicationUser, field);
        //then
        Assertions.assertThrows(RequestUserResolverException.class, executable);
    }

    private static Stream<Arguments> supportedFieldsWithWrongTypes() {
        return Stream.of(
                Arguments.of(RequestUser.ID, String.class),
                Arguments.of(RequestUser.FIRST_NAME, Boolean.class),
                Arguments.of(RequestUser.LAST_NAME, Integer.class),
                Arguments.of(RequestUser.EMAIL, ApplicationUser.class),
                Arguments.of(RequestUser.LOGIN, ApplicationUserFieldExtractorTest.class),
                Arguments.of(RequestUser.NO_FIELD, String.class)
        );
    }

    @ParameterizedTest
    @MethodSource("supportedFieldsWithApplicationUserType")
    void shouldNotReturnApplicationUserWhenFieldIsSet(String field, Class forClass) {
        //given
        ApplicationUserFieldExtractor extractor = new ApplicationUserFieldExtractor(forClass);
        //when
        Executable executable = () -> extractor.extract(applicationUser, field);
        //then
        Assertions.assertThrows(RequestUserResolverException.class, executable);
    }

    private static Stream<Arguments> supportedFieldsWithApplicationUserType() {
        return Stream.of(
                Arguments.of(RequestUser.ID, ApplicationUser.class),
                Arguments.of(RequestUser.FIRST_NAME, ApplicationUser.class),
                Arguments.of(RequestUser.LAST_NAME, ApplicationUser.class),
                Arguments.of(RequestUser.EMAIL, ApplicationUser.class),
                Arguments.of(RequestUser.LOGIN, ApplicationUser.class)
        );
    }
}