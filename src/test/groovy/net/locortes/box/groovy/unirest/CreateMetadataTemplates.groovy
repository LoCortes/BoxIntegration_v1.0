package net.locortes.box.groovy.unirest

import com.mashape.unirest.http.Unirest
import groovy.io.FileType
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXConnection
import net.locortes.box.java.unirest.BOXConnectionHelper
import net.locortes.box.java.unirest.metadata.MetadataManager
import org.apache.http.HttpHost

/**
 * Created by vicenc.cortesolea on 03/02/2017.
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

MetadataManager metadataManager = new MetadataManager()

def dir = new File(ResourcesHelper.getURI("metadataTemplates\\Mexico"))
dir.eachFileRecurse (FileType.FILES) { file ->
    println("*" * 30)
    println("Creating template: " + file.getName())
    println("${file.getText("UTF-8")}")
    println("*" * 30)

    metadataManager.createMetadataTemplate(boxconnection.getAccess_token(), file.getText("UTF-8"))
}