package it.mfm;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.util.StringUtils;

import it.mfm.biz.BuildHelper;
import it.mfm.biz.GitHelper;
import it.mfm.model.Widget;
import it.mfm.util.FileSystemUtil;

@Mojo(name = "multi-module-git", defaultPhase = LifecyclePhase.PACKAGE, requiresOnline = true, threadSafe = false)
public class MultiModuleGitMojo extends AbstractMojo {

    /******************************************************************************
     * La root dell'applicazione invocante. Serve per calcolare i path relativi
     * alle risorse
     ******************************************************************************/
    @Parameter(property = "multi-module-git.basedir", required = true)
    private String basedir;

    /******************************************************************************
     * La directory dove clonare e controllare i repository
     ******************************************************************************/
    @Parameter(property = "multi-module-git.repodir")
    private String repodir;

    /******************************************************************************
     * La lista dei vari componenti da inseire nell'app
     ******************************************************************************/
    @Parameter(property = "multi-module-git.widgets")
    private List<Widget> widgets;

    /******************************************************************************
     * Gestori delle logiche del plugin
     ******************************************************************************/
    GitHelper gitHelper = new GitHelper();
    BuildHelper buildHelper = new BuildHelper();

    /*****************************************************************************
     * getter & setter
     *****************************************************************************/
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

    /******************************************************************************
     * Esecuzione del plugin
     ******************************************************************************/
    public void execute() throws MojoExecutionException, MojoFailureException {

        /*
         * CONTROLLI PARAMETRI
         */
        try {
            checkInputParams();
        } catch (Exception e) {
            // Errore parametri di configurazione
            getLog().error("Errore nella verifica dei parametri di input.\n" + e.getMessage() + "\n", e);
            return;
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
                gitHelper.handleRepository(widget, repodir);
            } catch (Exception e) {
                getLog().error("Errore durante la gestione del repository.", e);
                return;
            }
            
            // Copia dei file
            try {
                buildHelper.copyResources( widget, repodir, basedir);
            } catch (IOException e) {
                getLog().error("Errore durante la copia delle risorse: " + e.getMessage()+" .", e);
                e.printStackTrace();
            }
        }

        // PARTE APP
        // Build della app
        //buildHelper.buildApp(basedir);

    }

    /******************************************************************************
     * Metodo di utilità per il controllo dei parametri di input del plugin.
     * Restituisce un eccezione in caso il parametro {@code basedir} non sia
     * presente o non punti ad una directory
     * 
     * @throws Exception
     ******************************************************************************/
    private void checkInputParams() throws Exception {

        String basedir = this.getBasedir();
        if (StringUtils.isEmptyOrNull(basedir)) {
            throw new Exception("Errore di inizializzazione: il campo basedir è obbligatorio");
        }

        File file = new File(basedir);
        if (!FileSystemUtil.isFileADirectory(file)) {
            throw new Exception("Errore di inizializzazione: il campo basedir non punta ad una directory corretta");
        }
    }

}
