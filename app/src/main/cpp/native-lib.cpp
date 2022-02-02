#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_tj_esthata_newsapp_repository_nativerepository_NativeRepositoryIml_00024Companion_getBaseUrlFromNdk(
        JNIEnv *env, jobject thiz) {
    std::string baseUrl = "https://newsapi.org";
    return env->NewStringUTF(baseUrl.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_tj_esthata_newsapp_repository_nativerepository_NativeRepositoryIml_00024Companion_getApiKeyFromNdk(
        JNIEnv *env, jobject thiz) {
    std::string apiKey = "5769b78a970e4a4e83274770741b02c0";
    return env->NewStringUTF(apiKey.c_str());
}