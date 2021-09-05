package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.command.JGitFlowCommand
import org.gradle.api.DefaultTask

class AbstractTask extends DefaultTask {

	static final String BLANK = ""
	static final String SPACE = " "

	void setCommandPrefixAndSuffix(JGitFlowCommand command) {
		if (project.hasProperty('scmMessagePrefix')) {
			command.setScmMessagePrefix(project.property('scmMessagePrefix').toString().trim() + SPACE)
		}

		if (project.hasProperty('scmMessageSuffix')) {
			command.setScmMessageSuffix(SPACE + project.property('scmMessageSuffix').toString().trim())
		}
	}

	def getScmMessagePrefix(JGitFlowCommand command) {
		if (command.getScmMessagePrefix()?.trim()) {
			return command.getScmMessagePrefix()
		} else {
			return BLANK
		}
	}

	def getScmMessageSuffix(JGitFlowCommand command) {
		if (command.getScmMessageSuffix()?.trim()) {
			return command.getScmMessageSuffix()
		} else {
			return BLANK
		}
	}

}
