package com.lsj.videocompress.compress.file;

/**
 * 功能：数据类型
 * 描述：
 * Created by KasoGG on 2017/7/26.
 */
public enum XLDataType {
    /**
     * 公共目录，默认存放在外部存储公共空间，如果没外部存储则返回私有目录路径，不会自动删除，与其它应用共享
     * 适合存放能被其它应用访问到的公共资源，例如：公共的照片、视频、录音、文档等
     * 默认路径：外部存储根目录
     */
    Public,

    /**
     * 私有目录，应用卸载后删除
     * 适合存放只与应用相关的资源，其它应用不能访问，例如配置信息、私有的媒体资源或文档等
     * 默认路径：data/packageName/files/
     */
    Private,

    /**
     * 缓存目录，清理缓存时清空
     * 适合存放重复使用的缓存资源，以提高速度，清理后应不影响正常使用，例如展示用的图片、视频缩略图
     * 默认路径：data/packageName/cache/
     */
    Cache,

    /**
     * 临时目录，每次应用重启时进行清理
     * 适合存放只使用一次临时资源，例如用于上传生成的缩略图和视频、临时录音等
     * 默认路径：data/packageName/files/Temp/
     */
    Temp
}
