package br.com.jadson.snooper.redmine.data.issue;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;
}