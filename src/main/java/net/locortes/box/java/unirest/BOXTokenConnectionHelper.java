package net.locortes.box.java.unirest;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import net.locortes.box.java.sdk.helper.ResourcesHelper;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;


/**
 * Created by VICENC.CORTESOLEA on 10/10/2016.
 * Based on Abel Salgado Romero's work: https://github.com/abelsromero/box-groovy-wrapper/blob/master/src/main/groovy/org/abelsromero/box/sdk/auth/EnterpriseAPIConnection.groovy
 */
public class BOXTokenConnectionHelper {
    private final String oauth2_url = "https://api.box.com/oauth2/token";

    //The Client ID is the unique identifier of the application created
    private String API_KEY;
    private String PRIMARY_ACCESS_TOKEN;
    private String SECONDARY_ACCESS_TOKEN;

    private String scope;
    private String[] documentIds;

    private int expires_in;

    /**
     *
     * @param configKey
     * @throws URISyntaxException
     * @throws IOException
     */
    public BOXTokenConnectionHelper(String configKey) throws URISyntaxException, IOException {
        ConfigObject config = new ConfigSlurper().parse(ResourcesHelper.getURI("config.groovy").toURL());
        ConfigObject appConfig = (ConfigObject) config.get(configKey);

        API_KEY = (String) appConfig.get("apiKey");
        PRIMARY_ACCESS_TOKEN = (String) appConfig.get("primaryAccessToken");
        SECONDARY_ACCESS_TOKEN = (String) appConfig.get("secondaryAccessToken");

        expires_in = 1;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public BOXConnection getFileTokenPrimary() throws Exception {
        return getFileToken(PRIMARY_ACCESS_TOKEN, null);
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public BOXConnection getFileTokenSecondary() throws Exception {
        return getFileToken(SECONDARY_ACCESS_TOKEN, null);
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public BOXConnection getFileTokenPrimary(String documentId) throws Exception {
        return getFileToken(PRIMARY_ACCESS_TOKEN, documentId);
    }

    /**
     * https://docs.box.com/docs/getting-started-with-new-box-view
     * @param accessToken
     * @param fileID
     * @return
     * @throws Exception
     */
    private BOXConnection getFileToken(String accessToken, String fileID) throws Exception {
        MultipartBody requestWithBody = Unirest.post(oauth2_url)
                .header("Accept-Encoding", "gzip")
                .header("Accept-Charset", "utf-8")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("subject_token", URLEncoder.encode(accessToken, "UTF-8"))
                .field("subject_token_type", "urn:ietf:params:oauth:token-type:access_token")
                .field("scope", URLEncoder.encode(scope, "UTF-8"))
                .field("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");

        if(fileID != null)
            requestWithBody.field("resource", "https://api.box.com/2.0/files/" + fileID);

        HttpResponse<JsonNode> jsonResponse = requestWithBody.asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return new Gson().fromJson(String.valueOf(body), BOXConnection.class);
        } else
            throw new Exception("Error managing authentication with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText() + " and body " + jsonResponse.getBody());
    }


    int getExpires_in() {
        return expires_in;
    }

    void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
