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
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import luofeng.myjnitest.R;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        Display display = getWindowManager().getDefaultDisplay();
//        setContentView(new BitmapView(this, display.getWidth(), display.getHeight()));
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.x).copy(Bitmap.Config.ARGB_8888,true);
        JniHelper.toBlur(mBitmap,100,1,1,1);
        JniHelper.toBlur(mBitmap,100,1,1,2);

//        blur(mBitmap,128);
        iv.setImageBitmap(mBitmap);
    }

    /* load our native library */
    static {
        System.loadLibrary("jnitest");
    }


    public Bitmap blur(Bitmap original, float radius) {
        Bitmap bitmapOut = original.copy(Bitmap.Config.ARGB_8888, true);

        int cores = StackBlurManager.EXECUTOR_THREADS;

        ArrayList<NativeTask> horizontal = new ArrayList<NativeTask>(cores);
        ArrayList<NativeTask> vertical = new ArrayList<NativeTask>(cores);
        for (int i = 0; i < cores; i++) {
            horizontal.add(new NativeTask(bitmapOut, (int) radius, cores, i, 1));
            vertical.add(new NativeTask(bitmapOut, (int) radius, cores, i, 2));
        }

        try {
            StackBlurManager.EXECUTOR.invokeAll(horizontal);
        } catch (InterruptedException e) {
            return bitmapOut;
        }

        try {
            StackBlurManager.EXECUTOR.invokeAll(vertical);
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

        @Override public Void call() throws Exception {
            JniHelper.toBlur(_bitmapOut, _radius, _totalCores, _coreIndex, _round);
            return null;
        }

    }

}

//class BitmapView extends View {
//    private Bitmap mBitmap;
//    private long mStartTime;
//
//    /* implementend by libplasma.so */
//
//
//    public BitmapView(Context context, int width, int height) {
//        super(context);
////        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.x);
//        mStartTime = System.currentTimeMillis();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        //canvas.drawColor(0xFFCCCCCC);
////        JniHelper.renderPlasma(mBitmap, System.currentTimeMillis() - mStartTime);
//        JniHelper.toBlur(mBitmap,128,1,1,1);
//        canvas.drawBitmap(mBitmap, 0, 0, null);
//        // force a redraw, with a different time-based pattern.
//        invalidate();
//    }
//}
