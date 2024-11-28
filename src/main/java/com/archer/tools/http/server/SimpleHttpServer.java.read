package com.archer.tools.http.server;

import com.archer.net.HandlerList;
import com.archer.net.ServerChannel;
import com.archer.net.ssl.SslContext;

public class SimpleHttpServer {
	
	private SslOption sslOption;
	private ServerChannel server;
	
	public SimpleHttpServer() {
		this(null);
	}
	
	public SimpleHttpServer(SslOption sslOption) {
		this.sslOption = sslOption;
	}
	
	public void listen(String host, int port, HttpListener listener) throws HttpServerException {
		HandlerList handlerList = new HandlerList();
		handlerList.add(new HttpHandler(listener));
		if(sslOption != null) {
			if(sslOption.getCert() == null || sslOption.getKey() == null) {
				throw new HttpServerException("certificate and privateKey is required");
			}
			SslContext opt = new SslContext(false, false)
					.useCertificate(sslOption.getCert(), sslOption.getKey());
			if(sslOption.getCa() != null) {
				opt.trustCertificateAuth(sslOption.getCa());
			}
			if(sslOption.getEncryptCert() != null && sslOption.getEncryptKey() != null) {
				opt.useEncryptCertificate(sslOption.getEncryptCert(), sslOption.getEncryptKey());
			}
			server = new ServerChannel(opt);
		} else {
			server = new ServerChannel();
		}
		server.handlerList(handlerList);
		server.listen(host, port);
	}
	
	public void destroy() {
		server.close();
	}
}
