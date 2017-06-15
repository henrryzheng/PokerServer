
#include "math.h"



//目的：使用弱分类器组合成强分类器的原理判断出角点
void my_core_juige_0307(short*Imm,short *CC,int nr,int nc)
{
	int i,j,i2,j2;
	int Imax;
	int LL,L;
	short im;

	//图像寻址
	short **Im=new short *[nr];
	Im[0]=Imm;
	for(i=1;i<nr;i++)
		Im[i]=Im[i-1]+nc;
	//将角点图像置0
	Imax=nr*nc-1;
	for(i=0;i<Imax;i++)
		CC[i]=1;
	//
	short**C=new short*[nr];
	C[0]=CC;
	for(i=1;i<nr;i++)
		C[i]=C[i-1]+nc;

	//


	short Min[4],Max[4];
	LL=2;
	i2=2;
	for(i=LL;i<nr-LL;i++)
	{
		for(j=LL;j<nc-LL;j++)
		{
			if(C[i][j]>0)
			{
				C[i][j]=0;
				


				if(Im[i+i2][j]<Im[i-i2][j])
				{
					Min[0]=Im[i+i2][j];
					Max[0]=Im[i-i2][j];
				}
				else
				{
					Max[0]=Im[i+i2][j];
					Min[0]=Im[i-i2][j];
				}
				if(Im[i][j+i2]<Im[i][j-i2])
				{
					Min[1]=Im[i][j+i2];
					Max[1]=Im[i][j-i2];
				}
				else
				{
					Max[1]=Im[i][j+i2];
					Min[1]=Im[i][j-i2];
				}
				if(Im[i+i2][j+i2]<Im[i-i2][j-i2])
				{
					Min[2]=Im[i+i2][j+i2];
					Max[2]=Im[i-i2][j-i2];
				}
				else
				{
					Max[2]=Im[i+i2][j+i2];
					Min[2]=Im[i-i2][j-i2];
				}
				if(Im[i+i2][j-i2]<Im[i-i2][j+i2])
				{
					Min[3]=Im[i+i2][j-i2];
					Max[3]=Im[i-i2][j+i2];

				}
				else
				{
					Max[3]=Im[i+i2][j-i2];
					Min[3]=Im[i-i2][j+i2];
				}
				im=Im[i][j];
				if((Max[1]<im&&im<Min[0])||(Max[0]<im&&im<Min[1])||(Max[2]<im&&im<Min[3])||(Max[3]<im&&im<Min[2]))
				{
					C[i][j]=1;
				}



			}
		}
	}/**/

	delete Im;
	delete C;
}
