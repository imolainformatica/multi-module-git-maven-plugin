package it.mfm.biz;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

import it.mfm.model.Widget;
import it.mfm.util.JGitUtil;

/**
 * Handler per l'interazione con i repository git
 */
public class GitHelper {

    public static final Logger logger = LogManager.getLogger(GitHelper.class);

    /**
     * Metodo per la gestione dei repository all'interno del build della app.
     * Deve verificare che un repository esista e se sia stato o meno clonato in
     * locale, in maniera che si possa fare la copia dei file all'interno della
     * app.
     * 
     * @param widget
     *            - il {@link Widget} di cui si stanno recuperando i sorgenti da
     *            repository
     * @param repodir
     *            - il percorso dove si vuole effettuare il clone/checkout del
     *            progetto
     * @throws Exception
     */
    public void handleRepository(Widget widget, String repodir) {

        if (widget != null) {

            String widgetLocalNameRepository = widget.getLocalDir();
            String localPathRepository = repodir + "/" + widgetLocalNameRepository;
            String widgetVersion = widget.getVersion();

            logger.debug("widgetLocalNameRepository=[" + widgetLocalNameRepository + "];localPathRepository=["
                    + localPathRepository + "];widgetVersion=[" + widgetVersion + "]");
            
            try {
                JGitUtil.checkoutRepo(widget.getRepository(), widget.getVersion(), localPathRepository);
            } catch (RefNotFoundException e) {
                logger.error("Versione non trovata. [branch="+widget.getVersion()+"]", e);
            } catch (InvalidRefNameException e) {
                logger.error("Nome versione non valido. [branch="+widget.getVersion()+"].", e);
            } catch (GitAPIException e) {
                logger.error("Eccezione API Git:", e);
            } catch (URISyntaxException e) {
                logger.error("Uri sintatticamente non corretta.", e);
            } catch (IOException e) {
                logger.error("Errore di I/O.", e);
            }

        }
    }

}
