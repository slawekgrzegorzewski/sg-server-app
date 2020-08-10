package pl.sg.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
@Slf4j
public class RegistrationController {

    private ApplicationUserRepository applicationUserRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String uname = user.getUname();
        String upass = user.getUpass();
        if (uname == null || upass == null || "".equals(uname.trim()) || "".equals(upass.trim())) {
            return ResponseEntity.badRequest().body("Username/ password is required");
        }
        Optional<ApplicationUser> firstByLogin = applicationUserRepository.findFirstByLogin(uname);
        if (firstByLogin.isPresent()) {
            return ResponseEntity.badRequest().body(String.format("Username %s already exists.", uname));
        }
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setLogin(uname);
        applicationUser.setPassword(upass);
        applicationUser.setUsing2FA(true);
        applicationUserRepository.save(applicationUser);

        return ResponseEntity.ok().body("User is successfully registered");
    }

    public static class User {
        private final String uname;
        private final String upass;

        public User(String uname, String upass) {
            this.uname = uname;
            this.upass = upass;
        }

        public String getUname() {
            return uname;
        }

        public String getUpass() {
            return upass;
        }
    }
}
