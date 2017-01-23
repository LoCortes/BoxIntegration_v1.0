package net.locortes.box.java.unirest.document;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.locortes.box.java.unirest.folder.Folder;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

/**
 * Created by vicenc.cortesolea on 17/01/2017.
 */
public class DocumentManager {
    private final static String document_template_base_url = "https://api.box.com/2.0/files/";
    private final static String document_template_url = document_template_base_url + "%s";
    private final static String document_embed_link_template_url = document_template_url + "?fields=expiring_embed_link";

    /**
     *
     * @param token
     * @param documentId
     * @throws Exception
     */
    public static void deleteFile(String token, String documentId) throws Exception {
        String url = String.format(document_template_url, documentId);
        HttpResponse<JsonNode> jsonResponse = Unirest.delete(url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            System.out.println(body);
        } else
            throw new Exception("Error managing authentication with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     *
     * @param token
     * @param documentId
     * @return
     * @throws Exception
     */
    public static String getEmbedLink(String token, String documentId) throws Exception {
        String url = String.format(document_embed_link_template_url, documentId);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return (String)body.get("url");
        } else
            throw new Exception("Error managing authentication with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }
}
