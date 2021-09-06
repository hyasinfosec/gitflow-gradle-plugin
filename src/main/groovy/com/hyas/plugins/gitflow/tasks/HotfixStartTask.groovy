package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.JGitFlow
import com.hyas.plugins.gitflow.tasks.credentials.CredentialsHelper
import org.gradle.api.tasks.TaskAction

class HotfixStartTask extends AbstractTask {

    @TaskAction
    void taskAction() {
        CredentialsHelper.setupProvider(project)

        String hotfixName = project.property('hotfixName')
        JGitFlow flow = JGitFlow.get(project.rootProject.rootDir)
        def command = flow.hotfixStart(hotfixName)

        setCommandPrefixAndSuffix(command)

        if (project.hasProperty('baseCommit')) {
            String baseCommit = project.property('baseCommit')
            command.setStartCommit(baseCommit)
        }

        command.call()

        flow.git().close()
    }

}
