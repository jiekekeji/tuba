package com.jack.tuba.domain;

import java.io.Serializable;


public class Image implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private ResponseData responseData=new ResponseData();

	private String responseDetails;
	
	private int responseStatus;

    public Image() {
		// TODO Auto-generated constructor stub
	}

	public String getResponseDetails() {
		return responseDetails;
	}

	public void setResponseDetails(String responseDetails) {
		this.responseDetails = responseDetails;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(ResponseData responseData) {
		this.responseData = responseData;
	}


	
}
