package net.locortes.box.java.unirest.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.locortes.box.java.unirest.document.Document;
import net.locortes.box.java.unirest.metadata.serializers.MetadataSerializer4Search;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by VICENC.CORTESOLEA on 10/10/2016.
 *
 */
public class MetadataManager {
    private final String metadata_template_enterprise_url = "https://api.box.com/2.0/metadata_templates/enterprise/";
    private final String metadata_template_schema_url = "https://api.box.com/2.0/metadata_templates/enterprise/%s/schema";
    private final String metadata_template_create_url = "https://api.box.com/2.0/metadata_templates/schema";
    private final String metadata_template_search_url = "https://api.box.com/2.0/search?mdfilters=%s&scope=enterprise_content";
    private final String metadata_template_delete_file_url = "https://api.box.com/2.0/files/%s/metadata/enterprise/%s";

    /**
     *
     * @param token
     * @param metadataTemplate
     * @return
     * @throws Exception
     */
    public Metadata getMetadataTemplateDefinition(String token, String metadataTemplate) throws Exception {
        String url = String.format(metadata_template_schema_url, metadataTemplate);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();

            //Extraction of metadata schemas from the answer received from BOX
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Metadata metadata = gson.fromJson(String.valueOf(body), new TypeToken<Metadata>(){}.getType());

            return metadata;

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
    public List<Metadata> getAllMetadataTemplates(String token) throws Exception {
        HttpResponse<JsonNode> jsonResponse = Unirest.get(metadata_template_enterprise_url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();

            //Extraction of metadata schemas from the answer received from BOX
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Metadata> list = gson.fromJson(String.valueOf(body.get("entries")), new TypeToken<List<Metadata>>(){}.getType());

            return list;
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

    /**
     *
     * @param token
     * @param metadataTemplate
     * @param json
     * @throws Exception
     */
    public void updateMetadataTemplate(String token, String metadataTemplate, String json) throws Exception{
        String url = String.format(metadata_template_schema_url, metadataTemplate);
        HttpResponse<JsonNode> jsonResponse = Unirest.put(url)
                .header("Authorization", "Bearer " + token)
                .body(json)
                .asJson();

        if (jsonResponse.getStatus() != HttpStatus.SC_OK)
            throw new Exception("Error creating Metadata template with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     *
     * @param token
     * @param metadataTemplate
     * @throws Exception
     */
    public void hideMetadataTemplate(String token, String metadataTemplate) throws Exception {
        JsonArray array = new JsonArray();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("op", "editTemplate");

        final JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.addProperty("hidden", true);
        jsonObject.add("data", jsonDataObject);
        array.add(jsonObject);

        Gson gson = new GsonBuilder().create();
        updateMetadataTemplate(token, metadataTemplate, gson.toJson(array));
    }

    /**
     *
     * @param token
     * @param metadataTemplate
     * @throws Exception
     */
    public void showMetadataTemplate(String token, String metadataTemplate) throws Exception {
        JsonArray array = new JsonArray();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("op", "editTemplate");

        final JsonObject jsonDataObject = new JsonObject();
        jsonDataObject.addProperty("hidden", false);
        jsonObject.add("data", jsonDataObject);
        array.add(jsonObject);

        Gson gson = new GsonBuilder().create();
        updateMetadataTemplate(token, metadataTemplate, gson.toJson(array));
    }

    /**
     * This performs a search using a Metadata template
     * @param token
     * @param metadata
     */
    public List<Document> searchByMetadataTemplate(String token, List<Metadata> metadata) throws Exception {

        /**
         * We create a gsonbuilder using the custom serializer to change names
         */
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Metadata.class, new MetadataSerializer4Search());

        Gson gson = gsonBuilder.create();
        String json = gson.toJson(metadata); System.out.println(json);

        String url = String.format(metadata_template_search_url, URLEncoder.encode(json, "UTF-8"));

        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();

            //Extraction of metadata schemas from the answer received from BOX
            gson = new GsonBuilder().setPrettyPrinting().create();
            //json = gson.toJson(body.get("entries"));
            List<Document> list = gson.fromJson(String.valueOf(body.get("entries")), new TypeToken<List<Document>>(){}.getType());

            return list;
        } else
            throw new Exception("Error searching by metadata with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     * Method that returns the total of results.
     * @param token
     * @param metadata
     * @return
     * @throws Exception
     */
    public long countDocumentByMetadataTemplate(String token, List<Metadata> metadata) throws Exception {
        /**
         * We create a gsonbuilder using the custom serializer to change names
         */
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Metadata.class, new MetadataSerializer4Search());

        Gson gson = gsonBuilder.create();
        String json = gson.toJson(metadata);
        String url = String.format(metadata_template_search_url, URLEncoder.encode(json, "UTF-8"));

        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).header("Authorization", "Bearer " + token).asJson();
        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return body.getLong("total_count");
        } else
            throw new Exception("Error searching by metadata with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     *
     * @param token
     * @param fileId
     * @param metadataTemplate
     */
    public void deleteMetadatafromFile(String token, String fileId, String metadataTemplate) throws Exception {

        String url = String.format(metadata_template_delete_file_url, fileId, metadataTemplate);

        HttpResponse<JsonNode> jsonResponse = Unirest.delete(url)
                .header("Authorization", "Bearer " + token)
                .asJson();

        if (jsonResponse.getStatus() != HttpStatus.SC_NO_CONTENT)
            throw new Exception("Error unlinking Metadata template from document with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }
}
