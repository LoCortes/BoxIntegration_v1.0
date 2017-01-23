package net.locortes.box.groovy.unirest

import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXConnection
import net.locortes.box.java.unirest.BOXTokenConnectionHelper
import net.locortes.box.java.unirest.document.DocumentManager
import org.apache.http.HttpHost

/**
 * Created by VICENC.CORTESOLEA on 18/01/2017.
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

BOXTokenConnectionHelper boxTokenConnectionHelper = new BOXTokenConnectionHelper(key)
BOXConnection token;

//TESTING ITEM_PREVIEW without specifying any resource
        boxTokenConnectionHelper.setScope("item_preview")
try{
    token = boxTokenConnectionHelper.getFileTokenPrimary()
    println(token)
    DocumentManager.getEmbedLink(token.getAccess_token(), "123914884047")
}catch (Exception e){
    e.printStackTrace()
}

try{
    token = boxTokenConnectionHelper.getFileTokenPrimary("123914884047")
    println(token)
    DocumentManager.getEmbedLink(token.getAccess_token(), "123914884047")
}catch (Exception e){
    e.printStackTrace()
}


//TESTING ITEM_DELETE without specifying any resource
boxTokenConnectionHelper.setScope("item_delete")
try{
    token = boxTokenConnectionHelper.getFileTokenPrimary()
    println(token)
    DocumentManager.getEmbedLink(token.getAccess_token(), "123914884047")
}catch (Exception e){
    e.printStackTrace()
}

try{
    token = boxTokenConnectionHelper.getFileTokenPrimary("123914884047")
    println(token)
    DocumentManager.getEmbedLink(token.getAccess_token(), "123914884047")
}catch (Exception e){
    e.printStackTrace()
}

//TESTING ITEM_CONTENT_UPLOAD without specifying any resource
boxTokenConnectionHelper.setScope("item_content_upload")
try{
    token = boxTokenConnectionHelper.getFileTokenPrimary()
    println(token)
    DocumentManager.getEmbedLink(token.getAccess_token(), "123914884047")
}catch (Exception e){
    e.printStackTrace()
}

try{
    token = boxTokenConnectionHelper.getFileTokenPrimary("123914884047")
    println(token)
    DocumentManager.getEmbedLink(token.getAccess_token(), "123914884047")
}catch (Exception e){
    e.printStackTrace()
}

