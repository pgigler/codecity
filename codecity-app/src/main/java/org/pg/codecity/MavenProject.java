package org.pg.codecity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MavenProject {
    List<JavaClass> javaClasses = new ArrayList<JavaClass>();

    private MavenProject() {};
    public static MavenProject parseMavenProject(String projectPath) throws Exception {
        MavenProject mavenProject = new MavenProject();

        String sourceDir = projectPath + "\\src\\main\\java";

        Collection files = FileUtils.listFiles(
                new File(sourceDir),
                new WildcardFileFilter("*.java"),
                DirectoryFileFilter.DIRECTORY
        );

        for (File file : (File[]) files.toArray(new File[] {})) {
            mavenProject.addJavaClass(JavaClass.parseCommonTree(file));
        }
        return mavenProject;
    }

    private void addJavaClass(JavaClass javaClass) {
        javaClasses.add(javaClass);
    }

    public void dump() {
        for (JavaClass javaClass : javaClasses) {
            javaClass.dump();
        }
    }
}
