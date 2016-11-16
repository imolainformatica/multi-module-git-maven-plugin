package it.mfm.biz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public void handleRepository(Widget widget, String repodir) throws Exception {

        if (widget != null) {

            String widgetLocalNameRepository = widget.getLocalDir();
            String localPathRepository = repodir + "/" + widgetLocalNameRepository;
            String widgetVersion = widget.getVersion();

            logger.debug("widgetLocalNameRepository=[" + widgetLocalNameRepository + "];localPathRepository=["
                    + localPathRepository + "];widgetVersion=[" + widgetVersion + "]");

            if (!JGitUtil.checkRepositoryExists(localPathRepository)) {
                // Il repository non esiste - clone
                logger.debug("Clone del repository.");
                try {
                    String widgetRemoteRepository = widget.getRepository();
                    logger.debug("widgetRemoteRepository=[" + widgetRemoteRepository + "]");
                    JGitUtil.cloneRepositoryToPath(widgetRemoteRepository, widgetVersion, repodir);
                } catch (Exception e) {
                    throw new Exception("Errore in fase di clone del repository.", e);
                }
            } else {
                // Il repository esiste - checkout
                logger.debug("Checkout del repository.");
                try {
                    JGitUtil.checkoutRepository(localPathRepository, widgetVersion);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    throw new Exception("Errore in fase di pull del repository.", e);
                }
            }

        }
    }

}
