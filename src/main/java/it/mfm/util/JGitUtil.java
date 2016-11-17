package it.mfm.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Classe di utilità per l'interazione con i repository git attraverso JGit.
 */
public class JGitUtil {

    /**
     * Logger di classe.
     */
    public static final Logger logger = LogManager.getLogger(JGitUtil.class);

    /**
     * Metodo per la gestione del checkout del progetto. Se la versione indicata
     * da {@code branchName} non è presente nel percorso indicato dal parametro
     * {@code localRepoPath}, viene fatto il clone dall'indirizzo
     * {@code cloneUrl}, altrimenti il solo checkout. La sequenza dei passi da
     * fare per riutilizzare una cartella non vuota è la seguente: git init . +
     * git remote add origin ... + git fetch + git reset --hard origin/master
     * 
     * @param cloneUrl
     *            - l'indirizzo del repository dal quale fare il clone
     * @param branchName
     *            - la branch di cui fare il clone
     * @param localRepoPath
     *            - il path locale dove fare il clone o verificare la presenza
     *            del repository
     * @throws GitAPIException
     *             - Eccezioni generiche di JGit
     * @throws URISyntaxException
     *             - Eccezione in caso di UTI sintatticamente non corretta
     * @throws IOException
     *             - Eccezione in caso di problemi di I/O
     * @throws RefNotFoundException
     *             - Eccezione in caso di riferimento non trovato
     */
    public static void checkoutRepo(String cloneUrl, String branchName, String localRepoPath)
            throws URISyntaxException, IOException, GitAPIException, RefNotFoundException {
        File repoLocalDirectory = new File(localRepoPath);
        Git git = Git.init().setDirectory(repoLocalDirectory).call();
        Repository repo = git.getRepository();

        // Original code in question works, is shorter,
        // but this is most likely the "proper" way to do it.
        StoredConfig config = repo.getConfig();
        RemoteConfig remoteConfig = new RemoteConfig(config, "origin");
        remoteConfig.addURI(new URIish(cloneUrl));
        // May use * instead of branch name to fetch all branches.
        // Same as config.SetString("remote", "origin", "fetch", ...);
        remoteConfig.addFetchRefSpec(new RefSpec("+refs/heads/" + branchName + ":refs/remotes/origin/" + branchName));
        remoteConfig.update(config);
        config.save();

        try {
            // clone
            git.fetch().setCredentialsProvider(getCredential()).call();
            git.branchCreate().setName(branchName).setStartPoint("origin/" + branchName)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).call();
        } catch (RefAlreadyExistsException e) {
            // update branch
            logger.debug("Versione gia' esistente, skip del clone. Messaggio di errore:"+e.getMessage());
            git.fetch().setCredentialsProvider(getCredential()).call();
            git.reset().setRef("origin/" + branchName).call();
        }
        git.checkout().setName(branchName).call();

        closeGit(git);
    }

    /**
     * Chiude la connessione per evitare memory leakage
     * 
     * @param git
     *            - l'interfaccia {@link Git} attualmente utilizzata
     */
    private static void closeGit(Git git) {
        if (git != null) {
            git.close();
        }
    }

    /**
     * Metodo per il recupero delle credenziali del repository TODO spostare le
     * credenziali o prevedere da file
     */
    private static CredentialsProvider getCredential() {

        return new UsernamePasswordCredentialsProvider("icompagnoni@imolinfo.it", "m2GSyVXB2bkI");

    }

}
