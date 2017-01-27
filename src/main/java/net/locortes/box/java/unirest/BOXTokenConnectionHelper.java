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
 * Created by VICENC.CORTESOLEA on 25/01/2017.
  */
public class BOXTokenConnectionHelper {
    //URL to get a TOKEN
    private final String url_template_oauth2 = "https://api.box.com/oauth2/token";

    //The Client ID is the unique identifier of the application created
    private String API_KEY;
    private String PRIMARY_ACCESS_TOKEN;
    private String SECONDARY_ACCESS_TOKEN;

    //Different available scopes
    public final static String SCOPE_UPLOAD = "item_content_upload";
    public final static String SCOPE_PREVIEW = "item_preview";
    public final static String SCOPE_DELETE = "item_delete";

    //This is the default scope. Other valid scopes are item_content_upload and item_delete
    private String scope = "item_preview";

    //This is the fileId where the scope is limited to
    private String fileID = null;

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
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public BOXConnection getFileTokenPrimary() throws Exception {
        return getFileToken(PRIMARY_ACCESS_TOKEN);
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public BOXConnection getFileTokenSecondary() throws Exception {
        return getFileToken(SECONDARY_ACCESS_TOKEN);
    }

    /**
     * https://docs.box.com/docs/getting-started-with-new-box-view
     * @param accessToken
     * @return
     * @throws Exception
     */
    private BOXConnection getFileToken(String accessToken) throws Exception {
        MultipartBody requestWithBody = Unirest.post(url_template_oauth2)
                .header("Accept-Encoding", "gzip")
                .header("Accept-Charset", "utf-8")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("subject_token", URLEncoder.encode(accessToken, "UTF-8"))
                .field("subject_token_type", "urn:ietf:params:oauth:token-type:access_token")
                .field("scope", scope)
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

    /**
     *
     * @return
     */
    public String getScope() {
        return scope;
    }

    /**
     *
     * @param scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     *
     * @return
     */
    public String getFileID() {
        return fileID;
    }

    /**
     *
     * @param fileID
     */
    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getPRIMARY_ACCESS_TOKEN() {
        return PRIMARY_ACCESS_TOKEN;
    }

    public void setPRIMARY_ACCESS_TOKEN(String PRIMARY_ACCESS_TOKEN) {
        this.PRIMARY_ACCESS_TOKEN = PRIMARY_ACCESS_TOKEN;
    }

    public String getSECONDARY_ACCESS_TOKEN() {
        return SECONDARY_ACCESS_TOKEN;
    }

    public void setSECONDARY_ACCESS_TOKEN(String SECONDARY_ACCESS_TOKEN) {
        this.SECONDARY_ACCESS_TOKEN = SECONDARY_ACCESS_TOKEN;
    }
}
