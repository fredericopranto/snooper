package br.com.jadson.snooper.redmine.operations;

import br.com.jadson.snooper.utils.ConnectionUtils;
import org.springframework.http.HttpHeaders;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Common functions to execute queries on redmine using redmine API https://www.redmine.org/projects/redmine/wiki/Rest_api/
 */
abstract class AbstractRedmineQueryExecutor {

    /**
     * We can have the gitlab running on-premise, like redmine.ufrn.br, the API url will be "https://redmine.ufrn.br/api/v4".
     */
    protected String redmineURL = "";
    protected String redmineToken = "";
    protected int limit = 5;
    protected String queryParameters;
    protected boolean testEnvironment = false;

    /**
     * Ignore invalid SSL certificates to specific scenarios
     */
    protected boolean disableSslVerification = false;

    /**
     * Return the Gitlab URL to specific gitlab instance.
     *
     * The following is a basic example of a request to the fictional gitlab.example.com endpoint:
     * "https://gitlab.example.com/api/v4/projects"
     *
     * @return
     */
    public final String getRedmineAPIURL() {
        return redmineURL +"/issues.json";
    }

    public void setLimit(int limit) {
        if (limit >= 0 && limit <= 100) {
            this.limit = limit;
        } else {
            throw new RuntimeException("Invalid Limit: " + limit + " see: https://docs.gitlab.com/ee/api/#pagination");
        }
    }

    public void setQueryParameters(String[] parametersArrays) {
        if (parametersArrays != null && parametersArrays.length != 0) {
            this.queryParameters = "";
            String[] var2 = parametersArrays;
            int var3 = parametersArrays.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String p = var2[var4];
                this.queryParameters = this.queryParameters + p + "&";
            }

        } else {
            throw new RuntimeException("Invalid Query Parameters: " + this.queryParameters);
        }
    }

    protected final void validateRepoName(String repoFullName) {
        if (repoFullName != null && !repoFullName.trim().equals("")) {
            if (!repoFullName.contains("/")) {
                throw new RuntimeException("Invalid GitHub repo full name: " + repoFullName + ". The name should be owner/repo");
            }
        } else {
            throw new RuntimeException("Invalid GitHub repo full name: " + repoFullName);
        }
    }

    /**
     * https://docs.gitlab.com/ee/api/rest/#personalprojectgroup-access-tokens
     * @return
     */
    protected HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        if (this.redmineToken != null && ! this.redmineToken.isEmpty() ) {
            headers.set("X-Redmine-API-Key", this.redmineToken);
        }
        return headers;
    }

    /*
     * If using namespaced API requests, make sure that the NAMESPACE/PROJECT_PATH is URL-encoded.
     * For example, / is represented by %2F:
     * GET /api/v4/projects/diaspora%2Fdiaspora
     */
    protected static String encodeProjectName(String repoFullName) {
        repoFullName = repoFullName.replace("/", "%2F");
        return repoFullName;
    }

    public void setRedmineToken(String redmineToken) {
        this.redmineToken = redmineToken;
    }

    public void setRedmineURL(String redmineURL) {
        this.redmineURL = redmineURL;
    }

    public void setTestEnvironment(boolean testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    public void setDisableSslVerification(boolean disableSslVerification) {
        this.disableSslVerification = disableSslVerification; }

    public String getQueryParameters() {
        return this.queryParameters;
    }

    /**
     * Check if should disalbbe ssl verification
     */
    protected void checkDisableSslVerification() {
        if(disableSslVerification) {
            try {
                new ConnectionUtils().disableSslVerification();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (KeyManagementException e) {
                throw new RuntimeException(e);
            }
        }
    }
}