package com.lsj.videocompress.compress.file;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：文件目录工厂
 * 描述：生成不同类型的文件目录实例
 * Created by KasoGG on 2017/7/27.
 */
public class XLFileDirFactory {
    private static final Map<XLDataType, XLFileDirImpl> mXLFileDirs = new HashMap<>();

    public static XLFileDirImpl getDirImpl(XLDataType type) {
        XLFileDirImpl xlFileDir = mXLFileDirs.get(type);
        if (xlFileDir == null) {
            switch (type) {
                case Public:
                    if (XLFileDirImpl.isExternalStorageWritable()) {
                        xlFileDir = new XLFileDirPublic();
                    } else {
                        xlFileDir = new XLFileDirPrivate();
                    }
                    break;
                case Private:
                    xlFileDir = new XLFileDirPrivate();
                    break;
                case Temp:
                    xlFileDir = new XLFileDirTemp();
                    break;
                case Cache:
                    xlFileDir = new XLFileDirCache();
                    break;
            }
            mXLFileDirs.put(type, xlFileDir);
        }
        return xlFileDir;
    }

}
