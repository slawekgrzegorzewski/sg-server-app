package pl.sg.checker.engine;

import org.springframework.stereotype.Component;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.PageFetcher;
import pl.sg.checker.model.*;
import pl.sg.checker.service.PageVersionsService;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class CheckerProcessor implements CheckerContext {
    private final PageFetcher fetcher;
    private final PageElementExtractor pageElementExtractor;
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<Class<? extends CheckerStep>, Supplier<StepProcessor<? extends CheckerStep>>> processors = new HashMap<>();
    private CheckerTask task;

    public CheckerProcessor(PageFetcher fetcher, PageElementExtractor pageElementExtractor, PageVersionsService pageVersionsService) {
        this.fetcher = fetcher;
        this.pageElementExtractor = pageElementExtractor;
        processors.put(GetPageContentStep.class, () -> new GetPageStepProcessor(this, this.fetcher));
        processors.put(PageElementExtractorStep.class, () -> new PageElementExtractorStepProcessor(this, this.pageElementExtractor));
        processors.put(EditElementsAttributeStep.class, () -> new EditElementsAttributeStepProcessor(this, this.pageElementExtractor));
        processors.put(ExtractPageElementsStep.class, () -> new ExtractPageElementsStepProcessor(this, this.pageElementExtractor));
        processors.put(FilterElementsStep.class, () -> new FilterElementsStepProcessor(this));
        processors.put(StoreResultStep.class, () -> new StoreResultStepProcessor(this));
        processors.put(SaveResultStep.class, () -> new SaveResultStepProcessor(this, pageVersionsService));
    }

    public void clearContext() {
        getVariables().clear();
        setTask(null);
    }

    @Override
    public CheckerTask getTask() {
        return task;
    }

    public CheckerProcessor setTask(CheckerTask task) {
        this.task = task;
        return this;
    }

    public TaskResult process(CheckerTask checkerTask) {
        this.variables.clear();

        List<CheckerStep> tasks = checkerTask.getSteps().stream()
                .sorted(Comparator.comparing(CheckerStep::getStepOrder))
                .collect(Collectors.toUnmodifiableList());
        Result result = Result.OK;
        List<String> messages = new ArrayList<>();
        for (CheckerStep checkerStep : tasks) {
            TaskResult taskResult = singleStep(checkerStep);
            messages.addAll(taskResult.getMessages());
            if (taskResult.getResult() == Result.WARNING) {
                result = Result.WARNING;
            }
            if (taskResult.getResult() == Result.ERROR) {
                result = Result.ERROR;
                break;
            }
        }
        return new TaskResult(result, messages);
    }

    private <T extends CheckerStep> TaskResult singleStep(T step) {
        StepProcessor<T> stepProcessor = (StepProcessor<T>) this.processors.get(step.getClass()).get();
        stepProcessor.process(step);
        return new TaskResult(stepProcessor.getResult(), stepProcessor.getMessages());
    }

    @Override
    public Map<String, Object> getVariables() {
        return this.variables;
    }

    @Override
    public <T> T getVariable(String name) {
        return (T) variables.get(name);
    }

    @Override
    public <T> void setVariable(String name, T value) {
        variables.put(name, value);
    }
}
