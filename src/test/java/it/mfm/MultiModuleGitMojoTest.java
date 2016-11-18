package it.mfm;

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import it.mfm.util.XMLTestUtil;

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
        Assert.assertNotNull("Errore: pom.xml null", pom);
        Assert.assertTrue("Errore: pom.xml non e' un file valido", pom.exists());
        MultiModuleGitMojo mojo = (MultiModuleGitMojo) rule.lookupMojo("multi-module-git", pom);
        Assert.assertNotNull("Errore: mojo null", mojo);
        mojo.execute();
        URL urlDir = Thread.currentThread().getContextClassLoader().getResource("unit/multi-module-git/fake-app");
        File fakeApp = new File(urlDir.getPath());
        Assert.assertTrue("Errore in fase di validazione risultato esecuzione plugin",
                validatePluginExecution(fakeApp, pom));

        logger.debug("FINE TEST - testSimpleBuild");
    }

    private boolean validatePluginExecution(File fakeApp, File pom) {
        int expectedWidgets = 0;
        try {
            expectedWidgets = XMLTestUtil.getEntityNumberFromXml("widget", pom);
        } catch (Exception e) {
            logger.error("Errore in fase di calcolo dei widget nel pom", e);
            return false;
        }
        Assert.assertNotNull("Errore: app null", fakeApp);
        Assert.assertTrue("Errore: app non e' una directory", fakeApp.isDirectory());
        String[] subdirectories = fakeApp.list();
        Assert.assertEquals(
                "Errore: numero di directory nell'app discordante - Attesi: 4 - Trovati:" + subdirectories.length, 4,
                subdirectories.length);
        for (String subdirectory : subdirectories) {
            File sub = new File(fakeApp.getPath() + "/" + subdirectory);
            Assert.assertNotNull("Errore: " + subdirectory + " null", sub);
            Assert.assertTrue("Errore: " + subdirectory + " non e' una directory", sub.isDirectory());
            Assert.assertEquals("Errore: numero di elementi directory discordante - Attesi: " + expectedWidgets
                    + " - Trovati: " + sub.list().length, expectedWidgets, sub.list().length);
        }

        return true;

    }

}
