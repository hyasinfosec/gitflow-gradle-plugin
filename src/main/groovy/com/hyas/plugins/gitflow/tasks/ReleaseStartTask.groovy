package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.JGitFlow
import com.hyas.plugins.gitflow.tasks.credentials.CredentialsHelper
import com.hyas.plugins.gitflow.tasks.helper.ArtifactHelper
import com.hyas.plugins.gitflow.tasks.helper.GitHelper
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.TaskAction

class ReleaseStartTask extends AbstractTask {

    @TaskAction
    void taskAction() {
        CredentialsHelper.setupProvider(project)

        String releaseVersion = project.properties['releaseVersion'] ?: loadVersionFromGradleProperties()

        validateReleaseVersion(releaseVersion)

        JGitFlow flow = JGitFlow.get(project.rootProject.rootDir)

        flow.git().checkout().setName(flow.getDevelopBranchName()).call()

        String allowSnapshotDependencies = project.hasProperty('allowSnapshotDependencies') ? project.property('allowSnapshotDependencies') : false

        String allowProjectGroupSnapshotDependencies = project.hasProperty('allowProjectGroupSnapshotDependencies') ? project.property('allowProjectGroupSnapshotDependencies') : true

        if (!allowSnapshotDependencies) {
            checkThatNoDependencyIsASnapshot(allowProjectGroupSnapshotDependencies)
        }

        def command = flow.releaseStart(releaseVersion)

        setCommandPrefixAndSuffix(command)

        if (project.hasProperty('baseCommit')) {
            String baseCommit = project.property('baseCommit')
            command.setStartCommit(baseCommit)
        }

        command.call()

        GitHelper.updateGradlePropertiesFile(project, releaseVersion)

        GitHelper.commitGradlePropertiesFile(flow.git(), getScmMessagePrefix(command) + "Updated gradle.properties for v" + releaseVersion + " release" + getScmMessageSuffix(command))

        flow.git().close()
    }

    private String loadVersionFromGradleProperties() {
        if (!project.hasProperty('version')) {
            throw new GradleException('version or releaseVersion property have to be present')
        }

        String version = project.property('version') as String

        if (version == "unspecified") {
            throw new GradleException("Cannot get version property from ${Project.GRADLE_PROPERTIES}")
        }

        ArtifactHelper.removeSnapshot(version)
    }

    private void validateReleaseVersion(String releaseVersion) {
        if (project.version == releaseVersion){
            throw new GradleException("Release version '${releaseVersion}' and current version '${project.version}' must not be equal.")
        }

        if (ArtifactHelper.isSnapshot(releaseVersion)){
            throw new GradleException("Release version must not be a snapshot version: ${releaseVersion}")
        }
    }

    private void checkThatNoDependencyIsASnapshot(String allowProjectGroupSnapshotDependencies) {
        def snapshotDependencies = [] as Set

        project.allprojects.each { project ->
            project.configurations.each { configuration ->
                configuration.allDependencies.each { Dependency dependency ->
                    if ((!allowProjectGroupSnapshotDependencies || !dependency.group.equals(project.group)) && ArtifactHelper.isSnapshot
(dependency.version)) {
                        snapshotDependencies.add("${dependency.group}:${dependency.name}:${dependency.version}")
                    }
                }
            }
        }

        if (!snapshotDependencies.isEmpty()) {
            throw new GradleException("Cannot start a release due to snapshot dependencies: ${snapshotDependencies}")
        }
    }

}
