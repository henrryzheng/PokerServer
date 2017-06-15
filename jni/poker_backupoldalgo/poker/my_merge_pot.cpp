#include "math.h"






/*Ŀ�ģ��ϲ����ڵĵ�
˼·��1���ӵ�һ���㿪ʼ����������ͬ�㣬����м�Ȩ�ϲ���
2��ѡ����һ���������ң��ϲ�ʣ��㣻
3�������κϲ������Ŀ���䣬��ֹͣ�ϲ���
*/
/*Center�Ĳ�����0.x�������������
1.y�������������
2.���ɸõ����Ŀ
3.ֱ���Ƿ񱻱���������
*/
#include"my_pot.h"
void my_merge_pot(my_pot *pot)
{
	int i,j;
	if(pot->num<1)
	{
		return;
	}
	//������ʱ�ռ䣬����ϲ����ֱ��
	double (*center1)[4]=new double [pot->num][4];
	double (*Center1)[4]=center1;
	int Center_num1=pot->num;
	//��ȡ��ʼֱ��
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
		//1���ӵ�һ���㿪ʼ����������ͬб��ֱ�ߣ�����м�Ȩ�ϲ��������ݺϲ�ֱ�ߵ����ġ����ȡ�б��
		i=0;
		while(i<=Center_num1-1)
		{
			//cout<<i<<'\t';
			//�����ֱ�߻�û�б��ϲ����򽫸�ֱ����ȡΪ�µ���ʼֱ��
			if(Center1[i][3]==1)
			{
				//2��ѡ����һ���������ң��ϲ�ʣ��ֱ�ߣ�
				Center_num2=Center_num2+1;

				Center2[Center_num2-1][0]=Center1[i][0];
				Center2[Center_num2-1][1]=Center1[i][1];
				Center2[Center_num2-1][2]=Center1[i][2];
				Center2[Center_num2-1][3]=Center1[i][3];

				if(i<Center_num1-1)
				{
					for(j=i+1;j<=Center_num1-1;j++)
					{
						//�жϽǶ��Ƿ�����
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
		//�������ֱ����Ҫ�ϲ����򽫺ϲ���ĸ�������ǰ�ģ�����ʼ���ϲ�ǰ��
		if(Center_num2<Center_num1)
		{
			Center1=Center2;
			Center_num1=Center_num2;
			Center_num2=0;
		}
	}
	//���ϲ����ֱ�߸���ԭʼ�ռ�
	for(i=0;i<Center_num1;i++)
	{
		pot->X1[i]=Center1[i][0];
		pot->Y1[i]=Center1[i][1];
	}
	pot->num=Center_num1;



	delete []center1;
	delete []center2;

}
