package net.locortes.box.sdk.groovy.tests

import com.box.sdk.BoxDeveloperEditionAPIConnection
import com.box.sdk.BoxUser
import com.box.sdk.CreateUserParams
import com.box.sdk.EncryptionAlgorithm
import com.box.sdk.IAccessTokenCache
import com.box.sdk.InMemoryLRUAccessTokenCache
import com.box.sdk.JWTEncryptionPreferences
import org.abelsromero.box.sdk.helpers.FileHelpers

import static org.abelsromero.box.sdk.helpers.FileHelpers.getFile

/**
 * Created by Vicenç Cortés Olea on 07/09/2016.
 * Based on Abel Salgado Romero work (https://github.com/abelsromero/box-groovy-wrapper)
 */

def config = new ConfigSlurper().parse(getFile('config.groovy').toURI().toURL())

// Params
def key = 'APP_ID'

//The Client ID is the unique identifier of the application created
final String CLIENT_ID = config."$key".clientId;
//The Client Secret is another value of the application created needed to login
final String CLIENT_SECRET = config."$key".secret;
//This is the identifier of the Public Key associated to the application. An application can have more than one public_key.
final String PUBLIC_KEY_ID = config."$key".keyId;
//This is the Private Key PEM file to match with the public key already configured on the application
final String PRIVATE_KEY_FILE = config."$key".keyLocation;
//The Password that is needed to open the Private Key file
final String PRIVATE_KEY_PASSWORD = config."$key".keyPass;
//The Enterprise ID is the unique ID for the BOX repository to where we are connecting.
final String ENTERPRISE_ID = config."$key".enterpriseId;

/*
    PREPARING THE ENCRYPTION PREFERENCES
 */
println "Defining Encryption Preferences"
JWTEncryptionPreferences encryptionPref = new JWTEncryptionPreferences();
encryptionPref.setPublicKeyID(PUBLIC_KEY_ID);
encryptionPref.setPrivateKey(new String(FileHelpers.getFile(PRIVATE_KEY_FILE).readBytes()));
encryptionPref.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);
encryptionPref.setEncryptionAlgorithm(EncryptionAlgorithm.RSA_SHA_256);

//It is a best practice to use an access token cache to prevent unneeded requests to Box for access tokens.
//For production applications it is recommended to use a distributed cache like Memcached or Redis, and to
//implement IAccessTokenCache to store and retrieve access tokens appropriately for your environment.
final int MAX_CACHE_ENTRIES = 100;
IAccessTokenCache accessTokenCache = new InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);

/*
    OBTAINING CONNECTION TO THE BOX REPOSITORY
    To make this work I needed to downloed the "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files <jre_version>" and add the jars in the jvm
 */
println "Performing connection trough the JAVA API..."
BoxDeveloperEditionAPIConnection api =
        BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(ENTERPRISE_ID, CLIENT_ID, CLIENT_SECRET, encryptionPref, accessTokenCache)

/*
    DEFINING THE PARAMETERS THAT THE USERS WILL HAVE
 */
CreateUserParams params = new CreateUserParams()
params.spaceAmount = 512 * 1024 * 1024 //512 MB

def name = "APP_USER_NAME"
println "Creating user: $name"
BoxUser.Info user = BoxUser.createAppUser(api, name, params)
println "User created with name $name and id ${user.ID} and login: ${user.login}"
println "User created with name $name"
