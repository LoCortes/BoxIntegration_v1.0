package net.locortes.box.sdk.java;

import com.box.sdk.*;
import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import net.locortes.box.sdk.java.helper.ApplicationKeyID;
import net.locortes.box.sdk.java.helper.ResourcesHelper;


import java.io.*;
import java.net.URISyntaxException;

/**
 * Created by VICENC.CORTESOLEA on 08/09/2016.
 * Based on Abel Salgado Romero work (https://github.com/abelsromero/box-groovy-wrapper)
 * <p>
 * This class helps to perform a connection to BOX and performs some basic actions
 */
public class BOXConnectionHelper {

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

    //Preferences for getting the BOX Connection
    JWTEncryptionPreferences encryptionPref;

    //Cache for the token
    IAccessTokenCache accessTokenCache;

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

            /*
            PREPARING THE ENCRYPTION PREFERENCES
            */
        System.out.println("Defining Encryption Preferences");
        encryptionPref = new JWTEncryptionPreferences();
        encryptionPref.setPublicKeyID(PUBLIC_KEY_ID);

        File privateKeyFile = new File(ResourcesHelper.getURI(PRIVATE_KEY_FILE));
        encryptionPref.setPrivateKey(new String(ResourcesHelper.readBytes(privateKeyFile)));
        encryptionPref.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);
        encryptionPref.setEncryptionAlgorithm(EncryptionAlgorithm.RSA_SHA_256);

        //It is a best practice to use an access token cache to prevent unneeded requests to Box for access tokens.
        //For production applications it is recommended to use a distributed cache like Memcached or Redis, and to
        //implement IAccessTokenCache to store and retrieve access tokens appropriately for your environment.
        final int MAX_CACHE_ENTRIES = 100;
        accessTokenCache = new InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);
    }

    /**
     * Get the connection using the Information indicated on the constructor
     *
     * @return a connection to BOX using an enterprise ID
     */
    public BoxAPIConnection getEnterpriseConnection() {
        /*
        OBTAINING CONNECTION TO THE BOX REPOSITORY
        To make this work I needed to downloed the "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction
        Policy Files <jre_version>" and add the jars in the jvm
        */
        System.out.println("Performing connection trough the JAVA API...");
        BoxDeveloperEditionAPIConnection api =
                BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(
                        ENTERPRISE_ID,
                        CLIENT_ID,
                        CLIENT_SECRET,
                        encryptionPref,
                        accessTokenCache
                );
        return api;
    }

    /**
     * Get the connection using the Information indicated on the constructor
     *
     * @return a connection to BOX using an APP User
     */
    public BoxAPIConnection getUserConnection() {
        /*
        OBTAINING CONNECTION TO THE BOX REPOSITORY
        To make this work I needed to downloed the "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction
        Policy Files <jre_version>" and add the jars in the jvm
        */
        System.out.println("Performing connection trough the JAVA API...");
        BoxDeveloperEditionAPIConnection api =
                BoxDeveloperEditionAPIConnection.getAppUserConnection(
                        APP_USER_ID,
                        CLIENT_ID,
                        CLIENT_SECRET,
                        encryptionPref,
                        accessTokenCache
                );
        return api;
    }

    /**
     * Method to create a user
     *
     * @param api
     * @param name
     */
    public void createUser(BoxAPIConnection api, String name) {
        /*
        DEFINING THE PARAMETERS THAT THE USERS WILL HAVE
        */
        CreateUserParams params = new CreateUserParams();
        params.setSpaceAmount(512 * 1024 * 1024); //512 MB

        System.out.println("Creating user:" + name);
        BoxUser.Info user = BoxUser.createAppUser(api, name, params);
        System.out.println("User created with name " + name + " and id " + user.getID() + " and login: " + user.getLogin());
        System.out.println("User created with name " + name);
    }

}
