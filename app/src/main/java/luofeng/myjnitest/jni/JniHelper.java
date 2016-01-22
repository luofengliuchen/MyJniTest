package luofeng.myjnitest.jni;

import android.graphics.Bitmap;

/**
 * Created by 美工 on 2016/1/19.
 */
public class JniHelper {

    static{
        System.loadLibrary("jnitest");
    }

    public static native String createMyName();

    public static native String getKey(String name);

    public static  native String getSpecialUrl(String name);

    public static native boolean votifyKey();

    public static native int getMsgCode();

    public static native void renderPlasma(Bitmap bitmap, long time_ms);

    public static native void toBlur(Object bitmapOut, int radius, int threadCount, int threadIndex, int round);

}
