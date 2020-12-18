package pl.sg.checker.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.checker.engine.CheckerProcessor;
import pl.sg.checker.engine.TaskResult;
import pl.sg.checker.model.CheckerTask;
import pl.sg.checker.model.CheckerTaskHistory;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class Checker {
    private final CheckerProcessor processor;
    private final CheckerTaskRepository checkerTaskRepository;
    private final CheckerTaskHistoryRepository checkerTaskHistoryRepository;


    public Checker(CheckerProcessor processor, CheckerTaskRepository checkerTaskRepository, CheckerTaskHistoryRepository checkerTaskHistoryRepository) {
        this.processor = processor;
        this.checkerTaskRepository = checkerTaskRepository;
        this.checkerTaskHistoryRepository = checkerTaskHistoryRepository;
    }

    @Scheduled(fixedDelay = 1 * 1000)
    public void scheduleFixedDelayTask() {
        List<CheckerTask> tasks = this.checkerTaskRepository.findTasksToRun();
        for (CheckerTask task : tasks) {
            processor.clearContext();
            processor.setTask(task);
            TaskResult result = processor.process(task);
            CheckerTaskHistory history = new CheckerTaskHistory()
                    .setTask(task)
                    .setResult(result.getResult())
                    .setMessages(result.getMessages())
                    .setRunTime(LocalDateTime.now());
            checkerTaskHistoryRepository.save(history);
            task.getHistory().add(history);
            task.setNextRun(history.getRunTime().plus(task.getInterval()));
            checkerTaskRepository.save(task);
            processor.clearContext();
        }
    }

}
