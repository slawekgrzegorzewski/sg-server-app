package pl.sg.utils;

import pl.sg.accountant.model.ledger.FinancialTransaction;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserDomainRelation;
import pl.sg.application.model.Domain;
import pl.sg.graphql.schema.types.*;

import static java.util.Optional.ofNullable;

public class GraphqlMappers {

    public static AuthenticationInfo mapAuthenticationInfo(ApplicationUser applicationUser, String jwt) {
        return AuthenticationInfo
                .newBuilder()
                .jwt(jwt)
                .user(mapUser(applicationUser))
                .build();
    }

    public static User mapUser(ApplicationUser applicationUser) {
        return User.newBuilder()
                .name(applicationUser.getFirstName() + " " + applicationUser.getLastName())
                .email(applicationUser.getEmail())
                .roles(applicationUser.getRoles())
                .defaultDomainId(applicationUser.getDefaultDomain().getId())
                .domains(
                        applicationUser.getAssignedDomains()
                                .stream()
                                .map(ApplicationUserDomainRelation::getDomain)
                                .map(GraphqlMappers::mapDomainSimple)
                                .toList()
                )
                .build();
    }

    public static Account mapAccount(pl.sg.accountant.model.accounts.Account account) {
        final pl.sg.banks.model.BankAccount bankAccount = account.getBankAccount();
        final Domain domain = account.getDomain();
        FinancialTransaction lastTransactionIncludedInBalance = account.getLastTransactionIncludedInBalance();
        return Account.newBuilder()
                .id(account.getId())
                .name(account.getName())
                .currency(account.getCurrency())
                .creditLimit(account.getCreditLimit())
                .balanceIndex(ofNullable(lastTransactionIncludedInBalance).map(FinancialTransaction::getId).orElse(null))
                .visible(account.isVisible())
                .bankAccount(ofNullable(bankAccount).map(GraphqlMappers::mapBankAccount).orElse(null))
                .domain(ofNullable(domain).map(GraphqlMappers::mapDomainSimple).orElse(null))
                .currentBalance(account.getCurrentBalance())
                .build();

    }

    public static BankAccount mapBankAccount(pl.sg.banks.model.BankAccount bankAccount) {
        Domain domain = bankAccount.getDomain();
        return BankAccount.newBuilder()
                .id(bankAccount.getId())
                .iban(bankAccount.getIban())
                .currency(bankAccount.getCurrency())
                .owner(bankAccount.getOwner())
                .product(bankAccount.getProduct())
                .bic(bankAccount.getBic())
                .externalId(bankAccount.getExternalId())
                .domain(ofNullable(domain).map(GraphqlMappers::mapDomainSimple).orElse(null))
                .build();
    }

    public static DomainSimple mapDomainSimple(Domain domain) {
        return DomainSimple.newBuilder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }
}
