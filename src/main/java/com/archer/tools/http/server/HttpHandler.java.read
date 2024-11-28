package com.archer.tools.http.server;

import java.time.LocalDateTime;

import com.archer.net.http.ContentType;
import com.archer.net.http.HttpRequest;
import com.archer.net.http.HttpResponse;
import com.archer.net.http.HttpStatus;
import com.archer.net.http.HttpWrappedHandler;

final class HttpHandler extends HttpWrappedHandler {
	
	private static final String DEFAULT_ENCODING = "utf-8";
	private HttpListener listener;
	
	public HttpHandler(HttpListener listener) {
		this.listener = listener;
	}

	@Override
	public void handle(HttpRequest req, HttpResponse res) throws Exception {
		res.setContentType(ContentType.APPLICATION_JSON);
		res.setContentEncoding(DEFAULT_ENCODING);
		listener.inComingMessage(req, res);
	}

	@Override
	public void handleException(HttpRequest req, HttpResponse res, Throwable t) {
		try {
			listener.onServerException(req, res, t);
		} catch(Exception ignore) {}
		
		if(res.getStatus() == null) {
			String body = "{" +
					"\"server\": \"Archer Http Server Support\"," +
					"\"time\": \"" + LocalDateTime.now().toString() + "\"," +
					"\"status\": \"" + HttpStatus.SERVICE_UNAVAILABLE.getStatus() + "\"" +
				"}";
			
			res.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
			res.setContentType(ContentType.APPLICATION_JSON);
			res.setContent(body.getBytes());
		}
	}
}
