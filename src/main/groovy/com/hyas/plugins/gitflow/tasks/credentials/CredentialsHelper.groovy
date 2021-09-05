package com.hyas.plugins.gitflow.tasks.credentials

import com.atlassian.jgitflow.core.JGitFlowReporter
import com.google.common.base.Strings
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.gradle.api.Project

class CredentialsHelper {

    public static void setupProvider(Project project) {
        if (project.hasProperty('gpr.user') && project.hasProperty('gpr.key')) {
            String username = project.property("gpr.user")
            String password = project.property("gpr.key")

            if (!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(password)) {
                JGitFlowReporter.get().debugText(getClass().getSimpleName(), "Using provided username and password");
                CredentialsProvider.setDefault(new UsernamePasswordCredentialsProvider(username, password));
            }
        }
    }
}
