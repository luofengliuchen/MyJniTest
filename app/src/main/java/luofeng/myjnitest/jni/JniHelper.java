package luofeng.myjnitest.jni;

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

}
