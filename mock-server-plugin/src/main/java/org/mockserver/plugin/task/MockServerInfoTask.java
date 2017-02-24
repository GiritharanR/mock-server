package org.mockserver.plugin.task;

import org.gradle.api.tasks.TaskAction;
import org.mockserver.client.server.MockServerClient;

public class MockServerInfoTask extends AbstractTask {

	@TaskAction
	public void action() throws Exception {
		MockServerClient client = new MockServerClient(getHost(), getPort());
		client.dumpToLog();
	}
}
