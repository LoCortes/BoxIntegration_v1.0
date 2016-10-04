package net.locortes.box.sdk.java.helper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by vicenc.cortesolea on 04/10/2016.
 */
public class ResourcesHelper {

    public static URI getURI(String name) throws URISyntaxException{
        ClassLoader loader = ResourcesHelper.class.getClassLoader();
        URL url = loader.getResource(name);
        return url.toURI();
    }
}
