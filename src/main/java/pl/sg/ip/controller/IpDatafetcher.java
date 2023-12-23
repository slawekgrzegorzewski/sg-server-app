package pl.sg.ip.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.*;
import pl.sg.ip.graphql.IntellectualPropertyContext;
import pl.sg.ip.service.IntellectualPropertyService;
import pl.sg.ip.service.TaskService;
import pl.sg.ip.service.TimeRecordService;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class IpDatafetcher {
    private final IntellectualPropertyService intellectualPropertyService;
    private final ModelMapper modelMapper;
    private final TaskService taskService;
    private final TimeRecordService timeRecordService;

    public IpDatafetcher(IntellectualPropertyService intellectualPropertyService,
                         ModelMapper modelMapper,
                         TaskService taskService,
                         TimeRecordService timeRecordService) {
        this.intellectualPropertyService = intellectualPropertyService;
        this.modelMapper = modelMapper;
        this.taskService = taskService;
        this.timeRecordService = timeRecordService;
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

    @DgsQuery
    @TokenBearerAuth(any = {"IPR"})
    public List<TimeRecord> nonIPTimeRecords(
            @RequestHeader("domainId") int domainId
    ) {
        return timeRecordService.getUnassociatedTimeRecords(domainId)
                .stream()
                .map(tr -> modelMapper.map(tr, TimeRecord.class))
                .collect(Collectors.toList());
    }

    @DgsQuery
    @TokenBearerAuth(any = {"IPR"})
    public List<TimeRecordCategory> allTimeRecordCategories(
            @RequestHeader("domainId") int domainId
    ) {
        return timeRecordService.getAllTimeRecordCategories(domainId)
                .stream()
                .map(tr -> modelMapper.map(tr, TimeRecordCategory.class))
                .collect(Collectors.toList());
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public IntellectualProperty addIPR(
            @RequestHeader("domainId") int domainId,
            @InputArgument("input") IntellectualPropertyData input,
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
            @InputArgument("intellectualPropertyId") int intellectualPropertyId,
            @InputArgument("input") IntellectualPropertyData input,
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
            @InputArgument("intellectualPropertyId") int intellectualPropertyId) {
        intellectualPropertyService.delete(domainId, intellectualPropertyId);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String createTask(
            @RequestHeader(value = "domainId") int domainId,
            @InputArgument("intellectualPropertyId") int intellectualPropertyId,
            @RequestBody TaskData taskData) {
        intellectualPropertyService.createTask(domainId, intellectualPropertyId, taskData);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String updateTask(
            @RequestHeader(value = "domainId") int domainId,
            @InputArgument("taskId") int taskId,
            @InputArgument("taskData") TaskData taskData) {
        taskService.update(domainId, taskId, taskData);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String deleteTask(
            @RequestHeader("domainId") int domainId,
            @InputArgument("taskId") int taskId) {
        taskService.delete(domainId, taskId);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public TimeRecord createTimeRecord(
            @RequestHeader("domainId") int domainId,
            @InputArgument("timeRecordData") TimeRecordData timeRecordData) {
        return modelMapper.map(timeRecordService.create(domainId, timeRecordData), TimeRecord.class);
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String updateTimeRecord(
            @RequestHeader("domainId") int domainId,
            @InputArgument("timeRecordId") int timeRecordId,
            @InputArgument("timeRecordData") TimeRecordData timeRecordData) {
        this.timeRecordService.update(domainId, timeRecordId, timeRecordData);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String deleteTimeRecord(
            @RequestHeader("domainId") int domainId,
            @InputArgument("timeRecordId") int timeRecordId) {
        this.timeRecordService.delete(domainId, timeRecordId);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String assignCategoryToTimeRecord(
            @RequestHeader("domainId") int domainId,
            @InputArgument("timeRecordId") int timeRecordId,
            @InputArgument("timeRecordCategoryId") Integer timeRecordCategoryId) {
        if (timeRecordCategoryId == null) {
            this.timeRecordService.clearCategoryOnTimeRecord(domainId, timeRecordId);
        } else {
            this.timeRecordService.assignCategoryToTimeRecord(domainId, timeRecordId, timeRecordCategoryId);
        }
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public TimeRecordCategory createTimeRecordCategory(
            @RequestHeader("domainId") int domainId,
            @InputArgument("name") String name) {
        return modelMapper.map(
                this.timeRecordService.createTimeRecordCategory(name, domainId),
                TimeRecordCategory.class
        );
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String updateTimeRecordCategory(
            @RequestHeader("domainId") int domainId,
            @InputArgument("timeRecordId") int timeRecordId,
            @InputArgument("name") String name) {
        this.timeRecordService.updateTimeRecordCategory(domainId, timeRecordId, name);
        return "OK";
    }

    @DgsMutation
    @TokenBearerAuth(any = "IPR")
    public String deleteTimeRecordCategory(
            @RequestHeader("domainId") int domainId,
            @InputArgument("timeRecordId") int timeRecordId) {
        this.timeRecordService.deleteTimeRecordCategory(domainId, timeRecordId);
        return "OK";
    }
}