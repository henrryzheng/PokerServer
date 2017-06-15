#include "Jpeg-dec.h"
#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <math.h>
#include "globle.h"
#include "display_interface.h"
#include "interface.h"
#include <string.h>

#define LOGV ALOGV 
#define LOGD ALOGD 
#define LOGI ALOGI 
#define LOGW ALOGW 
#define LOGE ALOGE 

#include "Itoa.h"
#include "Itoa2.h"
#include "my_space.h"//
#include "my_jiance_barcode0307.h"

FILE *fp_dec = NULL;
int g_exit = 0;
char pout[500000];
int dst_temp[500000];
short tmpbuf[1920*1080];
int pos_orig[2]={0,0};
unsigned long get_file_size(const char *path)  
{  
    unsigned long filesize = -1;      
    struct stat statbuff;  
    if(stat(path, &statbuff) < 0){  
        return filesize;  
    }else{  
        filesize = statbuff.st_size;  
    }  
    return filesize;  
} 

void exit_sig_handler(int sig)
{
	if(sig == SIGTERM || sig == SIGINT)
	{
		printf("sig exit\n");
    	
		g_exit = 1;
   }
}



char readcpus[19];
char* cmd_system(const char* command);
int readcpu(void)
{
    //char str[20]={"0"};
    int i;
    char* result = cmd_system("cat /proc/cpuinfo");
    //閫氳繃璇ユ柟娉曞彲浠ュ皢char*杞崲涓篶har鏁扮粍     //strcpy(str,result);
    //printf("The result:%s\n",result);
    char *substr = ":";
    char *ss  = strstr(result, substr);
    for(i=0;i<19;i++)
        readcpus[i]=ss[i+2];
    //printf("the cpu ser is %s \n",readcpus);
    return 0;
}

char* cmd_system(const char* command)
{
    char* result = "";
    FILE *fpRead;
    fpRead = popen(command, "r");
    char buf[1024];
    memset(buf,'\0',sizeof(buf));
    while(fgets(buf,1024-1,fpRead)!=NULL)
    {
        result = buf;
    }
    if(fpRead!=NULL)
        pclose(fpRead);
    return result;
}

int my_ajust(int *Im,int M,int N,int add,int mult)
{
	int i,j,ave=0,Num=0;
	//瀵绘壘涓嶄负鑳屾櫙鐨勭偣
	for (i=0;i<=M*N-1;i++)
	{
		if (Im[i]!=0)
		{
			ave=ave+Im[i];
			Num=Num+1;
		}
	}
	ave=ave/Num;

	//鎵惧埌鏂扮殑浜害
	int ave2=ave+add;
	//cout<<ave<<'\t'<<ave2<<'\t'<<mult<<endl;
	//璋冩暣浜害鍜屽姣斿害
	for (i=0;i<=M*N-1;i++)
	{
		if (Im[i]!=0)
		{

			Im[i]=((Im[i]-ave)*mult)/100+ave2;
			Im[i]=(Im[i]>0?Im[i]:0);
			Im[i]=(Im[i]<255?Im[i]:255);
		}
	}
	return 0;
}

int  YUV_All_crop(unsigned char* allBuff,unsigned char* cropBufferY, unsigned char* cropBufferC,
	int width ,int heigth,int cropleft,int croptop, int cropwidth,int cropheight)
{
    	int i=0;
    	int yuv_crop_size=0;  
	unsigned char *yuvcrop_buf_y = cropBufferY;
	unsigned char *yuvcrop_buf_c = cropBufferC;
	unsigned char *inputBuffer=allBuff;
	
	for(i=0;i<cropheight;i++)
	{	
		memcpy(yuvcrop_buf_y+yuv_crop_size, inputBuffer+width*(croptop+i)+cropleft,cropwidth);
		yuv_crop_size+=cropwidth;
	}
	

	yuv_crop_size=0;
    	for(i=0;i<cropheight/2;i++)
	{
		memcpy(yuvcrop_buf_c+yuv_crop_size, inputBuffer+width*heigth+width*(croptop/2+i)+cropleft,cropwidth);
		yuv_crop_size+=cropwidth;
	}

	//cedarx_cache_op(cropBufferY, cropBufferY+cropwidth*cropheight*3/2, 0);

    return 0;
}

#define MDEVICE_FILE "/dev/gpio_cdev"
int gpio_fd;
char write_1t[2];
int main()
{
	void *inbuffer;
	int in_size;
	void *y;
	void *c;
	int size_y;
	int size_c;
	int width = 1920;
	int height = 1080;

	signal(SIGTERM, exit_sig_handler);
	signal(SIGINT, exit_sig_handler);

	/*
	in_size = get_file_size(INPUT_DECODE_FILE_NAME);

	LOGD("get_file_size: %d", in_size);

	inbuffer = malloc(in_size);
	
	if(fp_dec == NULL) {
		fp_dec = fopen(INPUT_DECODE_FILE_NAME, "rb");
		if(fp_dec == NULL){
			LOGE("open input dec file %s failed", INPUT_DECODE_FILE_NAME);
			return 0;
		}
	}
	//char *mycpuchar = 
	
	fread(inbuffer, 1, in_size, fp_dec);*/

	char mycpuchar[] = "90110825180028150d2";
	readcpu();
//	if(strcmp(mycpuchar,readcpus)!=0)
  //        while(1);
	FpsPrint fps;
	char* cameraBuffer = NULL;
	char* outbuffer = NULL;
	char* mytempout1 = NULL;
	int ii = 0;
	int dwRet =0;
	int pos[2]={0,0};
	
	int test_ret=0;
	int i = 0;
	for( i = 0; i < 30; i++)
	{
		test_ret = init_usb_camera(i, width, height);
		if(test_ret == 0)
			break;
	}
	if(i >= 30)
	{
		printf("exit failed\n");
		exit(0);
	}
	
	
//	set_exposure_auto(0);
//	set_exposure_absolute(240);

//	set_ctrl("Brightness",0); 
//	set_ctrl("Contrast", 64);
 
	jpeg_init(width, height);
////////////////////////////////////////////////////////////////////////////////////////////


	my_space *Consequence=new my_space;
	//鍙戠墝灞炴��
	Consequence->card_p_old=new int[55];//涓婃妫�娴嬪悗鐨勬棫灞炴��
	Consequence->card_p=new int[55];//鏈妫�娴嬬殑灞炴��
	for(int i=0;i<55;i++)
		Consequence->card_p[i]=0;

	//宸插彂鐗岀殑鐗屽睘鎬�
	Consequence->card_pro_pot=new double[55][4][6];//鐗岀殑浣嶇疆灞炴�э細4鏉¤竟鐨�4缁勮(姣忕粍瑙掓湁2瀵瑰潗鏍�)
	Consequence->card_pro_line=new double[55][4][2];//鐗岀殑浣嶇疆灞炴�э細4鏉¤竟鐨�4鏉＄洿绾�(姣忔潯鐩寸嚎鏈変袱涓睘鎬�)
	Consequence->card_pro_center=new double[55][2];//鐗岀殑浣嶇疆灞炴�э細鐗岀殑涓績

	//鐗岃仛绫荤殑灞炴��
	Consequence->para_num=0;//鐗岀殑鍒嗘鏁�
	Consequence->card_para_num=new int[55];//姣忕被鐗岀殑鏁扮洰
	Consequence->card_para=new int[55][55];//瀵圭墝鐨勫垎绫荤粨鏋�	
	Consequence->card_para_center=new double[55][2];//姣忕被鐗岀殑涓績

	//鏂扮墝鐨勫睘鎬�
	Consequence->new_card_num=0;//鏂扮墝鐨勬暟鐩�
	Consequence->new_card=new int[55];//鏂扮墝

	//鏀剧缉灞炴��
	Consequence->mult_x=2;
	Consequence->mult_y=2;

////////////////////////////////////////////////////////////////////////////////////////////
	while(1)
	{
		usleep(100);
		int ret = get_image(&cameraBuffer, width, height);
		usleep(300);
		if(ret < 0)
		{
			usleep(50000);
			printf("fuck! o fuck break! get image failed!\n");
			continue;
		}
		if(jpeg_dec(width, height, cameraBuffer, ret, &outbuffer) < 0){
			printf("dec break\n");
			continue;
		}
		mytempout1 = outbuffer;
     		for(int ii=0;ii<1920*1080;ii++)
        	{
        		tmpbuf[ii] = * mytempout1++;
        	}

		//dwRet = my_Location2(tmpbuf,height,width,480,640,pos);
////////////////////////////////////////////////////////////////////////////////////////////////
		my_jiance_barcode0307(tmpbuf,1080,1920,Consequence);
		
		if (Consequence->para_num>0)
		{
			//cout<<"     璇ヨ棰戜腑鏈�"<<Consequence->para_num<<"鍫嗙墝锛屽垎鍒负锛�"<<endl;
			printf("There is  %d  heap card !!!\n",Consequence->para_num);
			for(int i=0;i<Consequence->para_num;i++)
			{
				//cout<<"     绗�"<<i<<"鍫嗙殑涓績涓猴紙"<<(int)Consequence->card_para_center[i][0]<<","<<
				//		(int)Consequence->card_para_center[i][1]<<"), 鏈�"<<Consequence->card_para_num[i]<<"寮犵墝锛屽叿浣撲负锛�";
				printf("The %d  heap center is (%d,%d) ,have %d cards is :",i,(int)Consequence->card_para_center[i][0],(int)Consequence->card_para_center[i][1],Consequence->card_para_num[i]);
				for(int j=0;j<Consequence->card_para_num[i];j++)
				{
					Itoa2(Consequence->card_para[i][j]);
					printf(",");
				}
				printf("\n");
			}

		}
		else
		{
		//	cout<<"     璇ヨ棰戜腑鏃犵墝銆�"<<endl;
		}
////////////////////////////////////////////////////////////////////////////////////////////////
/*		if(dwRet > 0)
		{
		
			
			int py =0;
        		int px =0;
        		int y=0;

			if((abs(pos[0]-pos_orig[0])<100)&&(abs(pos[1]-pos_orig[1])<100))
			{
			   py = pos_orig[0];
        		   px = pos_orig[1];
			}
			else
			{
			   py = pos[0];
			   px = pos[1];
			   pos_orig[0]= pos[0];
			   pos_orig[1]= pos[1];
			}

        		if(px >= width-640)
				px = width-640;
        		if(py >= height-480)
				py = height-480;

        		int dstheight = 480;
        		int dstwidth  = 640;
			int i = 0;
			int j = 0;
			int k = 0;
			 mytempout1 = outbuffer;
			for(i=0;i<dstheight;i++)
			{
				for(j=0;j<dstwidth;j++)
				{
					char *psrc = mytempout1+(i+py)* width+px;
					pout[i*dstwidth+j]=psrc[dstwidth-1-j];
				}
			}
			int h = dstheight>>1;
			int w = dstwidth;
			int pos = dstwidth*dstheight;
			 mytempout1 = outbuffer;

			for(i=0;i<h;i++)
			{
				for(j=0,k=0;j<w;j+=2,k++)
				{
						char *psrc =  mytempout1+( width*height)+(i+(py>>1))*width+px;
						pout[pos+i*w+j+1]=psrc[(w-1-j)+1];
						pout[pos+i*w+j]=psrc[(w-1-j)];
				}
			}
			
			display_render(pout, 640, 480);
		}else{

			display_render(outbuffer, width, height);
		}*/
		
//		display_render(outbuffer, width, height);
		fps.printFps("dec");
	}

	jpeg_exit();
	exit_usb_camera();


	return 0;
}

