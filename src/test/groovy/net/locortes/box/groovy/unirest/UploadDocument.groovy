package net.locortes.box.groovy.unirest

import com.box.sdk.BoxFile
import com.box.sdk.BoxFolder
import com.box.sdk.FileUploadParams
import com.box.sdk.Metadata
import com.box.sdk.ProgressListener
import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.BOXConnectionHelper
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXTokenConnectionHelper
import net.locortes.box.java.unirest.document.DocumentManager
import net.locortes.box.java.unirest.folder.Folder
import net.locortes.box.java.unirest.folder.FolderManager
import org.apache.http.HttpHost

import java.awt.Desktop
import java.text.DecimalFormat

/**
 * Created by vicenc.cortesolea on 05/10/2016.
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

println("*" * 30 )
println("START" )
println("*" * 30 )

//Getting the connection to BOX
def boxConnectionHelper = new BOXConnectionHelper(key)
def boxConnection = boxConnectionHelper.getUserConnection()

//Obtaining folder to act as root
Folder root = FolderManager.getFolderInformation(boxConnection.getAccessToken(), "18785003678")

def file = new File(ResourcesHelper.getURI("documents/Test Document 1.docx"))
def documentId = DocumentManager.upload(boxConnection.getAccessToken(), root.getId(), file, "Test Document 1 - ${System.currentTimeMillis()}.doc")
println("Documet uploaded with ID: $documentId")


println("*" * 30 )
println("END" )
println("*" * 30 )