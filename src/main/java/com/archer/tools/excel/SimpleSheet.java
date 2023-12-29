package com.archer.tools.excel;

import java.util.List;

public class SimpleSheet {

	
	private String name;
	
	private List<List<String>> cells;

	public SimpleSheet(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public List<List<String>> cells() {
		return cells;
	}

	public SimpleSheet cells(List<List<String>> cells) {
		this.cells = cells;
		return this;
	}
}
