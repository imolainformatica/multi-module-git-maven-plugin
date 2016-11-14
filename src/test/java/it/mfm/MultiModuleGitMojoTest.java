package it.mfm;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class MultiModuleGitMojoTest extends AbstractMojoTestCase {
    
    public void testSimpleBuild() throws Exception {
        File pom = getTestFile("src/test/resources/unit/multi-module-git-mojo/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());
        MultiModuleGitMojo mojo = (MultiModuleGitMojo) lookupMojo("multi-module-git", pom);
        assertNotNull(mojo);
        mojo.execute();
    }

}
