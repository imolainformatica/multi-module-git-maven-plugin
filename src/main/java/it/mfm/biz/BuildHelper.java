package it.mfm.biz;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

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
     * cartelle presenti in {@code basedir}. Per farlo sfrutta il mojo executor plugin invocando
     * il {@code maven-resource-plugin}
     * 
     * @param mavenProject
     *            - paramentro per l'esecuzione del mojo executor, progetto
     *            maven
     * @param mavenSession
     *            - paramentro per l'esecuzione del mojo executor, sessione
     *            maven
     * @param pluginManager
     *            - paramentro per l'esecuzione del mojo executor, gestore
     *            plugin
     * @param widget
     *            - il widget per il recupero del percorso
     * @param repodir
     *            - il path del repository di base
     * @param basedir
     *            - la root in cui copiare le risorse
     * @throws MojoExecutionException
     */
    public void copyResources(MavenProject mavenProject, MavenSession mavenSession, BuildPluginManager pluginManager,
            Widget widget, String repodir, String basedir) throws MojoExecutionException {

        String widgetCompletePath = repodir + "/" + widget.getLocalDir();
        logger.debug("Copia delle risorse da ["+widgetCompletePath+"] a ["+basedir+"]");
        
        // images
        executeCopyResourcesPlugin(mavenProject, mavenSession, pluginManager, widgetCompletePath + "/images",
                basedir + "/images");
        logger.debug("Copia immagini eseguita");
        // js
        executeCopyResourcesPlugin(mavenProject, mavenSession, pluginManager, widgetCompletePath + "/js",
                basedir + "/js");
        logger.debug("Copia js eseguita");
        // fonts
        executeCopyResourcesPlugin(mavenProject, mavenSession, pluginManager, widgetCompletePath + "/fonts",
                basedir + "/fonts");
        logger.debug("Copia fonts eseguita");
        // css
        executeCopyResourcesPlugin(mavenProject, mavenSession, pluginManager, widgetCompletePath + "/styles",
                basedir + "/styles");
        logger.debug("Copia css eseguita");
        // html
        executeCopyResourcesPlugin(mavenProject, mavenSession, pluginManager, widgetCompletePath + "/views",
                basedir + "/views");
        logger.debug("Copia view eseguita");
        //
        // // Bisogna copiare i vari file nella app
        // // images
        // File source = new File(widgetCompletePath + "/images");
        // File dest = new File(basedir+"/images");
        // FileUtils.copyDirectory(source, dest);
        // // js
        // source = new File(widgetCompletePath + "/js");
        // dest = new File(basedir+"/js");
        // FileUtils.copyDirectory(source, dest);
        // // fonts
        // source = new File(widgetCompletePath + "/fonts");
        // dest = new File(basedir+"/fonts");
        // FileUtils.copyDirectory(source, dest);
        // // css
        // source = new File(widgetCompletePath + "/styles");
        // dest = new File(basedir+"/styles");
        // FileUtils.copyDirectory(source, dest);
        // // html
        // source = new File(widgetCompletePath + "/views");
        // dest = new File(basedir+"/views");
        // FileUtils.copyDirectory(source, dest);

    }

    /**
     * Invocazione del plugin {@code frontend-maven-plugin} per il build tramite gulp
     * 
     * @param mavenProject
     *            - paramentro per l'esecuzione del mojo executor, progetto
     *            maven
     * @param mavenSession
     *            - paramentro per l'esecuzione del mojo executor, sessione
     *            maven
     * @param pluginManager
     *            - paramentro per l'esecuzione del mojo executor, gestore
     *            plugin
     * @throws MojoExecutionException
     */
    public void buildApp(MavenProject mavenProject, MavenSession mavenSession, BuildPluginManager pluginManager)
            throws MojoExecutionException {
        logger.debug("Invocazione plugin maven gulp.");
        executeMojo(
                plugin(
                    groupId("com.github.eirslett"), 
                    artifactId("frontend-maven-plugin"), 
                    version("LATEST_VERSION")
                ),
                goal("gulp"), 
                configuration(element("arguments", "prod")), 
                executionEnvironment(mavenProject, mavenSession, pluginManager)
        );
        logger.debug("Invocazione plugin maven gulp effettuata correttamente.");
    }

    /**
     * Invocazione del plugin {@code maven-resources-plugin} per la copia di
     * file da una directory ad un'altra
     * 
     * @@param mavenProject - paramentro per l'esecuzione del mojo executor,
     *         progetto maven
     * @param mavenSession
     *            - paramentro per l'esecuzione del mojo executor, sessione
     *            maven
     * @param pluginManager
     *            - paramentro per l'esecuzione del mojo executor, gestore
     *            plugin
     * @param sourceDirectory
     *            - la directory dalla quale recuperare gli elementi da copiare
     * @param destinationDirectory
     *            - la directory nella quale copiare gli elementi
     * @throws MojoExecutionException
     */
    private void executeCopyResourcesPlugin(MavenProject mavenProject, MavenSession mavenSession,
            BuildPluginManager pluginManager, String sourceDirectory, String destinationDirectory)
            throws MojoExecutionException {
        logger.debug("Invocazione plugin maven copy resources.");
        executeMojo(
            plugin(
                groupId("org.apache.maven.plugins"), 
                artifactId("maven-resources-plugin"), 
                version("3.0.1")
            ),
            goal("copy-resources"),
            configuration(
                element(name("outputDirectory"), destinationDirectory),
                element(name("directory"), sourceDirectory)
            ),
            executionEnvironment(mavenProject, mavenSession, pluginManager)
        );
        logger.debug("Invocazione plugin maven copy resources effettuata correttamente.");
    }

}
