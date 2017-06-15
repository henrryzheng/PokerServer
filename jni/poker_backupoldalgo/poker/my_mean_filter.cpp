
#include "math.h"


void my_mean_filter(int *imSrc,const int windows,const int width,const int height,int *Acol,int *imDst)
{
	int i,j,k,r,n,sum;
	r=(windows-1)/2;
	
	for(i=0;i<height;i++)
	{
		if(i==0)
		{
			for(k=0;k<width;k++)
			{
				Acol[k]=imSrc[k];
				for(n=1;n<=r;n++)
					Acol[k]+=imSrc[n*width+k]*2;
			}
		}
		else
		{
			if(i>0&&i<=r)
			{
				for(k=0;k<width;k++)
					Acol[k]=Acol[k]-imSrc[(r+1-i)*width+k]+imSrc[(i+r)*width+k];
			}
			else if(i>r&&i<height-r)
			{
				for(k=0;k<width;k++)
					Acol[k]=Acol[k]-imSrc[(i-r-1)*width+k]+imSrc[(i+r)*width+k];
			}
			else
			{
				for(k=0;k<width;k++)
					Acol[k]=Acol[k]-imSrc[(i-r-1)*width+k]+imSrc[(2*height-i-r-1)*width+k];
			}
		}
		sum=Acol[0];
		for(n=1;n<=r;n++)
			sum+=2*Acol[n];


		int Num=windows*windows;
		imDst[i*width]=sum/Num;

		
		int ind=i*width;
		for(j=1;j<width;j++)
		{
			if(j>=1&&j<r+1)
			{
				sum=sum-Acol[r+1-j]+Acol[j+r];
				imDst[ind+j]=sum/Num;
			}
			else if(j>=r+1&&j<width-r)
			{
				sum=sum-Acol[j-1-r]+Acol[j+r];
				imDst[ind+j]=sum/Num;
			}
			else
			{
				sum=sum-Acol[j-1-r]+Acol[2*width-j-r-1];
				imDst[ind+j]=sum/Num;
			}
		}
	}

}
