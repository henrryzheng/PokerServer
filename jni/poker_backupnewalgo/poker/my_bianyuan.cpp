
#include "stdafx.h"
#include "math.h"


void my_bianyuan(short *Iout0,int M,int N,int L)
{
	int i,j,ii2;
	int L1=L;
	short *Iout;
	for (i=0;i<L1;i++)
	{

		Iout=Iout0+i*N;
		for( j=0;j<N;j++)
		{
			Iout[j]=0;
		}
	}
	for (i=M-1-(L1-1);i<M;i++)
	{
		Iout=Iout0+i*N;
		for( j=0;j<N;j++)
		{
			Iout[j]=0;
		}
	}
	for (i=0;i<M;i++)
	{
		Iout=Iout0+i*N;
		for(j=0;j<L1;j++)
		{
			Iout[j]=0;
		}
	}
	for (i=0;i<M;i++)
	{
		Iout=Iout0+i*N;
		for(j=N-1-(L1-1);j<N;j++)
		{
			Iout[j]=0;
		}
	}
}