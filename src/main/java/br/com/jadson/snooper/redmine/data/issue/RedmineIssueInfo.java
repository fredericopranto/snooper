package br.com.jadson.snooper.redmine.data.issue;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RedmineIssueInfo {
    @JsonProperty("issues")
    public List<RedmineIssueInfoDetails> issues;

    @JsonProperty("total_count")
    public int totalCount;

    @JsonProperty("offset")
    public int offset;

    @JsonProperty("limit")
    public int limit;
}
