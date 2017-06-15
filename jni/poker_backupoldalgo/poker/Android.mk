LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_CFLAGS := -D__STDC_CONSTANT_MACROS -mfpu=neon -march=armv7-a
#LOCAL_CFLAGS += -fopenmp
#LOCAL_LDFLAGS += -fopenmp
LOCAL_C_INCLUDES += \
   

LOCAL_SRC_FILES += \
		Itoa.cpp \
		Itoa2.cpp \
		my_barcode_gai_1213.cpp \
		my_bianyuan.cpp \
		my_card_para.cpp \
		my_core_juige.cpp \
		my_core_juige_0307.cpp \
		my_find_pot.cpp \
		my_find_pot3.cpp \
		my_jiance_barcode0307.cpp \
		my_otsu_line_obtain.cpp \
		my_hole_calculation.cpp \
		my_mean_filter.cpp \
		my_merge_pot.cpp \
		my_num2num1.cpp \
		my_num2num2.cpp \
		my_otsu.cpp \
		my_real_juige.cpp \
		my_save_consequence.cpp \
    	NativePoker.cpp \
    	zjhxx.cpp

LOCAL_LDFLAGS += -llog -lz
LOCAL_MODULE := mypoker
include $(BUILD_SHARED_LIBRARY)
