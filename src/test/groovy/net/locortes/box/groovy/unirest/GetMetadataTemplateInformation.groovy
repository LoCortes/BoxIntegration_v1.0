package net.locortes.box.groovy.unirest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXConnection
import net.locortes.box.java.unirest.BOXConnectionHelper
import net.locortes.box.java.unirest.metadata.Metadata
import net.locortes.box.java.unirest.metadata.MetadataManager
import org.apache.http.HttpHost
import org.json.JSONObject

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
BOXConnection boxconnection = connectionHelper.authenticateEnterprise()

MetadataManager metadataManager = new MetadataManager()

//Retrieving all the templates available
println "*"*30
JSONObject body = metadataManager.getMetadataTemplates(boxconnection.access_token)
Gson gson = new GsonBuilder().setPrettyPrinting().create()
String json = gson.toJson(body.get("entries"))
//println json


// Extraction of metadata schemas from the answer received from BOX
List<Metadata> list = gson.fromJson(String.valueOf(body.get("entries")), new TypeToken<List<Metadata>>(){}.getType());
//println(list)

//Conversion from BOX answer to BOX creation templates format.
list.each { Metadata metadata ->
    if(!metadata.isHidden()){
        metadata.setScope("enterprise")

//        File file = new File("${new File(ResourcesHelper.getURI("metadataTemplates")).getAbsolutePath()}\\${metadata.getTemplateKey()}.json")
//        file << gson.toJson(metadata, Metadata.class)

        println "${metadata.getDisplayName().padLeft(30)} ... ${metadata.getTemplateKey()}"
    }
}

//def map = new TreeMap<String, String>()
//
//def test = (JSONArray)body.get("entries")
//for(int i=0; i<test.length(); i++){
//    JSONObject obj = (JSONObject)test.get(i)
//    map.put(obj.get("displayName"), obj.get("templateKey"))
//}

//for(Map.Entry<String,String> entry : map.entrySet()) {
//    println "${entry.getValue().padLeft(30)} ... ${entry.getKey()}"
//}



//Retrieving a specific template
/*
println "*"*30
body = metadataManager.getMetadataTemplateDefinition(boxconnection.access_token, "siniestro")
gson = new GsonBuilder().setPrettyPrinting().create()
json = gson.toJson(body)
println json
*/