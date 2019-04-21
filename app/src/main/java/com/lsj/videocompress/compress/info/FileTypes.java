package com.lsj.videocompress.compress.info;

/**
 * 功能：文件类型定义
 * Created by KasoGG on 2017/7/17
 */
public class FileTypes {
    public static final int FILE_ALL = 0;
    public static final int FILE_OTHER = 1;
    public static final int FILE_PPT = 2;
    public static final int FILE_WORD = 3;
    public static final int FILE_VIDEO = 4;
    public static final int FILE_AUDIO = 5;
    public static final int FILE_IMAGE = 6;
    public static final int FILE_EXCEL = 7;
    //下面是自定义的文件类型，和服务端无关，仅为开发方便定义的常量
    public static final int FILE_APK = 3301;
    public static final int FILE_PDF = 3302;
    public static final int FILE_TEXT = 3303;
    public static final int FILE_FLASH = 3304;
    public static final int FILE_RAR = 3305;
    public static final int FILE_DOC = 3306;

    public static final String all = FILE_ALL + "";
    public static final String other = FILE_OTHER + "";
    public static final String ppt = FILE_PPT + "";
    public static final String word = FILE_WORD + "";
    public static final String video = FILE_VIDEO + "";
    public static final String audio = FILE_AUDIO + "";
    public static final String image = FILE_IMAGE + "";
    public static final String excel = FILE_EXCEL + "";
    //下面是自定义的文件类型，和服务端无关，仅为开发方便定义的常量
    public static final String apk = FILE_APK + "";
    public static final String pdf = FILE_PDF + "";
    public static final String txt = FILE_TEXT + "";
    public static final String flash = FILE_FLASH + "";
    public static final String rar = FILE_RAR + "";
    public static final String doc = FILE_DOC + "";

    /**
     * 获取文件类型
     *
     * @deprecated 统一使用扩展名/Path/URL判断 {@link XLFileExtension#isImage(String)}
     */
    @Deprecated
    public static boolean isFileImage(String fileType) {
        return image.equals(fileType);
    }

    /**
     * 获取文件类型
     *
     * @deprecated 统一使用扩展名/Path/URL判断 {@link XLFileExtension#isVideo(String)}
     */
    @Deprecated
    public static boolean isFileVideo(String fileType) {
        return video.equals(fileType);
    }
}
