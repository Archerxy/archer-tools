package com.archer.tools.http.server;

import com.archer.net.http.HttpRequest;
import com.archer.net.http.HttpResponse;

public interface HttpListener {

	void inComingMessage(HttpRequest req, HttpResponse res);
	
	void onServerException(HttpRequest req, HttpResponse res, Throwable t);
}
