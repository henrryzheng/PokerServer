#include "stdafx.h"
#include "math.h"
typedef unsigned char uchar; using namespace std;



/*目的：进行快速膨胀
*/
void my_mean_fast(int *Im,int *B,int *Iout,int nr,int nc,int L)
{
	//
	if(nr<1||nc<1||L<1)
		return ;

	
	int i,j,z;
	int indx,ind;
	int im;
	//行膨胀
	for(i=0;i<nr;i++)
	{
		indx=i*nc;
		for(j=0;j<L;j++)
		{
			ind=indx+j;
			im=0;
			for(z=-j;z<=L;z++)
				//if(Im[ind+z]>im)
					im+=Im[ind+z];
			B[ind]=im;
		}
		for(j=L;j<nc-L;j++)
		{
			ind=indx+j;
			im=0;
			for(z=-L;z<=L;z++)
				//if(Im[ind+z]>im)
					im+=Im[ind+z];
			B[ind]=im;
		}
		for(j=nc-L;j<nc;j++)
		{
			ind=indx+j;
			im=0;
			for(z=-L;z<=nc-1-j;z++)
				//if(Im[ind+z]>im)
					im+=Im[ind+z];
			B[ind]=im;
		}
	}
	
	//列膨胀
	
	for(j=0;j<nc;j++)
	{
		
		for(i=0;i<L;i++)
		{
			ind=i*nc+j;
			im=0;
			for(z=-i;z<=L;z++)
				//if(B[ind+z*nc]>im)
					im+=B[ind+z*nc];
			Iout[ind]=im;
		}
		for(i=L;i<nr-L;i++)
		{
			ind=i*nc+j;
			im=0;
			for(z=-L;z<=L;z++)
				//if(B[ind+z*nc]>im)
					im+=B[ind+z*nc];
			Iout[ind]=im;
		}
		for(i=nr-L;i<nr;i++)
		{
			ind=i*nc+j;
			im=0;
			for(z=-L;z<=nr-1-i;z++)
				//if(B[ind+z*nc]>im)
					im+=B[ind+z*nc];
			Iout[ind]=im;
		}
	}
}