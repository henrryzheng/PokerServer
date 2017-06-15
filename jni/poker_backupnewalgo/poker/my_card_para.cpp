#include "stdafx.h"
#include "math.h"

#include "my_space.h"
/*
目的：使用生长法对重合的牌进行分类

/*
int *card_p;//牌的属性，是否已经被发放过
double (*card_pro_pot)[4][4];//牌的位置属性：4条边的4组角(每组角有2对坐标)
double (*card_pro_line)[4][2];//牌的位置属性：4条边的4条直线(每条直线有两个属性)
double (*card_pro_center)[2];//牌的位置属性：牌的中心
int (*card_para)[55];//对牌的分类结果
int *card_para_num;//牌的分段数
*/
void my_card_para(my_space*Consequence)
{
	int i,j,m,n;



	//1、对字符建立邻域索引矩阵

	int *Neighbor_ptr=new int[55*55];
	for(i=0;i<55*55;i++)
		Neighbor_ptr[i]=0;
	int **Neighbor=new int*[55];
	for(i=0;i<55;i++)
		Neighbor[i]=Neighbor_ptr+i*55;



	double (*pot1)[6],(*pot2)[6];
	double (*line1)[2],(*line2)[2];
	//cout<<"开始邻域表建立"<<endl;
	int p;//是否有交集的属性，0为无交集，1为有交集
	//对每组被读取的数字，求他们的4条边是否有交集作为牌是否有重合部分
	for(i=0;i<55;i++)
	{
		if(Consequence->card_p[i]==1)
		{
			pot1=Consequence->card_pro_pot[i];
			line1=Consequence->card_pro_line[i];
			for(j=0;j<55;j++)
			{
				if(i!=j)
				{

				if(Consequence->card_p[j]==1)
				{
					pot2=Consequence->card_pro_pot[j];
					line2=Consequence->card_pro_line[j];
					p=0;
					for(m=0;m<4;m++)
					{
						for(n=0;n<4;n++)
						{
							if((pot1[m][0]*line2[n][0]+line2[n][1]-pot1[m][1])*(pot1[m][2]*line2[n][0]+line2[n][1]-pot1[m][3])<=0 &&
								(pot2[n][0]*line1[m][0]+line1[m][1]-pot2[n][1])*(pot2[n][2]*line1[m][0]+line1[m][1]-pot2[n][3])<=0)
							{
								p++;
								break;
							}

						}
						if(p>0)
							break;
					}
				/*	for(m=0;m<4;m++)
					{
						for(n=0;n<3;n++)
						{
							if((pot1[m][n*2]*line2[0][0]+line2[0][1]-pot1[m][n*2+1])*(pot1[m][n*2]*line2[2][0]+line2[2][1]-pot1[m][n*2+1])<0&&
								(pot1[m][n*2]*line2[1][0]+line2[1][1]-pot1[m][n*2+1])*(pot1[m][n*2]*line2[3][0]+line2[3][1]-pot1[m][n*2+1])<0)

							{
								p++;
								break;
							}

						}
						if(p>0)
							break;
					}*/

					if(p>0)
					{
						Neighbor[i][j]=1;
						Neighbor[j][i]=1;
					}
				}
				}
			}
		}
	}

	int *C=new int[55];//标号：是否已寻找过该点邻域
	for(i=0;i<55;i++)
		C[i]=0;
	int *BB2=new int[55];//标号：该点是否是其他点的邻域
	for(i=0;i<55;i++)
		BB2[i]=-1;



	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//建立组的元素数目矩阵
	int*para_Num=new int[55];


	//建立堆栈空间
	int*Stack=new int[55*10];
	int num=0;//num从1开始
	for(i=0;i<55;i++)
	{
		if(Consequence->card_p[i]==1)
		{
			Stack[num]=i;
			num++;
		}
	}
	
	if(num==0)
	{
		//cout<<"无聚类"<<endl;
		Consequence->para_num=0;
		delete []Neighbor_ptr;
		delete Neighbor;
		delete C;
		delete BB2;
		delete para_Num;
		delete Stack;
		return ;
	}
	//放置已有的堆牌
	if(Consequence->para_num>0)
	{
		for(i=Consequence->para_num-1;i>=0;i--)
		{
			Stack[num]=Consequence->card_para[i][0];
			num++;
		}
	}
	//从栈顶提取新的点，如果没有新的点，则返回1；
	int S=0;//S从1开始
	int temp;//某点被选择的坐标
	int label=-1;//分类标号
	while (num>=1)
	{	
		temp=Stack[num-1];
		num--;
		//cout<<temp<<",";
		if(C[temp]==0)//若该点没寻找过其他邻域点，则从其开始寻找其邻域点
		{
			C[temp]=1;
			//判断该点是否已被标号，如果没被标号，则该点是新组的起点，标新号；	
			if (BB2[temp]==-1)
			{
				//赋值新标号
				label++;
				para_Num[label]=1;
				BB2[temp]=label;

			}
			//将该点附近的点标号并压入堆栈；
			for (i=0;i<55;i++)
			{
				//是邻域，且没被其他点寻找过
				if(Neighbor[temp][i]==1&&BB2[i]==-1)
				{
					BB2[i]=label;
					num++;
					Stack[num-1]=i;
					para_Num[label]++;
				}
			}
		}
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//获取聚类结果
	Consequence->para_num=label+1;
	for(i=0;i<55;i++)
	{
		Consequence->card_para_num[i]=0;
		Consequence->card_para_center[i][0]=0;
		Consequence->card_para_center[i][1]=0;
	}
	for(i=0;i<55;i++)
	{
		if(Consequence->card_p[i]==1)
		{
			//保存牌
			Consequence->card_para[BB2[i]][Consequence->card_para_num[BB2[i]]]=i;
			Consequence->card_para_num[BB2[i]]++;
			//保存中心
			Consequence->card_para_center[BB2[i]][0]+=Consequence->card_pro_center[i][0];
			Consequence->card_para_center[BB2[i]][1]+=Consequence->card_pro_center[i][1];
		}
	}
	for(i=0;i<Consequence->para_num;i++)
	{
		Consequence->card_para_center[i][0]=Consequence->card_para_center[i][0]/Consequence->card_para_num[i]*Consequence->mult_x;
		Consequence->card_para_center[i][1]=Consequence->card_para_center[i][1]/Consequence->card_para_num[i]*Consequence->mult_y;
	}
	delete []Neighbor_ptr;
	delete Neighbor;
	delete C;
	delete BB2;
	delete para_Num;
	delete Stack;
}