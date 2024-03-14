package pl.sg.mortgage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanCalculatorConfig {

    public static final String MORE_PRECISE_SCALE = "MORE_PRECISE_SCALE";
    public static final String LESS_PRECISE_SCALE = "LESS_PRECISE_SCALE";

    @Bean(MORE_PRECISE_SCALE)
    int morePreciseScale(){
        return 10;
    }

    @Bean(LESS_PRECISE_SCALE)
    int lessPreciseScale(){
        return 2;
    }
}
