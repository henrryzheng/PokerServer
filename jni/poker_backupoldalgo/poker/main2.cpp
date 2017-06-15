#include "stdafx.h"
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <iomanip>
#include <fstream>
#include "windows.h"
#include "Itoa.h"
#include "Itoa2.h"
using namespace std;
using namespace cv;


#include "my_space.h"
#include "my_jiance_barcode0307.h"
#include "my_imshow_short.h"
int main()
{
	int i,j,m,n;

	while (1)
	{
		//打开视频
		cout<<"输入视频地址："<<endl;
		string y;
		cin>>y;
		VideoCapture capture(y);
		if(!capture.isOpened())
		{
			cout<<"视频地址错误，请重新输入"<<endl;
			continue;
		}


		//获取该获取视频的参数
		Mat frame;
		capture.read(frame);
		int nr=frame.rows;
		int nc=frame.cols;
		cout<<"视频的高和宽分别为：（"<<nr<<","<<nc<<")"<<endl;



		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////初始化检测结果的保存空间////////////////////////////////////
		my_space *Consequence=new my_space;
		//发牌属性
		Consequence->card_p_old=new int[55];//上次检测后的旧属性
		Consequence->card_p=new int[55];//本次检测的属性
		for(int i=0;i<55;i++)
			Consequence->card_p[i]=0;

		//已发牌的牌属性
		Consequence->card_pro_pot=new double[55][4][6];//牌的位置属性：4条边的4组角(每组角有2对坐标)
		Consequence->card_pro_line=new double[55][4][2];//牌的位置属性：4条边的4条直线(每条直线有两个属性)
		Consequence->card_pro_center=new double[55][2];//牌的位置属性：牌的中心

		//牌聚类的属性
		Consequence->para_num=0;//牌的分段数
		Consequence->card_para_num=new int[55];//每类牌的数目
		Consequence->card_para=new int[55][55];//对牌的分类结果	
		Consequence->card_para_center=new double[55][2];//每类牌的中心

		//新牌的属性
		Consequence->new_card_num=0;//新牌的数目
		Consequence->new_card=new int[55];//新牌

		//缩放属性
		Consequence->mult_x=2;
		Consequence->mult_y=2;

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




		int currentFrame=0;
		//读取下一帧
		while(capture.read(frame))
		{
			currentFrame++;
			/////////////////////////////////////////////取灰度图像///////////////////////////////////////////
			Mat img=frame;
			Mat im;
			if (img.channels()==3)
				cvtColor(img,im, CV_BGR2GRAY);
			else
				im=img;		

			//取图像的数据
			short *Im=new short [nr*nc];
			for (j=0; j<nr; j++)
			{  
				uchar* data= im.ptr<uchar>(j);  
				for (i=0; i<nc; i++) 
				{  
					Im[j*nc+i]=  data[i];  
				}                    
			} 
			
			///////////////////////////////////////////////////检测牌/////////////////////////////////////////
		    //计算时间
			DWORD start_time,end_time;
			start_time=GetTickCount();
			
			my_jiance_barcode0307(Im,nr,nc,Consequence);


			end_time=GetTickCount();
			///////////////////////////////////////////////////输出检测结果///////////////////////////////////
			char *a=new char[100];
			cout<<"第"<<Itoa(currentFrame,a,10)<<"帧的检测时间为："<<end_time-start_time<<"ms"<<endl;;
			if (Consequence->para_num>0)
			{
				cout<<"     该视频中有"<<Consequence->para_num<<"堆牌，分别为："<<endl;
				for(int i=0;i<Consequence->para_num;i++)
				{
					cout<<"     第"<<i<<"堆的中心为（"<<(int)Consequence->card_para_center[i][0]<<","<<
						(int)Consequence->card_para_center[i][1]<<"), 有"<<Consequence->card_para_num[i]<<"张牌，具体为：";
					for(int j=0;j<Consequence->card_para_num[i];j++)
					{
					    Itoa2(Consequence->card_para[i][j]);
					    cout<<" , ";
					}
					cout<<endl;
				}

			}
			else
			{
				cout<<"     该视频中无牌。"<<endl;
			}
			
			cout<<endl<<endl<<endl;

			/////////////////////////////原始图像结果显示///////////////////////////////////////////
			//缩小原始图像显示
			my_imshow_short(Im,nr,nc,"原始图像",1);

			waitKey(30);
			////////////////////////////////////释放空间//////////////////////////////////
			delete Im;
			img.release();

			im.release();
			frame.release();

			////////////////////////////////////控制循环//////////////////////////////////
			
			system("pause");		
		}
		//关闭视频文件
		capture.release();
		return 0;
	}

}
