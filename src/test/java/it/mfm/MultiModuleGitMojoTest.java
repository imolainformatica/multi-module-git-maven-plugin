package it.mfm;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class MultiModuleGitMojoTest extends AbstractMojoTestCase {

    public static final Logger logger = LogManager.getLogger(MultiModuleGitMojoTest.class);

    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
    }

    protected void tearDown() throws Exception {
        // required
        super.tearDown();
    }

    public void testSimpleBuild() throws Exception {

        logger.debug("INIZIO TEST - testSimpleBuild");

        File pom = getTestFile("src/test/resources/unit/multi-module-git-mojo/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());
        MultiModuleGitMojo mojo = (MultiModuleGitMojo) lookupMojo("multi-module-git", pom);
        assertNotNull(mojo);
        mojo.execute();

        logger.debug("FINE TEST - testSimpleBuild");
    }

}
