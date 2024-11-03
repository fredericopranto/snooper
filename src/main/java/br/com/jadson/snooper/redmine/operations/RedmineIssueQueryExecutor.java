package br.com.jadson.snooper.redmine.operations;

import br.com.jadson.snooper.redmine.data.issue.RedmineIssueInfo;
import br.com.jadson.snooper.redmine.data.issue.RedmineIssueInfoDetails;
import br.com.jadson.snooper.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


public class RedmineIssueQueryExecutor extends AbstractRedmineQueryExecutor {

    private ObjectMapper objectMapper;

    public RedmineIssueQueryExecutor() {
        this.objectMapper = new ObjectMapper();
    }

    public RedmineIssueQueryExecutor(String redmineDomain, String redmineToken) {
        if (redmineToken == null || redmineToken.trim().equals("") ) {
            throw new RuntimeException("Invalid Redmine Token: " + redmineToken);
        }
        if (redmineDomain == null || redmineDomain.trim().equals("") ) {
            throw new RuntimeException("Invalid Redmine URL: " + redmineDomain);
        }

        this.redmineURL = redmineDomain;
        this.redmineToken = redmineToken;
    }

    /**
     * Return the issues of a project
     * @param repoFullName
     * @return
     */
    public List<RedmineIssueInfoDetails> issues(String repoFullName) {
        int offset = 0;
        String parameters = "";
        ArrayList all = new ArrayList();

        ResponseEntity result;
        RedmineIssueInfo redmineIssueInfo = null;
        do {
            if (this.queryParameters != null && !this.queryParameters.isEmpty()) {
                parameters = "?" + this.queryParameters + "offset=" + offset + "&limit=" + this.limit;
            } else {
                parameters = "?offset=" + offset + "&limit=" + this.limit;
            }

            parameters += "&project_id=" + repoFullName;

            String query = getRedmineAPIURL() + parameters;

            // encode "/" in "%2F" supported
            URI uri = null;
            try {
                uri = new URI(query);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            System.out.println("Getting Next Issues Info: " + query);
            RestTemplate restTemplate = new RestTemplate();

            checkDisableSslVerification();

            HttpEntity entity = new HttpEntity(this.getDefaultHeaders());
            result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            try {
                redmineIssueInfo = objectMapper.readValue(result.getBody().toString(), RedmineIssueInfo.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            all.addAll(redmineIssueInfo.issues);
            offset = offset + limit;
        } while(result != null && (redmineIssueInfo.issues.size() > 0 && !this.testEnvironment));

        return all;
    }

    public List<RedmineIssueInfoDetails> issuesInPeriod(String repoFullName, LocalDateTime start, LocalDateTime end) {

        List<RedmineIssueInfoDetails> issues = new ArrayList();

        List<RedmineIssueInfoDetails> allIssues = issues(repoFullName);

        DateUtils dateUtils = new DateUtils();

        for (RedmineIssueInfoDetails issue : allIssues) {

            if (issue.createdOn != null) {
                LocalDateTime startIssue = issue.createdOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (dateUtils.isBetweenDates(startIssue, start, end)) {
                    issues.add(issue);
                }
            }
        }

        return issues;
    }
}
