package net.locortes.box.sdk.groovy.tests

import com.box.sdk.BoxFile
import com.box.sdk.BoxFolder
import com.box.sdk.FileUploadParams
import com.box.sdk.Metadata
import com.box.sdk.ProgressListener
import net.locortes.box.sdk.java.BOXConnectionHelper
import net.locortes.box.sdk.java.helper.ApplicationKeyID
import net.locortes.box.sdk.java.helper.ResourcesHelper

import javax.annotation.Resource
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

//Class that only contains a static method that returns the ID of the configuration to be used
//The key only identifies which set or properties will be taken to connect to BOX.
def key = ApplicationKeyID.getConfigKey()

//Getting the connection to BOX
def boxConnectionHelper = new BOXConnectionHelper(key)
def api = boxConnectionHelper.getUserConnection()

//Obtaining folder to act as root
BoxFolder root = new BoxFolder(api, "11561513256")

//Creating a folder for testing purposes
BoxFolder.Info folderInfo = root.createFolder("Test Folder ${System.currentTimeMillis()}")
println("Folder with ID ${folderInfo.getID()} has been created")

//Uploading a document using the uploadFile
def formatter = new DecimalFormat("###,##0.00")
BoxFolder folder = folderInfo.getResource()
BoxFile.Info fileInfo = folder.uploadFile([
        content : ResourcesHelper.getResource("documents/Test Document 1.docx"),
        name    : "My_test_file-${System.currentTimeMillis()}.docx",
        created : new Date() - 10,
        size    : new File(ResourcesHelper.getURI("Test Document 1.docx")).size(),
        listener: { numBytes, totalBytes ->
            println "Uploading... ${formatter.format(numBytes / 1024)} KB out of: " +
                    "${formatter.format(totalBytes / 1024)} KB (${formatter.format(numBytes/totalBytes*100)}%)"
        } as ProgressListener,
] as FileUploadParams)

print("Document with ID ${fileInfo.getID()} has been created on the Path: /")
fileInfo.getPathCollection().each { BoxFolder.Info info ->
    print("${info.getName()}/")
}
print("\n")

//Adding metadata values to the file
Metadata metadata = new Metadata()
metadata.add("/claimnumber", "1234567890")
metadata.add("/claimantName", "Name")
metadata.add("/policynumber", "0123456789")

BoxFile file = fileInfo.getResource()
file.createMetadata("/claim", metadata)
println("Metadata updated.")

//Obtaining the metadata just created on json format
metadata = file.getMetadata("/claim")
println(metadata.toString())


