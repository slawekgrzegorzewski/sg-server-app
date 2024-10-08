package pl.sg.twofa;

import org.jboss.aerogear.security.otp.Totp;

public class QRCode {
    private static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    private final String application;
    private final String secret;
    private final String user;

    public static QRCode fromApplicationUser(String application, String login, String secret) {
        return new QRCode(application, secret, login);
    }

    public QRCode(String application, String secret, String user) {
        this.application = application;
        this.secret = secret;
        this.user = user;
    }

    public String qrLink() {
        return QR_PREFIX + new Totp(secret).uri(String.format("%s (%s)", application, user));
    }
}
