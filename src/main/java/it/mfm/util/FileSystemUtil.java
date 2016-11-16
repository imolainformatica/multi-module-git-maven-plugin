package it.mfm.util;

import java.io.File;

import org.eclipse.jgit.util.StringUtils;

/**
 * Classe di utilità per controlli sul filesystem
 */
public class FileSystemUtil {

    /**
     * Metodo per controllare che il {@link File} sia una directory
     * 
     * @param file
     *            - il {@link File} da controllare
     * @return - un boolean che vale {@code true} se il {@link File} è una
     *         directory, false altrimenti
     */
    public static boolean isFileADirectory(File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return false;
        }
        return true;
    }

    /**
     * Metodo per controllare che l'elemento indicato dal parametro {@code filePath} sia una directory
     * 
     * @param filePath
     *            - il percorso dell'elemento da controllare
     * @return - un boolean che vale {@code true} se l'elemento indicato da {@code filePath} è una
     *         directory, false altrimenti
     */
    public static boolean isFileADirectory(String filePath) {
        if (StringUtils.isEmptyOrNull(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isDirectory()) {
            return false;
        }
        return true;
    }

}
