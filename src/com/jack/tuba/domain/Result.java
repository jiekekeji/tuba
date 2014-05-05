package com.jack.tuba.domain;

import java.io.Serializable;

public class Result implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gsearchResultClass;
    private int width;
    private int  height;
    private String imageId;
    private int tbWidth;
    private int tbHeight;
    private String unescapedUrl;
    private String url;
    private String visibleUrl;
    private String title;
    private String titleNoFormatting;
    private String originalContextUrl;
    private String content;
    private String contentNoFormatting;
    private String tbUrl;
    
	public String getGsearchResultClass() {
		return gsearchResultClass;
	}
	public void setGsearchResultClass(String gsearchResultClass) {
		this.gsearchResultClass = gsearchResultClass;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public int getTbWidth() {
		return tbWidth;
	}
	public void setTbWidth(int tbWidth) {
		this.tbWidth = tbWidth;
	}
	public int getTbHeight() {
		return tbHeight;
	}
	public void setTbHeight(int tbHeight) {
		this.tbHeight = tbHeight;
	}
	public String getUnescapedUrl() {
		return unescapedUrl;
	}
	public void setUnescapedUrl(String unescapedUrl) {
		this.unescapedUrl = unescapedUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVisibleUrl() {
		return visibleUrl;
	}
	public void setVisibleUrl(String visibleUrl) {
		this.visibleUrl = visibleUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleNoFormatting() {
		return titleNoFormatting;
	}
	public void setTitleNoFormatting(String titleNoFormatting) {
		this.titleNoFormatting = titleNoFormatting;
	}
	public String getOriginalContextUrl() {
		return originalContextUrl;
	}
	public void setOriginalContextUrl(String originalContextUrl) {
		this.originalContextUrl = originalContextUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentNoFormatting() {
		return contentNoFormatting;
	}
	public void setContentNoFormatting(String contentNoFormatting) {
		this.contentNoFormatting = contentNoFormatting;
	}
	public String getTbUrl() {
		return tbUrl;
	}
	public void setTbUrl(String tbUrl) {
		this.tbUrl = tbUrl;
	}

}
