package pl.sg.security;

import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller("/tfa")
@Slf4j
public class TFAController {

    private ApplicationUserRepository applicationUserRepository;

    @PostMapping("/setup")
    public ResponseEntity<String> setup() {
        Totp totp = new Totp(Base32.random());
        totp.uri("Accountant app");
//    const secret = speakeasy.generateSecret({
//                length: 10,
//                name: commons.userObject.uname,
//                issuer: 'NarenAuth v0.0'
//    });
//        var url = speakeasy.otpauthURL({
//                secret: secret.base32,
//                label: commons.userObject.uname,
//                issuer: 'NarenAuth v0.0',
//                encoding: 'base32'
//    });
        QRCode.toDataURL(url, (err, dataURL) => {
            commons.userObject.tfa = {
                    secret: '',
                    tempSecret: secret.base32,
                    dataURL,
                    tfaURL: url
        };
            return res.json({
                    message: 'TFA Auth needs to be verified',
                    tempSecret: secret.base32,
                    dataURL,
                    tfaURL: secret.otpauth_url
        });
        });
    }

}
