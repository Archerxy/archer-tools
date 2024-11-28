package com.archer.tools.http.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.archer.net.ssl.NamedCurve;

public class SslOption {
	
	private byte[] ca;

	private byte[] cert;

	private byte[] key;

	private byte[] encryptCert;

	private byte[] encryptKey;
	
	private NamedCurve[] curves;

	public SslOption(Path cert, Path key) throws IOException, HttpServerException {
		this(null, cert, key, null, null, null);
	}
	
	public SslOption(Path ca, Path cert, Path key, Path encryptCert, Path encryptKey, NamedCurve[] curves)
			throws IOException, HttpServerException {
		byte[] caBs = null, certBs = Files.readAllBytes(cert), keyBs = Files.readAllBytes(key), enCertBs = null, enKeyBs = null;
		if(ca != null) {
			caBs = Files.readAllBytes(ca);
		}
		if(encryptCert != null) {
			enCertBs = Files.readAllBytes(encryptCert);
		}
		if(encryptKey != null) {
			enKeyBs = Files.readAllBytes(encryptKey);
		}
		this.ca = caBs;
		this.cert = certBs;
		this.key = keyBs;
		this.encryptCert = enCertBs;
		this.encryptKey = enKeyBs;
		this.curves = curves;
	}

	public byte[] getCa() {
		return ca;
	}

	public byte[] getCert() {
		return cert;
	}

	public byte[] getKey() {
		return key;
	}

	public byte[] getEncryptCert() {
		return encryptCert;
	}

	public byte[] getEncryptKey() {
		return encryptKey;
	}

	public NamedCurve[] getCurves() {
		return curves;
	}

	public void setCa(byte[] ca) {
		this.ca = ca;
	}

	public void setEncryptCert(byte[] encryptCert) {
		this.encryptCert = encryptCert;
	}

	public void setEncryptKey(byte[] encryptKey) {
		this.encryptKey = encryptKey;
	}

	public void setCurves(NamedCurve[] curves) {
		this.curves = curves;
	}
	
}
