package pl.sg.loans;

import com.netflix.graphql.dgs.*;
import org.javamoney.moneta.Money;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.repository.DomainRepository;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.DgsConstants;
import pl.sg.graphql.schema.types.*;
import pl.sg.loans.model.ConstantForNFirstInstallmentRateStrategyConfig;
import pl.sg.loans.model.Loan;
import pl.sg.loans.model.NthDayOfMonthRepaymentDayStrategyConfig;
import pl.sg.loans.service.LoanService;
import pl.sg.loans.utils.LoansException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@DgsComponent
public class LoansDatafetcher {
    private final DomainRepository domainRepository;
    private final LoanService loanService;

    public LoansDatafetcher(DomainRepository domainRepository, LoanService loanService) {
        this.domainRepository = domainRepository;
        this.loanService = loanService;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public pl.sg.graphql.schema.types.Loans loans(
            @RequestHeader("domainId") int domainId
    ) {
        return Loans.newBuilder()
                .loans(loanService.findAllDomainLoans(domainRepository.getReferenceById(domainId)).stream().map(LoansDatafetcher::map).toList())
                .build();
    }

    @DgsData(parentType = DgsConstants.LOANS.TYPE_NAME, field = DgsConstants.LOANS.RateStrategyConfigs)
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<RateStrategyConfig> rateStrategyConfigs(
            @RequestHeader("domainId") int domainId
    ) {
        return loanService
                .findAllDomainRateStrategyConfigs(domainRepository.getReferenceById(domainId))
                .stream()
                .map(LoansDatafetcher::map)
                .toList();
    }

    @DgsData(parentType = DgsConstants.LOANS.TYPE_NAME, field = DgsConstants.LOANS.RepaymentDayStrategyConfigs)
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<RepaymentDayStrategyConfig> repaymentDayStrategyConfigs(
            @RequestHeader("domainId") int domainId
    ) {
        return loanService
                .findAllDomainRepaymentDayStrategyConfigs(domainRepository.getReferenceById(domainId))
                .stream()
                .map(LoansDatafetcher::map)
                .toList();
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public pl.sg.graphql.schema.types.Loan singleLoan(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") LoanQuery input
    ) {
        return map(
                loanService
                        .findLoanByPublicId(
                                input.getPublicId(),
                                domainRepository.getReferenceById(domainId)));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public pl.sg.graphql.schema.types.Loan createLoan(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") LoanCreationInput input
    ) {
        return map(
                loanService
                        .createLoan(
                                input.getName(),
                                input.getPaymentDate(),
                                input.getNumberOfInstallments(),
                                input.getRepaymentDayStrategyConfigId(),
                                input.getRateStrategyConfigId(),
                                map(input.getPaidAmount()),
                                domainRepository.getReferenceById(domainId)));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public pl.sg.graphql.schema.types.Loan updateLoan(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") LoanUpdateInput input
    ) {
        return map(
                loanService
                        .updateLoan(
                                input.getLoanId(),
                                input.getName(),
                                domainRepository.getReferenceById(domainId)));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteLoan(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") LoanDeleteInput input
    ) {
        loanService
                .deleteLoan(
                        input.getLoanId(),
                        domainRepository.getReferenceById(domainId));
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public pl.sg.graphql.schema.types.Installment createInstallment(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") InstallmentCreationInput input
    ) {
        return map(
                loanService
                        .createInstallment(
                                input.getLoanId(),
                                input.getPaidAt(),
                                map(input.getRepaidInterest()),
                                map(input.getRepaidAmount()),
                                map(input.getOverpayment()),
                                domainRepository.getReferenceById(domainId)));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteInstallment(
            @RequestHeader("domainId") int domainId,
            @InputArgument("installmentPublicId") UUID installmentPublicId
    ) {
        loanService.deleteInstallment(installmentPublicId, domainRepository.getReferenceById(domainId));
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public RateStrategyConfig createConstantForNFirstInstallmentRateStrategyConfig(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") ConstantForNFirstInstallmentRateStrategyConfigCreationInput input
    ) {
        return map(
                loanService
                        .createConstantForNFirstInstallmentRateStrategy(
                                domainRepository.getReferenceById(domainId),
                                input.getName(),
                                input.getConstantRate(),
                                input.getVariableRateMargin(),
                                input.getBecomesVariableRateAfterNInstallments()));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteRateStrategyConfig(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") RateStrategyConfigDeleteInput rateStrategyConfigDeleteInput
    ) {
        loanService.deleteRateStrategyConfig(rateStrategyConfigDeleteInput.getPublicId(), domainRepository.getReferenceById(domainId));
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public RepaymentDayStrategyConfig createNthDayOfMonthRepaymentDayStrategyConfig(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") NthDayOfMonthRepaymentDayStrategyConfigInput input
    ) {
        return map(
                loanService
                        .createNthDayOfMonthStrategy(
                                domainRepository.getReferenceById(domainId),
                                input.getName(),
                                input.getDayOfMonth()));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteRepaymentDayStrategyConfig(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") RepaymentDayStrategyConfigDeleteInput repaymentDayStrategyConfigDeleteInput
    ) {
        loanService.deleteRepaymentDayStrategyConfig(repaymentDayStrategyConfigDeleteInput.getPublicId(), domainRepository.getReferenceById(domainId));
        return "OK";
    }

    @NotNull
    private static pl.sg.graphql.schema.types.Loan map(Loan loan) {
        return pl.sg.graphql.schema.types.Loan.newBuilder()
                .publicId(loan.getPublicId())
                .name(loan.getName())
                .numberOfInstallments(loan.getNumberOfInstallments())
                .paidAmount(map(loan.getPaidAmount()))
                .paymentDate(loan.getPaymentDate())
                .rateStrategyConfig(map(loan.getRateStrategyConfig()))
                .repaymentDayStrategyConfig(map(loan.getRepaymentDayStrategyConfig()))
                .installments(loan.getInstallments().stream().map(LoansDatafetcher::map).toList())
                .build();
    }

    private static RateStrategyConfig map(pl.sg.loans.model.RateStrategyConfig config) {
        if (config instanceof ConstantForNFirstInstallmentRateStrategyConfig rateStrategyConfig) {
            return pl.sg.graphql.schema.types.ConstantForNFirstInstallmentRateStrategyConfig.newBuilder()
                    .publicId(rateStrategyConfig.getPublicId())
                    .name(rateStrategyConfig.getName())
                    .constantRate(rateStrategyConfig.getConstantRate())
                    .variableRateMargin(rateStrategyConfig.getVariableRateMargin())
                    .becomesVariableRateAfterNInstallments(rateStrategyConfig.getBecomesVariableRateAfterNInstallments())
                    .build();
        }
        throw new LoansException("Not known rate strategy to map " + config.getClass());
    }

    private static RepaymentDayStrategyConfig map(pl.sg.loans.model.RepaymentDayStrategyConfig config) {
        if (config instanceof NthDayOfMonthRepaymentDayStrategyConfig repaymentDayStrategyConfig) {
            return pl.sg.graphql.schema.types.NthDayOfMonthRepaymentDayStrategyConfig.newBuilder()
                    .publicId(repaymentDayStrategyConfig.getPublicId())
                    .name(repaymentDayStrategyConfig.getName())
                    .dayOfMonth(repaymentDayStrategyConfig.getDayOfMonth())
                    .build();
        }
        throw new LoansException("Not known repayment strategy to map " + config.getClass());
    }

    private static Installment map(pl.sg.loans.model.Installment installment) {
        return pl.sg.graphql.schema.types.Installment.newBuilder()
                .publicId(installment.getPublicId())
                .paidAt(installment.getPaidAt())
                .repaidInterest(map(installment.getRepaidInterest()))
                .repaidAmount(map(installment.getRepaidAmount()))
                .overpayment(map(installment.getOverpayment()))
                .build();
    }

    private static MonetaryAmount map(javax.money.MonetaryAmount monetaryAmount) {
        return MonetaryAmount.newBuilder()
                .amount(monetaryAmount.getNumber().numberValue(BigDecimal.class))
                .currency(Currency.getInstance(monetaryAmount.getCurrency().getCurrencyCode()))
                .build();
    }

    private static javax.money.MonetaryAmount map(MonetaryAmountInput monetaryAmount) {
        return Money.of(
                monetaryAmount.getAmount(),
                monetaryAmount.getCurrency().getCurrencyCode());
    }


}