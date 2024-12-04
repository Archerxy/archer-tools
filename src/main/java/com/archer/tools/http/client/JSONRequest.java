package com.archer.tools.http.client;

import java.io.UnsupportedEncodingException;

import com.archer.net.http.HttpException;
import com.archer.net.http.HttpStatus;
import com.archer.net.http.client.NativeRequest;
import com.archer.net.http.client.NativeRequest.*;
import com.archer.net.http.client.NativeResponse;
import com.archer.xjson.JavaTypeRef;
import com.archer.xjson.XJSONException;
import com.archer.xjson.XJSONStatic;

public class JSONRequest {
	
	public static String get(String httpUrl) throws Exception {
        return get(httpUrl, (Options)null);
    }

    public static String post(String httpUrl, Object body) throws Exception {
        return post(httpUrl, body, (Options)null);
    }

    public static String put(String httpUrl, Object body) throws Exception {
        return put(httpUrl, body, (Options)null);
    }

    public static String delete(String httpUrl, Object body) throws Exception {
        return delete(httpUrl, body, (Options)null);
    }

    public static String get(String httpUrl, Options options) throws Exception {
        return request("GET", httpUrl, null, options);
    }

    public static String post(String httpUrl, Object body, Options options) throws Exception {
        return request("POST", httpUrl, body, options);
    }

    public static String put(String httpUrl, Object body, Options options) throws Exception {
        return request("PUT", httpUrl, body, options);
    }

    public static String delete(String httpUrl, Object body, Options options) throws Exception {
        return request("DELETE", httpUrl, body, options);
    }
    

	public static <T> T get(String httpUrl, Class<T> cls) throws Exception {
        return get(httpUrl, null, cls);
    }

    public static <T> T post(String httpUrl, Object body, Class<T> cls) throws Exception {
        return post(httpUrl, body, null, cls);
    }

    public static <T> T put(String httpUrl, Object body, Class<T> cls) throws Exception {
        return put(httpUrl, body, null, cls);
    }

    public static <T> T delete(String httpUrl, Object body, Class<T> cls) throws Exception {
        return delete(httpUrl, body, null, cls);
    }

    public static <T> T get(String httpUrl, Options options, Class<T> cls) throws Exception {
        return request("GET", httpUrl, null, options, cls);
    }

    public static <T> T post(String httpUrl, Object body, Options options, Class<T> cls) throws Exception {
        return request("POST", httpUrl, body, options, cls);
    }

    public static <T> T put(String httpUrl, Object body, Options options, Class<T> cls) throws Exception {
        return request("PUT", httpUrl, body, options, cls);
    }

    public static <T> T delete(String httpUrl, Object body, Options options, Class<T> cls) throws Exception {
        return request("DELETE", httpUrl, body, options, cls);
    }
    

	public static <T> T get(String httpUrl, JavaTypeRef<T> ref) throws Exception {
        return get(httpUrl, null, ref);
    }

    public static <T> T post(String httpUrl, Object body, JavaTypeRef<T> ref) throws Exception {
        return post(httpUrl, body, null, ref);
    }

    public static <T> T put(String httpUrl, Object body, JavaTypeRef<T> ref) throws Exception {
        return put(httpUrl, body, null, ref);
    }

    public static <T> T delete(String httpUrl, Object body, JavaTypeRef<T> ref) throws Exception {
        return delete(httpUrl, body, null, ref);
    }

    public static <T> T get(String httpUrl, Options options, JavaTypeRef<T> ref) throws Exception {
        return request("GET", httpUrl, null, options, ref);
    }

    public static <T> T post(String httpUrl, Object body, Options options, JavaTypeRef<T> ref) throws Exception {
        return request("POST", httpUrl, body, options, ref);
    }

    public static <T> T put(String httpUrl, Object body, Options options, JavaTypeRef<T> ref) throws Exception {
        return request("PUT", httpUrl, body, options, ref);
    }

    public static <T> T delete(String httpUrl, Object body, Options options, JavaTypeRef<T> ref) throws Exception {
        return request("DELETE", httpUrl, body, options, ref);
    }
	
    public static <T> T request(String method, String httpUrl, Object body, Options option, JavaTypeRef<T> ref) 
			throws UnsupportedEncodingException, XJSONException {
    	String json = request(method, httpUrl, body, option);
    	try {
        	return XJSONStatic.parse(json, ref);
    	} catch(Exception e) {
    		throw new HttpException(HttpStatus.OK.getCode(), "can not parse '" + json+"' to " + ref.getJavaType().getTypeName());
    	}
	}
    
    public static <T> T request(String method, String httpUrl, Object body, Options option, Class<T> cls) 
			throws UnsupportedEncodingException, XJSONException {
    	String json = request(method, httpUrl, body, option);
    	try {
        	return XJSONStatic.parse(json, cls);
    	} catch(Exception e) {
    		throw new HttpException(HttpStatus.OK.getCode(), "can not parse '" + json+"' to " + cls.getName());
    	}
	}
    
	public static String request(String method, String httpUrl, Object body, Options option) 
			throws UnsupportedEncodingException, XJSONException {
		if(option == null) {
			option = new Options();
		}
		byte[] data = new byte[0];
		if(body != null) {
			data = XJSONStatic.stringify(body).getBytes(option.getEncoding());
		}
		NativeResponse res = NativeRequest.request(method, httpUrl, data, option);
		String resBody = new String(res.getBody(), option.getEncoding());
		if(res.getStatusCode() != NativeResponse.HTTP_OK) {
			throw new HttpException(res.getStatusCode(), resBody);
		}
		return resBody;
	}
	
	public static String request(String method, String httpUrl, String body, Options option) 
			throws UnsupportedEncodingException, XJSONException {
		if(option == null) {
			option = new Options();
		}
		byte[] data = new byte[0];
		if(body != null) {
			data = body.getBytes(option.getEncoding());
		}
		NativeResponse res = NativeRequest.request(method, httpUrl, data, option);
		String resBody = new String(res.getBody(), option.getEncoding());
		if(res.getStatusCode() != NativeResponse.HTTP_OK) {
			throw new HttpException(res.getStatusCode(), resBody);
		}
		return resBody;
	}
}

