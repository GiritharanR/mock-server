package org.mockserver.plugin.task;

import org.gradle.api.tasks.TaskAction;
import org.mockserver.client.server.MockServerClient;

public class MockServerCleanTask extends AbstractTask {

	@TaskAction
	public void action() {
		new MockServerClient(getHost(), getPort()).reset();
	}
	
}
