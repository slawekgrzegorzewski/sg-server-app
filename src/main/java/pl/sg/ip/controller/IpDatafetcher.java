package pl.sg.ip.controller;

import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.DgsConstants;
import pl.sg.graphql.schema.types.*;
import pl.sg.ip.graphql.IntellectualPropertyContext;
import pl.sg.ip.service.IntellectualPropertyService;
import pl.sg.ip.service.TaskService;

import java.util.List;

@DgsComponent
public class IpDatafetcher {
    private final IntellectualPropertyService intellectualPropertyService;
    private final TaskService taskService;

    public IpDatafetcher(IntellectualPropertyService intellectualPropertyService, TaskService taskService) {
        this.intellectualPropertyService = intellectualPropertyService;
        this.taskService = taskService;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"IPR"})
    public List<IntellectualProperty> allIPRs(
            @RequestHeader("domainId") int domainId,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        IntellectualPropertyContext intellectualPropertyContext = DgsContext.getCustomContext(dataFetchingEnvironment);
        intellectualPropertyContext.fetchForDomain(domainId);
        return intellectualPropertyContext.getIntellectualProperties();
    }

    @DgsData(parentType = DgsConstants.INTELLECTUALPROPERTY.TYPE_NAME, field = DgsConstants.INTELLECTUALPROPERTY.Tasks)
    @TokenBearerAuth(any = {"IPR"})
    public List<Task> tasks(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        IntellectualPropertyContext intellectualPropertyContext = DgsContext.getCustomContext(dataFetchingEnvironment);
        IntellectualProperty intellectualProperty = dataFetchingEnvironment.getSource();
        return intellectualPropertyContext.getTasks().get(intellectualProperty.getId());
    }

    @DgsData(parentType = DgsConstants.TASK.TYPE_NAME, field = DgsConstants.TASK.TimeRecords)
    @TokenBearerAuth(any = {"IPR"})
    public List<TimeRecord> timeRecords(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        IntellectualPropertyContext intellectualPropertyContext = DgsContext.getCustomContext(dataFetchingEnvironment);
        Task task = dataFetchingEnvironment.getSource();
        return intellectualPropertyContext.getTimeRecords().get(task.getId());
    }

    @DgsData(parentType = DgsConstants.TASK.TYPE_NAME, field = DgsConstants.TASK.Attachments)
    @TokenBearerAuth(any = {"IPR"})
    public List<String> attachments(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        IntellectualPropertyContext intellectualPropertyContext = DgsContext.getCustomContext(dataFetchingEnvironment);
        Task task = dataFetchingEnvironment.getSource();
        return intellectualPropertyContext.getAttachments().get(task.getId());
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public IntellectualProperty addIPR(
            @RequestHeader("domainId") int domainId,
            @InputArgument IntellectualPropertyData input,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        IntellectualPropertyContext intellectualPropertyContext = DgsContext.getCustomContext(dataFetchingEnvironment);
        intellectualPropertyContext.fetchSingleForIntellectualProperty(intellectualPropertyService.create(domainId, input));
        return intellectualPropertyContext.getIntellectualProperty();
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public IntellectualProperty updateIPR(
            @RequestHeader("domainId") int domainId,
            @InputArgument int intellectualPropertyId,
            @InputArgument IntellectualPropertyData input,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        intellectualPropertyService.update(domainId, intellectualPropertyId, input);
        IntellectualPropertyContext intellectualPropertyContext = DgsContext.getCustomContext(dataFetchingEnvironment);
        intellectualPropertyContext.fetchSingle(intellectualPropertyId);
        return intellectualPropertyContext.getIntellectualProperty();
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String deleteIPR(
            @RequestHeader(value = "domainId") int domainId,
            @InputArgument int intellectualPropertyId) {
        intellectualPropertyService.delete(domainId, intellectualPropertyId);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String createTask(
            @RequestHeader(value = "domainId") int domainId,
            @InputArgument int intellectualPropertyId,
            @RequestBody TaskData taskData) {
        intellectualPropertyService.createTask(domainId, intellectualPropertyId, taskData);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String updateTask(
            @RequestHeader(value = "domainId") int domainId,
            @InputArgument int taskId,
            @InputArgument TaskData taskData) {
        taskService.update(domainId, taskId, taskData);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String deleteTask(
            @RequestHeader("domainId") int domainId,
            @InputArgument int taskId) {
        taskService.delete(domainId, taskId);
        return "OK";
    }
}