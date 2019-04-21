package com.lsj.videocompress.compress;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import android.view.Surface;

import com.lsj.videocompress.compress.info.VideoSourceInfo;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 对视频文件进行解码
 */
@TargetApi(18)
public final class DecodeHelper {
    public static final String KEY_ROTATION_DEGREES = "rotation-degrees";
    private Surface mSurface;
    private VideoSourceInfo mSourceInfo;
    private MediaExtractor mMediaExtractor;
    private MediaCodec mMediaCodec = null;
    private boolean mIsFinished = false;

    public DecodeHelper(Surface surface, VideoSourceInfo sourceInfo) {
        this.mSourceInfo = sourceInfo;
        this.mSurface = surface;
    }

    public final boolean initVideo() {
        this.mMediaExtractor = new MediaExtractor();
        try {
            this.mMediaExtractor.setDataSource(mSourceInfo.getSourcePath());
            MediaFormat mediaFormat = mSourceInfo.getTrackResult().mVideoTrackFormat;
            if (mediaFormat.containsKey(KEY_ROTATION_DEGREES)) {
                // Decoded video is rotated automatically in Android 5.0 lollipop.
                // Turn off here because we don't want to encode rotated one.
                // refer: https://android.googlesource.com/platform/frameworks/av/+blame/lollipop-release/media/libstagefright/Utils.cpp
                mediaFormat.setInteger(KEY_ROTATION_DEGREES, 0);
            }
            mMediaExtractor.selectTrack(mSourceInfo.getTrackResult().mVideoTrackIndex);

            this.mMediaCodec = MediaCodec.createDecoderByType(mediaFormat.getString("mime"));
            this.mMediaCodec.configure(mediaFormat, this.mSurface, null, 0);
            return true;

        } catch (IOException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public final void release() {
        if (this.mMediaCodec != null) {
            this.mMediaCodec.stop();
            this.mMediaCodec.release();
            this.mMediaCodec = null;
        }

        if (this.mMediaExtractor != null) {
            this.mMediaExtractor.release();
            this.mMediaExtractor = null;
        }

    }

    public final void start() {
        this.mMediaCodec.start();
    }

    public void writeAllAudioData(MediaMuxer mediaMuxer, int audioIndex) {
        mMediaExtractor.unselectTrack(mSourceInfo.getTrackResult().mVideoTrackIndex);
        mMediaExtractor.selectTrack(mSourceInfo.getTrackResult().mAudioTrackIndex);

        BufferInfo info = new BufferInfo();
        info.presentationTimeUs = 0;
        ByteBuffer buffer = ByteBuffer.allocate(100 * 1024);
        while (true) {
            int sampleSize = mMediaExtractor.readSampleData(buffer, 0);
            if (sampleSize < 0) {
                break;
            }

            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs = mMediaExtractor.getSampleTime();
            mediaMuxer.writeSampleData(audioIndex, buffer, info);
            mMediaExtractor.advance();
        }
    }

    public final boolean next() {
        if (this.mIsFinished) {
            return false;
        } else {
            boolean var1 = false;
            ByteBuffer[] var2 = this.mMediaCodec.getInputBuffers();
            int var3;
            if ((var3 = this.mMediaCodec.dequeueInputBuffer(0)) >= 0) {
                ByteBuffer var4;
                (var4 = var2[var3]).clear();
                int var5;
                if ((var5 = this.mMediaExtractor.readSampleData(var4, 0)) < 0) {
                    this.mMediaCodec.queueInputBuffer(var3, 0, 0, 0L, 4);
                    var1 = true;
                } else {
                    this.mMediaCodec.queueInputBuffer(var3, 0, var5, this.mMediaExtractor.getSampleTime(), 0);
                    this.mMediaExtractor.advance();
                }
            }

            return var1;
        }
    }

    public final long getVideoDuration() {
        if (this.mIsFinished) {
            return 0L;
        } else {
            BufferInfo var1 = new BufferInfo();
            int var2;
            if ((var2 = this.mMediaCodec.dequeueOutputBuffer(var1, 10000L)) != -1 && var2 != -3) {
                if (var2 == -2) {
                    this.mMediaCodec.getOutputFormat();
                } else if (var2 < 0) {
                    Log.e("sno", "unexpected result from dequeueOutputBuffer: " + var2);
                } else {
                    boolean var3 = var1.size != 0;
                    this.mMediaCodec.releaseOutputBuffer(var2, var3);
                    if ((var1.flags & 4) != 0) {
                        this.mIsFinished = true;
                    }

                    if (var3) {
                        return var1.presentationTimeUs;
                    }
                }
            }

            return -1L;
        }
    }

    public final boolean isFinished() {
        return this.mIsFinished;
    }
}