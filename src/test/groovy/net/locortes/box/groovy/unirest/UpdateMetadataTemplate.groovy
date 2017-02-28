package net.locortes.box.groovy.unirest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXConnection
import net.locortes.box.java.unirest.BOXConnectionHelper
import net.locortes.box.java.unirest.metadata.Metadata
import net.locortes.box.java.unirest.metadata.MetadataManager
import net.locortes.box.java.unirest.user.User
import net.locortes.box.java.unirest.user.UserManager
import org.apache.http.HttpHost

/**
 * Created by vicenc.cortesolea on 28/02/2017.
 */

/**
 * Created by VICENC.CORTESOLEA on 14/09/2016.
 */

def config = new ConfigSlurper().parse(ResourcesHelper.getURI("config.groovy").toURL())

//PROXY ISSUES
System.setProperty("http.proxyHost", config.proxy.host)
System.setProperty("http.proxyPort", config.proxy.port)
System.setProperty("https.proxyHost", config.proxy.host)
System.setProperty("https.proxyPort", config.proxy.port)
Unirest.setProxy(new HttpHost(config.proxy.host, Integer.parseInt(config.proxy.port)));

//Class that only contains a static method that returns the ID of the configuration to be used
//The key only identifies which set or properties will be taken to connect to BOX.
def key = ApplicationKeyID.getConfigKey()

def connectionHelper = new BOXConnectionHelper(key)
BOXConnection boxconnection = connectionHelper.authenticateUser()
//BOXConnection boxconnection = connectionHelper.authenticateEnterprise()

println "*"*30
User user = UserManager.getCurrentUserInformation(boxconnection.getAccess_token())
println("Connected with user $user")

println "*"*30

MetadataManager metadataManager = new MetadataManager()

int offset = 1;
JsonArray array = new JsonArray();
127.times {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("op", "addEnumOption");
    jsonObject.addProperty("fieldKey", "test");

    final JsonObject jsonDataObject = new JsonObject();
    jsonDataObject.addProperty("key", "test${it+offset}");

    jsonObject.add("data", jsonDataObject);
    array.add(jsonObject);
}

Gson gson = new GsonBuilder().create();
String json = gson.toJson(array)
println(json)
metadataManager.updateMetadataTemplate(boxconnection.getAccess_token(), "buildingDetails", json);

println "*"*30

        //{"op":"addEnumOption","fieldKey":"category","data":{"key":"Technology"}}
