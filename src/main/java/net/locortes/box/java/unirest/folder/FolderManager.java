package net.locortes.box.groovy.unirest.folder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by VICENC.CORTESOLEA on 11/10/2016.
 */
public class FolderManager {
    private final static String folder_template_base_url = "https://api.box.com/2.0/folders/";
    private final static String folder_template_url = folder_template_base_url + "%s";
    private final static String folder_template_url_items = folder_template_url + "/items?limit=%d&offset=%d";

    /**
     *
     * @param token
     * @param folderId
     * @return
     * @throws Exception
     */
    public static Folder getFolderInformation(String token, String folderId) throws Exception {
        String url = String.format(folder_template_url, folderId);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return new Gson().fromJson(String.valueOf(body), Folder.class);
        } else
            throw new Exception("Error managing authentication with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());

    }

    /**
     *
     * @param token
     * @param folderId
     * @return
     * @throws Exception
     */
    public static ArrayList<Folder> getFolderItems(String token, String folderId) throws Exception {
        String url = String.format(folder_template_url_items, folderId, 1000, 0);
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            Type listType = new TypeToken<ArrayList<Folder>>(){}.getType();
            JSONObject body = jsonResponse.getBody().getObject();
            JSONArray array = body.getJSONArray("entries");
            return new Gson().fromJson(array.toString(), listType);
        } else
            throw new Exception("Error managing authentication with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }
}
