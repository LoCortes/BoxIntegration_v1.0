package net.locortes.box.unirest.helper;

import net.locortes.box.sdk.java.helper.ResourcesHelper;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by vicenc.cortesolea on 04/10/2016.
 * Based on Abel Salgado Romero Work
 */
public class RSAKeyReader {

    private static final String KEY_TYPE = "RSA";

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static PublicKey readPublic(File file) throws Exception {
        PKCS8EncodedKeySpec spec = readKey(file);
        KeyFactory kf = KeyFactory.getInstance(KEY_TYPE);
        return kf.generatePublic(spec);
    }

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static PrivateKey readPrivate(File file) throws Exception {
        PKCS8EncodedKeySpec spec = readKey(file);
        KeyFactory kf = KeyFactory.getInstance(KEY_TYPE);
        return kf.generatePrivate(spec);
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static PKCS8EncodedKeySpec readKey(File file) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();
        return new PKCS8EncodedKeySpec(keyBytes);
    }

    /**
     *
     * @param privateKeyFile
     * @param privateKeyPassword
     * @return
     * @throws IOException
     */
    public static PrivateKey decryptPrivateKey(File privateKeyFile, String privateKeyPassword) throws IOException {

        PrivateKey decryptedPrivateKey;
        PEMParser pemParser = new PEMParser(new StringReader(new String(ResourcesHelper.readBytes(privateKeyFile))));
        Object keyPair = pemParser.readObject();
        pemParser.close();

        if (keyPair instanceof PEMEncryptedKeyPair) {
            JcePEMDecryptorProviderBuilder jcePEMDecryptorProviderBuilder = new JcePEMDecryptorProviderBuilder();
            PEMDecryptorProvider pemDecryptorProvider = jcePEMDecryptorProviderBuilder.build(privateKeyPassword.toCharArray());
            keyPair = ((PEMEncryptedKeyPair) keyPair).decryptKeyPair(pemDecryptorProvider);
        }

        PrivateKeyInfo keyInfo = ((PEMKeyPair) keyPair).getPrivateKeyInfo();
        decryptedPrivateKey = (new JcaPEMKeyConverter()).getPrivateKey(keyInfo);

        return decryptedPrivateKey;
    }

}
