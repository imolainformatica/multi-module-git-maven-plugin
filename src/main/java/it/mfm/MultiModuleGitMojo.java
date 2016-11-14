package it.mfm;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.util.StringUtils;

import it.mfm.biz.GitHandler;
import it.mfm.biz.ResourceHandler;
import it.mfm.model.Widget;
import it.mfm.util.MultiModuleGitUtil;

@Mojo(name = "multi-module-git", defaultPhase = LifecyclePhase.PACKAGE, requiresOnline = true, threadSafe = false)
public class MultiModuleGitMojo extends AbstractMojo {

    /*
     * STRUTTURA:
     * 
     * 1) APP PRINCIPALE 2) W1 . . . WN
     * 
     * - SCARICARE I SORGENTI DEI WIDGET DA GIT 
     * - COPIARE LE RISORSE NEL PATH CORRETTO DELLA APP 
     * - COMPILARE IL TUTTO CON GULP
     * 
     * NOTE: I WIDGET DEVONO AVERE UNA CERTA STRUTTURA. ALTRIMENTI BISOGNA
     * PREVEDERE DIVERSI PARAMETRI (UNO PER RISORSA E.G. CSS, JS, ...)
     * 
     */

    /**
     * La root dell'applicazione invocante. Serve per calcolare i path relativi
     * alle risorse
     */
    @Parameter(property = "multi-module-git.basedir", required = true)
    private String basedir;

    /**
     * La directory dove clonare e controllare i repository
     */
    @Parameter(property = "multi-module-git.repodir")
    private String repodir;

    /**
     * La lista dei vari componenti da inseire nell'app
     */
    @Parameter(property = "multi-module-git.widgets")
    private List<Widget> widgets;

    GitHandler gitHandler = new GitHandler();
    ResourceHandler resourceHandler = new ResourceHandler();

    public String getBasedir() {
        return basedir;
    }

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public String getRepodir() {
        return repodir;
    }

    public void setRepodir(String repodir) {
        this.repodir = repodir;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {

        /*
         * CONTROLLI PARAMETRI
         */
        try {
            MultiModuleGitUtil.checkInputParams(this);
        } catch (Exception e) {
            // Errore parametri di configurazione
            getLog().error("Errore nella verifica dei parametri di input.\n" + e.getMessage() + "\n", e);
        }

        /*
         * LOGICA GESTIONE COMPONENTI ESTERNI
         */
        for (Widget widget : widgets) {

            if (StringUtils.isEmptyOrNull(repodir)) {
                // Se la directory dei repository non è settata, ne creo una in
                // quella corrente
                repodir = "./repository";
            }
            
            // Gestione repository git
            try {
                gitHandler.handleRepository(widget, repodir);
            } catch (Exception e) {
                getLog().error("Errore durante la gestione del repository.", e);
            }

            // TODO Gestione risorse
            resourceHandler.handleResources(widget, repodir);
            
            // STEP 5 - Copio le risorse nel path corretto della app --> TODO
            // !!!Clash di nomi ? !!!
        }

        // PARTE APP
        // STEP 1 - Build della app

    }

}
