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

*/



/*Ŀ�ģ��ϲ����ڵĵ�
˼·��1���ӵ�һ���㿪ʼ����������ͬ�㣬����м�Ȩ�ϲ���
2��ѡ����һ���������ң��ϲ�ʣ��㣻
3�������κϲ������Ŀ���䣬��ֹͣ�ϲ���
*/
/*Center�Ĳ�����0.x�������������
1.y�������������
2.���ɸõ����Ŀ
3.ֱ���Ƿ񱻱���������
*/
#include"my_pot.h"
#include "my_limit.h"
void my_find_pot(my_pot *pot, short*Imm, int nr, int nc, my_limit Limit_size)
{
	//my_imshow_short(Imm,nr,nc,"pan",1);/**/
	int i,j,z,i2,j2;
	pot->core_num = 0;
	if(pot->num<4)
		return;
	double Lab1[2]={0,0};
	double Lab2[2]={0,0};
	double Lab3[2]={0,0};
	double Lab4[2]={0,0};
	double V1[2]={0,0};
	double V2[2]={0,0};
	double L1,L2;
	double mult_min=(double)28/66;
	double mult_max=(double)44/66;
	double Vsum,Vsum_max=0.20;
	double Lab_d,Lab_dmin;
	int tab;
	double Lab_dmax=3,Lab_dmax_mult;
	double Len_height_min=(double)36*(double)nr/1080/2,Len_width_min=(double)66*(double)nr/1080/2,Len_line_min=(double)75*(double)nr/1080/2;
	//cout<<Len_height_min<<","<<Len_width_min<<","<<Len_line_min<<endl;
	double dmin,dmin_tab;

	short im;
	int LL,num,LL_sum;


	char a[100];

	//�����֮������ƶ�
	short **Im=new short*[nr];
	Im[0]=Imm;
	for(i=1;i<nr;i++)
		Im[i]=Im[i-1]+nc;

	int (*image)[81]=new int[pot->num][81];
	int x,y;
	for(i=0;i<pot->num;i++)
	{
		x=pot->X1[i];
		y=pot->Y1[i];
		num=0;
	/*	im=Im[x][y];
		for(i2=x-4;i2<=x+4;i2++)
		{
			for(j2=y-4;j2<=y+4;j2++)
			{
				image[i][num]=(Im[i2][j2]>im?1:0);
				num++;
			}
		}*/
		im=0;
		for(i2=x-4;i2<=x+4;i2++)
		{
			for(j2=y-4;j2<=y+4;j2++)
			{
				im=im+Im[i2][j2];
				
			}
		}
		im=im/81;
		for(i2=x-4;i2<=x+4;i2++)
		{
			for(j2=y-4;j2<=y+4;j2++)
			{
				image[i][num]=(Im[i2][j2]>im?1:0);
				num++;
			}
		}

	}
	int *Sim_ptr=new int[pot->num*pot->num];
	int **Sim=new int*[pot->num];
	Sim[0]=Sim_ptr;
	for(i=1;i<pot->num;i++)
		Sim[i]=Sim[i-1]+pot->num;
	int sim_right;
	for(i=0;i<pot->num-1;i++)
	{
		for(j=i+1;j<pot->num;j++)
		{
			sim_right=0;
			for(i2=0;i2<81;i2++)
				if(image[i][i2]==image[j][i2])
					sim_right++;
			Sim[i][j]=sim_right;
			Sim[j][i]=sim_right;

		}
	}

	//��������֮��ľ���
	double *D_ptr=new double[pot->num*pot->num];
	double **D=new double*[pot->num];
	D[0]=D_ptr;
	for(i=1;i<pot->num;i++)
		D[i]=D[i-1]+pot->num;
	for(i=0;i<pot->num-1;i++)
	{
		for(j=i+1;j<pot->num;j++)
		{
			D[i][j]=sqrt((pot->X1[i]-pot->X1[j])*(pot->X1[i]-pot->X1[j])+(pot->Y1[i]-pot->Y1[j])*(pot->Y1[i]-pot->Y1[j]));
			D[j][i]=D[i][j];
		}
	}
	//�����ͼ��ԭ��֮��ľ���
	double *orig_D=new double[pot->num];

	for(i=0;i<pot->num;i++)
	{
		orig_D[i]=pot->X1[i]*pot->X1[i]+pot->Y1[i]*pot->Y1[i];
	}
	//��4����ѡ����������֮��ľ���
	double dx,dy;
	//������ʵĵ�
	pot->core_num=0;
	for(i=0;i<pot->num;i++)
	{
		for(z=0;z<pot->num;z++)
		{
			
			if (i == z)
				continue;
			//�ж�1���Ƿ񿿽�����ԭ��
			if(orig_D[i]>=orig_D[z])
				continue;
			if(D[i][z]<Len_line_min)
				continue;
			if (D[i][z]<Limit_size.len_min || D[i][z]>Limit_size.len_max)
				continue;
			//if(Sim[i][z]<56)
			//	continue;

			//cout << "(" << i << "," << z << "),";
			for(j=0;j<pot->num;j++)
			{
				if(i!=j&&j!=z&&i!=z)
				{
					//�ж�1���Ƿ񿿽�����ԭ��
					if(orig_D[i]>orig_D[j])
						continue;

					//�ж������ǵ����ƶ�:1-3����1-2��2-3
					if(Sim[i][z]<Sim[i][j]||Sim[i][z]<Sim[j][z])
						continue;
					//if(Sim[i][j]>24||Sim[j][z]>24)
					//	continue;

					//�жϳ��ȱ��� L1<L2
					
					L1=D[i][j];
					L2=D[j][z];
					if (L1>L2)
						continue;
					if(L1/L2<mult_min||L1/L2>mult_max)
						continue;
					//�ж�1-2��2-3�ĳ���
					if(D[i][j]<Len_height_min)
				          continue;
					if(D[z][j]<Len_width_min)
				          continue;

					if (D[i][j]<Limit_size.heigth_min || D[i][j]>Limit_size.heigth_max)
						continue;
					if (D[j][z]<Limit_size.width_min || D[j][z]>Limit_size.width_max)
						continue;
				



					//�ж�2-1��2-3�����ļн�
					Lab1[0]=pot->X1[i],Lab1[1]=pot->Y1[i];
					Lab2[0]=pot->X1[j],Lab2[1]=pot->Y1[j];
					Lab3[0]=pot->X1[z],Lab3[1]=pot->Y1[z];
					V1[0]=(Lab1[0]-Lab2[0])/D[i][j];
					V1[1]=(Lab1[1]-Lab2[1])/D[i][j];
					V2[0]=(Lab2[0]-Lab3[0])/D[j][z];
					V2[1]=(Lab2[1]-Lab3[1])/D[j][z];
					
					Vsum=V1[0]*V2[0]+V1[1]*V2[1];
					Vsum=(Vsum>=0?Vsum:-Vsum);
					if(Vsum>Vsum_max)
						continue;

					//�ҵ���4����
					Lab4[0]=Lab1[0]+(Lab3[0]-Lab2[0]);
					Lab4[1]=Lab1[1]+(Lab3[1]-Lab2[1]);
					if(Lab4[0]>nr-1||Lab4[0]<0||Lab4[1]>nc-1||Lab4[1]<0)
						continue;

					//�ҵ������4��������ĵ�
					Lab_dmin=(Lab4[0]-pot->X1[0])*(Lab4[0]-pot->X1[0])+(Lab4[1]-pot->Y1[0])*(Lab4[1]-pot->Y1[0]);
					tab=0;
					//Lab_dmax_mult=L1*Lab_dmax/36;
					for(i2=1;i2<pot->num;i2++)
					{
						dx=Lab4[0]-pot->X1[i2];
						dx=(dx>=0?dx:-dx);				
						//if(dx>Lab_dmax_mult)
						//	continue;

						dy=Lab4[1]-pot->Y1[i2];
						dy=(dy>=0?dy:-dy);
						//if(dy>Lab_dmax_mult)
						//	continue;

						Lab_d=dx*dx+dy*dy;
						if(Lab_d<Lab_dmin)
						{
							Lab_dmin=Lab_d;
							tab=i2;
						}
					}
					Lab_dmin=sqrt(Lab_dmin);
				/*	if(Lab_dmin>(L1*Lab_dmax/36))
						continue;
					*/	
					//��ֵ��4����
					Lab4[0]=pot->X1[tab];
					Lab4[1]=pot->Y1[tab];
					

					//�ж�4��������ƶ�
					sim_right=(Sim[i][z]<Sim[j][tab]?Sim[i][z]:Sim[j][tab]);
					if(sim_right<Sim[i][j]||sim_right<Sim[i][tab]||sim_right<Sim[z][j]||sim_right<Sim[z][tab])
						continue;


					//�ж�1-4��4-3,4-2�ĳ���
					if(D[z][tab]<Len_height_min)
				          continue;
					if(D[i][tab]<Len_width_min)
				          continue;
					if(D[j][tab]<Len_line_min)
				          continue;

						  /**/
					//�ж�4-1��4-3�����ļн�
					Lab1[0]=pot->X1[i],Lab1[1]=pot->Y1[i];
					Lab2[0]=pot->X1[tab],Lab2[1]=pot->Y1[tab];
					Lab3[0]=pot->X1[z],Lab3[1]=pot->Y1[z];
					V1[0]=(Lab1[0]-Lab2[0])/D[i][tab];
					V1[1]=(Lab1[1]-Lab2[1])/D[i][tab];
					V2[0]=(Lab2[0]-Lab3[0])/D[tab][z];
					V2[1]=(Lab2[1]-Lab3[1])/D[tab][z];
					Vsum=V1[0]*V2[0]+V1[1]*V2[1];
					Vsum=(Vsum>=0?Vsum:-Vsum);
					//cout<<"Vsum="<<Vsum<<'\t';
					if(Vsum>Vsum_max)
						continue;
					/**/
					//�ж�1-2��1-4�����ļн�
					Lab1[0]=pot->X1[tab],Lab1[1]=pot->Y1[tab];
					Lab2[0]=pot->X1[i],Lab2[1]=pot->Y1[i];
					Lab3[0]=pot->X1[j],Lab3[1]=pot->Y1[j];
					V1[0]=(Lab1[0]-Lab2[0])/D[tab][i];
					V1[1]=(Lab1[1]-Lab2[1])/D[tab][i];
					V2[0]=(Lab2[0]-Lab3[0])/D[j][i];
					V2[1]=(Lab2[1]-Lab3[1])/D[j][i];
					Vsum=V1[0]*V2[0]+V1[1]*V2[1];
					Vsum=(Vsum>=0?Vsum:-Vsum);
					if(Vsum>Vsum_max)
						continue;
					/**/
					//�ж�3-2��3-4�����ļн�
					Lab1[0]=pot->X1[tab],Lab1[1]=pot->Y1[tab];
					Lab2[0]=pot->X1[z],Lab2[1]=pot->Y1[z];
					Lab3[0]=pot->X1[j],Lab3[1]=pot->Y1[j];
					V1[0]=(Lab1[0]-Lab2[0])/D[tab][z];
					V1[1]=(Lab1[1]-Lab2[1])/D[tab][z];
					V2[0]=(Lab2[0]-Lab3[0])/D[j][z];
					V2[1]=(Lab2[1]-Lab3[1])/D[j][z];
					Vsum=V1[0]*V2[0]+V1[1]*V2[1];
					Vsum=(Vsum>=0?Vsum:-Vsum);
					if(Vsum>Vsum_max)
						continue;
						/**/
					//������Ч��
					pot->core[pot->core_num][0]=pot->X1[i];
					pot->core[pot->core_num][1]=pot->Y1[i];
					pot->core[pot->core_num][2]=pot->X1[j];
					pot->core[pot->core_num][3]=pot->Y1[j];
					pot->core[pot->core_num][4]=pot->X1[z];
					pot->core[pot->core_num][5]=pot->Y1[z];
					pot->core[pot->core_num][6]=pot->X1[tab];
					pot->core[pot->core_num][7]=pot->Y1[tab];
					//��������
					pot->core_label[pot->core_num][0]=i;
					pot->core_label[pot->core_num][1]=j;
					pot->core_label[pot->core_num][2]=z;
					pot->core_label[pot->core_num][3]=tab;

					pot->core_num++;
					


				}
			}
		}
	}
	if (pot->core_num > 0)
	{
		for (i = 0; i < pot->core_num; i++)
			pot->core_p[i] = 1;
	}


	//�ͷſռ�
	delete Im;
	delete []image;
	delete Sim_ptr;
	delete Sim;
	delete D_ptr;
	delete D;
	delete orig_D;

}