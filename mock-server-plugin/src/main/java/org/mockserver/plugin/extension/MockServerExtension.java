package org.mockserver.plugin.extension;

import lombok.Data;

@Data
public class MockServerExtension {

	String port = "1080";
	String host = "localhost";
	
}
