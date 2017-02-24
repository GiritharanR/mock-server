package org.mockserver.plugin.task;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.plugin.extension.MockServerExtension;
import org.mockserver.plugin.model.MockEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public abstract class AbstractTask extends DefaultTask {

	public int getPort() {
		MockServerExtension mockServerExtension = getProject().getExtensions().getByType(MockServerExtension.class);
		Project project = getProject();
		Integer port = null;
		if (project.hasProperty("mockserver.port")) {
			port = Integer.parseInt(project.getProperties().get("mockserver.port").toString());
		} else {
			port = Integer.parseInt(mockServerExtension.getPort());
		}
		return port;
	}
	
	public String getHost() {
		MockServerExtension mockServerExtension = getProject().getExtensions().getByType(MockServerExtension.class);
		Project project = getProject();
		String host = null;
		if (project.hasProperty("mockserver.host")) {
			host = project.getProperties().get("mockserver.host").toString();
		} else {
			host = mockServerExtension.getHost();
		}
		return host;
	}
	
	public MockEntity[] mockEntity() throws Exception {
		MockEntity[] mockEntities = null;
		String filePath = getProject().getProjectDir().getAbsolutePath() + "/src/test/resources/mock/mockconfiguration.yml";
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			try {
				mockEntities = mapper.readValue(file, MockEntity[].class);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return mockEntities;
	}
	
	protected HttpRequest buildRequest(MockEntity mockEntity) {
		HttpRequest httpRequest = new HttpRequest().withPath(mockEntity.getPath());
		if (isNotBlank(mockEntity.getMethod())) {
			httpRequest = httpRequest.withMethod(mockEntity.getMethod());
		}
		if (mockEntity.getRequestHeaders() != null) {
			Map<String, List<String>> headers = mockEntity.getRequestHeaders();
			System.out.println("headers are -> " + headers.entrySet().isEmpty());
			if (!headers.entrySet().isEmpty()) {
				List<Header> requestHeaders = new ArrayList<>();
				headers.entrySet().forEach(entry -> {
					Header header = new Header(entry.getKey(), entry.getValue());
					requestHeaders.add(header);
				});
				httpRequest = httpRequest.withHeaders(requestHeaders);
			}
		}
		if (isNotBlank(mockEntity.getRequest())) {
			httpRequest = httpRequest.withBody(mockEntity.getRequest());
		} else if (isNotBlank(mockEntity.getRequestResource())) {
			String request = read("/src/test/resources/mock" + mockEntity.getRequestResource());
			if (isNotBlank(request)) {
				httpRequest = httpRequest.withBody(request);
			}
		}
		return httpRequest;
	}
	
	protected String read(String location) {
		String result = null;
		String filePath = getProject().getProjectDir().getAbsolutePath() + location;
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			try {
				byte[] content = Files.readAllBytes(Paths.get(filePath));
				result = new String(content, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
