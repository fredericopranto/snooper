package br.com.jadson.snooper.redmine.data.issue;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class RedmineIssueInfoDetails {
    public int id;

    @JsonProperty("project")
    public Project project;

    @JsonProperty("tracker")
    public Tracker tracker;

    @JsonProperty("status")
    public Status status;

    @JsonProperty("priority")
    public Priority priority;

    @JsonProperty("author")
    public Author author;

    @JsonProperty("assigned_to")
    public AssignedTo assignedTo;

    @JsonProperty("parent")
    public Parent parent;

    public String subject;
    public String description;

    @JsonProperty("start_date")
    public String startDate;

    @JsonProperty("due_date")
    public String dueDate;

    @JsonProperty("done_ratio")
    public int doneRatio;

    @JsonProperty("is_private")
    public boolean isPrivate;

    @JsonProperty("estimated_hours")
    public Double estimatedHours;

    @JsonProperty("custom_fields")
    public List<CustomField> customFields;

    @JsonProperty("created_on")
    public Date createdOn;

    @JsonProperty("updated_on")
    public Date updatedOn;

    @JsonProperty("closed_on")
    public Date closedOn;
}
