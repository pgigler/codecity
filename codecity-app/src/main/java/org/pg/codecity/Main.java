package org.pg.codecity;

public class Main {
    public static void main(String[] args) throws Exception {
        String projectPath = "c:\\Projects\\HPS\\Development\\11-HPR\\20 Track and Trace\\TrackingAll\\tracking-utils";
        MavenProject mavenProject = MavenProject.parseMavenProject(projectPath);

        mavenProject.dump();

    }

}
