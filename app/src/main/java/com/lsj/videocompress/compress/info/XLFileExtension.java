package com.lsj.videocompress.compress.info;

import android.text.TextUtils;


/**
 * 功能：文件扩展名、类型管理
 * Created by KasoGG on 2017/7/17
 */
public class XLFileExtension {
    private static final String[] IMG_EXTENSIONS = {"png", "gif", "jpg", "jpeg", "bmp", "webp"};
    private static final String[] AUDIO_EXTENSIONS = {"mp3", "wav", "ogg", "midi", "wma"};
    private static final String[] VIDEO_EXTENSIONS = {"mp4", "rmvb", "avi", "flv", "mpg", "wmv", "mov", "mkv", "mpeg","3gp"};
    private static final String[] PPT_EXTENSIONS = {"ppt", "pptx"};
    private static final String[] WORD_EXTENSIONS = {"doc", "docx", "wps"};
    private static final String[] EXCEL_EXTENSIONS = {"xls", "xlsx"};
    private static final String[] TXT_EXTENSIONS = {"txt", "java", "c", "cpp", "py", "xml", "json", "log"};
    private static final String[] PDF_EXTENSIONS = {"pdf"};
    private static final String[] FLASH_EXTENSIONS = {"swf"};
    private static final String[] RAR_EXTENSIONS = {"rar","zip"};

    /**
     * 是否为图片
     *
     * @param extensionOrPathOrUrl 扩展名、路径、Url
     */
    public static boolean isImage(String extensionOrPathOrUrl) {
        String fileType = getFileType(extensionOrPathOrUrl);
        return FileTypes.image.equals(fileType);
    }

    /**
     * 是否为视频
     *
     * @param extensionOrPathOrUrl 扩展名、路径、Url
     */
    public static boolean isVideo(String extensionOrPathOrUrl) {
        String fileType = getFileType(extensionOrPathOrUrl);
        return FileTypes.video.equals(fileType);
    }

    /**
     * 获取文件类型
     *
     * @param extensionOrPathOrUrl 扩展名、路径、Url
     * @return 文件类型 {@link FileTypes}
     */
    public static String getFileType(String extensionOrPathOrUrl) {
        String extension = getFileExtension(extensionOrPathOrUrl);
        return getFileTypeByExtension(extension);
    }

    /**
     * 获取文件的后缀
     * 比如：abc.mp3 -> mp3
     *
     * @param extensionOrPathOrUrl extension直接返回，Path或Url截取最后的扩展名
     * @return 扩展名
     */
    public static String getFileExtension(String extensionOrPathOrUrl) {
        if (TextUtils.isEmpty(extensionOrPathOrUrl)) {
            return extensionOrPathOrUrl;
        }

        String extension = extensionOrPathOrUrl;

        int dotIndex = extension.lastIndexOf('.');
        if (dotIndex != -1) {
            extension = extension.substring(dotIndex + 1);
        }

        int questIndex = extension.indexOf("?");
        if (questIndex != -1) {
            extension = extension.substring(0, questIndex);
        }

        //加强了对融云聊天图片格式的处理比如 www.baidu.com/img.png&key=3301
        int andIndex = extension.indexOf("&");
        if (andIndex != -1) {
            extension = extension.substring(0, andIndex);
        }

        return extension;
    }

    /**
     * 根据扩展名获取文件类型
     *
     * @param extension 扩展名
     * @return 文件类型 {@link FileTypes}
     */
    private static String getFileTypeByExtension(String extension) {
        if (TextUtils.isEmpty(extension)) {
            return FileTypes.other;
        }
        if (CommonUtil.containStr(IMG_EXTENSIONS, extension)) {
            return FileTypes.image;
        }
        if (CommonUtil.containStr(VIDEO_EXTENSIONS, extension)) {
            return FileTypes.video;
        }
        if (CommonUtil.containStr(AUDIO_EXTENSIONS, extension)) {
            return FileTypes.audio;
        }
        if (CommonUtil.containStr(WORD_EXTENSIONS, extension)) {
            return FileTypes.word;
        }
        if (CommonUtil.containStr(EXCEL_EXTENSIONS, extension)) {
            return FileTypes.excel;
        }
        if (CommonUtil.containStr(PPT_EXTENSIONS, extension)) {
            return FileTypes.ppt;
        }
        if (CommonUtil.containStr(PDF_EXTENSIONS, extension)) {
            return FileTypes.pdf;
        }
        if (CommonUtil.containStr(TXT_EXTENSIONS, extension)) {
            return FileTypes.txt;
        }
        if (CommonUtil.containStr(FLASH_EXTENSIONS, extension)) {
            return FileTypes.flash;
        }
        if (CommonUtil.containStr(RAR_EXTENSIONS, extension)) {
            return FileTypes.rar;
        }

        return FileTypes.other;
    }

}
