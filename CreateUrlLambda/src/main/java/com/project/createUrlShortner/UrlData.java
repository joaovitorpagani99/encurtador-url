package com.project.createUrlShortner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlData {

	private String originalUrl;
	private long expirationTime;
	
	public UrlData(String originalUrl, long expirationTime) {
	    this.originalUrl = originalUrl;
	    this.expirationTime = expirationTime;
	}

}
