package net.locortes.box.java.unirest.document;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by vicenc.cortesolea on 17/01/2017.
 */
public class DocumentManager {
    private final static String document_template_base_url = "https://api.box.com/2.0/files/";
    private final static String document_template_url = document_template_base_url + "%s";
    private final static String document_template_url_embed_link = document_template_url + "?fields=expiring_embed_link";
    private final static String document_template_url_upload = "https://upload.box.com/api/2.0/files/content";

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

        if (jsonResponse.getStatus() != HttpStatus.SC_NO_CONTENT)
            throw new Exception("Error with code " + jsonResponse.getStatus()
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
        String url = String.format(document_template_url_embed_link, documentId);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return (String)body.getJSONObject("expiring_embed_link").get("url");
        } else
            throw new Exception("Error with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     *
     * @param token
     * @param folderId
     * @return
     * @throws Exception
     */
    public static String upload(String token, String folderId, File file, String fileName) throws Exception {

        HttpRequestWithBody requestWithBody = Unirest.post(document_template_url_upload);
        requestWithBody.header("Authorization", "Bearer " + token)
            //.header("Content-Type", "multipart/form-data") NEVER ADD. It brokes the boundary and then fails
            .field("filename",
                    new FileInputStream(file),
                    ContentType.APPLICATION_OCTET_STREAM,
                    fileName
            ).field("filename", fileName)
            .field("parent_id", folderId);

        HttpResponse<JsonNode> jsonResponse = requestWithBody.asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_CREATED) {
            JSONObject body = jsonResponse.getBody().getObject();
            JSONArray array = body.getJSONArray("entries");
            return (String)array.getJSONObject(0).get("id");
        } else {
            JSONObject body = jsonResponse.getBody().getObject();
            throw new Exception("Error with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
        }
    }
}
