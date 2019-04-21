package com.lsj.videocompress.compress.file;

import android.os.Environment;

/**
 * 功能：公共目录
 * 描述：永久保存，存放在外部存储公共空间，可以被应用其它访问到
 * Created by KasoGG on 2017/7/19.
 */
class XLFileDirPublic extends XLFileDirImpl {

    @Override
    public String getRootDir() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    @Override
    public String getPhotoDir() {
        return createIfNotExist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
    }

    @Override
    public String getVideoDir() {
        return createIfNotExist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
    }

    @Override
    public String getAudioDir() {
        return createIfNotExist(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
    }

    @Override
    public String getDocumentDir() {
        return createIfNotExist(Environment.getExternalStoragePublicDirectory(DOCUMENTS));
    }

    @Override
    public String getDownloadDir() {
        return createIfNotExist(Environment.getExternalStoragePublicDirectory(DOWNLOADS));
    }

    @Override
    public String getDir(String dirName) {
        return createIfNotExist(Environment.getExternalStoragePublicDirectory(dirName));
    }

}