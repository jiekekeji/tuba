package com.jack.tuba.domain;

import java.io.Serializable;

public class Page implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int start;
	private int label;
	
    public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
