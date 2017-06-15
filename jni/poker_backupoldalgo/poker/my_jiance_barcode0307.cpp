#include "my_space.h"
#include "my_barcode_gai_1213.h"
#include "my_mean_filter.h"
#include "my_card_para.h"
#include "my_save_consequence.h"
/*
目的：检测视频中的牌

传入参数：灰度图像Im_0，图像的高和宽nr、nc，整个视频的检测结果储存结构体Consequence
返回参数：该帧中是否检测到牌的属性P_jiance,若没有检测到牌，返回0，若检测到牌，返回1
*/
int my_jiance_barcode0307(short*Im_0,int nr,int nc,my_space *Consequence)
{
	
	int i,j,m;
	int mult_x=Consequence->mult_x,mult_y=Consequence->mult_y;
	if (nr < 1000 && nc < 1000)
	{
		mult_x = 1;
		mult_y = 1;
		Consequence->mult_x = 1;
		Consequence->mult_y = 1;
	}
	int nr2 = nr/ Consequence[0].mult_x, nc2 = nc / Consequence[0].mult_y;//用于检测的图像的大小
	short *Im=new short[nr2*nc2];//用于检测的图像


	//cout<<endl<<nr<<","<<nc<<","<<nr2<<","<<nc2<<","<<mult_x<<","<<mult_y<<endl;


	short ave;
	short sum;

	short*im,*Im_ind=Im;

	/////////////////////////////////////////均值滤波///////////////////////////////////////
	int *Im2=new int[nr2*nc2];
	int *Iout2=new int[nr2*nc2];
	short *Iout22=new short[nr2*nc2];
	int *temp=new int[nc2];
	for(i=0;i<nr2;i++)
		for(j=0;j<nc2;j++)
			Iout2[i*nc2 + j] = (int)Im_0[i*nc*mult_x + j * mult_y];

	
	my_mean_filter(Iout2,5,nc2,nr2,temp,Im2);

	for(i=0;i<nr2*nc2;i++)
	{
		Im[i]=(short)Im2[i];
		Iout22[i]=(short)Iout2[i];
	}


	///////////////////////////////////////牌是否被发放的属性//////////////////////
	for(i=0;i<55;i++)
	{
		Consequence->card_p_old[i]=Consequence->card_p[i];
	}
	//检测
	int P_jiance=0;
	P_jiance=my_barcode_gai_1213(Im,Iout22,nr2,nc2,Consequence);



	////////////////////////////////////////////////////// ////////////////////////////////////////////////////// //////////////
	//////////////////////////////////////////////////////// 对读取到的数字进行聚类/////////////////////////////////////////////////////
	my_card_para(Consequence);
	////////////////////////////////////////////////////// ////////////////////////////////////////////////////// //////////////
	//////////////////////////////////////////////////////// 获取其中的新牌/////////////////////////////////////////////////////
	
	Consequence->new_card_num=0;
	for(i=0;i<55;i++)
	{
		if(Consequence->card_p_old[i]==0&&Consequence->card_p[i]==1)
		{
			Consequence->new_card[Consequence->new_card_num]=i;
			Consequence->new_card_num++;
		}
	}

	//////////////////////////////////////////////////////获取最新的牌/////////////////////////////////////////
	for(i=0;i<55;i++)
	{
		if(Consequence->card_p_old[i]==0&&Consequence->card_p[i]==1)
			Consequence->newst_card=i;
	}

	delete Im;
	delete Iout2;
	delete Iout22;
	delete Im2;
	delete temp;
	return P_jiance;
}

