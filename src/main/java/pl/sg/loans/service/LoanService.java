package pl.sg.loans.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.sg.application.model.Domain;
import pl.sg.graphql.schema.types.LoanSimulationParams;
import pl.sg.loans.calculator.LoanSimulator;
import pl.sg.loans.model.*;
import pl.sg.loans.repositories.InstallmentRepository;
import pl.sg.loans.repositories.LoanRepository;
import pl.sg.loans.repositories.RateStrategyConfigRepository;
import pl.sg.loans.repositories.RepaymentDayStrategyConfigRepository;
import pl.sg.loans.service.rate.ConstantRateStrategy;
import pl.sg.loans.service.rate.RateStrategyFactory;
import pl.sg.loans.service.repayment.RepaymentDayStrategyFactory;
import pl.sg.loans.utils.LoansException;
import pl.sg.loans.utils.RateStrategy;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class LoanService {

    private static final Logger LOG = LoggerFactory.getLogger(LoanService.class);
    private final InstallmentRepository installmentRepository;
    private final LoanRepository loanRepository;
    private final LoanSimulator loanSimulator;
    private final RepaymentDayStrategyConfigRepository repaymentDayStrategyConfigRepository;
    private final RepaymentDayStrategyFactory repaymentDayStrategyFactory;
    private final RateStrategyConfigRepository rateStrategyConfigRepository;
    private final RateStrategyFactory rateStrategyFactory;

    public LoanService(InstallmentRepository installmentRepository,
                       LoanRepository loanRepository, LoanSimulator loanSimulator,
                       RepaymentDayStrategyConfigRepository repaymentDayStrategyConfigRepository, RepaymentDayStrategyFactory repaymentDayStrategyFactory,
                       RateStrategyConfigRepository rateStrategyConfigRepository, RateStrategyFactory rateStrategyFactory) {
        this.installmentRepository = installmentRepository;
        this.loanRepository = loanRepository;
        this.loanSimulator = loanSimulator;
        this.repaymentDayStrategyConfigRepository = repaymentDayStrategyConfigRepository;
        this.repaymentDayStrategyFactory = repaymentDayStrategyFactory;
        this.rateStrategyConfigRepository = rateStrategyConfigRepository;
        this.rateStrategyFactory = rateStrategyFactory;
    }

    public Loan findLoanByPublicId(
            UUID loanPublicId,
            Domain domain
    ) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanPublicId));
        validateDomain(domain, loan.getDomain());
        return loan;
    }

    public Loan createLoan(
            String name,
            LocalDate paymentDate,
            int numberOfInstallments,
            UUID repaymentDayStrategyConfigPublicId,
            UUID rateStrategyConfigPublicId,
            MonetaryAmount paidAmount,
            Domain domain
    ) {
        RepaymentDayStrategyConfig repaymentDayStrategyConfig = Objects.requireNonNull(repaymentDayStrategyConfigRepository.findByPublicId(repaymentDayStrategyConfigPublicId));
        validateDomain(domain, repaymentDayStrategyConfig.getDomain());
        RateStrategyConfig rateStrategyConfig = Objects.requireNonNull(rateStrategyConfigRepository.findByPublicId(rateStrategyConfigPublicId));
        validateDomain(domain, rateStrategyConfig.getDomain());
        Loan loan = new Loan();
        loan.setName(name);
        loan.setPaymentDate(paymentDate);
        loan.setNumberOfInstallments(numberOfInstallments);
        loan.setRepaymentDayStrategyConfig(repaymentDayStrategyConfig);
        loan.setRateStrategyConfig(rateStrategyConfig);
        loan.setPaidAmount(paidAmount);
        loan.setDomain(domain);
        return loanRepository.save(loan);
    }

    public Loan updateLoan(
            UUID loanId,
            String name,
            Domain domain
    ) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanId));
        validateDomain(domain, loan.getDomain());
        loan.setName(name);
        return loanRepository.save(loan);
    }

    public void deleteLoan(UUID loanId, Domain domain) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanId));
        validateDomain(domain, loan.getDomain());
        installmentRepository.deleteAllByLoan(loan);
        loanRepository.delete(loan);
    }

    public Installment createInstallment(
            UUID loanPublicId,
            LocalDate paidAt,
            MonetaryAmount repaidInterest,
            MonetaryAmount repaidAmount,
            MonetaryAmount overpayment,
            Domain domain
    ) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanPublicId));
        validateDomain(domain, loan.getDomain());
        validateCurrency(loan.getPaidAmount().getCurrency(), repaidInterest.getCurrency(), "repaidInterest");
        validateCurrency(loan.getPaidAmount().getCurrency(), repaidAmount.getCurrency(), "repaidAmount");
        validateCurrency(loan.getPaidAmount().getCurrency(), overpayment.getCurrency(), "overpayment");
        Installment installment = new Installment();
        installment.setLoan(loan);
        installment.setPaidAt(paidAt);
        installment.setRepaidInterest(repaidInterest);
        installment.setRepaidAmount(repaidAmount);
        installment.setOverpayment(overpayment);
        return installmentRepository.save(installment);
    }

    public void deleteInstallment(UUID installmentPublicId, Domain domain) {
        Installment installment = Objects.requireNonNull(installmentRepository.findInstallmentByPublicId(installmentPublicId));
        validateDomain(domain, installment.getLoan().getDomain());
        installmentRepository.delete(installment);
    }

    private void validateDomain(Domain domain, Domain otherDomain) {
        if (!domain.getId().equals(otherDomain.getId())) {
            throw new LoansException("Domains doesn't match " + domain.getId() + ":" + otherDomain.getId());
        }
    }

    private void validateCurrency(CurrencyUnit currency, CurrencyUnit otherCurrency, String fieldName) {
        if (!currency.equals(otherCurrency)) {
            throw new LoansException("Currencies of loan and " + fieldName + " doesn't match " + currency + ":" + otherCurrency);
        }
    }

    public List<Loan> findAllDomainLoans(Domain domain) {
        return loanRepository.findAllByDomain(domain);
    }

    public List<RepaymentDayStrategyConfig> findAllDomainRepaymentDayStrategyConfigs(Domain domain) {
        return repaymentDayStrategyConfigRepository.findAllByDomain(domain);
    }

    public List<RateStrategyConfig> findAllDomainRateStrategyConfigs(Domain domain) {
        return rateStrategyConfigRepository.findAllByDomain(domain);
    }

    public NthDayOfMonthRepaymentDayStrategyConfig createNthDayOfMonthStrategy(Domain domain, String name, int nthDayOfMonth) {
        NthDayOfMonthRepaymentDayStrategyConfig nthDayOfMonthRepaymentDayStrategyConfig = new NthDayOfMonthRepaymentDayStrategyConfig();
        nthDayOfMonthRepaymentDayStrategyConfig.setDomain(domain);
        nthDayOfMonthRepaymentDayStrategyConfig.setName(name);
        nthDayOfMonthRepaymentDayStrategyConfig.setDayOfMonth(nthDayOfMonth);
        return repaymentDayStrategyConfigRepository.save(nthDayOfMonthRepaymentDayStrategyConfig);
    }

    public void deleteRateStrategyConfig(UUID publicId, Domain domain) {
        RateStrategyConfig rateStrategyConfig = Objects.requireNonNull(rateStrategyConfigRepository.findByPublicId(publicId));
        validateDomain(rateStrategyConfig.getDomain(), domain);
        rateStrategyConfigRepository.delete(rateStrategyConfig);
    }

    public ConstantForNFirstInstallmentRateStrategyConfig createConstantForNFirstInstallmentRateStrategy(
            Domain domain,
            String name,
            BigDecimal constantRate,
            BigDecimal variableRateMargin,
            int becomesVariableRateAfterNInstallments) {
        ConstantForNFirstInstallmentRateStrategyConfig constantForNFirstInstallmentRateStrategyConfig = new ConstantForNFirstInstallmentRateStrategyConfig();
        constantForNFirstInstallmentRateStrategyConfig.setDomain(domain);
        constantForNFirstInstallmentRateStrategyConfig.setName(name);
        constantForNFirstInstallmentRateStrategyConfig.setConstantRate(constantRate);
        constantForNFirstInstallmentRateStrategyConfig.setVariableRateMargin(variableRateMargin);
        constantForNFirstInstallmentRateStrategyConfig.setBecomesVariableRateAfterNInstallments(becomesVariableRateAfterNInstallments);
        return rateStrategyConfigRepository.save(constantForNFirstInstallmentRateStrategyConfig);
    }

    public void deleteRepaymentDayStrategyConfig(UUID publicId, Domain domain) {
        RepaymentDayStrategyConfig repaymentDayStrategyConfig = Objects.requireNonNull(repaymentDayStrategyConfigRepository.findByPublicId(publicId));
        validateDomain(repaymentDayStrategyConfig.getDomain(), domain);
        repaymentDayStrategyConfigRepository.delete(repaymentDayStrategyConfig);
    }

    public List<LoanCalculationInstallment> simulate(pl.sg.graphql.schema.types.LoanCalculationParams loanCalculationParams) {
        return loanSimulator.simulate(new LoanCalculationParams(
                        loanCalculationParams.getLoanAmount(),
                        loanCalculationParams.getRepaymentStart(),
                        loanCalculationParams.getRate(),
                        loanCalculationParams.getWibor(),
                        loanCalculationParams.getNumberOfInstallments(),
                        loanCalculationParams.getOverpaymentMonthlyBudget(),
                        loanCalculationParams.getOverpaymentYearlyBudget()),
                new ConstantRateStrategy(loanCalculationParams.getRate()),
                (LocalDate lastInstallmentDate) -> lastInstallmentDate.plusMonths(1).withDayOfMonth(1)
        );
    }

    public List<LoanCalculationInstallment> simulateExistingLoan(LoanSimulationParams loanSimulationParams, Domain domain) {
        Loan loan = Objects.requireNonNull(findLoanByPublicId(loanSimulationParams.getLoanId(), domain));
        MonetaryAmount leftToPay = loan.getPaidAmount();
        for (Installment installment : loan.getInstallments()) {
            leftToPay = leftToPay.subtract(installment.getRepaidAmount()).subtract(installment.getOverpayment());
        }
        Optional<Installment> lastRegularInstallment = loan.getInstallments().stream()
                .filter(installment -> !installment.getRepaidInterest().isNegativeOrZero())
                .max(Comparator.comparing(Installment::getPaidAt));

        RateStrategy rateStrategy = rateStrategyFactory.create(loan);
        return loanSimulator.simulate(
                new LoanCalculationParams(
                        leftToPay.getNumber().numberValue(BigDecimal.class),
                        lastRegularInstallment.map(Installment::getPaidAt).map(paidAt -> paidAt.plusDays(1)).orElseGet(loan::getPaymentDate),
                        rateStrategy.getNextInstallmentPercent(),
                        new BigDecimal("0"),
                        loan.getNumberOfInstallments() - (int) loan.getInstallments().stream().filter(installment -> !installment.getRepaidInterest().isNegativeOrZero()).count(),
                        loanSimulationParams.getMonthlyBudget().getAmount(),
                        loanSimulationParams.getYearlyOverpayment().getAmount()),
                rateStrategy,
                repaymentDayStrategyFactory.create(loan)
        );
    }
}
