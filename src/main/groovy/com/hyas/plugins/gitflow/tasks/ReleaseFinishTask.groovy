package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.JGitFlow
import com.atlassian.jgitflow.core.ReleaseMergeResult
import com.hyas.plugins.gitflow.tasks.credentials.CredentialsHelper
import com.hyas.plugins.gitflow.tasks.helper.ArtifactHelper
import com.hyas.plugins.gitflow.tasks.helper.GitHelper
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class ReleaseFinishTask extends AbstractTask {

    @TaskAction
    void taskAction() {
        CredentialsHelper.setupProvider(project)

        String newVersionIncrement = project.properties['newVersionIncrement'] ?: "PATCH";
        String releaseVersion = project.properties['releaseVersion'] ?: loadVersionFromGradleProperties()
        String newVersion = project.properties['newVersion'] ?: ArtifactHelper.newSnapshotVersion(releaseVersion, newVersionIncrement)
        boolean pushRelease = true

        if (project.properties.containsKey('pushRelease')) {
            pushRelease = Boolean.valueOf(project.property('pushRelease') as String)
        }

        JGitFlow flow = JGitFlow.get(project.rootProject.rootDir)
        def command = flow.releaseFinish(releaseVersion)

        setCommandPrefixAndSuffix(command)

        ReleaseMergeResult mergeResult = command.call()
        if (!mergeResult.wasSuccessful()) {
            if (mergeResult.masterHasProblems()) {
                logger.error("Error merging into " + flow.getMasterBranchName() + ":")
                logger.error(mergeResult.getMasterResult().toString());
            }

            if (mergeResult.developHasProblems()) {
                logger.error("Error merging into " + flow.getDevelopBranchName() + ":")
                logger.error(mergeResult.getDevelopResult().toString());
            }

            throw new GradleException("Error while merging release!");
        }

        GitHelper.updateGradlePropertiesFile(project, newVersion)

        GitHelper.commitGradlePropertiesFile(flow.git(), getScmMessagePrefix(command) + "Updated gradle.properties to version '${newVersion}'" + getScmMessageSuffix(command))

        if (pushRelease) {
            flow.git().push().setPushAll().setPushTags().call();
        }

        flow.git().close()
    }

    private String loadVersionFromGradleProperties() {
        if (!project.hasProperty('version')) {
            throw new GradleException('version or releaseVersion property have to be present')
        }

        ArtifactHelper.removeSnapshot(project.property('version') as String)
    }

}
