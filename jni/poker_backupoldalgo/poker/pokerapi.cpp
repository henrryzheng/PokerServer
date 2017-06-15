#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <android/log.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
//#include <omp.h>
#include "Itoa.h"
#include "Itoa2.h"
#include "my_space.h"
#include "my_jiance_barcode0307.h"
#include "zjh_3102.h"
#ifndef  _mylog_
#define  _mylog_
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, "ProjectName", __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG , "ProjectName", __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO  , "ProjectName", __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN  , "ProjectName", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , "ProjectName", __VA_ARGS__)
#endif
short tmpbufx[1920*1080];
char* mytempout1 = NULL;

//////////////////////////////////////////////
char caonima[1024];
int P_jiance=0;//
int Num_jiance=0;//
int zjh_pepole;
int last_card_num;
int change=1;
int i_colour=51;
int iir=0;
int jjr=0;
short   gwCLen      	= 0;
volatile unsigned int PokerCode_zhou[55];
struct zjh_rlt my_zjh_rlt;
///////////////////////////////////////////

my_space *Consequence=new my_space;
#ifdef __cplusplus
extern "C" {
#endif

void Itoa3(int n)
{


	switch(n)
	{
	case 0:
		{
			LOGI("FP 1");
			break;
		}
	case 1:
		{
			LOGI("FP 2");
			break;
		}
	case 2:
		{
			LOGI("FP 3");
			break;
		}
	case 3:
		{
			LOGI("FP 4");
			break;
		}
	case 4:
		{
			LOGI("FP 5");
			break;
		}
	case 5:
		{
			LOGI("FP 6");
			break;
		}
	case 6:
		{
			LOGI("FP 7");
			break;
		}
	case 7:
		{
			LOGI("FP 8");
			break;
		}
	case 8:
		{
			LOGI("FP 9");
			break;
		}
	case 9:
		{
			LOGI("FP 10");
			break;
		}
	case 10:
		{
			LOGI("FP J");
			break;
		}
	case 11:
		{
			LOGI("FP Q");
			break;
		}
	case 12:
		{
			LOGI("FP K");
			break;
		}
	case 13:
		{
			LOGI("MH 1");
			break;
		}
	case 14:
		{
			LOGI("MH 2");
			break;
		}
	case 15:
		{
			LOGI("MH 3");
			break;
		}
	case 16:
		{
			LOGI("MH 4");
			break;
		}
	case 17:
		{
			LOGI("MH 5");
			break;
		}
	case 18:
		{
			LOGI("MH 6");
			break;
		}
	case 19:
		{
			LOGI("MH 7");
			break;
		}
	case 20:
		{
			LOGI("MH 8");
			break;
		}
	case 21:
		{
			LOGI("MH 9");
			break;
		}
	case 22:
		{
			LOGI("MH 10");
			break;
		}
	case 23:
		{
			LOGI("MH J");
			break;
		}
	case 24:
		{
			LOGI("MH Q");
			break;
		}
	case 25:
		{
			LOGI("MH K");
			break;
		}
	case 26:
		{
			LOGI("HOT 1");
			break;
		}
	case 27:
		{
			LOGI("HOT 2");
			break;
		}
	case 28:
		{
			LOGI("HOT 3");
			break;
		}
	case 29:
		{
			LOGI("HOT 4");
			break;
		}
	case 30:
		{
			LOGI("HOT 5");
			break;
		}
	case 31:
		{
			LOGI("HOT 6");
			break;
		}
	case 32:
		{
			LOGI("HOT 7");
			break;
		}
	case 33:
		{
			LOGI("HOT 8");
			break;
		}
	case 34:
		{
			LOGI("HOT 9");
			break;
		}
	case 35:
		{
			LOGI("HOT 10");
			break;
		}
	case 36:
		{
			LOGI("HOT J");
			break;
		}
	case 37:
		{
			LOGI("HOT Q");
			break;
		}
	case 38:
		{
			LOGI("HOT K");
			break;
		}
	case 39:
		{
			LOGI("HET 1");
			break;
		}
	case 40:
		{
			LOGI("HET 2");
			break;
		}
	case 41:
		{
			LOGI("HET 3");
			break;
		}
	case 42:
		{
			LOGI("HET 4");
			break;
		}
	case 43:
		{
			LOGI("HET 5");
			break;
		}
	case 44:
		{
			LOGI("HET 6");
			break;
		}
	case 45:
		{
			LOGI("HET 7");
			break;
		}
	case 46:
		{
			LOGI("HET 8");
			break;
		}
	case 47:
		{
			LOGI("HET 9");
			break;
		}
	case 48:
		{
			LOGI("HET 10");
			break;
		}
	case 49:
		{
			LOGI("HET J");
			break;
		}
	case 50:
		{
			LOGI("HET Q");
			break;
		}
	case 51:
		{
			LOGI("HET K");
			break;
		}
	case 52:
		{
			LOGI("XIAO gui");
			break;
		}
	case 53:
		{
			LOGI("DA gui");
			break;
		}
	case 54:
		{
			LOGI("GUANG GAO PAI");
			break;
		}
	}

}

JNICALL JNIEXPORT jint  Java_com_google_pokerserver_MYCamCalBk_DecodePicture(JNIEnv * env,jobject obj,jbyteArray indata,jint img_w,jint img_h,jbyteArray outdata)
{
	if(change == 1)
	{
		LOGE("Init ..111.");
		change = 0;
        char buf[100];
        memset(buf,0,sizeof(buf));
        LOGI(buf);
       //////////////////////////////////////////////////////
		Consequence->card_p_old=new int[55];
		Consequence->card_p=new int[55];
		for(int iiy=0;iiy<55;iiy++)
			Consequence->card_p[iiy]=0;

		Consequence->card_pro_pot=new double[55][4][6];
		Consequence->card_pro_line=new double[55][4][2];
		Consequence->card_pro_center=new double[55][2];

		Consequence->para_num=0;
		Consequence->card_para_num=new int[55];
		for(int iix=0;iix<55;iix++)
			Consequence->card_para_num[iix]=0;
		Consequence->card_para=new int[55][55];
		Consequence->card_para_center=new double[55][2];

		Consequence->new_card_num=0;
		Consequence->new_card=new int[55];

		Consequence->mult_x=2;
		Consequence->mult_y=2;

    }


    {

        char* pin = (char *)env->GetByteArrayElements(indata,0);
    	char* pout = (char *)env->GetByteArrayElements(outdata,0);
        char newcard = 0;
        int beast_card=0;

        int  dwRet = 0;
        int  testopenmp=0;
        int th_id;
       // int i;
        LOGI("oh fuck mingge 55555555555555555!");
        ///////////////////////////////////////////////////////////////////////////////////
		mytempout1 = pin;

		for(int ii=0;ii<1920*1080;ii++)
		{
			tmpbufx[ii] = * mytempout1++;
		}
		LOGI("oh fuck mingge 666666666666666666!");
		P_jiance=my_jiance_barcode0307(tmpbufx,480,640,Consequence);
		LOGI("oh fuck mingge 777777777777777777!");

		if(P_jiance==1)
			Num_jiance=0;
		else
			Num_jiance++;

		if(Num_jiance>2)
		{
			Num_jiance=0;

			Consequence->para_num=0;
			for(int iit=0;iit<55;iit++)
			{
				Consequence->card_para_num[iit]=0;
				Consequence->card_p[iit]=0;
			}
			LOGI("1111111111111111111111");
		}

		if (Consequence->para_num>0)
		{
			//     	char* pin = (char *)env->GetByteArrayElements(indata,0);
			//		char* pout = (char *)env->GetByteArrayElements(outdata,0);
			gwCLen = 0;
			for(int ttt=0;ttt<1024;ttt++)
			{
				caonima[ttt]=0;
			}
			LOGI("There is  %d  heap card !!!\n",Consequence->para_num);
			for(iir=0;iir<Consequence->para_num;iir++)
			{
				LOGI("The %d  heap center is (%d,%d) ,have %d cards is :",iir,(int)Consequence->card_para_center[iir][0],(int)Consequence->card_para_center[iir][1],Consequence->card_para_num[iir]);
				for(jjr=0;jjr<Consequence->card_para_num[iir];jjr++)
				{
					Itoa3(Consequence->card_para[iir][jjr]);
					caonima[iir*20+jjr]=(char)(Consequence->card_para[iir][jjr])+1;
				}
			}


			zjh_pepole=Consequence->para_num;//玩家人数
			LOGI("zjh_pepole=%d",zjh_pepole);
			if(last_card_num!=3)
				last_card_num=Consequence->card_para_num[Consequence->para_num -1];//最后一个玩家牌的数量
			LOGI("last_card_num=%d",last_card_num);

			if(Consequence->card_para_num[Consequence->para_num -1]!=3)
				zjh_pepole=Consequence->para_num -1;//玩家人数


			gwCLen = 0;
			int iiq=0;
			int jjq=0;
			//LOGI("There is  %d  heap card !!!\n",Consequence->para_num);
			for(iiq=0;iiq<zjh_pepole;iiq++)
			{
				//LOGI("The %d  heap center is (%d,%d) ,have %d cards is :",iir,(int)Consequence->card_para_center[iir][0],(int)Consequence->card_para_center[iir][1],Consequence->card_para_num[iir]);
				for(jjq=0;jjq<3;jjq++)
				{
					//Itoa3(Consequence->card_para[iir][jjr]);
					PokerCode_zhou[gwCLen]=Consequence->card_para[iiq][jjq];
					gwCLen++;
				}

			}
			if(gwCLen>0 && gwCLen<=55 && last_card_num==3){
				volatile zjh_card mycard[52];
				int *baopai;
				for(int iii=0;iii<gwCLen;iii++)
				{
						mycard[iii].Value=(PokerCode_zhou[iii])%13+1;
						mycard[iii].Hs=(PokerCode_zhou[iii])/13+1;
				}
				//
				my_zjh_rlt=ZJH_3102_Call(zjh_pepole,mycard);

				caonima[500]=my_zjh_rlt.first;
				caonima[501]=my_zjh_rlt.second;

			}


			for(int clrx=0;clrx<1024;clrx++)
			{
					*pout++ =caonima[clrx];
			}
			LOGI("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2=%d!",caonima[500]);
			(env)->ReleaseByteArrayElements(outdata,(jbyte *)pout,0);
			(env)->ReleaseByteArrayElements(indata,(jbyte *)pin,0);

			return 1024;

		}
		else
		{
			LOGI("########################################################!");
			gwCLen = 0;
			newcard=0;
			last_card_num=0;
			for(int tts=0;tts<1024;tts++)
			{
				caonima[tts]=0;
			}
			for(int clrx=0;clrx<1024;clrx++)
			{
					*pout++ =caonima[clrx];
			}

			(env)->ReleaseByteArrayElements(outdata,(jbyte *)pout,0);
			(env)->ReleaseByteArrayElements(indata,(jbyte *)pin,0);

			return 1024;
		}
		//LOGI("newcard=%d",newcard);
  ///////////////////////////////////////////////////////////////////////////////////


    }
	return 0;
}

#ifdef __cplusplus
}
#endif


//static class MyVideoFrameExtractor;


