package com.archer.tools.psi;

import java.util.List;

public class Pair {
	List<byte[]> p0;
	List<byte[]> p1;
	
	public Pair(List<byte[]> p0, List<byte[]> p1) {
		super();
		this.p0 = p0;
		this.p1 = p1;
	}
	public List<byte[]> getP0() {
		return p0;
	}
	public List<byte[]> getP1() {
		return p1;
	}
}
