package com.lipu.findnearbyplacesapp;

public class Type {
	private String typeName;
	private String additional;
	private int iconID;
	
	
	public Type(String typeName, int iconID, String additional) {
		super();
		this.typeName = typeName;
		this.additional = additional;
		this.iconID = iconID;
		
	}
	
	public String gettypeName() {
		return typeName;
	}
	public String getadditional() {
		return additional;
	}
	public int getIconID() {
		return iconID;
	}
}
