package com.lsj.videocompress.compress.file;

import android.support.annotation.NonNull;
import android.text.TextUtils;


import java.io.File;
import java.io.IOException;

/**
 * 功能：文件管理
 * 描述：
 * Created by KasoGG on 2017/7/19.
 */
public class XLFileManager {
    /**
     * 获取图片文件
     *
     * @param type     目录的保存类型 {@link XLDataType}
     * @param fileName 文件名
     * @return 文件Path
     */
    public static String getPhotoFile(XLDataType type, String fileName) {
        return getPhotoDir(type) + File.separator + fileName;
    }

    /**
     * 获取视频文件
     *
     * @param type     目录的保存类型 {@link XLDataType}
     * @param fileName 文件名
     * @return 文件Path
     */
    public static String getVideoFile(XLDataType type, String fileName) {
        return getVideoDir(type) + File.separator + fileName;
    }

    /**
     * 获取音频文件
     *
     * @param type     目录的保存类型 {@link XLDataType}
     * @param fileName 文件名
     * @return 文件Path
     */
    public static String getAudioFile(XLDataType type, String fileName) {
        return getAudioDir(type) + File.separator + fileName;
    }

    /**
     * 获取文档文件
     *
     * @param type     目录的保存类型 {@link XLDataType}
     * @param fileName 文件名
     * @return 文件Path
     */
    public static String getDocumentFile(XLDataType type, String fileName) {
        return getDocumentDir(type) + File.separator + fileName;
    }

    /**
     * 获取下载文件
     *
     * @param type     目录的保存类型 {@link XLDataType}
     * @param fileName 文件名
     * @return 文件Path
     */
    public static String getDownloadFile(XLDataType type, String fileName) {
        return getDownloadDir(type) + File.separator + fileName;
    }

    /**
     * 获取文件
     *
     * @param type     目录的保存类型 {@link XLDataType}
     * @param dirName  子目录名称
     * @param fileName 文件名
     * @return 文件Path
     */
    public static String getFile(XLDataType type, String dirName, String fileName) {
        return getDir(type, dirName) + File.separator + fileName;
    }

    /**
     * 获取根目录
     *
     * @param type 目录的保存类型 {@link XLDataType}
     * @return 目录Path
     */
    public static String getRootDir(XLDataType type) {
        return XLFileDirFactory.getDirImpl(type).getRootDir();
    }

    /**
     * 获取图片目录
     *
     * @param type 目录的保存类型 {@link XLDataType}
     * @return 目录Path
     */
    public static String getPhotoDir(XLDataType type) {
        return XLFileDirFactory.getDirImpl(type).getPhotoDir();
    }

    /**
     * 获取视频目录
     *
     * @param type 目录的保存类型 {@link XLDataType}
     * @return 目录Path
     */
    public static String getVideoDir(XLDataType type) {
        return XLFileDirFactory.getDirImpl(type).getVideoDir();
    }

    /**
     * 获取音频目录
     *
     * @param type 目录的保存类型 {@link XLDataType}
     * @return 目录Path
     */
    public static String getAudioDir(XLDataType type) {
        return XLFileDirFactory.getDirImpl(type).getAudioDir();
    }

    /**
     * 获取文档目录
     *
     * @param type 目录的保存类型 {@link XLDataType}
     * @return 目录Path
     */
    public static String getDocumentDir(XLDataType type) {
        return XLFileDirFactory.getDirImpl(type).getDocumentDir();
    }

    /**
     * 获取下载目录
     *
     * @param type 目录的保存类型 {@link XLDataType}
     * @return 目录Path
     */
    public static String getDownloadDir(XLDataType type) {
        return XLFileDirFactory.getDirImpl(type).getDownloadDir();
    }

    /**
     * 获取指定名称目录
     *
     * @param type    目录的保存类型 {@link XLDataType}
     * @param dirName 子目录名称
     * @return 目录Path
     */
    public static String getDir(XLDataType type, String dirName) {
        return XLFileDirFactory.getDirImpl(type).getDir(dirName);
    }

    /**
     * 创建新文件
     *
     * @param filePath  文件路径
     * @param overwrite 如果文件存在是否覆盖
     * @return true文件创建成果，false文件创建失败或已存在
     */
    public static boolean createNewFile(String filePath, boolean overwrite) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);

        if (file.exists()) {
            if (!overwrite) {
                return false;
            }
            if (!file.delete()) {
                return false;
            }
        }

        if (file.getParentFile() != null && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            return false;
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除目标文件或文件夹
     *
     * @param path 文件或文件夹路径
     * @return true删除成功或文件/文件夹不存在 false删除失败
     */
    public static boolean delete(@NonNull String path) {
        return delete(new File(path));
    }

    /**
     * 清空目标文件夹中的内容，不删除目标文件夹
     *
     * @param path 文件夹路径
     * @return true清空成功，false清空失败
     */
    public static boolean cleanDir(@NonNull String path) {
        File dir = new File(path);
        delete(dir);
        return !dir.exists() && dir.mkdirs();
    }

    /**
     * 清理临时文件夹
     */
    public static boolean cleanTempDir() {
        return XLFileManager.cleanDir(XLFileDirFactory.getDirImpl(XLDataType.Temp).getRootDir());
    }



    /**
     * 删除文件夹或文件
     *
     * @param path 文件或文件夹路径
     * @return true删除成功 false删除失败
     */
    private static boolean delete(File path) {
        if (path == null || !path.exists()) {
            return true;
        }
        if (path.isFile()) {
            return path.delete();
        }
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                delete(file);
            }
        }
        return path.delete();
    }
}