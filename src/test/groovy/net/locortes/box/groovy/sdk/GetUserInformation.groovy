package net.locortes.box.groovy.sdk

import com.box.sdk.BoxAPIConnection
import com.box.sdk.BoxUser
import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.BOXConnectionHelper
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import org.apache.http.HttpHost

/**
 * Created by vicenc.cortesolea on 24/01/2017.
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
    def api = boxConnectionHelper.getUserConnection()

BoxUser currentUser = BoxUser.getCurrentUser(api)
BoxUser.Info currentUserInfo = currentUser.getInfo()
println("Current User with ID: ${currentUserInfo.ID}, name: ${currentUserInfo.name} and mail: ${currentUserInfo.login}")

println("")

Iterable<BoxUser.Info> users = BoxUser.getAllEnterpriseUsers(api);
users.each {BoxUser.Info userInfo->
    println("User with ID: ${userInfo.ID}, name: ${userInfo.name} and mail: ${userInfo.login}")
}

println("")



println("*" * 30 )
println("END" )
println("*" * 30 )

