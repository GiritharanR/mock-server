package org.mockserver.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.mockserver.plugin.extension.MockServerExtension;
import org.mockserver.plugin.task.MockServerCleanTask;
import org.mockserver.plugin.task.MockServerExpectationTask;
import org.mockserver.plugin.task.MockServerInfoTask;
import org.mockserver.plugin.task.MockServerStartTask;
import org.mockserver.plugin.task.MockServerStopTask;

public class MockServerPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getExtensions().create("mockserver", MockServerExtension.class);
		project.getTasks().create("startMockServer", MockServerStartTask.class);
		project.getTasks().create("mockExpectation", MockServerExpectationTask.class);
		project.getTasks().create("mockExpectationInfo", MockServerInfoTask.class);
		project.getTasks().create("clearExpectation", MockServerCleanTask.class);
		project.getTasks().create("stopMockServer", MockServerStopTask.class);
	}

}
