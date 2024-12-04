package com.archer.tools.http.client;

import java.io.IOException;

import com.archer.net.http.HttpException;
import com.archer.net.http.HttpStatus;
import com.archer.net.http.client.NioRequest;
import com.archer.net.http.client.NioRequest.*;
import com.archer.net.http.client.NioResponse;
import com.archer.xjson.JavaTypeRef;
import com.archer.xjson.XJSONStatic;

class JSONNioRequest {
	
	public static String get(String httpUrl) throws IOException {
        return get(httpUrl, (Options)null);
    }

    public static String post(String httpUrl, Object body) throws IOException {
        return post(httpUrl, body, (Options)null);
    }

    public static String put(String httpUrl, Object body) throws IOException {
        return put(httpUrl, body, (Options)null);
    }

    public static String delete(String httpUrl, Object body) throws IOException {
        return delete(httpUrl, body, (Options)null);
    }

    public static String get(String httpUrl, Options options) throws IOException {
        return request("GET", httpUrl, null, options);
    }

    public static String post(String httpUrl, Object body, Options options) throws IOException {
        return request("POST", httpUrl, body, options);
    }

    public static String put(String httpUrl, Object body, Options options) throws IOException {
        return request("PUT", httpUrl, body, options);
    }

    public static String delete(String httpUrl, Object body, Options options) throws IOException {
        return request("DELETE", httpUrl, body, options);
    }
    

	public static <T> T get(String httpUrl, Class<T> cls) throws IOException {
        return get(httpUrl, null, cls);
    }

    public static <T> T post(String httpUrl, Object body, Class<T> cls) throws IOException {
        return post(httpUrl, body, null, cls);
    }

    public static <T> T put(String httpUrl, Object body, Class<T> cls) throws IOException {
        return put(httpUrl, body, null, cls);
    }

    public static <T> T delete(String httpUrl, Object body, Class<T> cls) throws IOException {
        return delete(httpUrl, body, null, cls);
    }

    public static <T> T get(String httpUrl, Options options, Class<T> cls) throws IOException {
        return request("GET", httpUrl, null, options, cls);
    }

    public static <T> T post(String httpUrl, Object body, Options options, Class<T> cls) throws IOException {
        return request("POST", httpUrl, body, options, cls);
    }

    public static <T> T put(String httpUrl, Object body, Options options, Class<T> cls) throws IOException {
        return request("PUT", httpUrl, body, options, cls);
    }

    public static <T> T delete(String httpUrl, Object body, Options options, Class<T> cls) throws IOException {
        return request("DELETE", httpUrl, body, options, cls);
    }
    

	public static <T> T get(String httpUrl, JavaTypeRef<T> ref) throws IOException {
        return get(httpUrl, null, ref);
    }

    public static <T> T post(String httpUrl, Object body, JavaTypeRef<T> ref) throws IOException {
        return post(httpUrl, body, null, ref);
    }

    public static <T> T put(String httpUrl, Object body, JavaTypeRef<T> ref) throws IOException {
        return put(httpUrl, body, null, ref);
    }

    public static <T> T delete(String httpUrl, Object body, JavaTypeRef<T> ref) throws IOException {
        return delete(httpUrl, body, null, ref);
    }

    public static <T> T get(String httpUrl, Options options, JavaTypeRef<T> ref) throws IOException {
        return request("GET", httpUrl, null, options, ref);
    }

    public static <T> T post(String httpUrl, Object body, Options options, JavaTypeRef<T> ref) throws IOException {
        return request("POST", httpUrl, body, options, ref);
    }

    public static <T> T put(String httpUrl, Object body, Options options, JavaTypeRef<T> ref) throws IOException {
        return request("PUT", httpUrl, body, options, ref);
    }

    public static <T> T delete(String httpUrl, Object body, Options options, JavaTypeRef<T> ref) throws IOException {
        return request("DELETE", httpUrl, body, options, ref);
    }
	
    public static <T> T request(String method, String httpUrl, Object body, Options option, JavaTypeRef<T> ref) 
			throws IOException {
    	String json = request(method, httpUrl, body, option);
    	try {
        	return XJSONStatic.parse(json, ref);
    	} catch(Exception e) {
    		throw new HttpException(HttpStatus.OK.getCode(), "can not parse '" + json+"' to " + ref.getJavaType().getTypeName());
    	}
	}
    
    public static <T> T request(String method, String httpUrl, Object body, Options option, Class<T> cls) 
			throws IOException {
    	String json = request(method, httpUrl, body, option);
    	try {
        	return XJSONStatic.parse(json, cls);
    	} catch(Exception e) {
    		throw new HttpException(HttpStatus.OK.getCode(), "can not parse '" + json+"' to " + cls.getName());
    	}
	}
    
	public static String request(String method, String httpUrl, Object body, Options option) 
			throws IOException {
		if(option == null) {
			option = new Options();
		}
		byte[] data = new byte[0];
		if(body != null) {
			data = XJSONStatic.stringify(body).getBytes(option.getEncoding());
		}
		NioResponse res = NioRequest.request(method, httpUrl, data, option);
		if(res.getStatusCode() != NioResponse.HTTP_OK) {
			throw new HttpException(res.getStatusCode(), res.getStatus());
		}
		return new String(res.getBody(), option.getEncoding());
	}
	
	public static String request(String method, String httpUrl, String body, Options option) 
			throws IOException {
		if(option == null) {
			option = new Options();
		}
		byte[] data = new byte[0];
		if(body != null) {
			data = body.getBytes(option.getEncoding());
		}
		NioResponse res = NioRequest.request(method, httpUrl, data, option);
		if(res.getStatusCode() != NioResponse.HTTP_OK) {
			throw new HttpException(res.getStatusCode(), res.getStatus());
		}
		return new String(res.getBody(), option.getEncoding());
	}
}

