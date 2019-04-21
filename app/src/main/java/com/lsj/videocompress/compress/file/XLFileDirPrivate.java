package com.lsj.videocompress.compress.file;


import java.io.File;

/**
 * 功能：私有目录
 * 描述：应用卸载后删除，优先使用外部存储空间
 * Created by KasoGG on 2017/7/19.
 */
class XLFileDirPrivate extends XLFileDirImpl {

    @Override
    public String getRootDir() {
      return "";
    }

    @Override
    public String getPhotoDir() {
        return createChildDir(PHOTOS);
    }

    @Override
    public String getVideoDir() {
        return createChildDir(VIDEOS);
    }

    @Override
    public String getAudioDir() {
        return createChildDir(AUDIOS);
    }

    @Override
    public String getDocumentDir() {
        return createChildDir(DOCUMENTS);
    }

    @Override
    public String getDownloadDir() {
        return createChildDir(DOWNLOADS);
    }

    @Override
    public String getDir(String dirName) {
        return createChildDir(dirName);
    }

}