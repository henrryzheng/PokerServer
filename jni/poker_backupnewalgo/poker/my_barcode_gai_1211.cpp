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
Ŀ�ģ���ͼ���ж�ȡ���е�����
˼·��
1����8������ɨ��ͼ��
2����ɨ��ͼ����б���ֵ�ָ�
3����ͼ����������򣬻ָ�ͼ��
4���ϲ��෴�����෴�Ķ�ֵͼ��
5��ȥ����ֱ�߳ɷֵ���ͨ��
6����ͼ�����8����˳���򣬻ָ�Ϊɨ���ֵͼ��
7����ɨ���ֵͼ����ʶ������
8���������ֺ���ȡ��Ч��
*/

#include "my_space.h"
#include "my_bianyuan.h"
#include "my_core_juige.h"
#include "my_pot.h"
#include "my_limit.h"
#include "my_merge_pot.h"
#include "my_find_pot.h"
#include "my_find_pot3.h"
#include "my_otsu.h"
#include "my_num2num2.h"
#include "my_num2num1.h"
#include "my_core_juige_0307.h"
#include "my_card_para.h"
#include "my_save_consequence.h"
#include "my_real_juige.h"
#include "my_otsu_line_obtain.h"
#include "my_jiance_temp_big.h"
#include "my_jiance_temp_small.h"


#include "my_remove_pot.h"
#include "my_merge_core_pot3.h"


int my_barcode_gai_1211(short*Im, short *Im_pre, int nr, int nc, my_space *Consequence)
{
	int P_jiance = 0;
	int i, j, k2;
	//ͼ��ָ��
	short *Im2_ptr, *Im_ptr;
	short *Iout2_ptr, *Iout_ptr;

	/////////////////////////////////�ߴ�����////////////////////////////////////
	my_limit Limit_size;
	Limit_size.heigth_min = 5, Limit_size.heigth_max = nr / 2;
	Limit_size.width_min = 5, Limit_size.width_max = nc / 2;
	Limit_size.len_min = 5, Limit_size.len_max = nc+nr;

	//////////////////////////////////����////////////////////////////////////
	int k;
	int NrNc = nr*nc;//ͼ�����������Ŀ
	int Nummax = 0;//���ֵ����Ƶ��

	//////////////////////////////////����Ƶ��////////////////////////////////////
	int(*card_spot)[2] = new int[56][2];//���ֳ��ֵ���Ƶ��



	//////////////////////////////////����Ƶ��////////////////////////////////////
	int *Num = new int[56];//���ֳ��ֵ���Ƶ��
	for (i = 0; i <= 55; i++)
		Num[i] = 0;



	//DWORD start_time,end_time0,end_time1,end_time2,end_time3,end_time4,end_time5;
	//start_time=GetTickCount();
	//////////////////////////////////////////////�������ǵ�////////////////////////////////////////////
	int nr2 = nr / 2, nc2 = nc / 2;
	short *Im2 = new short[nr2*nc2];
	short *Iout2 = new short[nr2*nc2];
	//����ͼ��
	Im2_ptr = Im2;
	Im_ptr = Im;
	for (i = 0; i < nr2; i++)
	{
		Im_ptr = Im + i*nc * 2;
		for (j = 0; j < nc2; j++)
		{

			*Im2_ptr = *Im_ptr;
			Im2_ptr++;
			Im_ptr = Im_ptr + 2;
		}

	}
	my_core_juige_0307(Im2, Iout2, nr2, nc2);
	my_bianyuan(Iout2, nr2, nc2, 2);
	//my_imshow_short(Iout2, nr2, nc2, "�ǵ�ͼ2", 1);
	////////////////////////////////////////////////���ǵ�ͼ����0;//////////////////////////////////////////////
	short *Iout = new short[nr*nc];//�ǵ�ͼ
	short *Iout_B = new short[nr*nc];//�ǵ�ͼ
	int Imax = nr*nc;
	for (i = 0; i < Imax; i++)
		Iout[i] = 0;
	int ii2, jj2;
	Iout2_ptr = Iout2;
	for (i = 0; i < nr2; i++)
	{
		for (j = 0; j < nc2; j++)
		{

			if (*Iout2_ptr == 1)
			{
				Iout_ptr = Iout + i * 2 * nc + j * 2 - nc - 1;
				Iout_ptr[0] = 1;
				Iout_ptr[1] = 1;
				Iout_ptr[2] = 1;
				Iout_ptr = Iout_ptr + nc;
				Iout_ptr[0] = 1;
				Iout_ptr[1] = 1;
				Iout_ptr[2] = 1;
				Iout_ptr = Iout_ptr + nc;
				Iout_ptr[0] = 1;
				Iout_ptr[1] = 1;
				Iout_ptr[2] = 1;
			}
			Iout2_ptr++;
		}

	}
	//////////////////////////////////////////////���ǵ�////////////////////////////////////////////

	my_core_juige(Im, Iout, nr, nc);
	my_bianyuan(Iout, nr, nc, 5);

	//cout<<"�ǵ������"<<endl;
	//my_imshow_short(Im,nr,nc,"ԭʼ�Ҷ�ͼ",1);
	//my_imshow_short(Iout,nr,nc,"�ǵ�ͼ",1);


	//end_time1=GetTickCount();
	//cout<<"end1="<<end_time1-start_time<<endl;
	//waitKey();
	//////////////////////////////////////////////����ǵ�////////////////////////////////////////////
	my_pot pot;
	pot.num = 0;

	short *Iout_temp = Iout;
	for (i = 0; i < nr; i++)
	{
		for (j = 0; j < nc; j++)
		{
			if (*Iout_temp++>0)
			{
				
				pot.num++;
			}
		}
	}
	if (pot.num>0)
	{
		pot.X1 = new double[pot.num];
		pot.Y1 = new double[pot.num];
		pot.num = 0;
		Iout_temp = Iout;
		for (i = 0; i < nr; i++)
		{
			for (j = 0; j < nc; j++)
			{
				if (*Iout_temp++>0)
				{
					pot.X1[pot.num] = i;
					pot.Y1[pot.num] = j;
					pot.num++;
				}
			}
		}
	}

	pot.core_num = 0;
	if (pot.num>0)
	{
		//�ϲ��ǵ�
		my_merge_pot(&pot);
		//�����ǵ�ռ�,
		pot.core = new double[pot.num*pot.num][8];
		pot.core_p = new int[pot.num*pot.num];
		pot.core_label = new int[pot.num*pot.num][4];
		pot.pot_p = new int[pot.num];
		pot.pot_p_card = new int[pot.num];

		for (i = 0; i < pot.num; i++)
		{
			pot.pot_p[i] = 1;
			pot.pot_p_card[i] = -1;
		}

		///////////////////////////////////////////////////////////////////�������////////////////////////////////////////////
		int II_limit = 2;
		int mult_region, false_num_limit;
		for (int II = 1; II <= II_limit; II++)
		{
			//ÿ�μ��֮ǰ�����ȹ���height�ĸ߶�
			double len_pos = 0,height_pos=0,width_pos=0;
			double pos_num = 0;
			double len_mult = sqrt((double)36 * 36 + (double)66 * 66) / sqrt((double)56 * 56 + (double)86 * 86);
			double height_mult = (double)36 / (double)56;
			double width_mult = (double)66 / (double)86;
			for (int i = 0; i < 55; i++)
			{
				//cout << i << "," << Consequence->card_p[i] << endl;
				if (Consequence->card_p[i] == 1)
				{

					len_pos += sqrt(
						(Consequence->card_pro_pot[i][0][0] - Consequence->card_pro_pot[i][2][0])*
						(Consequence->card_pro_pot[i][0][0] - Consequence->card_pro_pot[i][2][0]) +
						(Consequence->card_pro_pot[i][0][1] - Consequence->card_pro_pot[i][2][1])*
						(Consequence->card_pro_pot[i][0][1] - Consequence->card_pro_pot[i][2][1]))*len_mult;

					height_pos += sqrt(
						(Consequence->card_pro_pot[i][0][0] - Consequence->card_pro_pot[i][1][0])*
						(Consequence->card_pro_pot[i][0][0] - Consequence->card_pro_pot[i][1][0]) +
						(Consequence->card_pro_pot[i][0][1] - Consequence->card_pro_pot[i][1][1])*
						(Consequence->card_pro_pot[i][0][1] - Consequence->card_pro_pot[i][1][1]))*height_mult;

					width_pos += sqrt(
						(Consequence->card_pro_pot[i][1][0] - Consequence->card_pro_pot[i][2][0])*
						(Consequence->card_pro_pot[i][1][0] - Consequence->card_pro_pot[i][2][0]) +
						(Consequence->card_pro_pot[i][1][1] - Consequence->card_pro_pot[i][2][1])*
						(Consequence->card_pro_pot[i][1][1] - Consequence->card_pro_pot[i][2][1]))*width_mult;
					pos_num++;
				}
			}
			//	cout<<kkkk<<","<<II<<","<<"pos_num="<<pos_num<<endl;

			//���³ߴ�
			double len_mult_max = (double)1.5;
			double len_mult_min = (double)1 / (double)1.5;
			if (pos_num > 0)
			{
				len_pos = len_pos / (double)pos_num;
				height_pos = height_pos / (double)pos_num;
				width_pos = width_pos / (double)pos_num;

				Limit_size.len_min = len_pos*len_mult_min, Limit_size.len_max = len_pos*len_mult_max;
				Limit_size.heigth_min = height_pos*len_mult_min, Limit_size.heigth_max = height_pos*len_mult_max;
				Limit_size.width_min = width_pos*len_mult_min, Limit_size.width_max = width_pos*len_mult_max;
				
			}
			//cout<<"���ܵĿ��Ϊ��"<<len_pos<<endl;
			//ȥ���Ѿ������Ľ�
			int new_pot_num = 0;
			for (i = 0; i < pot.num; i++)
			{
				if (pot.pot_p[i] == 1)
				{
					pot.X1[new_pot_num] = pot.X1[i];
					pot.Y1[new_pot_num] = pot.Y1[i];
					new_pot_num++;
				}
			}
			pot.num = new_pot_num;
			//�ҵ��������������
			
			if (II == 1)
			{
				my_find_pot(&pot, Im, nr, nc, Limit_size);
				//my_merge_core_pot3(&pot);
			}
			else
			{
				my_find_pot3(&pot, Im, nr, nc,Limit_size);
				//�ϲ�����ͬһ���Ƶ�3���ǵ���
				//my_merge_core_pot3(&pot);
			}
			//������ǵ���
			if (pot.core_num > 0)
			{
				for (k = 0; k < pot.core_num; k++)
				{
					if (pot.core_p[k] > 0)
					{
						//ȷ���������������ű���
						if (II == 1)
						{
							mult_region = 1;
							false_num_limit = 0 * mult_region;
						}
						else
						{
							mult_region = 1;
							false_num_limit = 2 * mult_region;
						}
						//1�����е�һ��С�ߴ���,�����������Ŀ������0�����򷵻�����
						int  p1 = my_jiance_temp_small(&pot, k, Im_pre, nr, nc, mult_region, false_num_limit, II);

						if (p1 < 0)
							continue;
						//	cout << endl << p1 << endl;


						//2�����еڶ��δ�ߴ���,�����������Ŀ������0�����򷵻�����
						mult_region = 2;
						false_num_limit = 2 * mult_region;
						int  p2 = my_jiance_temp_big(&pot, k, Im_pre, nr, nc, mult_region, false_num_limit, p1, Consequence, II);
						if (p2 > 0)
						{
							
							//��ʶ���Ƶ��ýǵ�����Ϊ0
							pot.pot_p[pot.core_label[k][0]] = 0;
							pot.pot_p[pot.core_label[k][1]] = 0;
							pot.pot_p[pot.core_label[k][2]] = 0;
							if (pot.core_label[k][3]>=0 && pot.core_label[k][3]<pot.num)
							    pot.pot_p[pot.core_label[k][3]] = 0;

							P_jiance = 1;
							//ȥ���ǵ��ڱ�ʶ�����ڲ���Ľǵ�
							my_remove_pot(&pot, k, II);
							
						}
						//	cout << endl << p2 << endl;
					}
				}
			}
		}
	}




	//�ͷſռ�
	
	if (pot.num > 0)
	{
		delete[]pot.X1;
		delete[]pot.Y1;
		delete[]pot.core_label;
		delete[]pot.pot_p;
		delete[]pot.core;
		delete[]pot.core_p;
		delete[]pot.pot_p_card;
	}

	delete[]card_spot;
	delete[]Num;
	delete[]Iout;
	delete[]Iout_B;


	delete[]Im2;
	delete[]Iout2;
	/**/
	return P_jiance;

}



