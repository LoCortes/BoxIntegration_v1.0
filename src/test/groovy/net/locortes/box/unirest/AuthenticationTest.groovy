package net.locortes.box.unirest

import com.mashape.unirest.http.Unirest
import net.locortes.box.sdk.java.helper.ApplicationKeyID
import net.locortes.box.sdk.java.helper.ResourcesHelper
import net.locortes.box.unirest.helper.RSAKeyReader
import org.apache.http.HttpHost

import java.security.PrivateKey

/**
 * Created by VICENC.CORTESOLEA on 14/09/2016.
 *
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

BOXConnectionHelper connectionHelper = new BOXConnectionHelper(key)
BOXConnection connectionEnterprise = connectionHelper.authenticateEnterprise()
println(connectionEnterprise.getAccess_token())

BOXConnection connectionUser = connectionHelper.authenticateUser()
println(connectionUser.getAccess_token())
