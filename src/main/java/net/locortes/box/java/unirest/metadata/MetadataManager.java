package net.locortes.box.java.unirest.metadata;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

/**
 * Created by VICENC.CORTESOLEA on 10/10/2016.
 *
 */
public class MetadataManager {
    private final String metadata_template_enterprise_url = "https://api.box.com/2.0/metadata_templates/enterprise/";
    private final String metadata_template_schema_url = "https://api.box.com/2.0/metadata_templates/enterprise/%s/schema";
    private final String metadata_template_create_url = "https://api.box.com/2.0/metadata_templates/schema";

    /**
     *
     * @param token
     * @param metadataTemplate
     * @return
     * @throws Exception
     */
    public JSONObject getMetadataTemplateDefinition(String token, String metadataTemplate) throws Exception {
        String url = String.format(metadata_template_schema_url, metadataTemplate);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return body;
        } else
            throw new Exception("Error getting Metadata Template definition with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     *
     * @param token
     * @return
     * @throws Exception
     */
    public JSONObject getMetadataTemplates(String token) throws Exception {
        HttpResponse<JsonNode> jsonResponse = Unirest.get(metadata_template_enterprise_url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return body;
        } else
            throw new Exception("Error getting metadata templates with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     *
     * @param token
     * @param json
     */
    public void createMetadataTemplate(String token, String json) throws Exception{
        HttpResponse<JsonNode> jsonResponse = Unirest.post(metadata_template_create_url)
                .header("Authorization", "Bearer " + token)
                .body(json)
                .asJson();

        if (jsonResponse.getStatus() != HttpStatus.SC_CREATED)
            throw new Exception("Error creating Metadata template with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }
}
