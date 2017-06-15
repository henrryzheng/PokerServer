#include "stdafx.h"
#include "math.h"

#include "my_space.h"
/*
Ŀ�ģ�ʹ�����������غϵ��ƽ��з���

/*
int *card_p;//�Ƶ����ԣ��Ƿ��Ѿ������Ź�
double (*card_pro_pot)[4][4];//�Ƶ�λ�����ԣ�4���ߵ�4���(ÿ�����2������)
double (*card_pro_line)[4][2];//�Ƶ�λ�����ԣ�4���ߵ�4��ֱ��(ÿ��ֱ������������)
double (*card_pro_center)[2];//�Ƶ�λ�����ԣ��Ƶ�����
int (*card_para)[55];//���Ƶķ�����
int *card_para_num;//�Ƶķֶ���
*/
void my_card_para(my_space*Consequence)
{
	int i,j,m,n;



	//1�����ַ�����������������

	int *Neighbor_ptr=new int[55*55];
	for(i=0;i<55*55;i++)
		Neighbor_ptr[i]=0;
	int **Neighbor=new int*[55];
	for(i=0;i<55;i++)
		Neighbor[i]=Neighbor_ptr+i*55;



	double (*pot1)[6],(*pot2)[6];
	double (*line1)[2],(*line2)[2];
	//cout<<"��ʼ�������"<<endl;
	int p;//�Ƿ��н��������ԣ�0Ϊ�޽�����1Ϊ�н���
	//��ÿ�鱻��ȡ�����֣������ǵ�4�����Ƿ��н�����Ϊ���Ƿ����غϲ���
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

	int *C=new int[55];//��ţ��Ƿ���Ѱ�ҹ��õ�����
	for(i=0;i<55;i++)
		C[i]=0;
	int *BB2=new int[55];//��ţ��õ��Ƿ��������������
	for(i=0;i<55;i++)
		BB2[i]=-1;



	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//�������Ԫ����Ŀ����
	int*para_Num=new int[55];


	//������ջ�ռ�
	int*Stack=new int[55*10];
	int num=0;//num��1��ʼ
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
		//cout<<"�޾���"<<endl;
		Consequence->para_num=0;
		delete []Neighbor_ptr;
		delete Neighbor;
		delete C;
		delete BB2;
		delete para_Num;
		delete Stack;
		return ;
	}
	//�������еĶ���
	if(Consequence->para_num>0)
	{
		for(i=Consequence->para_num-1;i>=0;i--)
		{
			Stack[num]=Consequence->card_para[i][0];
			num++;
		}
	}
	//��ջ����ȡ�µĵ㣬���û���µĵ㣬�򷵻�1��
	int S=0;//S��1��ʼ
	int temp;//ĳ�㱻ѡ�������
	int label=-1;//������
	while (num>=1)
	{	
		temp=Stack[num-1];
		num--;
		//cout<<temp<<",";
		if(C[temp]==0)//���õ�ûѰ�ҹ���������㣬����俪ʼѰ���������
		{
			C[temp]=1;
			//�жϸõ��Ƿ��ѱ���ţ����û����ţ���õ����������㣬���ºţ�	
			if (BB2[temp]==-1)
			{
				//��ֵ�±��
				label++;
				para_Num[label]=1;
				BB2[temp]=label;

			}
			//���õ㸽���ĵ��Ų�ѹ���ջ��
			for (i=0;i<55;i++)
			{
				//��������û��������Ѱ�ҹ�
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
	//��ȡ������
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
			//������
			Consequence->card_para[BB2[i]][Consequence->card_para_num[BB2[i]]]=i;
			Consequence->card_para_num[BB2[i]]++;
			//��������
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