package org.mockserver.plugin.task;

import org.gradle.api.tasks.TaskAction;
import org.mockserver.client.server.MockServerClient;

public class MockServerStopTask extends AbstractTask {

	@TaskAction
	public void action() throws Exception {
		MockServerClient mockServerClient = new MockServerClient(getHost(), getPort());
		if (mockServerClient.isRunning()) {
			mockServerClient.reset();
			try {
				mockServerClient.stop();
			} catch (Exception e) {
				mockServerClient.stop();
			}
		}
	}
}