package net.locortes.box.java.unirest;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import net.locortes.box.java.sdk.helper.ResourcesHelper;
import net.locortes.box.java.unirest.helper.RSAKeyReader;
import org.apache.http.HttpStatus;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by VICENC.CORTESOLEA on 10/10/2016.
 * Based on Abel Salgado Romero's work: https://github.com/abelsromero/box-groovy-wrapper/blob/master/src/main/groovy/org/abelsromero/box/sdk/auth/EnterpriseAPIConnection.groovy
 */
public class BOXConnectionHelper {
    private final String oauth2_url = "https://api.box.com/oauth2/token";

    //The Client ID is the unique identifier of the application created
    private String CLIENT_ID;
    //The Client Secret is another value of the application created needed to login
    private String CLIENT_SECRET;
    //This is the identifier of the Public Key associated to the application. An application can have more than one public_key.
    private String PUBLIC_KEY_ID;
    //This is the Private Key PEM file to match with the public key already configured on the application
    private String PRIVATE_KEY_FILE;
    //The Password that is needed to open the Private Key file
    private String PRIVATE_KEY_PASSWORD;
    //The Enterprise ID is the unique ID for the BOX repository to where we are connecting.
    private String ENTERPRISE_ID;
    //The APP User ID is the user used to login on the system through the API
    private String APP_USER_ID;

    private PrivateKey privateKey;
    private int expires_in;

    /**
     *
     * @param configKey
     * @throws URISyntaxException
     * @throws IOException
     */
    public BOXConnectionHelper(String configKey) throws URISyntaxException, IOException {
        ConfigObject config = new ConfigSlurper().parse(ResourcesHelper.getURI("config.groovy").toURL());
        ConfigObject appConfig = (ConfigObject) config.get(configKey);

        CLIENT_ID = (String) appConfig.get("clientId");
        CLIENT_SECRET = (String) appConfig.get("secret");
        PUBLIC_KEY_ID = (String) appConfig.get("keyId");
        PRIVATE_KEY_FILE = (String) appConfig.get("keyLocation");
        PRIVATE_KEY_PASSWORD = (String) appConfig.get("keyPass");
        ENTERPRISE_ID = (String) appConfig.get("enterpriseId");
        APP_USER_ID = (String) appConfig.get("appUserId");

        privateKey = RSAKeyReader.decryptPrivateKey(new File(ResourcesHelper.getURI(PRIVATE_KEY_FILE)), PRIVATE_KEY_PASSWORD);
        expires_in = 1;
    }

    /**
     * https://docs.box.com/v2.0/docs/constructing-the-json-web-token-jwt-assertion
     * @param issuer
     * @param subject
     * @param properties
     * @return
     * @throws JoseException
     */
    private String generateJwtPayload(String issuer, String subject, Map<String, String> properties) throws JoseException {

        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setSubject(subject);

        Set<String> keys = properties.keySet();
        for (String key : keys) {
            claims.setStringClaim(key, properties.get(key));
        }

        claims.setAudience(oauth2_url);
        claims.setGeneratedJwtId(); // random unique identifier for the token
        claims.setExpirationTimeMinutesInTheFuture(getExpires_in()); // exp

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        jws.setKeyIdHeaderValue(PUBLIC_KEY_ID);

        System.out.println(jws.toString());
        System.out.println(claims.toJson());

        // The JWT is signed using the private key
        jws.setKey(privateKey);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        return jws.getCompactSerialization();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public BOXConnection authenticateEnterprise() throws Exception {
        return authenticate(ENTERPRISE_ID, "enterprise");
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public BOXConnection authenticateUser() throws Exception {
        return authenticate(APP_USER_ID, "user");
    }

    /**
     * https://docs.box.com/docs/app-auth
     * @param user
     * @param subType
     * @return
     * @throws Exception
     */
    private BOXConnection authenticate(String user, String subType) throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("box_sub_type", subType);

        String jwtPayload = generateJwtPayload(CLIENT_ID, user, properties);

        HttpResponse<JsonNode> jsonResponse = Unirest.post(oauth2_url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "gzip")
                .header("Accept-Charset", "utf-8")
                .field("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                .field("client_id", CLIENT_ID)
                .field("client_secret", CLIENT_SECRET)
                .field("assertion", jwtPayload).asJson();

        if (jsonResponse.getStatus() == HttpStatus.SC_OK) {
            JSONObject body = jsonResponse.getBody().getObject();
            return new Gson().fromJson(String.valueOf(body), BOXConnection.class);
        } else
            throw new Exception("Error managing authentication with code " + jsonResponse.getStatus()
                    + " and message " + jsonResponse.getStatusText());
    }

    /**
     * https://docs.box.com/docs/getting-started-with-new-box-view
     * @param accessToken
     * @return
     * @throws Exception
     */
    public BOXConnection getFileToken(String accessToken) throws Exception {
        MultipartBody requestWithBody = Unirest.post(oauth2_url)
                .header("Accept-Encoding", "gzip")
                .header("Accept-Charset", "utf-8")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("subject_token", URLEncoder.encode(accessToken, "UTF-8"))
                .field("subject_token_type", "urn:ietf:params:oauth:token-type:access_token")
                //.field("scope", URLEncoder.encode("item_preview", "UTF-8"))
                .field("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");

//        if(fileID != null)
//            requestWithBody.field("resource", "https://api.box.com/2.0/files/" + fileID);

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
}
