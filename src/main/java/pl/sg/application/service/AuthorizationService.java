package pl.sg.application.service;

import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface AuthorizationService {
    void validateRequiredRoles(String token, String[] all, String[] any);

    ApplicationUser getUserInfo(String token);

    String generateJWTToken(String subject, List<String> roles, int defaultDomainId);
}
