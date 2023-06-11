package pl.sg.mortgage;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.MortgageCalculationInstallment;
import pl.sg.graphql.schema.types.MortgageCalculationParams;

import java.util.List;

@DgsComponent
public class MortgageDatafetcher {
    private final MortgageSimulator mortgageSimulator;

    public MortgageDatafetcher(MortgageSimulator mortgageSimulator) {
        this.mortgageSimulator = mortgageSimulator;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<MortgageCalculationInstallment> simulateMortgage(
            @InputArgument MortgageCalculationParams mortgageCalculationParams
    ) {
        return mortgageSimulator.simulate(mortgageCalculationParams);
    }
}