#include "math.h"






/*目的：合并相邻的点
思路：1、从第一条点开始，若有相邻同点，则进行加权合并，
2、选择下一个点往右找，合并剩余点；
3、若两次合并后点数目不变，则停止合并。
*/
/*Center的参数：0.x方向的中心坐标
1.y方向的中心坐标
2.构成该点的数目
3.直线是否被遍历的属性
*/
#include"my_pot.h"
void my_merge_pot(my_pot *pot)
{
	int i,j;
	if(pot->num<1)
	{
		return;
	}
	//申请临时空间，储存合并后的直线
	double (*center1)[4]=new double [pot->num][4];
	double (*Center1)[4]=center1;
	int Center_num1=pot->num;
	//读取初始直线
	for(i=0;i<pot->num;i++)
	{
		Center1[i][0]=pot->X1[i];
		Center1[i][1]=pot->Y1[i];
		Center1[i][2]=1;
		Center1[i][3]=1;
	}


	double (*center2)[4]=new double [pot->num][4];
	double (*Center2)[4]=center2;
	int Center_num2=0;






	int num1,num2,num;
	double d1,d2,d_max=2.5;
	while(Center_num2==0)
	{
		//1、从第一个点开始，若有相邻同斜度直线，则进行加权合并，并根据合并直线的中心、长度、斜度
		i=0;
		while(i<=Center_num1-1)
		{
			//cout<<i<<'\t';
			//如果该直线还没有被合并，则将该直线提取为新的起始直线
			if(Center1[i][3]==1)
			{
				//2、选择下一个点往右找，合并剩余直线；
				Center_num2=Center_num2+1;

				Center2[Center_num2-1][0]=Center1[i][0];
				Center2[Center_num2-1][1]=Center1[i][1];
				Center2[Center_num2-1][2]=Center1[i][2];
				Center2[Center_num2-1][3]=Center1[i][3];

				if(i<Center_num1-1)
				{
					for(j=i+1;j<=Center_num1-1;j++)
					{
						//判断角度是否满足
						d1=Center2[Center_num2-1][0]-Center1[j][0];
						d2=Center2[Center_num2-1][1]-Center1[j][1];
						d1=(d1>=0?d1:-d1);
						d2=(d2>=0?d2:-d2);
						if(d1<d_max&&d2<d_max)
						{
							num1=Center2[Center_num2-1][2];
							num2=Center1[j][2];
							num=num1+num2;

							Center2[Center_num2-1][0]=(Center2[Center_num2-1][0]*(double)num1+Center1[j][0]*(double)num2)/(double)num;
							Center2[Center_num2-1][1]=(Center2[Center_num2-1][1]*(double)num1+Center1[j][1]*(double)num2)/(double)num;
							Center2[Center_num2-1][2]=num;
							Center1[j][3]=0;
						}

					}
				}
			}
			i=i+1;
		}
		//如果仍有直线需要合并，则将合并后的赋给赋给前的，并初始化合并前的
		if(Center_num2<Center_num1)
		{
			Center1=Center2;
			Center_num1=Center_num2;
			Center_num2=0;
		}
	}
	//将合并后的直线赋给原始空间
	for(i=0;i<Center_num1;i++)
	{
		pot->X1[i]=Center1[i][0];
		pot->Y1[i]=Center1[i][1];
	}
	pot->num=Center_num1;



	delete []center1;
	delete []center2;

}
