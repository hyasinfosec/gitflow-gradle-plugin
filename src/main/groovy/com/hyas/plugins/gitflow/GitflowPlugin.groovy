package com.hyas.plugins.gitflow

import com.hyas.plugins.gitflow.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class GitflowPlugin implements Plugin<Project> {

    static final String GROUP_NAME = 'HYAS gitflow'

    static final String INIT_GITFLOW_TASK_NAME = 'initGitflow'

    static final String RELEASE_START_TASK_NAME = 'releaseStart'
    static final String RELEASE_FINISH_TASK_NAME = 'releaseFinish'

    static final String FEATURE_START_TASK_NAME = 'featureStart'
    static final String FEATURE_FINISH_TASK_NAME = 'featureFinish'

    static final String HOTFIX_START_TASK_NAME = 'hotfixStart'
    static final String HOTFIX_FINISH_TASK_NAME = 'hotfixFinish'

    @Override
    void apply(Project project) {
        project.task(
            INIT_GITFLOW_TASK_NAME,
            type: InitGitflowTask,
            group: GROUP_NAME,
            description: 'Initializes the JGitflow context.')

        project.task(
            RELEASE_START_TASK_NAME,
            type: ReleaseStartTask,
            group: GROUP_NAME,
            description: 'Creates a release branch and updates gradle with the release version.')

        project.task(
            RELEASE_FINISH_TASK_NAME,
            type: ReleaseFinishTask,
            group: GROUP_NAME,
            description: 'Merges a release branch back into the master branch and develop branch and pushes everything to the remote origin.')

        project.task(
            FEATURE_START_TASK_NAME,
            type: FeatureStartTask,
            description: 'Creates a feature branch.',
            group: GROUP_NAME)

        project.task(
            FEATURE_FINISH_TASK_NAME,
            type: FeatureFinishTask,
            description: 'Merges a feature branch back into the develop branch.',
            group: GROUP_NAME)

        project.task(
            HOTFIX_START_TASK_NAME,
            type: HotfixStartTask,
            description: 'Creates a hotfix branch.',
            group: GROUP_NAME)

        project.task(
            HOTFIX_FINISH_TASK_NAME,
            type: HotfixFinishTask,
            description: ' Merges a hotfix branch back into the master branch and develop branch.',
            group: GROUP_NAME) {
        }
    }

}
