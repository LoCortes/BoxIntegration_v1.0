package net.locortes.box.sdk.java.helper;

import java.io.*;
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

    public static InputStream getResource(String name){
        ClassLoader loader = ResourcesHelper.class.getClassLoader();
        return loader.getResourceAsStream(name);
    }

    /**
     * Method to read bytes
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fileInputStream);
        try {
            dis.readFully(bytes);
            InputStream temp = dis;
            dis = null;
            temp.close();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }
}
