#include <jni.h>

#ifndef _Included_rtrk_pnrs_gameclock_GameTime
#define _Included_rtrk_pnrs_gameclock_GameTime
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_rtrk_pnrs_gameclock_GameTime_increaseTime(
  JNIEnv *env, jobject this, jlong time, jlong delta);

JNIEXPORT jlong JNICALL Java_rtrk_pnrs_gameclock_GameTime_decreaseTime(
  JNIEnv *env, jobject this, jlong time, jlong delta);

#ifdef __cplusplus
}
#endif
#endif
