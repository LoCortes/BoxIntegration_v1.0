package net.locortes.box.unirest

import com.google.gson.Gson
import com.mashape.unirest.http.Unirest
import net.locortes.box.sdk.java.helper.ApplicationKeyID
import net.locortes.box.sdk.java.helper.ResourcesHelper
import net.locortes.box.unirest.folder.Folder
import net.locortes.box.unirest.folder.FolderManager
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
