package net.locortes.box.java.unirest.user;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import net.locortes.box.java.unirest.folder.Folder;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

/**
 * Created by vicenc.cortesolea on 24/01/2017.
 */
public class UserManager {
    private final static String user_template_base_url = "https://api.box.com/2.0/users/";
    private final static String user_template_getcurrent = user_template_base_url + "me";

    public static User getCurrentUserInformation(String token) throws Exception {
        HttpResponse<JsonNode> jsonResponse = Unirest.get(user_template_getcurrent)
                .header("Authorization", "Bearer " + token).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return new Gson().fromJson(String.valueOf(body), User.class);
        } else
            throw new Exception("Error managing authentication with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());

    }
}
