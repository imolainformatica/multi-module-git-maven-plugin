package it.mfm.biz;

import it.mfm.model.Widget;
import it.mfm.util.JGitUtil;

public class GitHandler {

    public void handleRepository(Widget widget, String repodir) throws Exception {

        if (widget != null) {

            String widgetLocalNameRepository = widget.getLocalDir();
            String localPathRepository = repodir + "/" + widgetLocalNameRepository;
            String widgetVersion = widget.getVersion();

            if (!JGitUtil.checkRepositoryExists(localPathRepository)) {
                // Il repository non esiste - clone
                try {
                    String widgetRemoteRepository = widget.getRepository();
                    JGitUtil.cloneRepositoryToPath(widgetRemoteRepository, widgetVersion, repodir);
                } catch (Exception e) {
                    throw new Exception("Errore in fase di clone del repository.", e);
                }
            } else {
                // Il repository esiste - checkout
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
