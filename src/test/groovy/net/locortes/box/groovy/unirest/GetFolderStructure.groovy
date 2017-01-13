package net.locortes.box.groovy.unirest

import com.box.sdk.BoxCollaboration
import com.box.sdk.BoxFolder
import com.box.sdk.BoxItem
import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXConnection
import net.locortes.box.java.unirest.BOXConnectionHelper
import net.locortes.box.java.unirest.folder.Folder
import net.locortes.box.java.unirest.folder.FolderManager
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
BOXConnection boxconnection = connectionHelper.authenticateUser();

def token = boxconnection.getAccess_token()
def rootFolderObject = FolderManager.getFolderInformation(token, "0")
println rootFolderObject

def folders = FolderManager.getFolderItems(token, "0")
printTree(token, folders, 1)

/**
 * Recursive method to print the existing folder structure
 * @param tree
 * @param level
 */
private void printTree(String token, ArrayList<Folder> tree, int level) {
    tree.each { Folder item ->
        if (item.isFolder()) {
            ArrayList<Folder> children = FolderManager.getFolderItems(token, item.getId())
            int size = children.size()
            println("${"\t" * level} ${size > 0 ? "+" : "-"} Folder ID ${item.getId()}")
            if (size > 0) printTree(token, children, (level + 1))
        } else {
            println("${"\t" * level} · Document ID ${item.getId()}")
        }
    }
}

/**
 * Recursive method to print the existing folder structure
 * @param tree The
 * @param level
 * @param showDocuments
 * @param showPermissions
 */
private void printTree(BoxFolder root, int level, boolean showDocuments, boolean showPermissions) {
    Iterable<BoxItem.Info> tree = root.getChildren()
    tree.each { BoxItem.Info item ->
        if (item instanceof BoxFolder.Info) {
            BoxFolder childFolder = (BoxFolder) item.getResource();
            int size = childFolder.getChildren().size()

            println("${"\t" * level} ${size > 0 ? "+" : "-"} Folder ${item.getName()} - ID ${item.getID()}")
            if (showPermissions) printPermissions(childFolder.getCollaborations(), level)

            if (size > 0) printTree(childFolder, (level + 1), showDocuments, showPermissions)
        } else if (showDocuments) {
            println("${"\t" * level} · Document ID ${item.getID()}")
        }
    }
}

/**
 * Print the collaborations at a folder level
 * @param collaborations
 * @param level
 */
private void printPermissions(Collection<BoxCollaboration.Info> collaborations, int level) {
    collaborations.each { collaboration ->
        println "${"\t" * (level + 1)} ${Character.toString((char) 240)} User: ${collaboration.getAccessibleBy().getName()} - Role: ${collaboration.getRole()} "
    }
}