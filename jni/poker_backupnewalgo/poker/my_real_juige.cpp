
#include "stdafx.h"
#include "math.h"


/*
目的：根据标志判断牌是否有效
*/
int my_real_juige(short *region, int nr, int nc, int mult_region, int k, int false_num_limit)
{
	int i,j;
	int ind1,ind2;
	int block_num0,block_num1;
	//上判断
	if(k==0)
	{
		ind1=(3*mult_region-1+0*6*mult_region)*nc*mult_region+16*mult_region;
		ind2=(3*mult_region+0*6*mult_region)*nc*mult_region+16*mult_region;
	}
	else
	{
		ind1=(3*mult_region-1+0*6*mult_region)*nc*mult_region+3*mult_region;
		ind2=(3*mult_region+0*6*mult_region)*nc*mult_region+3*mult_region;
	}

	block_num0=0,block_num1=0;				
	for(j=0;j<14*mult_region;j++)
	{
		block_num0=block_num0+region[ind1+j];
		block_num1=block_num1+region[ind2+j];
	}
	if (block_num0<14 * mult_region - false_num_limit || block_num1<14 * mult_region - false_num_limit)
	{
		//cout<<1;
		return 0;
	}

	//下判断
	if(k==0)
	{
		ind1=(3*mult_region-1+7*6*mult_region)*nc*mult_region+16*mult_region;
		ind2=(3*mult_region+7*6*mult_region)*nc*mult_region+16*mult_region;
	}
	else
	{
		ind1=(3*mult_region-1+7*6*mult_region)*nc*mult_region+3*mult_region;
		ind2=(3*mult_region+7*6*mult_region)*nc*mult_region+3*mult_region;
	}
	block_num0=0,block_num1=0;				
	for(j=0;j<14*mult_region;j++)
	{
		block_num0=block_num0+region[ind1+j];
		block_num1=block_num1+region[ind2+j];
	}
	if (block_num0<14 * mult_region - false_num_limit || block_num1<14 * mult_region - false_num_limit)
	{
		//cout<<2;
		return 0;
	}
	//左判断
	if(k==0)
	{
		ind1=5*mult_region;
		ind2=5*mult_region+1;
		block_num0=0,block_num1=0;				
		for(j=16*mult_region*nc*mult_region;j<32*mult_region*nc*mult_region;j=j+nc*mult_region)
		{
			block_num0=block_num0+region[ind1+j];
			block_num1=block_num1+region[ind2+j];
		}
		if (block_num0<16 * mult_region - false_num_limit || block_num1<16 * mult_region - false_num_limit)
		{
			//cout<<"("<<3<<","<<block_num0<<")";
			return 0;
		}

	}
	//右判断
	else
	{
		ind1=26*mult_region;
		ind2=26*mult_region+1;
		block_num0=0,block_num1=0;				
		for(j=16*mult_region*nc*mult_region;j<32*mult_region*nc*mult_region;j=j+nc*mult_region)
		{
			block_num0=block_num0+region[ind1+j];
			block_num1=block_num1+region[ind2+j];
		}
		if (block_num0<16 * mult_region - false_num_limit || block_num1<16 * mult_region - false_num_limit)
		{
			//cout<<4;
			return 0;
		}

	}
	return 1;

}