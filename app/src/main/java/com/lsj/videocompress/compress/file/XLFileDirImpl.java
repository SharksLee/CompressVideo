package com.lsj.videocompress.compress.file;

import android.os.Environment;


import java.io.File;

/**
 * 功能：文件目录
 * 描述：文件目录的基类
 * Created by KasoGG on 2017/7/19.
 */
public abstract class XLFileDirImpl {
    static final String PHOTOS = "Photos";
    static final String VIDEOS = "Videos";
    static final String AUDIOS = "Audios";
    static final String DOCUMENTS = "Documents";
    static final String DOWNLOADS = "Downloads";

    public abstract String getRootDir();

    public abstract String getPhotoDir();

    public abstract String getVideoDir();

    public abstract String getAudioDir();

    public abstract String getDocumentDir();

    public abstract String getDownloadDir();

    public abstract String getDir(String dirName);

    /**
     * Checks if external storage is available for read and write
     **/
    static boolean isExternalStorageWritable() {
        try {
            // 部分rom Environment.getExternalStorageState() 会报NullPointerException
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建目录
     */
    String createIfNotExist(File file) {
        if (file != null) {
            file.mkdirs();
            return file.getPath();
        } else {
            return getRootDir();
        }
    }

    /**
     * 创建子目录
     */
    String createChildDir(String childDirName) {
        File file = new File(getRootDir() + File.separator + childDirName);
        return createIfNotExist(file);
    }
}