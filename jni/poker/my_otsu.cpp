


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
目的：使用otsu方法寻找最佳的分割阈值
思路：1、建立直方图；
2、计算总的图像的点数和质量矩;
3、逐点计算图像的类间方差，找到有最大类间方差的点;

传入参数：待分割图像的首地址int*BB，图像的宽和高int dx,int dy
*/
short my_otsu(short *Im,int dx,int dy)
{
	int i;

	///////////////////////////////////////////////1、建立直方图；///////////////////////////////////////////////////////

	double *pixelNum=new double[300];

	
	for (i=0;i<256;i++)
		pixelNum[i]=0;

	int Imax=dx*dy;
	for (int i=0;i<Imax;i++)
	{
		
		pixelNum[Im[i]]++;
	}

	///////////////////////////////////////////////2、计算总的图像的点数和质量矩///////////////////////////////////////////////////////
	double *sum_V = new double[300];
	double *num_V = new double[300];
	sum_V[0]=0;
	num_V[0]=pixelNum[0];
	for (i=1;i<=255;i++)
	{
		
		sum_V[i]=sum_V[i-1]+(double)i*pixelNum[i];
		num_V[i]=num_V[i-1]+pixelNum[i];
	}
	///////////////////////////////////////////////3、逐点计算图像的类间方差，找到有最大类间方差的点;///////////////////////////////////////////////////////

	int T=0;//拥有最大类间方差的阈值
	double fmax=-1;//最大类间方差
	double sub;//每个点的类间方差
	double n1,n2;//前景与后景的像素点总数
	double m1,m2;//前景与后景的平均灰度
	for (i=0;i<=255;i++)
	{
		//计算前景、后景的像素点总和
		n1=num_V[i];
		n2=num_V[255]-n1;
		if (n1<1)
			continue;
		if (n2<1)
			break;
		//计算前景、后景的平均灰度
		m1=sum_V[i]/n1;
		m2=(sum_V[255]-sum_V[i])/n2;
		//计算该像素值的类间方差
		sub=n1*n2*(m1-m2)*(m1-m2);
		//判断是否是目前最大的类间方差
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