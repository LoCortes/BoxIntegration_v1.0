package net.locortes.box.java.sdk.helper;

/**
 * Created by vicenc.cortesolea on 04/10/2016.
 */
public class ApplicationKeyID {

    //Change the position to apply a Key
    private final static int position = 4;
    private final static String[] configKey =
            {
                    "zurichDevECM",         //0
                    "zurichDevIndonesia",   //1
                    "zurichDevSwissPost",   //2
                    "zurichDevGC" ,         //3
                    "zurichDevECMToken"     //4
            };

    public static String getConfigKey(){
        return configKey[position];
    }
}
