package com.fitz;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.Enumeration;

import sun.security.pkcs11.SunPKCS11;

public class Main {

    /**
     * A quick and dirty program to access your token information. Use this as
     * a basic start and build from here. Or copy. Or whatever.
     */

    public static void main(String[] args) {
        try {
            String pin = "thisismytokenpin";
            String configpath = "./lib/key.cfg";

            CertStore cs = new CertStore();

            // Load the PKCS11 config file which denoted the filepath
            // of the USB token driver.
            String pkcs11Config = configpath;
            SunPKCS11 providerPKCS11 = new SunPKCS11(pkcs11Config);
            java.security.Security.addProvider(providerPKCS11);

            // Now the PKCS11 provider is available, create an instance of the
            // keystore from the new provider.
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("PKCS11");

            // Now we have a pin, we want to finally load the keystore
            // by initialising the token.
            keyStore.load(null, pin.toCharArray());

            Enumeration aliases = keyStore.aliases();
            while (aliases.hasMoreElements()){
                String alias = aliases.nextElement().toString();
                System.out.println(alias);

                cs.provider = providerPKCS11;
                cs.chain = keyStore.getCertificateChain(alias);
                cs.pk = (PrivateKey) keyStore.getKey(alias, pin.toCharArray());
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}