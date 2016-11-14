package it.mfm.util;

import java.io.File;

import org.eclipse.jgit.util.StringUtils;

import it.mfm.MultiModuleGitMojo;

public class MultiModuleGitUtil {

    public static void checkInputParams(MultiModuleGitMojo mojo) throws Exception {
       
        if(mojo == null){
            throw new Exception("Errore di inizializzazione generico.");
        }
        
        String basedir = mojo.getBasedir();
        if( StringUtils.isEmptyOrNull(basedir) ){
            throw new Exception("Errore di inizializzazione: il campo basedir è obbligatorio");
        }
        
        File file = new File(basedir);
        if(FileSystemUtil.isFileADirectory(file)){
            throw new Exception("Errore di inizializzazione: il campo basedir non punta ad una directory corretta");
        }
    }
}
