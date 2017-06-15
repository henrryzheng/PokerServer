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


#include "my_hole_calculation.h"
/*目的：恢复正确的分割图像*/
void my_otsu_line_obtain(short *Im_segment_ptr, int rows, int cols, int L,int L_d)
{
	int i, j, m, n, start, end, z;
	int indx, ind;
	int nr = rows, nc = cols;


	short **Im_segment = new short*[rows];
	for (i = 0; i < rows; i++)
	{
		Im_segment[i] = &Im_segment_ptr[i*cols];
	}
	int *Im_temp_ptr = new int[rows*cols];
	for (i = 0; i < rows*cols; i++)
		Im_temp_ptr[i] = 1;
	int **Im_temp = new int*[rows];
	for (i = 0; i < rows; i++)
	{
		Im_temp[i] = &Im_temp_ptr[i*cols];
	}
	
	int Num_sum = 0;
	// 行卷积
	int im;
	short *Im = Im_segment_ptr;
	int  *B = Im_temp_ptr;
	for (i = 0; i < nr; i++)
	{
		indx = i*nc;
		if (0)
		{
			for (j = 0; j < L; j++)
			{
				ind = indx + j;
				im = 0;
				for (z = -j; z <= L; z++)
					//if(Im[ind+z]>im)
					im += Im[ind + z];
				B[ind] = im;
				if (B[ind] <= L_d)
					Num_sum++;
			}
		}
		
		ind = indx + L;
		im = 0;
		for (z = -L; z <= L; z++)
			im += Im[ind + z];
		B[ind] = im;
		if (B[ind] <= L_d)
			Num_sum++;
	//	cout << B[ind] << ",";
		for (j = L+1; j < nc - L; j++)
		{
			ind = indx + j;
			B[ind] = B[ind - 1] + Im[ind + L] - Im[ind - L - 1];
			if (B[ind] <= L_d)
				Num_sum++;
		}
		if (0)
		{
			for (j = nc - L; j < nc; j++)
			{
				ind = indx + j;
				im = 0;
				for (z = -L; z <= nc - 1 - j; z++)
					//if(Im[ind+z]>im)
					im += Im[ind + z];
				B[ind] = im;
				if (B[ind] <= L_d)
					Num_sum++;
			}
		}
	}
	/*更新*/
	if (Num_sum==0)
		Num_sum = my_hole_calculation(Im_temp_ptr, nr, nc, Num_sum);
	/*恢复图像*/
	if (Num_sum > 0)
	{
		for (i = 0; i < rows*cols; i++)
			Im_segment_ptr[i] = 1;
		for (i = 0; i < nr; i++)
		{
			indx = i*nc;
			for (j = L; j < nc - L; j++)
			{
				ind = indx + j;
				if (B[ind] <= L_d)
				{
					if (B[ind-1] <= L_d)
					{
						Im_segment_ptr[ind + L] = 0;
					}
					else
					{
						for (z = -L; z <= L; z++)
							//if(Im[ind+z]>im)
							Im_segment_ptr[ind + z] = 0;
					}
				}
			}

		}
	}


	delete Im_segment;

	delete Im_temp_ptr;
	delete Im_temp;
}
