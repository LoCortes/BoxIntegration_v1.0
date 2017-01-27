package net.locortes.box.groovy.sdk

import com.box.sdk.BoxAPIConnection
import com.mashape.unirest.http.Unirest
import net.locortes.box.java.sdk.BOXConnectionHelper
import net.locortes.box.java.sdk.helper.ApplicationKeyID
import net.locortes.box.java.sdk.helper.ResourcesHelper
import org.apache.http.HttpHost

/**
 * Created by vicenc.cortesolea on 17/01/2017.
 * This script tests the Authentication using the JAVA SDK for BOX.
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

BOXConnectionHelper helper = new BOXConnectionHelper(key)
BoxAPIConnection apiEnterprise = helper.getEnterpriseConnection()
println(apiEnterprise.getAccessToken())

BoxAPIConnection apiUser = helper.getUserConnection()
println(apiUser.getAccessToken())
