package pl.sg.application.security;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.graphql.schema.types.MFAData;
import pl.sg.graphql.schema.types.MFAParameters;
import pl.sg.graphql.schema.types.RegistrationParameters;

import java.util.Optional;

import static pl.sg.twofa.QRCode.fromApplicationUser;

@DgsComponent
public class RegistrationMutation {
    private static final String APP_NAME = "accountant";
    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationMutation(ApplicationUserService applicationUserService, PasswordEncoder passwordEncoder) {
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @DgsMutation
    public MFAData register(@InputArgument RegistrationParameters registrationParameters) {
        final String login = registrationParameters.getLogin();
        final String password = registrationParameters.getPassword();
        final String repeatedPassword = registrationParameters.getRepeatedPassword();
        if (login == null || password == null || "".equals(login.trim()) || "".equals(password.trim()) || !password.equals(repeatedPassword)) {
            throw new BadCredentialsException("Username/ password is required");
        }
        Optional<ApplicationUser> firstByLogin = applicationUserService.findByUserLogins(login);
        if (firstByLogin.isPresent()) {
            throw new BadCredentialsException(String.format("Username %s already exists.", login));
        }
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setLogin(login);
        applicationUser.setPassword(passwordEncoder.encode(password));
        applicationUser.setFirstName(registrationParameters.getFirstName());
        applicationUser.setLastName(registrationParameters.getLastName());
        applicationUser.setEmail(registrationParameters.getEmail());
        applicationUserService.create(applicationUser);
        return MFAData.newBuilder()
                .qrLink(fromApplicationUser(APP_NAME, login, applicationUser.getSecret()).qrLink())
                .mfaCode(applicationUser.getSecret())
                .build();
    }

    @DgsMutation
    public boolean setupMFA(@InputArgument MFAParameters mfaParameters) {
        ApplicationUser applicationUser = applicationUserService.getByUserLogins(mfaParameters.getLogin());
        if (!passwordEncoder.matches(mfaParameters.getPassword(), applicationUser.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }
        if (applicationUser.isUsing2FA()) {
            throw new BadCredentialsException("2FA already configured");
        }
        Totp totp = new Totp(applicationUser.getSecret());
        if (!totp.verify(String.valueOf(mfaParameters.getOtp()))) {
            throw new BadCredentialsException("Invalid verification code");
        }
        applicationUser.setUsing2FA(true);
        applicationUserService.save(applicationUser);
        return true;
    }
}