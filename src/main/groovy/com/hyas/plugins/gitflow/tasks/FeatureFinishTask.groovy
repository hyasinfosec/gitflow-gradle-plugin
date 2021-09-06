package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.JGitFlow
import com.hyas.plugins.gitflow.tasks.credentials.CredentialsHelper
import org.eclipse.jgit.api.MergeResult
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class FeatureFinishTask extends AbstractTask {

    @TaskAction
    void taskAction() {
        CredentialsHelper.setupProvider(project)

        String featureName = project.property('featureName')
        JGitFlow flow = JGitFlow.get(project.rootProject.rootDir)
        def command = flow.featureFinish(featureName)

        setCommandPrefixAndSuffix(command)

        MergeResult mergeResult = command.call()

        if (!mergeResult.getMergeStatus().isSuccessful()) {
            getLogger().error("Error merging into " + flow.getDevelopBranchName() + ":");
            getLogger().error(mergeResult.toString());
            throw new GradleException("Error while merging feature!");
        }

        flow.git().close()
    }

}
