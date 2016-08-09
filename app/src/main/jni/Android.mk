LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
OPENCV_LIB_TYPE:=STATIC
ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
#try to load OpenCV.mk from default install location
include D:\Libraries\OpenCV-android-sdk\sdk\native\jni\OpenCV.mk
else
include $(OPENCV_MK_PATH)
endif
LOCAL_MODULE    := OpenCVCannyJNI
LOCAL_SRC_FILES := com_wuja6_android_getimageedgeapp_task_OpenCVProcessTask.cpp
include $(BUILD_SHARED_LIBRARY)