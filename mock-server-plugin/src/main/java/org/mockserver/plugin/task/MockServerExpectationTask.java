package org.mockserver.plugin.task;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.gradle.api.tasks.TaskAction;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.plugin.model.MockEntity;

public class MockServerExpectationTask extends AbstractTask {

	@TaskAction
	public void action() throws Exception {
		MockServerClient client = new MockServerClient(getHost(), getPort());
		if (!client.isRunning()) {
			startClientAndServer(getPort());
		}
		client.reset();
		MockEntity[] mockEntities = mockEntity();
		if (mockEntities != null) {
			Arrays.stream(mockEntities).forEach(mockEntity -> {
				HttpRequest httpRequest = buildRequest(mockEntity);
				HttpResponse httpResponse = buildResponse(mockEntity);
				client.when(httpRequest).respond(httpResponse);
			});
		}
	}

	private HttpResponse buildResponse(MockEntity mockEntity) {
		HttpResponse httpResponse = new HttpResponse().withStatusCode(mockEntity.getResponseStatusCode());
		if (mockEntity.getResponseHeaders() != null) {
			Map<String, List<String>> headers = mockEntity.getResponseHeaders();
			if (!headers.entrySet().isEmpty()) {
				List<Header> responseHeaders = new ArrayList<>();
				headers.entrySet().forEach(entry -> {
					Header header = new Header(entry.getKey(), entry.getValue());
					responseHeaders.add(header);
				});
				httpResponse = httpResponse.withHeaders(responseHeaders);
			}
		}
		if (isNotBlank(mockEntity.getResponse())) {
			httpResponse = httpResponse.withBody(mockEntity.getResponse());
		} else if (isNotBlank(mockEntity.getResponseResource())) {
			String response = read("/src/test/resources/mock" + mockEntity.getResponseResource());
			if (isNotBlank(response)) {
				httpResponse = httpResponse.withBody(response);
			}
		}
		return httpResponse;
	}

}
