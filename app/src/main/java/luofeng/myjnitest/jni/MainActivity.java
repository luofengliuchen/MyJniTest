/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package luofeng.myjnitest.jni;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import luofeng.myjnitest.R;

public class MainActivity extends Activity{


    Bitmap mBitmap;
    ImageView iv;
    static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        //        Display display = getWindowManager().getDefaultDisplay();
//        setContentView(new BitmapView(this, display.getWidth(), display.getHeight()));
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        SeekBar pb0 = (SeekBar) findViewById(R.id.sb0);
        SeekBar pb = (SeekBar) findViewById(R.id.sb);
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y).copy(Bitmap.Config.ARGB_8888, true);
//        JniHelper.toBlur(mBitmap,50,1,0,1);
//        JniHelper.toBlur(mBitmap,10,1,0,2);
        iv.setImageBitmap(mBitmap);
        pb.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
        pb0.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private float mprogress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mprogress = (float)progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mprogress>=1){
                    mBitmap = scaleBitmap(BitmapFactory.decodeResource(getResources()
                            , R.drawable.y).copy(Bitmap.Config.ARGB_8888, true),1.0f/mprogress);
                    iv.setImageBitmap(mBitmap);
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv.setImageBitmap(blur(mBitmap, 2));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.y).copy(Bitmap.Config.RGB_565, true);
                JniHelper.renderPlasma(bitmap,System.currentTimeMillis());
                iv.setImageBitmap(bitmap);
            }
        });
    }

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        private int mProgress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mProgress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            iv.setImageBitmap(blur(mBitmap,mProgress*254/100));
        }
    }


    /* load our native library */
    static {
        System.loadLibrary("jnitest");
    }


    private static Bitmap scaleBitmap(Bitmap bitmap,float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }


    public Bitmap blur(Bitmap original, float radius) {
        Bitmap bitmapOut = original.copy(Bitmap.Config.ARGB_8888, true);

        int cores = EXECUTOR_THREADS;

        ArrayList<NativeTask> horizontal = new ArrayList<NativeTask>(cores);
        ArrayList<NativeTask> vertical = new ArrayList<NativeTask>(cores);
        for (int i = 0; i < cores; i++) {
            horizontal.add(new NativeTask(bitmapOut, (int) radius, cores, i, 1));
            vertical.add(new NativeTask(bitmapOut, (int) radius, cores, i, 2));
        }

        try {
            EXECUTOR.invokeAll(horizontal);
        } catch (InterruptedException e) {
            return bitmapOut;
        }

        try {
            EXECUTOR.invokeAll(vertical);
        } catch (InterruptedException e) {
            return bitmapOut;
        }
        return bitmapOut;
    }

    private static class NativeTask implements Callable<Void> {
        private final Bitmap _bitmapOut;
        private final int _radius;
        private final int _totalCores;
        private final int _coreIndex;
        private final int _round;

        public NativeTask(Bitmap bitmapOut, int radius, int totalCores, int coreIndex, int round) {
            _bitmapOut = bitmapOut;
            _radius = radius;
            _totalCores = totalCores;
            _coreIndex = coreIndex;
            _round = round;
        }

        @Override
        public Void call() throws Exception {
            JniHelper.toBlur(_bitmapOut, _radius, _totalCores, _coreIndex, _round);
            return null;
        }
    }
}
