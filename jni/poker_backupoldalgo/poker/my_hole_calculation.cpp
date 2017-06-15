#include "math.h"




/*Ŀ�ģ���ÿ����ͨ���ţ���ȥ��С��ͨ��
˼·��1����ȡÿ����ͨ��
2���жϸ���ͨ���Ƿ����������Ŀ��Ҫ��ȥ�������ϵ�С��ͨ��
*/
int my_hole_calculation(int *Im,int dx,int dy,int Num_sum)
{
	int hole_num=0;
	int N_T = 10;
	int num=-1;//ջ��Ԫ�صĸ�������0��ʼ
	int label=0;//��ͨ���ţ���1��ʼ
	int i,j;
	int x,y;//��ȡ���ĵ������
	int mean;
	
	//��ʼ������

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
	//������ջ�ռ�
	int (*Stack)[2]=new int [dx*dy*2][2];


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//1����������ͼ�񣬴�������Ϊ1�ĵ�ѹ���ջ�У�
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
	//��ջ����ȡ�µĵ㣬���û���µĵ㣬�򷵻�1��
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
			//�жϸõ��Ƿ��ѱ���ţ����û����ţ����ж����б���Ƿ���Ŀǰ���ı�ţ������ºţ�	
			if (BB[x][y]==0)
			{
				//�ж�������ͨ���Ƿ���ϱ�׼����������ϣ���ȥ��
				if (S[0]>0)
				{
					if(S[0]>=N_T)
					{
						hole_num++;
					}
				}
				//��ֵ�±��
				label=label+1;
				BB[x][y]=label;
				S[0]=1;
			}

			//���õ㸽���ĵ��Ų�ѹ���ջ��
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
	//�ж�������ͨ���Ƿ���ϱ�׼����������ϣ���ȥ��
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
