#include "stdafx.h"
#include "math.h"


/*#include <opencv2/core/core.hpp>
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
#include "my_imshow_short.h"*/

/**/



#include "my_pot.h"



void my_remove_pot(my_pot*pot, int k,int II)
{
	int i, j;
	//找到所有在已确定图形内部的点
	int *pot_ind_remove = new int[pot->num + 4];
	int length_pot_ind_remove = 0;
	for (i = 0; i < 3; i++)
	{
		pot_ind_remove[i] = pot->core_label[k][i];
	}
	length_pot_ind_remove = 3;
	if (pot->core_label[k][3] >= 0)
	{
		pot_ind_remove[3] = pot->core_label[k][3];
		length_pot_ind_remove = 4;
	}
	/**/


	for (i = 0; i < pot->num; i++)
	{
		if (pot->pot_p[i] > 0)
		{
			double pot_temp[2];
			pot_temp[0] = pot->X1[i];
			pot_temp[1] = pot->Y1[i];
			double V1[2], V2[2];
			int p = 1;
			//确定起始的偏转
			double sign;
			V1[0] = pot->core[k][6] - pot_temp[0];
			V1[1] = pot->core[k][7] - pot_temp[1];
			V2[0] = pot->core[k][0] - pot_temp[0];
			V2[1] = pot->core[k][1] - pot_temp[1];
			if (V1[0] * V2[1] - V1[1] * V2[0] < 0)
				sign = -1;
			else
				sign = 1;
			//判断后续的是否是相同的偏转
			for (int z = 0; z < 3; z++)
			{
				V1[0] = pot->core[k][2 * z + 0] - pot_temp[0];
				V1[1] = pot->core[k][2 * z + 1] - pot_temp[1];
				V2[0] = pot->core[k][2 * z + 0 + 2] - pot_temp[0];
				V2[1] = pot->core[k][2 * z + 1 + 2] - pot_temp[1];
				if (sign*(V1[0] * V2[1] - V1[1] * V2[0]) < 0)
				{
					p = 0;
					break;
				}
			}
			if (p == 1)
			{

				pot_ind_remove[length_pot_ind_remove] = i;
				
				pot->pot_p[i] = 0;
				length_pot_ind_remove++;
			}
		}
	}

	//去除内部与已被识别的角点
	int *P = new int[pot->num];
	for (i = 0; i < pot->num; i++)
		P[i] = 0;
	for (i = 0; i < length_pot_ind_remove; i++)
		P[pot_ind_remove[i]] = 1;
	for (i = 0; i < pot->num; i++)
	{
		if (pot->pot_p[i] == 0)
		{
			//cout << "(" << II << "," << pot->num << "," << i << ")";
			P[i] = 1;
		}
	}

	//去除被去除角点的角点组
	int remove_num = 0;
	if (k < pot->core_num-1)
	{
		for (i = k+1; i < pot->core_num; i++)
		{
			if (pot->core_p[i] > 0)
			{
				for (j = 0; j < 3; j++)
				{
					if (P[pot->core_label[i][j]] == 1)
					{
						pot->core_p[i] = 0;
						remove_num++;
						break;
					}
				}
				if (pot->core_p[i] == 1)
				{
					if (pot->core_label[i][3] >= 0)
					{
						if (P[pot->core_label[i][3]] == 1)
						{
							pot->core_p[i] = 0;
							remove_num++;
						}
					}
				}
			}
		}
	}
	
	//cout << endl << "pot.core_num=" << pot->core_num << ",length_pot_ind_remove=" << length_pot_ind_remove << endl;
	delete[]P;
	delete[]pot_ind_remove;

	
	




}