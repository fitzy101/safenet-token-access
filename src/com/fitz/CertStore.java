package com.fitz;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import sun.security.pkcs11.SunPKCS11;

public class CertStore {
    public PrivateKey pk;
    public Certificate[] chain;
    public SunPKCS11 provider;
}