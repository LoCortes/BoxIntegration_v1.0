package net.locortes.box.groovy.unirest.tokenapp

import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import net.locortes.box.java.unirest.BOXConnection
import net.locortes.box.java.unirest.BOXTokenConnectionHelper
import net.locortes.box.java.unirest.user.User
import net.locortes.box.java.unirest.user.UserManager
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

BOXTokenConnectionHelper helper = new BOXTokenConnectionHelper(key)
BOXConnection apiEnterprise = helper.getFileTokenPrimary()
println(apiEnterprise.getAccess_token())

User currentUser = UserManager.getCurrentUserInformation(apiEnterprise.getAccess_token())


println("*" * 30 )
println("END" )
println("*" * 30 )

