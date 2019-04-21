package com.lsj.videocompress.compress;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import com.lsj.videocompress.compress.info.VideoSourceInfo;
import com.lsj.videocompress.compress.info.VideoUtils;


import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 对解码后的视频流进行编码输出到磁盘
 */
@TargetApi(18)
public final class EncoderHelper {
    MediaCodec mMediaCodec = null;
    private VideoSourceInfo mVideoSourceInfo;
    private MediaMuxer mMediaMuxer = null;
    private int h = -1;
    private int mAudioIndex;

    public EncoderHelper() {
    }

    public final void a(VideoSourceInfo videoSourceInfo) {
        mVideoSourceInfo = videoSourceInfo;
        try {
            this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
            this.mMediaCodec.configure(VideoUtils.generateCompressVideoFormat(videoSourceInfo.getWidth(), videoSourceInfo.getHeight(), videoSourceInfo.getBitRate()), null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    public final void init(String outputPath) {
        try {
            this.mMediaMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mMediaMuxer.setOrientationHint(mVideoSourceInfo.getOrientationDegree());
            mAudioIndex = mMediaMuxer.addTrack(mVideoSourceInfo.getTrackResult().mAudioTrackFormat);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        this.mMediaCodec.start();

        try {
            Thread.sleep(100L);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

    }

    public final void release() {
        if (this.mMediaMuxer != null) {
            try {
                this.mMediaMuxer.stop();
                this.mMediaMuxer.release();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        this.mMediaCodec.stop();

        if (this.mMediaCodec != null) {
            this.mMediaCodec.release();
            this.mMediaCodec = null;
        }
    }


    public final void writeMediaMuxer() {
        ByteBuffer[] var2 = this.mMediaCodec.getOutputBuffers();
        BufferInfo var3 = new BufferInfo();

        int var1;
        while ((var1 = this.mMediaCodec.dequeueOutputBuffer(var3, 0)) != -1) {
            if (var1 == -3) {
                var2 = this.mMediaCodec.getOutputBuffers();
            } else if (var1 == -2) {
                MediaFormat var4 = this.mMediaCodec.getOutputFormat();
                if (this.mMediaMuxer != null) {
                    this.h = this.mMediaMuxer.addTrack(var4);
                    this.mMediaMuxer.start();
                }
            } else if (var1 < 0) {
                Log.e("error", "unexpected result fromd equeueOutputBuffer" + var1);
            } else {
                ByteBuffer var5;
                if ((var5 = var2[var1]) == null) {
                    Log.e("sno", "encoderOutputBuffer " + var1 + " was null");
                }

                if ((var3.flags & 2) != 0) {
                    this.mMediaCodec.releaseOutputBuffer(var1, false);
                    return;
                }

                if (var3.size != 0) {
                    if (var5 != null) {
                        var5.position(var3.offset);
                        var5.limit(var3.offset + var3.size);
                        this.mMediaMuxer.writeSampleData(this.h, var5, var3);
                    }
                }

                this.mMediaCodec.releaseOutputBuffer(var1, false);
            }
        }

    }

    public MediaMuxer getMediaMuxer() {
        return mMediaMuxer;
    }

    public int getAudioIndex() {
        return mAudioIndex;
    }
}
