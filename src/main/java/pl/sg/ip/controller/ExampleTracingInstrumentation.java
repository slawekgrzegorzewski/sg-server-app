package pl.sg.ip.controller;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExampleTracingInstrumentation extends SimpleInstrumentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleTracingInstrumentation.class);
    private final Map<String, Long> times = new ConcurrentHashMap<>();

    @Override
    public InstrumentationState createState() {
        return new TracingState();
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        TracingState tracingState = parameters.getInstrumentationState();
        tracingState.startTime = System.nanoTime();
        return super.beginExecution(parameters);
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        // We only care about user code
//        if (parameters.isTrivialDataFetcher()) {
//            return dataFetcher;
//        }

        return environment -> {
            long startTime = System.nanoTime();
            Object result = dataFetcher.get(environment);
            String tag = findDatafetcherTag(parameters);
            if (result instanceof CompletableFuture) {
                ((CompletableFuture<?>) result).whenComplete((r, ex) -> {
                    long totalTime = System.nanoTime() - startTime;
                    times.compute(tag, (t, value) -> {
                        if (value == null) return totalTime;
                        return value + totalTime;
                    });
//                    LOGGER.info("Async datafetcher {} took {}ms", tag, totalTime / 1000000.0);
                });
            } else {
                long totalTime = System.nanoTime() - startTime;
                times.compute(tag, (t, value) -> {
                    if (value == null) return totalTime;
                    return value + totalTime;
                });
//                LOGGER.info("Datafetcher {} took {}ms", tag, totalTime / 1000000.0);
//                System.out.println(findDatafetcherTag(parameters) + ";" + totalTime);
            }

            return result;
        };
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        TracingState tracingState = parameters.getInstrumentationState();
        System.out.println("times = " + times);
        long totalTime = System.nanoTime() - tracingState.startTime;
        LOGGER.info("Total execution time: {}ms", totalTime / 1000000.0);

        return super.instrumentExecutionResult(executionResult, parameters);
    }

    private String findDatafetcherTag(InstrumentationFieldFetchParameters parameters) {
        GraphQLOutputType type = parameters.getExecutionStepInfo().getParent().getType();
        GraphQLObjectType parent;
        if (type instanceof GraphQLNonNull) {
            parent = (GraphQLObjectType) ((GraphQLNonNull) type).getWrappedType();
        } else {
            parent = (GraphQLObjectType) type;
        }

        return parent.getName() + "." + parameters.getExecutionStepInfo().getPath().getSegmentName();
    }

    static class TracingState implements InstrumentationState {
        long startTime;
    }
}

