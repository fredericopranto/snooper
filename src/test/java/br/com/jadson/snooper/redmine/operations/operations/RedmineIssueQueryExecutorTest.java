package br.com.jadson.snooper.redmine.operations.operations;

import br.com.jadson.snooper.redmine.data.issue.RedmineIssueInfoDetails;
import br.com.jadson.snooper.redmine.operations.RedmineIssueQueryExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RedmineIssueQueryExecutorTest {

    String token = System.getenv("REDMINE_TOKEN");

    @Test
    void testIssuesProject(){

        RedmineIssueQueryExecutor commitExecutor = new RedmineIssueQueryExecutor();
        commitExecutor.setQueryParameters(new String[]{"status_id=*"});
        commitExecutor.setLimit(100);
        commitExecutor.setRedmineToken(token);
        commitExecutor.setTestEnvironment(true);
        List<RedmineIssueInfoDetails> commitsInfo = commitExecutor.issues("jadsonjs/holter-ci");

        Assertions.assertTrue(commitsInfo.size() == 1);
        Assertions.assertEquals("redemine integration", commitsInfo.get(0).description);
    }
}