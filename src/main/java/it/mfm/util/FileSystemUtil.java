package it.mfm.util;

import java.io.File;

import org.eclipse.jgit.util.StringUtils;

public class FileSystemUtil {
    
    static boolean isFileADirectory(File file){
        if(file == null || !file.exists() || !file.isDirectory()){
            return false;
        }
        return true;
    }
    
    static boolean isFileADirectory(String filePath){
        if(StringUtils.isEmptyOrNull(filePath)){
            return false;
        }
        File file = new File(filePath);
        if(file == null || !file.exists() || !file.isDirectory()){
            return false;
        }
        return true;
    }

}
