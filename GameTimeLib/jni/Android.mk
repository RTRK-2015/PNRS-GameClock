LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

$(info $(TARGET_ARCH_ABI))

LOCAL_MODULE := libdyngametime
LOCAL_STATIC_LIBRARIES := libgametime-$(TARGET_ARCH_ABI)
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := rtrk_pnrs_gameclock_GameTime.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libgametime-$(TARGET_ARCH_ABI)
LOCAL_SRC_FILES := libgametime-$(TARGET_ARCH_ABI).a
include $(PREBUILT_STATIC_LIBRARY)
