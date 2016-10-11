package net.locortes.box.sdk.groovy.tests

import com.box.sdk.BoxFolder
import com.box.sdk.BoxItem
import net.locortes.box.sdk.java.BOXConnectionHelper
import net.locortes.box.sdk.java.helper.ApplicationKeyID
import net.locortes.box.sdk.java.helper.ResourcesHelper

/**
 * Created by VICENC.CORTESOLEA on 14/09/2016.
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

//Obtaining root folder. This returns the folder with ID 0. All users root folder ID is 0.
BoxFolder root = BoxFolder.getRootFolder(api)

println("=" * 30)
println("Root folder ID: ${root.getID()}")
printTree(root.getChildren(), 1)
println("=" * 30)

/**
 * Recursive method to print the existing folder structure
 * @param tree
 * @param level
 */
private void printTree(Iterable<BoxItem.Info> tree, int level) {
    tree.each { BoxItem.Info item ->
        if (item instanceof BoxFolder.Info) {
            BoxFolder childFolder = (BoxFolder) item.getResource();
            int size = childFolder.getChildren().size()
            println("${"\t" * level} ${size > 0 ? "+" : "-"} Folder ID ${item.getID()}")
            if (size > 0) printTree(childFolder.getChildren(), (level + 1))
        } else {
            println("${"\t" * level} · Document ID ${item.getID()}")
        }
    }
}