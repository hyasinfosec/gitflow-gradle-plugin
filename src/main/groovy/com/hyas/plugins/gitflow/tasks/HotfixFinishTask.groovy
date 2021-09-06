package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.JGitFlow
import com.atlassian.jgitflow.core.ReleaseMergeResult
import com.hyas.plugins.gitflow.tasks.credentials.CredentialsHelper
import com.hyas.plugins.gitflow.tasks.helper.ArtifactHelper
import com.hyas.plugins.gitflow.tasks.helper.GitHelper
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class HotfixFinishTask extends AbstractTask {

    @TaskAction
    void taskAction() {
        CredentialsHelper.setupProvider(project)

        String hotfixName = project.property('hotfixName')
        JGitFlow flow = JGitFlow.get(project.rootProject.rootDir)
        def command = flow.hotfixFinish(hotfixName)

        setCommandPrefixAndSuffix(command)

        ReleaseMergeResult mergeResult = command.call()

        if (!mergeResult.wasSuccessful()) {
            if (mergeResult.masterHasProblems()) {
                logger.error("Error merging into " + flow.getMasterBranchName() + ":");
                logger.error(mergeResult.getMasterResult().toString());
            }

            if (mergeResult.developHasProblems()) {
                logger.error("Error merging into " + flow.getDevelopBranchName() + ":");
                logger.error(mergeResult.getDevelopResult().toString());
            }
            throw new GradleException("Error while merging hotfix!");
        }

        flow.git().close()
    }

}
