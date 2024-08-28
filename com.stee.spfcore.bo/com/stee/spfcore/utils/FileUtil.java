package com.stee.spfcore.utils;

import java.io.File;

public class FileUtil {
    private FileUtil(){}
    public static boolean deleteDirectoryRecursively( File file ) {
        if ( file == null ) {
            return true;
        }

        if ( !file.exists() ) {
            return true;
        }

        if ( file.isDirectory() ) {
            File[] subFiles = file.listFiles();
            for ( File subFile : subFiles ) {
                if ( !FileUtil.deleteDirectoryRecursively( subFile ) ) {
                    return false;
                }
            }
        }

        return file.delete();
    }
}
