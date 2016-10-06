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

            // Load the PKCS11 config file, ensure this has the correct
            // path to the driver for the token.
            String pkcs11Config = configpath;
            SunPKCS11 providerPKCS11 = new SunPKCS11(pkcs11Config);
            java.security.Security.addProvider(providerPKCS11);

            // Using the PKCS11 provider, create an instance of the keystore.
            // You'll need to load the keystore with the pin above.
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("PKCS11");
            keyStore.load(null, pin.toCharArray());

            // This simply loops through the aliases and prints.
            // From here you should have access to the token.
            Enumeration aliases = keyStore.aliases();
            while (aliases.hasMoreElements()){
                String alias = aliases.nextElement().toString();
                System.out.println(alias);

                // Example of some info you can extract.
                // If you want to sign something with the certificate,
                // get the private key and cert chain. Bingo!
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