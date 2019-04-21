package com.lsj.videocompress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.lsj.videocompress.compress.CompressProcess;
import com.lsj.videocompress.compress.ICompressListener;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startCompress();
    }

    private void startCompress() {

        CompressProcess compressProcess = new CompressProcess("/sdcard/YinYueTai/VID_20190421_200551.mp4", "/sdcard/compress/VID_20190421_200551.mp4");
        compressProcess.setCompressListener(new ICompressListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "onStart: ");
            }

            @Override
            public void onProgress(int percent) {
                Log.d(TAG, "onProgress: "+percent);
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onFail() {
                Log.d(TAG, "onFail: ");
            }

            @Override
            public void onCanceled() {
                Log.d(TAG, "onCanceled: ");
            }
        });
        compressProcess.start();

    }


}
