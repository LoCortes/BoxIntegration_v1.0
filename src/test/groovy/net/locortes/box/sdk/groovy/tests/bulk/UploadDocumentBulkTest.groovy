package net.locortes.box.sdk.groovy.tests.bulk

import com.box.sdk.BoxFile
import com.box.sdk.BoxFolder
import com.box.sdk.FileUploadParams
import com.box.sdk.Metadata
import com.box.sdk.ProgressListener
import groovy.io.FileType
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
BoxFolder folder = folderInfo.getResource()
println("Folder with ID ${folderInfo.getID()} has been created")

def formatter = new DecimalFormat("###,##0.00")

File log = new File("UploadDocuments_${System.currentTimeMillis()}.csv")
log << "Name;ID;Size(bytes);Time(milliseconds)\n"

File folderWithDocuments = new File(ResourcesHelper.getURI("documents"))
folderWithDocuments.eachFile(FileType.FILES) { file ->
    10.times {
        println "*" * 30
        long time = System.currentTimeMillis()
        //Uploading a document using the uploadFile
        BoxFile.Info fileInfo = folder.uploadFile([
                content : file.newInputStream(),
                name    : "My_test_file-${System.currentTimeMillis()}.docx",
                created : new Date() - 10,
                size    : file.size(),
                listener: { numBytes, totalBytes ->
                    /*
                    println "Uploading... ${formatter.format(numBytes / 1024)} KB out of: " +
                            "${formatter.format(totalBytes / 1024)} KB (${formatter.format(numBytes / totalBytes * 100)}%)"
                    */
                } as ProgressListener,
        ] as FileUploadParams)

        time = System.currentTimeMillis() - time
        println("Document with ID ${fileInfo.getID()} has been uploaded. " +
                "Size ${formatter.format(file.size() / 1024)} KB. " +
                "Name ${file.getName()}. " +
                "It took ${time} milliseconds")

        log << "${file.getName()};${fileInfo.getID()};${file.size()};$time\n"
    }
}
