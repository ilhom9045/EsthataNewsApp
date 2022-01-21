#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_tj_esthata_newsapp_others_NativeUtil_00024Companion_getBaseUrl(JNIEnv *env, jobject thiz) {
    std::string hello = "https://newsapi.org";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_tj_esthata_newsapp_others_NativeUtil_00024Companion_getApiKey(JNIEnv *env, jobject thiz) {
    std::string hello = "9103e23179334683a62173d3ee37e612";
    return env->NewStringUTF(hello.c_str());
}