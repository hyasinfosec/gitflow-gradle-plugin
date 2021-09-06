package com.hyas.plugins.gitflow

import com.hyas.plugins.gitflow.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class GitflowPlugin implements Plugin<Project> {

    static final String GROUP_NAME = 'HYAS gitflow'

    @Override
    void apply(Project project) {
        project.task(
            'initGitflow',
            type: InitGitflowTask,
            group: GROUP_NAME,
            description: 'Initializes the gitflow context.')

        project.task(
            'releaseStart',
            type: ReleaseStartTask,
            group: GROUP_NAME,
            description: 'Creates a release branch and updates gradle with the release version.')

        project.task(
            'releaseFinish',
            type: ReleaseFinishTask,
            group: GROUP_NAME,
            description: 'Merges a release branch back into the master branch and develop branch and pushes everything to the remote origin.')

        project.task(
            'featureStart',
            type: FeatureStartTask,
            description: 'Creates a feature branch.',
            group: GROUP_NAME)

        project.task(
            'featureFinish',
            type: FeatureFinishTask,
            description: 'Merges a feature branch back into the develop branch.',
            group: GROUP_NAME)

        project.task(
            'hotfixStart',
            type: HotfixStartTask,
            description: 'Creates a hotfix branch.',
            group: GROUP_NAME)

        project.task(
            'hotfixFinish',
            type: HotfixFinishTask,
            description: ' Merges a hotfix branch back into the master branch and develop branch.',
            group: GROUP_NAME) {
        }
    }

}
