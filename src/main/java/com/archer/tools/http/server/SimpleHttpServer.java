package com.archer.tools.http.server;

import com.archer.net.EventLoop;
import com.archer.net.ServerChannel;
import com.archer.net.SslContext;

public class SimpleHttpServer {
	
	private SslOption sslOption;
	private ServerChannel server;
	
	public SimpleHttpServer() {
		this(null);
	}
	
	public SimpleHttpServer(SslOption sslOption) {
		this.sslOption = sslOption;
	}
	
	public void listen(int port, HttpListener listener) throws HttpServerException {
		EventLoop loop = new EventLoop();
		loop.addHandlers(new HttpHandler(listener));
		if(sslOption != null) {
			if(sslOption.getCert() == null || sslOption.getKey() == null) {
				throw new HttpServerException("certificate and privateKey is required");
			}
			SslContext opt = new SslContext()
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
		server.eventLoop(loop);
		server.listen(port);
	}
	
	public void destroy() {
		server.close();
	}
}
