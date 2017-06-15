LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)


LOCAL_SRC_FILES += \
		my_barcode_gai_1211.cpp \
		my_bianyuan.cpp \
		my_card_para.cpp \
		my_core_juige.cpp \
		my_core_juige_0307.cpp \
		my_find_pot.cpp \
		my_find_pot3.cpp \
		my_hole_calculation.cpp \
		my_jiance_barcode0307.cpp \
		my_jiance_temp_big.cpp \
		my_jiance_temp_small.cpp \
		my_mean_fast.cpp \
		my_mean_filter.cpp \
		my_merge_core_pot3.cpp \
		my_merge_pot.cpp \
		my_num2num1.cpp \
		my_num2num2.cpp \
		my_otsu.cpp \
		my_otsu_line_obtain.cpp \
		my_real_juige.cpp \
		my_remove_pot.cpp \
		my_save_consequence.cpp \
		stdafx.cpp \
    	NativePoker.cpp

LOCAL_C_INCLUDES := \

LOCAL_LDLIBS    := -lm -llog

LOCAL_MODULE:= libPoker
#include $(BUILD_STATIC_LIBRARY)
include $(BUILD_SHARED_LIBRARY)

