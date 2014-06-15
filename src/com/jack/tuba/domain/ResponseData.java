package com.jack.tuba.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ResponseData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Result> results=new LinkedList<Result>();
	
//	private Cursor cursor=new Cursor();
	
//	public Cursor getCursor() {
//		return cursor;
//	}
//
//	public void setCursor(Cursor cursor) {
//		this.cursor = cursor;
//	}
	
	public ResponseData() {
		// TODO Auto-generated constructor stub
	}

	public LinkedList<Result> getResults() {
		return results;
	}

	public void setResults(LinkedList<Result> results) {
		this.results = results;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
		

}
