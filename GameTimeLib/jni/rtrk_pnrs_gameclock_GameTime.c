#include "rtrk_pnrs_gameclock_GameTime.h"
#include "gametime.h"

JNIEXPORT jlong JNICALL Java_rtrk_pnrs_gameclock_GameTime_increaseTime(
  JNIEnv *env, jobject this, jlong time, jlong delta)
{
  return increaseTime(time, delta);
}


JNIEXPORT jlong JNICALL Java_rtrk_pnrs_gameclock_GameTime_decreaseTime(
  JNIEnv *env, jobject this, jlong time, jlong delta)
{
  return decreaseTime(time, delta);
}
