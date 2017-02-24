package org.mockserver.plugin.task;

import org.gradle.api.tasks.TaskAction;
import org.mockserver.client.server.MockServerClient;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class MockServerStartTask extends AbstractTask {

	@TaskAction
	public void action() throws Exception {
		MockServerClient mockServerClient = new MockServerClient(getHost(), getPort());
		if (!mockServerClient.isRunning()) {
			startClientAndServer(getPort());
		}
	}
}
