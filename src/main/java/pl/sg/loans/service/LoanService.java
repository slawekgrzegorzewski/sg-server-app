package pl.sg.loans.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.sg.application.repository.DomainRepository;
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
import pl.sg.loans.utils.RateStrategy;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static pl.sg.application.CurrencyValidator.validateCurrency;
import static pl.sg.application.DomainValidator.validateDomain;

@Component
public class LoanService {

    private static final Logger LOG = LoggerFactory.getLogger(LoanService.class);
    private final DomainRepository domainRepository;
    private final InstallmentRepository installmentRepository;
    private final LoanRepository loanRepository;
    private final LoanSimulator loanSimulator;
    private final RepaymentDayStrategyConfigRepository repaymentDayStrategyConfigRepository;
    private final RepaymentDayStrategyFactory repaymentDayStrategyFactory;
    private final RateStrategyConfigRepository rateStrategyConfigRepository;
    private final RateStrategyFactory rateStrategyFactory;

    public LoanService(DomainRepository domainRepository, InstallmentRepository installmentRepository,
                       LoanRepository loanRepository, LoanSimulator loanSimulator,
                       RepaymentDayStrategyConfigRepository repaymentDayStrategyConfigRepository, RepaymentDayStrategyFactory repaymentDayStrategyFactory,
                       RateStrategyConfigRepository rateStrategyConfigRepository, RateStrategyFactory rateStrategyFactory) {
        this.domainRepository = domainRepository;
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
            int domainId
    ) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanPublicId));
        validateDomain(domainRepository.getReferenceById(domainId), loan.getDomain());
        return loan;
    }

    public Loan createLoan(
            String name,
            LocalDate paymentDate,
            int numberOfInstallments,
            UUID repaymentDayStrategyConfigPublicId,
            UUID rateStrategyConfigPublicId,
            MonetaryAmount paidAmount,
            int domainId
    ) {
        RepaymentDayStrategyConfig repaymentDayStrategyConfig = Objects.requireNonNull(repaymentDayStrategyConfigRepository.findByPublicId(repaymentDayStrategyConfigPublicId));
        validateDomain(domainId, repaymentDayStrategyConfig.getDomain());
        RateStrategyConfig rateStrategyConfig = Objects.requireNonNull(rateStrategyConfigRepository.findByPublicId(rateStrategyConfigPublicId));
        validateDomain(domainId, rateStrategyConfig.getDomain());
        Loan loan = new Loan();
        loan.setName(name);
        loan.setPaymentDate(paymentDate);
        loan.setNumberOfInstallments(numberOfInstallments);
        loan.setRepaymentDayStrategyConfig(repaymentDayStrategyConfig);
        loan.setRateStrategyConfig(rateStrategyConfig);
        loan.setPaidAmount(paidAmount);
        loan.setDomain(domainRepository.getReferenceById(domainId));
        return loanRepository.save(loan);
    }

    public Loan updateLoan(
            UUID loanId,
            String name,
            int domainId
    ) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanId));
        validateDomain(domainId, loan.getDomain());
        loan.setName(name);
        return loanRepository.save(loan);
    }

    public void deleteLoan(UUID loanId, int domainId) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanId));
        validateDomain(domainId, loan.getDomain());
        installmentRepository.deleteAllByLoan(loan);
        loanRepository.delete(loan);
    }

    public Installment createInstallment(
            UUID loanPublicId,
            LocalDate paidAt,
            MonetaryAmount repaidInterest,
            MonetaryAmount repaidAmount,
            MonetaryAmount overpayment,
            int domainId
    ) {
        Loan loan = Objects.requireNonNull(loanRepository.findByPublicId(loanPublicId));
        validateDomain(domainId, loan.getDomain());
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

    public void deleteInstallment(UUID installmentPublicId, int domainId) {
        Installment installment = Objects.requireNonNull(installmentRepository.findInstallmentByPublicId(installmentPublicId));
        validateDomain(domainId, installment.getLoan().getDomain());
        installmentRepository.delete(installment);
    }

    public List<Loan> findAllDomainLoans(int domainId) {
        return loanRepository.findAllByDomain(domainRepository.getReferenceById(domainId));
    }

    public List<RepaymentDayStrategyConfig> findAllDomainRepaymentDayStrategyConfigs(int domainId) {
        return repaymentDayStrategyConfigRepository.findAllByDomain(domainRepository.getReferenceById(domainId));
    }

    public List<RateStrategyConfig> findAllDomainRateStrategyConfigs(int domainId) {
        return rateStrategyConfigRepository.findAllByDomain(domainRepository.getReferenceById(domainId));
    }

    public NthDayOfMonthRepaymentDayStrategyConfig createNthDayOfMonthStrategy(int domainId, String name, int nthDayOfMonth) {
        NthDayOfMonthRepaymentDayStrategyConfig nthDayOfMonthRepaymentDayStrategyConfig = new NthDayOfMonthRepaymentDayStrategyConfig();
        nthDayOfMonthRepaymentDayStrategyConfig.setDomain(domainRepository.getReferenceById(domainId));
        nthDayOfMonthRepaymentDayStrategyConfig.setName(name);
        nthDayOfMonthRepaymentDayStrategyConfig.setDayOfMonth(nthDayOfMonth);
        return repaymentDayStrategyConfigRepository.save(nthDayOfMonthRepaymentDayStrategyConfig);
    }

    public void deleteRateStrategyConfig(UUID publicId, int domainId) {
        RateStrategyConfig rateStrategyConfig = Objects.requireNonNull(rateStrategyConfigRepository.findByPublicId(publicId));
        validateDomain(rateStrategyConfig.getDomain(), domainRepository.getReferenceById(domainId));
        rateStrategyConfigRepository.delete(rateStrategyConfig);
    }

    public ConstantForNFirstInstallmentRateStrategyConfig createConstantForNFirstInstallmentRateStrategy(
            int domainId,
            String name,
            BigDecimal constantRate,
            BigDecimal variableRateMargin,
            int becomesVariableRateAfterNInstallments) {
        ConstantForNFirstInstallmentRateStrategyConfig constantForNFirstInstallmentRateStrategyConfig = new ConstantForNFirstInstallmentRateStrategyConfig();
        constantForNFirstInstallmentRateStrategyConfig.setDomain(domainRepository.getReferenceById(domainId));
        constantForNFirstInstallmentRateStrategyConfig.setName(name);
        constantForNFirstInstallmentRateStrategyConfig.setConstantRate(constantRate);
        constantForNFirstInstallmentRateStrategyConfig.setVariableRateMargin(variableRateMargin);
        constantForNFirstInstallmentRateStrategyConfig.setBecomesVariableRateAfterNInstallments(becomesVariableRateAfterNInstallments);
        return rateStrategyConfigRepository.save(constantForNFirstInstallmentRateStrategyConfig);
    }

    public void deleteRepaymentDayStrategyConfig(UUID publicId, int domainId) {
        RepaymentDayStrategyConfig repaymentDayStrategyConfig = Objects.requireNonNull(repaymentDayStrategyConfigRepository.findByPublicId(publicId));
        validateDomain(repaymentDayStrategyConfig.getDomain(), domainRepository.getReferenceById(domainId));
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

    public List<LoanCalculationInstallment> simulateExistingLoan(LoanSimulationParams loanSimulationParams, int domainId) {
        Loan loan = Objects.requireNonNull(findLoanByPublicId(loanSimulationParams.getLoanId(), domainId));
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
                        lastRegularInstallment.map(Installment::getPaidAt).orElseGet(loan::getPaymentDate),
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
