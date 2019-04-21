package com.lsj.videocompress.compress.info;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.text.TextUtils;


import java.io.File;

/**
 * 视频帮助类集合
 */

public class VideoUtils {




    /**
     * 获取视频的旋转角度
     *
     * @param path
     * @return
     */
    @TargetApi(17)
    public static int getVideoRotation(String path) {
        if (TextUtils.isEmpty(path)) return -1;

        File file = new File(path);

        if (!file.exists()) return -1;

        if (file.length() <= 0) return -1;

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);


        String rotationString = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

        mediaMetadataRetriever.release();

        try {
            return Integer.parseInt(rotationString);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 获取本地视频信息
     *
     * @param path 本地视频地址
     * @return
     */
    @TargetApi(17)
    public static TrackResult getFirstVideoAndAudioTrack(String path) {
        if (TextUtils.isEmpty(path)) return null;

        File file = new File(path);

        if (!file.exists()) return null;

        if (file.length() <= 0) return null;

        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(path);

            TrackResult result = getFirstVideoAndAudioTrack(extractor);
            extractor.release();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @TargetApi(17)
    public static TrackResult getFirstVideoAndAudioTrack(MediaExtractor extractor) {
        TrackResult trackResult = new TrackResult();
        trackResult.mVideoTrackIndex = -1;
        trackResult.mAudioTrackIndex = -1;
        int trackCount = extractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (trackResult.mVideoTrackIndex < 0 && mime.startsWith("video/")) {
                trackResult.mVideoTrackIndex = i;
                trackResult.mVideoTrackMime = mime;
                trackResult.mVideoTrackFormat = format;
            } else if (trackResult.mAudioTrackIndex < 0 && mime.startsWith("audio/")) {
                trackResult.mAudioTrackIndex = i;
                trackResult.mAudioTrackMime = mime;
                trackResult.mAudioTrackFormat = format;
            }
            if (trackResult.mVideoTrackIndex >= 0 && trackResult.mAudioTrackIndex >= 0) break;
        }
        if (trackResult.mVideoTrackIndex < 0 || trackResult.mAudioTrackIndex < 0) {
            return null;
        }
        return trackResult;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static MediaFormat generateCompressVideoFormat(int width, int height, int bitrate) {
        MediaFormat media = MediaFormat.createVideoFormat("video/avc", width, height);
        media.setInteger("color-format", MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        media.setInteger("bitrate", bitrate);
        media.setInteger("frame-rate", 30);
        media.setInteger("i-frame-interval", 1);
        return media;
    }

    /**
     * 什么情况下视频需要进行压缩，
     * 分辨率超过450p才开始压缩
     *
     * @param path 视频地址
     * @return 是否需要压缩
     */
    public static boolean needCompress(String path) {
        return needCompress(path, VideoFormatHelper.RESOLUTION_450P);
    }




    /**
     * 是否压缩视频是完整的
     * 两边时长一致就可以了
     *
     * @param sourcePath
     * @param compressedPath
     * @return
     */
    public static boolean isCompressedVideoComplete(String sourcePath, String compressedPath) {

        if (TextUtils.isEmpty(sourcePath)) return false;

        if (TextUtils.isEmpty(compressedPath)) return false;

        //  如果源地址和结尾地址一致，则认为是一致的
        if (TextUtils.equals(sourcePath, compressedPath)) return true;

        long sourceTime, resultTime;

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        try {
            mediaMetadataRetriever.setDataSource(sourcePath);
            sourceTime = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            mediaMetadataRetriever.setDataSource(compressedPath);
            resultTime = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            //在如果两边时长相差100毫秒内，则认为时长是一致的，因为压缩算法的关系，误差会有10毫秒左右
            return Math.abs(sourceTime - resultTime) < 100;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    /**
     * 什么情况下视频需要进行压缩
     *
     * @param path           视频地址
     * @param resolutionType 压缩分辨率阈值，超过则压缩
     * @return 是否需要压缩
     */
    public static boolean needCompress(String path, @VideoFormatHelper.ResolutionType int resolutionType) {
        if (TextUtils.isEmpty(path)) return false;

        File file = new File(path);

        if (!file.exists()) return false;

        if (file.length() <= 0) return false;

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            Size size = VideoFormatHelper.getResolutionSize(resolutionType);
            mediaMetadataRetriever.setDataSource(path);
            int width = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            return Math.min(width, height) >= size.getShortValue();

        } catch (Exception e) {
            //宽高尺寸获取有误，则默认启用压缩
            e.printStackTrace();
            return true;
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    public static class TrackResult {

        public int mVideoTrackIndex;
        public String mVideoTrackMime;
        /**
         *
         */
        public MediaFormat mVideoTrackFormat;
        public int mAudioTrackIndex;
        public String mAudioTrackMime;
        /**
         *
         */
        public MediaFormat mAudioTrackFormat;

        private TrackResult() {
        }
    }
}
