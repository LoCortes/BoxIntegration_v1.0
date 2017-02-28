package net.locortes.box.groovy.unirest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXConnection
import net.locortes.box.java.unirest.BOXConnectionHelper
import net.locortes.box.java.unirest.document.Document
import net.locortes.box.java.unirest.metadata.Metadata
import net.locortes.box.java.unirest.metadata.MetadataManager
import net.locortes.box.java.unirest.user.User
import net.locortes.box.java.unirest.user.UserManager
import org.apache.http.HttpHost

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

MetadataManager metadataManager = new MetadataManager()

//Retrieving all the templates available
List<Metadata> list = metadataManager.getAllMetadataTemplates(boxconnection.access_token)


//Configuration options
boolean onlyHidden = false //Shows on screen only hidden templates
boolean deleteDocuments = false //Delete metadata instance linked to a document
boolean writeFile = false //Write in an external file the json definition of the metadata template
boolean hideTemplates = false //Hide those templates with display name started with Obsolete
boolean printDocumentsByScreen = false //List on screen the first N documentts that have a specific metadata template

list.each { Metadata metadata ->
    metadata.setScope("enterprise")
    try{
        //If the template name starts with Obsolete mark them as Hidden
        if(metadata.getDisplayName().startsWith("Obsolete") && !metadata.isHidden() && hideTemplates)
            metadataManager.hideMetadataTemplate(boxconnection.access_token, metadata.getTemplateKey())

        if(metadata.isHidden()){
            println "\n" + "-"*30

            //Conversion from BOX answer to BOX creation templates format.
            //println "${metadata.getDisplayName().padLeft(30)} ... ${metadata.getTemplateKey()}"
            println "${metadata.getDisplayName()} ... ${metadata.getTemplateKey()}"

            //To search all the metadata templates being used we do not care about filters only about if it is being used or not
            metadata.setFields(new ArrayList<Metadata.Field>())

            //Setting a template to visible
            metadataManager.showMetadataTemplate(boxconnection.access_token, metadata.getTemplateKey())

            try{
                //Search which documents are using a specific Entry template
                long count = metadataManager.countDocumentByMetadataTemplate(boxconnection.access_token, [metadata])
                println("\t There are ${count} documents using this template.")

                if(printDocumentsByScreen) {
                    List<Document> documents = metadataManager.searchByMetadataTemplate(boxconnection.access_token, [metadata])
                    documents.each { Document document ->
                        println("\t\t Document with ID $document")

                        //Deleting documents that are part of the hidden templates
                        if (deleteDocuments) {
                            try {
                                metadataManager.deleteMetadatafromFile(boxconnection.access_token, document.getId(), metadata.getTemplateKey())
                                println("\t\t Deleted template dependency")
                            } catch (Exception e) {
                                println("\t\t Error deleting template dependency")
                            }
                        }
                    }
                }
            }finally{
                //Setting a template to hidden
                metadataManager.hideMetadataTemplate(boxconnection.access_token, metadata.getTemplateKey())
            }

        }else if(!onlyHidden){
            println "\n" + "-"*30

            //Conversion from BOX answer to BOX creation templates format.
            //println "${metadata.getDisplayName().padLeft(30)} ... ${metadata.getTemplateKey()}"
            println "${metadata.getDisplayName()} ... ${metadata.getTemplateKey()}"

            //Conversion from BOX answer to BOX creation templates format.
            if(writeFile){
                Gson gson = new GsonBuilder().create();
                File file = new File("${new File(ResourcesHelper.getURI("metadataTemplates")).getAbsolutePath()}\\${metadata.getTemplateKey()}.json")
                file < gson.toJson(metadata, Metadata.class)
            }

            //To search all the metadata templates being used we do not care about filters only about if it is being used or not
            metadata.setFields(new ArrayList<Metadata.Field>())

            //Search which documents are using a specific Entry template
            long count = metadataManager.countDocumentByMetadataTemplate(boxconnection.access_token, [metadata])
            println("\t There are ${count} documents using this template.")

            if(printDocumentsByScreen) {
                List<Document> documents = metadataManager.searchByMetadataTemplate(boxconnection.access_token, [metadata])
                documents.each { Document document ->
                    println("\t\t Document with ID $document")
                }
            }
        }

    }catch (Exception e){
        println("Exception captured processing template ${metadata.getTemplateKey()} with message ${e.getMessage()}")
    }
}
println "*"*30

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