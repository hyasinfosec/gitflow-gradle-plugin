package com.hyas.plugins.gitflow.tasks

import com.atlassian.jgitflow.core.InitContext
import com.atlassian.jgitflow.core.JGitFlow
import com.hyas.plugins.gitflow.tasks.credentials.CredentialsHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class InitGitflowTask extends DefaultTask {

    @Input
    @Optional
    String master = "master";

    @Input
    @Optional
    String develop = "develop";

    @Input
    @Optional
    String release = "release/";

    @Input
    @Optional
    String feature = "feature/";

    @Input
    @Optional
    String hotfix = "hotfix/";

    @Input
    @Optional
    String versiontag = "";

    @TaskAction
    void taskAction() {
        CredentialsHelper.setupProvider(project)

        InitContext initContext = new InitContext()

        initContext.setMaster(master)
                .setDevelop(develop)
                .setRelease(release)
                .setFeature(feature)
                .setHotfix(hotfix)
                .setVersiontag(versiontag)

        JGitFlow flow = JGitFlow.forceInit(project.rootProject.rootDir, initContext)

        flow.git().checkout().setName(flow.getDevelopBranchName()).call()

        flow.git().close()
    }

}
