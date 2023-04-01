package pl.sg.ip.graphql;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import pl.sg.graphql.schema.types.IntellectualProperty;
import pl.sg.graphql.schema.types.Task;
import pl.sg.graphql.schema.types.TimeRecord;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static pl.sg.jooq.Tables.*;
import static pl.sg.jooq.tables.Domain.DOMAIN;

public class IntellectualPropertyContext {

    private final DSLContext dslContext;
    private final ModelMapper modelMapper;
    List<IntellectualProperty> intellectualProperties;
    private Map<Integer, List<Task>> tasks;
    private Map<Integer, List<String>> attachments;
    private Map<Integer, List<TimeRecord>> timeRecords;

    public IntellectualPropertyContext(DSLContext dslContext, ModelMapper modelMapper) {
        this.dslContext = dslContext;
        this.modelMapper = modelMapper;
    }

    public void fetchSingleForIntellectualProperty(pl.sg.ip.model.IntellectualProperty intellectualProperty) {
        intellectualProperties = List.of(modelMapper.map(intellectualProperty, IntellectualProperty.class));
        fetchTasksSubtree(List.of(intellectualProperty.getId()));
    }

    public void fetchSingle(int intellectualPropertyId) {
        fetchFullTree(INTELLECTUAL_PROPERTY.ID.eq(intellectualPropertyId));
    }

    public void fetchForDomain(int domainId) {
        fetchFullTree(INTELLECTUAL_PROPERTY.DOMAIN_ID.eq(domainId));
    }

    private void fetchFullTree(Condition intellectualPropertyConstraint) {
        intellectualProperties = List.copyOf(
                dslContext.select(
                                INTELLECTUAL_PROPERTY.asterisk(),
                                DOMAIN.ID.as("domain_id"),
                                DOMAIN.NAME.as("domain_name")
                        )
                        .from(INTELLECTUAL_PROPERTY)
                        .join(DOMAIN).on(DOMAIN.ID.eq(INTELLECTUAL_PROPERTY.DOMAIN_ID))
                        .where(intellectualPropertyConstraint)
                        .fetch()
                        .map(record -> modelMapper.map(record, IntellectualProperty.class)));
        fetchTasksSubtree(intellectualProperties.stream().map(IntellectualProperty::getId).toList());
    }

    private void fetchTasksSubtree(List<Integer> intellectualPropertiesIds) {
        tasks = Map.copyOf(
                dslContext
                        .select(
                                TASK.asterisk(),
                                TASK_CATEGORY.ID.as("task_category_id"),
                                TASK_CATEGORY.NAME.as("task_category_name"))
                        .from(TASK)
                        .leftJoin(TASK_CATEGORY)
                        .on(TASK.TASK_CATEGORY_ID.eq(TASK_CATEGORY.ID))
                        .where(TASK.INTELLECTUAL_PROPERTY_ID.in(intellectualPropertiesIds))
                        .fetch()
                        .intoGroups(TASK.INTELLECTUAL_PROPERTY_ID, task -> modelMapper.map(task, Task.class)));
        List<Integer> list = tasks.values().stream().flatMap(List::stream).map(Task::getId).toList();
        fetchAttachments(list);
        fetchTimeRecords(list);
    }

    private void fetchAttachments(List<Integer> tasksIds) {
        attachments = Map.copyOf(
                dslContext
                        .select(TASK_ATTACHMENTS.asterisk())
                        .from(TASK_ATTACHMENTS)
                        .where(TASK_ATTACHMENTS.TASK_ID.in(tasksIds))
                        .fetch()
                        .intoGroups(TASK_ATTACHMENTS.TASK_ID, attachment -> attachment.get(TASK_ATTACHMENTS.ATTACHMENTS)));
    }

    private void fetchTimeRecords(List<Integer> tasksIds) {
        timeRecords = Map.copyOf(
                dslContext
                        .select(
                                TIME_RECORD.asterisk(),
                                DOMAIN.ID.as("domain_id"),
                                DOMAIN.NAME.as("domain_name"))
                        .from(TIME_RECORD)
                        .join(DOMAIN)
                        .on(TIME_RECORD.DOMAIN_ID.eq(DOMAIN.ID))
                        .where(TIME_RECORD.TASK_ID.in(tasksIds))
                        .fetch()
                        .intoGroups(TIME_RECORD.TASK_ID, task -> modelMapper.map(task, TimeRecord.class)));
    }

    public List<IntellectualProperty> getIntellectualProperties() {
        return intellectualProperties;
    }

    public IntellectualProperty getIntellectualProperty() {
        if (intellectualProperties.isEmpty())
            throw new NoSuchElementException("No value present");
        return intellectualProperties.get(0);
    }

    public Map<Integer, List<Task>> getTasks() {
        return tasks;
    }

    public Map<Integer, List<String>> getAttachments() {
        return attachments;
    }

    public Map<Integer, List<TimeRecord>> getTimeRecords() {
        return timeRecords;
    }
}
