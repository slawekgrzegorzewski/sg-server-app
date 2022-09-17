package pl.sg.integrations.nodrigen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.sg.application.configuration.Configuration;
import pl.sg.integrations.nodrigen.model.NodrigenAccess;
import pl.sg.integrations.nodrigen.model.rest.*;
import pl.sg.integrations.nodrigen.model.rest.balances.BalancesMain;
import pl.sg.integrations.nodrigen.model.rest.transactions.TransactionsMain;
import pl.sg.integrations.nodrigen.repository.NodrigenAccessRepository;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;
import pl.sg.utils.DebugRestTemplateInterceptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Controller
public class NodrigenClient {

    private static final Logger LOG = LoggerFactory.getLogger(NodrigenClient.class);
    @Value("${nodrigen.service-url}")
    private String nodrigenUrl;
    private final NodrigenAccessRepository nodrigenAccessRepository;
    private final Configuration configuration;

    public NodrigenClient(NodrigenAccessRepository nodrigenAccessRepository, Configuration configuration) {
        this.nodrigenAccessRepository = nodrigenAccessRepository;
        this.configuration = configuration;
    }

    public ResponseEntity<String> listInstitutions(String country) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(ensureAccess());
            ResponseEntity<String> response = createDebuggingRestTemplate()
                    .exchange(
                            RequestEntity.get(new URI(nodrigenUrl + "institutions/?country=" + country)).headers(headers).build(),
                            String.class);
            return returnOkIf429(response, "list institutions" + country, "");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AgreementCreationResponse> createAgreement(NodrigenPermissionRequest nodrigenPermissionRequest) {
        try {
            String token = ensureAccess();
            HttpHeaders agreementHeaders = new HttpHeaders();
            agreementHeaders.setBearerAuth(token);
            agreementHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
            AgreementCreationRequest agreementCreationRequest = new AgreementCreationRequest(nodrigenPermissionRequest.getMaxHistoricalDays(), 90, new String[]{"balances", "details", "transactions"}, nodrigenPermissionRequest.getInstitutionId());
            return logErrorCodeAndReturnOptional(
                    RequestEntity.post(new URI(nodrigenUrl + "agreements/enduser/")).headers(agreementHeaders).body(agreementCreationRequest),
                    AgreementCreationResponse.class,
                    "create agreement " + agreementCreationRequest,
                    createDebuggingRestTemplate());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<Requisition> createRequisition(NodrigenPermissionRequest nodrigenPermissionRequest, UUID requisitionId, String reference) {
        try {
            String token = ensureAccess();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(token);
            RequisitionCreationRequest requisitionCreationRequest = new RequisitionCreationRequest(nodrigenPermissionRequest.getRedirect(), nodrigenPermissionRequest.getInstitutionId(), requisitionId, reference, nodrigenPermissionRequest.getUserLanguage(), false, false);
            return logErrorCodeAndReturnOptional(
                    RequestEntity.post(new URI(nodrigenUrl + "requisitions/")).headers(headers).body(requisitionCreationRequest),
                    Requisition.class,
                    "create requisition " + requisitionCreationRequest,
                    createDebuggingRestTemplate());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Requisition> getRequisition(UUID requisitionId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(ensureAccess());
            return logErrorCodeAndReturnOptional(
                    RequestEntity.get(new URI(nodrigenUrl + "requisitions/" + requisitionId + "/")).headers(headers).build(),
                    Requisition.class, "get requisituion " + requisitionId,
                    createDebuggingRestTemplate());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AccountDetails> getAccountDetails(UUID bankAccountId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(ensureAccess());
            return logErrorCodeAndReturnOptional(
                    RequestEntity.get(new URI(nodrigenUrl + "accounts/" + bankAccountId + "/details")).headers(headers).build(),
                    AccountDetails.class,
                    "get account details " + bankAccountId,
                    createDebuggingRestTemplate());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TransactionsMain> getTransactions(UUID bankAccountId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(ensureAccess());
            return logErrorCodeAndReturnOptional(
                    RequestEntity.get(new URI(nodrigenUrl + "accounts/" + bankAccountId + "/transactions/")).headers(headers).build(),
                    TransactionsMain.class, "get transactions " + bankAccountId, createDebuggingRestTemplate());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<BalancesMain> getBalances(UUID bankAccountId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(ensureAccess());
            return logErrorCodeAndReturnOptional(
                    RequestEntity.get(new URI(nodrigenUrl + "accounts/" + bankAccountId + "/balances/")).headers(headers).build(),
                    BalancesMain.class,
                    "get balances " + bankAccountId,
                    createDebuggingRestTemplate());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Optional<T> logErrorCodeAndReturnOptional(RequestEntity<?> request, Class<T> entityClass, String requestDescription, RestTemplate restTemplate) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(request, entityClass);
            if (!response.getStatusCode().is2xxSuccessful()) {
                LOG.warn("Problem with " + requestDescription + ofNullable(response.getBody()).map(v -> " - " + v).orElse(""));
                return empty();
            }
            return ofNullable(response.getBody());
        } catch (HttpClientErrorException ex) {
            LOG.warn("Problem with " + requestDescription, ex);
            return empty();
        }
    }

    private <T> ResponseEntity<T> returnOkIf429(ResponseEntity<T> response, String requestDescription, T defaultValue) {
        if (response.getStatusCode().value() == 429) {
            LOG.warn("Quota exceeded - " + requestDescription);
            return ResponseEntity.ok(ofNullable(response.getBody()).orElse(defaultValue));
        }
        return response;
    }

    private String ensureAccess() {
        LocalDateTime now = LocalDateTime.now();
        Optional<NodrigenAccess> access = nodrigenAccessRepository.getAccess();
        if (access.isPresent()) {
            if (accessTokenExpired(now, access.get())) {
                if (refreshTokenExpired(now, access.get())) {
                    return getNew();
                } else {
                    return refresh(access.get());
                }
            } else {
                return access.get().getAccessToken();
            }
        } else {
            return getNew();
        }
    }

    private boolean accessTokenExpired(LocalDateTime now, NodrigenAccess nodrigenAccess) {
        return nodrigenAccess.getAccessExpiresAt().isBefore(now);
    }

    private boolean refreshTokenExpired(LocalDateTime now, NodrigenAccess nodrigenAccess) {
        return nodrigenAccess.getRefreshExpiresAt().map(d -> d.minusHours(1).isBefore(now)).orElse(false);
    }

    private String refresh(NodrigenAccess nodrigenAccess) {
        RestTemplate restTemplate = createDebuggingRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<RefreshTokenRequest> request = new HttpEntity<>(new RefreshTokenRequest(nodrigenAccess.getRefreshToken().orElseThrow()), headers);
        ResponseEntity<RefreshTokenResponse> response = restTemplate.postForEntity(nodrigenUrl + "token/refresh/", request, RefreshTokenResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            nodrigenAccess.setAccessToken(response.getBody().getAccess());
            nodrigenAccess.setAccessExpires(response.getBody().getAccessExpires());
            nodrigenAccess.setAccessExpiresAt(LocalDateTime.now().plusSeconds(response.getBody().getAccessExpires()));
            nodrigenAccessRepository.save(nodrigenAccess);
            return nodrigenAccess.getAccessToken();
        } else {
            return getNew();
        }
    }

    private String getNew() {
        nodrigenAccessRepository.archiveAll(LocalDateTime.now());

        RestTemplate restTemplate = createDebuggingRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<NewTokenRequest> request = new HttpEntity<>(new NewTokenRequest(configuration.getNordigenSecretId(), configuration.getNordigenSecretKey()), headers);
        ResponseEntity<NewTokenResponse> response = restTemplate.postForEntity(nodrigenUrl + "token/new/", request, NewTokenResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            NodrigenAccess nodrigenAccess = new NodrigenAccess();
            nodrigenAccess.setAccessToken(response.getBody().getAccess());
            nodrigenAccess.setAccessExpires(response.getBody().getAccessExpires());
            nodrigenAccess.setAccessExpiresAt(LocalDateTime.now().plusSeconds(response.getBody().getAccessExpires()));
            nodrigenAccess.setRefreshToken(response.getBody().getRefresh());
            nodrigenAccess.setRefreshExpires(response.getBody().getRefreshExpires());
            nodrigenAccess.setRefreshExpiresAt(LocalDateTime.now().plusSeconds(response.getBody().getRefreshExpires()));
            nodrigenAccessRepository.save(nodrigenAccess);
            return nodrigenAccess.getAccessToken();
        } else {
            throw new RuntimeException("Could not get nodrigen access token");
        }
    }

    private RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

    private RestTemplate createDebuggingRestTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(List.of(new DebugRestTemplateInterceptor()));
        return restTemplate;
    }
}
