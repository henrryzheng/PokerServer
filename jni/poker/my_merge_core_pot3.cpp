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


#include "my_pot.h"



void my_merge_core_pot3(my_pot*pot)
{
	int i, j;
	//针对每个角点，都让其与后续角点比较，去除后续的相似的角点
	int remove_num = 0;
	if (pot->core_num > 1)
	{
		for (i = 0; i < pot->core_num-1; i++)
		{
			if (pot->core_p[i] > 0)
			{
				for (j = i+1; j < pot->core_num; j++)
				{
					if (pot->core_p[j] > 0)
					{
						//找到相同的角点的数目
						int same_num = 0;
						int k1, k2;
						for (k1 = 0; k1 < 4; k1++)
						{
							for (k2 = 0; k2 < 4; k2++)
							{
								if (pot->core_label[i][k1] >= 0 && pot->core_label[i][k1] < pot->num&&
									pot->core_label[j][k2] >= 0 && pot->core_label[j][k2] < pot->num)
								{
									if (pot->core_label[i][k1] == pot->core_label[j][k2])
										same_num++;
								}
							}
						}
						
						if (same_num < 2)
						{
							continue;
						}
						//4个点都相同的话，则去除
						if (same_num == 4)
						{
							pot->core_p[j] = 0;
							remove_num++;
							continue;
						}

							
						//找出每个点的最近距离
						double d1 = sqrt((double)((pot->core[i][0] - pot->core[i][4])*(pot->core[i][0] - pot->core[i][4]) +
							(pot->core[i][1] - pot->core[i][5])*(pot->core[i][1] - pot->core[i][5])));
						double d_min[4];
						for (k1 = 0; k1 < 4; k1++)
							d_min[k1] = 100000;
						double d2;
						//cout << endl;
						for (k1 = 0; k1 < 4; k1++)
						{
							for (k2 = 0; k2 < 4; k2++)
							{
								d2 = sqrt((double)((pot->core[i][k1 * 2 + 0] - pot->core[j][k2 * 2 + 0])*(pot->core[i][k1 * 2 + 0] - pot->core[j][k2 * 2 + 0]) +
									(pot->core[i][k1 * 2 + 1] - pot->core[j][k2 * 2 + 1])*(pot->core[i][k1 * 2 + 1] - pot->core[j][k2 * 2 + 1])));
								d_min[k1] = (d_min[k1]< d2 ? d_min[k1]: d2);
							}
							//if (i==j)
						//	cout << d_min[k1] << ",";
						}
						//cout << endl;
						//找出最近距离的最大值
						double d_max = d_min[0];
						for (k1 = 0; k1 < 4; k1++)
						{
							d_max = (d_max> d_min[k1] ? d_max: d_min[k1]);
						}
						//判断是否属于同一个矩形
						if (d_max < d1 / (double)30)
						{
							pot->core_p[j] = 0;
							remove_num++;
						}
						/**/

					}
				}
			}
		}
	}
	//cout << endl << "pot.core_num=" << pot->core_num << ",remove_num=" << remove_num << endl;

}