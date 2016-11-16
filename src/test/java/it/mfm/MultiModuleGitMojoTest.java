package it.mfm;

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MultiModuleGitMojoTest {

    /**
     * Logger di classe
     */
    public static final Logger logger = LogManager.getLogger(MultiModuleGitMojoTest.class);

    @Rule
    public MojoRule rule = new MojoRule();
    
    @Test
    public void testSimpleBuild() throws Exception {

        logger.debug("INIZIO TEST - testSimpleBuild");
        
        URL url = Thread.currentThread().getContextClassLoader().getResource("unit/multi-module-git/pom.xml");
        File pom = new File(url.getPath());
        Assert.assertNotNull(pom);
        Assert.assertTrue(pom.exists());
        MultiModuleGitMojo mojo = (MultiModuleGitMojo) this.rule.lookupMojo("multi-module-git", pom);
        Assert.assertNotNull(mojo);
        mojo.execute();

        logger.debug("FINE TEST - testSimpleBuild");
    }

}
