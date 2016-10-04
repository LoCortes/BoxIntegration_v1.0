package net.locortes.box.sdk.groovy.tests

import com.box.sdk.Metadata
import net.locortes.box.sdk.java.BOXConnectionHelper
import net.locortes.box.sdk.java.helper.ApplicationKeyID

/**
 * Created by VICENC.CORTESOLEA on 14/09/2016.
 */

//The key only identifies which set or properties will be taken to connect to BOX.
def configKey = ApplicationKeyID.getConfigKey()

//Getting the connection to BOX
def boxConnectionHelper = new BOXConnectionHelper(configKey)
def api = boxConnectionHelper.getUserConnection()


//
Metadata metadata = new Metadata()

