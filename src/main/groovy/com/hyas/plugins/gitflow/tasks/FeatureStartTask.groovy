package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.JGitFlow
import com.hyas.plugins.gitflow.tasks.credentials.CredentialsHelper
import org.gradle.api.tasks.TaskAction

class FeatureStartTask extends AbstractTask {

    @TaskAction
    void taskAction() {
        CredentialsHelper.setupProvider(project)

        String featureName = project.property('featureName')
        JGitFlow flow = JGitFlow.get(project.rootProject.rootDir)
        def command = flow.featureStart(featureName)

        setCommandPrefixAndSuffix(command)

        command.call()

        flow.git().close()
    }

}
