/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class luofeng_myjnitest_jni_JniHelper */


int main(){






        return 0;
};



#ifndef _Included_luofeng_myjnitest_jni_JniHelper
#define _Included_luofeng_myjnitest_jni_JniHelper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     luofeng_myjnitest_jni_JniHelper
 * Method:    createMyName
 * Signature: ()Ljava/lang/String;
 */

struct myData{

        jint code;
        jstring name;
        jboolean isperson;
        jstring key;

};




JNIEXPORT jstring JNICALL Java_luofeng_myjnitest_jni_JniHelper_createMyName
        (JNIEnv *env, jclass instance){
        struct myData data = {125,"luofeng",1,"236522"};
  return (*env)->NewStringUTF(env,data.name);
};

/*
 * Class:     luofeng_myjnitest_jni_JniHelper
 * Method:    getKey
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_luofeng_myjnitest_jni_JniHelper_getKey
        (JNIEnv *env, jclass clazz, jstring str){
};

/*
 * Class:     luofeng_myjnitest_jni_JniHelper
 * Method:    getSpecialUrl
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_luofeng_myjnitest_jni_JniHelper_getSpecialUrl
        (JNIEnv *env, jclass clazz, jstring jstr){

};

/*
 * Class:     luofeng_myjnitest_jni_JniHelper
 * Method:    votifyKey
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_luofeng_myjnitest_jni_JniHelper_votifyKey
        (JNIEnv *env, jclass clazz){

};

/*
 * Class:     luofeng_myjnitest_jni_JniHelper
 * Method:    getMsgCode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_luofeng_myjnitest_jni_JniHelper_getMsgCode
        (JNIEnv *env, jclass clazz){

};

#ifdef __cplusplus
}
#endif
#endif
