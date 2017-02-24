package org.mockserver.plugin.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MockEntity {
	
	private String path;
	private String method;
	private Map<String, List<String>> requestHeaders;
	private String request;
	private String requestResource;
	private Integer responseStatusCode;
	private Map<String, List<String>> responseHeaders;
	private String response;
	private String responseResource;
	
}
