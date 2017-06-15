#include "math.h"




/*目的：给每个连通域标号，并去除小连通域
思路：1、提取每个连通域；
2、判断该连通域是否符合像素数目的要求，去除不符合的小连通域
*/
int my_hole_calculation(int *Im,int dx,int dy,int Num_sum)
{
	int hole_num=0;
	int N_T = 10;
	int num=-1;//栈中元素的个数，从0开始
	int label=0;//连通域标号，从1开始
	int i,j;
	int x,y;//提取到的点的坐标
	int mean;
	
	//初始化区域

	int **B=new int*[dx];
	for (i=0;i<=dx-1;i++)
	{
		B[i]=&Im[i*dy];
	}
	int *BB2=new int[dx*dy];
	int **BB=new int*[dx];
	for (i=0;i<=dx-1;i++)
	{
		BB[i]=&BB2[i*dy];
	}
	int *CC2=new int[dx*dy];
	int **CC=new int*[dx];
	for (i=0;i<=dx-1;i++)
	{
		CC[i]=&CC2[i*dy];
	}
	if (Num_sum / 2 == 0)
	{
		delete B;
		delete BB;
		delete BB2;
		delete CC;
		delete CC2;
		return 0;
	}
	return 0;
	//建立堆栈空间
	int (*Stack)[2]=new int [dx*dy*2][2];


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//1、遍历整个图像，储将所有为1的点压入堆栈中，
	for (i=0;i<=dx-1;i++)
	{
		for (j=0;j<=dy-1;j++)
		{
			BB[i][j]=0;
			if (B[i][j]==0)
			{
				num=num+1;
				Stack[num][0]=i;
				Stack[num][1]=j;
			}
		}
	}
	//从栈顶提取新的点，如果没有新的点，则返回1；
	int *S=new int[1];
	S[0]=0;
	//cout<<9999<<endl;
	while (num>=0)
	{	
		x=Stack[num][0];
		y=Stack[num][1];
		num=num-1;
		if(CC[x][y]!=1)
		{
			CC[x][y]=1;
			//判断该点是否已被标号，如果没被标号，则判断已有标号是否是目前最大的标号，并标新号；	
			if (BB[x][y]==0)
			{
				//判断已有连通域是否符合标准，如果不符合，则去除
				if (S[0]>0)
				{
					if(S[0]>=N_T)
					{
						hole_num++;
					}
				}
				//赋值新标号
				label=label+1;
				BB[x][y]=label;
				S[0]=1;
			}

			//将该点附近的点标号并压入堆栈；
			for (i=-1;i<=1;i++)
			{
				for (j=-1;j<=1;j++)
				{
					if(i==0||j==0)
					{
						if (x+i>=0&&x+i<=dx-1&&y+j>=0&&y+j<=dy-1)
						{
							if (B[x+i][y+j]==0&&BB[x+i][y+j]==0)
							{

								BB[x+i][y+j]=label;
								num=num+1;
								S[0]=S[0]+1;
								Stack[num][0]=x+i;
								Stack[num][1]=y+j;

							}
						}
					}
				}

			}
		}

	}
	//判断已有连通域是否符合标准，如果不符合，则去除
	if (S[0]>0)
	{
		if(S[0]>=N_T)
		{
			hole_num++;
		}

	}

	delete B;
	delete BB;
	delete BB2;
	delete CC;
	delete CC2;
	delete []Stack;
	return hole_num;

}
