


#include "stdafx.h"
#include "math.h"
/*
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
#include "my_imshow_short.h"

/**/
/*
Ŀ�ģ�ʹ��otsu����Ѱ����ѵķָ���ֵ
˼·��1������ֱ��ͼ��
2�������ܵ�ͼ��ĵ�����������;
3��������ͼ�����䷽��ҵ��������䷽��ĵ�;

������������ָ�ͼ����׵�ַint*BB��ͼ��Ŀ�͸�int dx,int dy
*/
short my_otsu(short *Im,int dx,int dy)
{
	int i;

	///////////////////////////////////////////////1������ֱ��ͼ��///////////////////////////////////////////////////////

	double *pixelNum=new double[300];

	
	for (i=0;i<256;i++)
		pixelNum[i]=0;

	int Imax=dx*dy;
	for (int i=0;i<Imax;i++)
	{
		
		pixelNum[Im[i]]++;
	}

	///////////////////////////////////////////////2�������ܵ�ͼ��ĵ�����������///////////////////////////////////////////////////////
	double *sum_V = new double[300];
	double *num_V = new double[300];
	sum_V[0]=0;
	num_V[0]=pixelNum[0];
	for (i=1;i<=255;i++)
	{
		
		sum_V[i]=sum_V[i-1]+(double)i*pixelNum[i];
		num_V[i]=num_V[i-1]+pixelNum[i];
	}
	///////////////////////////////////////////////3��������ͼ�����䷽��ҵ��������䷽��ĵ�;///////////////////////////////////////////////////////

	int T=0;//ӵ�������䷽�����ֵ
	double fmax=-1;//�����䷽��
	double sub;//ÿ�������䷽��
	double n1,n2;//ǰ����󾰵����ص�����
	double m1,m2;//ǰ����󾰵�ƽ���Ҷ�
	for (i=0;i<=255;i++)
	{
		//����ǰ�����󾰵����ص��ܺ�
		n1=num_V[i];
		n2=num_V[255]-n1;
		if (n1<1)
			continue;
		if (n2<1)
			break;
		//����ǰ�����󾰵�ƽ���Ҷ�
		m1=sum_V[i]/n1;
		m2=(sum_V[255]-sum_V[i])/n2;
		//���������ֵ����䷽��
		sub=n1*n2*(m1-m2)*(m1-m2);
		//�ж��Ƿ���Ŀǰ������䷽��
		if(sub>fmax)
		{
			fmax=sub;
			T=i;
		}
	}
	delete []pixelNum;
	delete []sum_V;
	delete []num_V;
	return (short)T;
}