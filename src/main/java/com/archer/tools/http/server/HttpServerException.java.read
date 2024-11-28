package com.archer.tools.http.server;


public class HttpServerException extends Exception {

	private static final long serialVersionUID = 128378174983472L;
	
	private static final int DEPTH = 16;
	
	public HttpServerException(String msg) {
		super(msg);
	}
	
	@Override
	public String toString() {
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		int len = DEPTH > stacks.length ? stacks.length : DEPTH;
		StringBuilder sb = new StringBuilder(DEPTH * 128);
		sb.append(super.getLocalizedMessage()).append("; at ");
		for(int i = 0; i < len; i++) {
			sb.append(stacks[i].getClassName()).append(':')
			  .append(stacks[i].getLineNumber()).append(';');
		}
		return sb.toString();
	}
}
