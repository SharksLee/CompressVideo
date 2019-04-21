package com.lsj.videocompress.compress.file;

import java.io.File;

/**
 * 功能：临时目录
 * 描述：每次应用重启时进行清理
 * Created by KasoGG on 2017/7/19.
 */
class XLFileDirTemp extends XLFileDirPrivate {

    @Override
    public String getRootDir() {
        String rootParent = super.getRootDir();
        return createIfNotExist(new File(rootParent + File.separator + "Temp"));
    }

}