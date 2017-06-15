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
		//����Ƶ
		cout<<"������Ƶ��ַ��"<<endl;
		string y;
		cin>>y;
		VideoCapture capture(y);
		if(!capture.isOpened())
		{
			cout<<"��Ƶ��ַ��������������"<<endl;
			continue;
		}


		//��ȡ�û�ȡ��Ƶ�Ĳ���
		Mat frame;
		capture.read(frame);
		int nr=frame.rows;
		int nc=frame.cols;
		cout<<"��Ƶ�ĸߺͿ�ֱ�Ϊ����"<<nr<<","<<nc<<")"<<endl;



		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////��ʼ��������ı���ռ�////////////////////////////////////
		my_space *Consequence=new my_space;
		//��������
		Consequence->card_p_old=new int[55];//�ϴμ���ľ�����
		Consequence->card_p=new int[55];//���μ�������
		for(int i=0;i<55;i++)
			Consequence->card_p[i]=0;

		//�ѷ��Ƶ�������
		Consequence->card_pro_pot=new double[55][4][6];//�Ƶ�λ�����ԣ�4���ߵ�4���(ÿ�����2������)
		Consequence->card_pro_line=new double[55][4][2];//�Ƶ�λ�����ԣ�4���ߵ�4��ֱ��(ÿ��ֱ������������)
		Consequence->card_pro_center=new double[55][2];//�Ƶ�λ�����ԣ��Ƶ�����

		//�ƾ��������
		Consequence->para_num=0;//�Ƶķֶ���
		Consequence->card_para_num=new int[55];//ÿ���Ƶ���Ŀ
		Consequence->card_para=new int[55][55];//���Ƶķ�����	
		Consequence->card_para_center=new double[55][2];//ÿ���Ƶ�����

		//���Ƶ�����
		Consequence->new_card_num=0;//���Ƶ���Ŀ
		Consequence->new_card=new int[55];//����

		//��������
		Consequence->mult_x=2;
		Consequence->mult_y=2;

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




		int currentFrame=0;
		//��ȡ��һ֡
		while(capture.read(frame))
		{
			currentFrame++;
			/////////////////////////////////////////////ȡ�Ҷ�ͼ��///////////////////////////////////////////
			Mat img=frame;
			Mat im;
			if (img.channels()==3)
				cvtColor(img,im, CV_BGR2GRAY);
			else
				im=img;		

			//ȡͼ�������
			short *Im=new short [nr*nc];
			for (j=0; j<nr; j++)
			{  
				uchar* data= im.ptr<uchar>(j);  
				for (i=0; i<nc; i++) 
				{  
					Im[j*nc+i]=  data[i];  
				}                    
			} 
			
			///////////////////////////////////////////////////�����/////////////////////////////////////////
		    //����ʱ��
			DWORD start_time,end_time;
			start_time=GetTickCount();
			
			my_jiance_barcode0307(Im,nr,nc,Consequence);


			end_time=GetTickCount();
			///////////////////////////////////////////////////��������///////////////////////////////////
			char *a=new char[100];
			cout<<"��"<<Itoa(currentFrame,a,10)<<"֡�ļ��ʱ��Ϊ��"<<end_time-start_time<<"ms"<<endl;;
			if (Consequence->para_num>0)
			{
				cout<<"     ����Ƶ����"<<Consequence->para_num<<"���ƣ��ֱ�Ϊ��"<<endl;
				for(int i=0;i<Consequence->para_num;i++)
				{
					cout<<"     ��"<<i<<"�ѵ�����Ϊ��"<<(int)Consequence->card_para_center[i][0]<<","<<
						(int)Consequence->card_para_center[i][1]<<"), ��"<<Consequence->card_para_num[i]<<"���ƣ�����Ϊ��";
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
				cout<<"     ����Ƶ�����ơ�"<<endl;
			}
			
			cout<<endl<<endl<<endl;

			/////////////////////////////ԭʼͼ������ʾ///////////////////////////////////////////
			//��Сԭʼͼ����ʾ
			my_imshow_short(Im,nr,nc,"ԭʼͼ��",1);

			waitKey(30);
			////////////////////////////////////�ͷſռ�//////////////////////////////////
			delete Im;
			img.release();

			im.release();
			frame.release();

			////////////////////////////////////����ѭ��//////////////////////////////////
			
			system("pause");		
		}
		//�ر���Ƶ�ļ�
		capture.release();
		return 0;
	}

}
