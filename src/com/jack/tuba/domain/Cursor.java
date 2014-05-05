package com.jack.tuba.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cursor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String resultCount;
    private List<Page> pages=new ArrayList<Page>();
    private String estimatedResultCount;
    private int currentPageIndex;
    private String moreResultsUrl;
    private float searchResultTime;
    
    
	public String getResultCount() {
		return resultCount;
	}
	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}
	public List<Page> getPages() {
		return pages;
	}
	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
	public String getEstimatedResultCount() {
		return estimatedResultCount;
	}
	public void setEstimatedResultCount(String estimatedResultCount) {
		this.estimatedResultCount = estimatedResultCount;
	}
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}
	public String getMoreResultsUrl() {
		return moreResultsUrl;
	}
	public void setMoreResultsUrl(String moreResultsUrl) {
		this.moreResultsUrl = moreResultsUrl;
	}
	public float getSearchResultTime() {
		return searchResultTime;
	}
	public void setSearchResultTime(float searchResultTime) {
		this.searchResultTime = searchResultTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    


}
