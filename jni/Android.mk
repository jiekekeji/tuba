   LOCAL_PATH := $(call my-dir)
 
   include $(CLEAR_VARS)
   #打包后的文件名
   LOCAL_MODULE    := url
   #c文件路径
   LOCAL_SRC_FILES := url.c

   include $(BUILD_SHARED_LIBRARY)