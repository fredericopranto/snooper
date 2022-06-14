package br.com.jadson.snooper.coverall.operations;

import br.com.jadson.snooper.coverall.data.CoveAllRepositoryInfo;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Return informatino about repo
 *
 * https://coveralls.io/api/docs
 */
public class CoveAllRepoQueryExecutor extends AbstractCoveAllQueryExecutor{

    public CoveAllRepoQueryExecutor(){ }

    public CoveAllRepoQueryExecutor(String token){
        if(token == null || token.trim().equals(""))
            throw new RuntimeException("Invalid Token: "+token);

        this.token = token;
    }


    /**
     * Return information about repository, or 404 if not exit
     * @param projectName
     * @param service
     * @return
     */
    public CoveAllRepositoryInfo getRepoInfo(String projectName, CODE_ALL_SERVICE service){


        ResponseEntity<CoveAllRepositoryInfo> result;

        /****************************************************************************
         * ENDPOINT
         * GET /api/repos/:service/:repo_user/:repo_name
         ***************************************************************************/
        String query = CODE_ALL_API_URL +"/api/repos" +"/"+ service.getValue()+"/"+projectName;

        System.out.println("Getting Next CodeAll Repo Infomation: "+query);

        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = getDefaultHeaders();

            // allow '%' character in URL
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(query);
            URI uri = builder.build(false).toUri();

            HttpEntity entity = new HttpEntity<>(headers);
            result = restTemplate.exchange(uri, HttpMethod.GET, entity, CoveAllRepositoryInfo.class);

            if (result.getStatusCode() == HttpStatus.OK)
                return result.getBody();
            else
                return null;
        }catch (Exception ex){
            System.err.println(">>>>> Error: "+ex.getMessage());
            return null;
        }

    }




}
