package it.mfm.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Classe di utilità per l'interazione con i repository git attraverso JGit.
 */
public class JGitUtil {

    /**
     * Logger di classe.
     */
    public static final Logger logger = LogManager.getLogger(JGitUtil.class);

    /**
     * Clona un repository nel path indicato
     * 
     * @param uri
     *            - la uri del repository
     * @param branch
     *            - la branch di cui fare il checkout
     * @param path
     *            - il path dove effettuare il clone
     * @return - il gestore della API Git di JGit
     * @throws Exception
     */
    public static void cloneRepositoryToPath(String uri, String branch, String path) throws Exception {
        File repoLocalDirectory = new File(path);
        if (FileSystemUtil.isFileADirectory(repoLocalDirectory)) {
            // With setBranchesToClone(), the command clones only the specified
            // branches. Note that the setBranch() directive is necessary to
            // also checkout the desired branch
            Git git = Git.cloneRepository().setURI(uri).setDirectory(repoLocalDirectory)
                    .setBranchesToClone(new ArrayList<String>(Arrays.asList("refs/head/" + branch)))
                    .setBranch("refs/head/" + branch).call();
            closeGit(git);
        } else {
            throw new Exception(
                    "Errore in fase di clone del repository: il path specificato non è relativo ad una directory");
        }
    }

    /**
     * Effettua il checkout di un repository locale ad una determinata versione
     * 
     * @param localPathRepo
     *            - path del repository locale
     * @param version
     *            - la versione di cui si vuole fare il checkout
     * @throws Exception
     */
    public static void checkoutRepository(String localPathRepo, String version) throws Exception {

        if (!FileSystemUtil.isFileADirectory(localPathRepo)) {
            throw new Exception(
                    "Errore in fase di clone del repository: il path specificato non è relativo ad una directory");
        }

        Git git = Git.open(new File(localPathRepo));
        git.checkout().setForce(true).setName(version).call();
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
     * Permette di effettuare il pull di un repository.
     * 
     * @param localPathRepo
     *            - path del repository locale
     * @throws Exception
     */
    public static void pullRepository(String localPathRepo) throws Exception {

        if (!FileSystemUtil.isFileADirectory(localPathRepo)) {
            throw new Exception(
                    "Errore in fase di clone del repository: il path specificato non è relativo ad una directory");
        }

        Git git = Git.open(new File(localPathRepo));
        git.pull().call();
        closeGit(git);
    }

    /**
     * Controlla se il repository indicato in ingresso esista o meno all'interno del filesystem
     * 
     * @param repository
     *            - l'indirizzo di un repository da controllare
     * @return - un {@code boolean} che vale false se il repository non esista, true altrimenti 
     */
    public static boolean checkRepositoryExists(String repository) {

        if (!FileSystemUtil.isFileADirectory(repository)) {
            return false;
        }

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setGitDir(new File(repository));
        repositoryBuilder.setMustExist(true);
        try {
            repositoryBuilder.build();
        } catch (IOException e) {
            logger.debug("Il repository non esiste!!! Bisogna clonarlo!");
            return false;
        }

        return true;
    }

}
