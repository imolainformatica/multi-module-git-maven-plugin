package it.mfm.biz;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;

import it.mfm.model.Widget;

/**
 * Classe per la gestione della build dell'app. Si occupa di copiare le risorse
 * all'interno dell'app e al build della stessa
 *
 */
public class BuildHelper {

    public static final Logger logger = LogManager.getLogger(BuildHelper.class);

    /**
     * Gestisce la copia delle varie risorse (css, js, img) dal widget alle
     * cartelle presenti in {@code basedir}.
     * 
     * @param widget
     *            - il widget per il recupero del percorso
     * @param repodir
     *            - il path del repository di base
     * @param basedir
     *            - la root in cui copiare le risorse
     * @throws MojoExecutionException
     * @throws IOException
     */
    public void copyResources(Widget widget, String repodir, String basedir) throws MojoExecutionException, IOException {

        String widgetCompletePath = repodir + "/" + widget.getLocalDir();
        logger.debug("Copia delle risorse da [" + widgetCompletePath + "] a [" + basedir + "]");

        // Bisogna copiare i vari file nella app
        // images
        File source = new File(widgetCompletePath + "/images");
        File dest = new File(basedir + "/images");
        FileUtils.copyDirectory(source, dest);
        logger.debug("Copia immagini eseguita");
        // js
        source = new File(widgetCompletePath + "/js");
        dest = new File(basedir + "/js");
        FileUtils.copyDirectory(source, dest);
        logger.debug("Copia js eseguita");
        // fonts
        source = new File(widgetCompletePath + "/fonts");
        dest = new File(basedir + "/fonts");
        FileUtils.copyDirectory(source, dest);
        logger.debug("Copia fonts eseguita");
        // css
        source = new File(widgetCompletePath + "/styles");
        dest = new File(basedir + "/styles");
        FileUtils.copyDirectory(source, dest);
        logger.debug("Copia css eseguita");
        // html
        source = new File(widgetCompletePath + "/views");
        dest = new File(basedir + "/views");
        FileUtils.copyDirectory(source, dest);
        logger.debug("Copia view eseguita");

    }

    /**
     * Invocazione del plugin {@code frontend-maven-plugin} per il build tramite
     * gulp
     */
    public void buildApp(String basedir) {
    }
}
