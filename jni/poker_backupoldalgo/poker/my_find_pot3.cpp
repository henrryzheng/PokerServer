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
void my_find_pot3(my_pot *pot,short*Imm,int nr,int nc,double height_limit_min,double height_limit_max)
{
	//my_imshow_short(Imm,nr,nc,"pan",1);/**/
	int i,j,z,i2,j2;
	if(pot->num<4)
		return;
	double Lab1[2]={0,0};
	double Lab2[2]={0,0};
	double Lab3[2]={0,0};
	double Lab4[2]={0,0};
	double V1[2]={0,0};
	double V2[2]={0,0};
	double L1,L2;
	double mult_min=(double)28/66;
	double mult_max=(double)44/66;
	double Vsum,Vsum_max=0.20;
	double Lab_d,Lab_dmin;
	int tab;
	double Len_height_min=(double)36*(double)nr/1080/2,Len_width_min=(double)66*(double)nr/1080/2,Len_line_min=(double)75*(double)nr/1080/2;
	double Lab_dmax=3,Lab_dmax_mult;

	double dmin,dmin_tab;

	short im;
	int LL,num,LL_sum;


	char a[100];

	//计算点之间的相似度
	short **Im=new short*[nr];
	Im[0]=Imm;
	for(i=1;i<nr;i++)
		Im[i]=Im[i-1]+nc;

	int (*image)[81]=new int[pot->num][81];
	int x,y;
	for(i=0;i<pot->num;i++)
	{
		x=pot->X1[i];
		y=pot->Y1[i];
		num=0;
	/*	im=Im[x][y];
		for(i2=x-4;i2<=x+4;i2++)
		{
			for(j2=y-4;j2<=y+4;j2++)
			{
				image[i][num]=(Im[i2][j2]>im?1:0);
				num++;
			}
		}*/
		im=0;
		for(i2=x-4;i2<=x+4;i2++)
		{
			for(j2=y-4;j2<=y+4;j2++)
			{
				im=im+Im[i2][j2];
				
			}
		}
		im=im/81;
		for(i2=x-4;i2<=x+4;i2++)
		{
			for(j2=y-4;j2<=y+4;j2++)
			{
				image[i][num]=(Im[i2][j2]>im?1:0);
				num++;
			}
		}

	}
	int *Sim_ptr=new int[pot->num*pot->num];
	int **Sim=new int*[pot->num];
	Sim[0]=Sim_ptr;
	for(i=1;i<pot->num;i++)
		Sim[i]=Sim[i-1]+pot->num;
	int sim_right;
	for(i=0;i<pot->num-1;i++)
	{
		for(j=i+1;j<pot->num;j++)
		{
			sim_right=0;
			for(i2=0;i2<81;i2++)
				if(image[i][i2]==image[j][i2])
					sim_right++;
			Sim[i][j]=sim_right;
			Sim[j][i]=sim_right;

		}
	}

	//计算点与点之间的距离
	double *D_ptr=new double[pot->num*pot->num];
	double **D=new double*[pot->num];
	D[0]=D_ptr;
	for(i=1;i<pot->num;i++)
		D[i]=D[i-1]+pot->num;
	for(i=0;i<pot->num-1;i++)
	{
		for(j=i+1;j<pot->num;j++)
		{
			D[i][j]=sqrt((pot->X1[i]-pot->X1[j])*(pot->X1[i]-pot->X1[j])+(pot->Y1[i]-pot->Y1[j])*(pot->Y1[i]-pot->Y1[j]));
			D[j][i]=D[i][j];
		}
	}
	//计算点图像原点之间的距离
	double *orig_D=new double[pot->num];

	for(i=0;i<pot->num;i++)
	{
		orig_D[i]=pot->X1[i]*pot->X1[i]+pot->Y1[i]*pot->Y1[i];
	}
	//第4个候选点与其他点之间的距离
	double dx,dy;
	//找最合适的点
	pot->core_num=0;
	for(i=0;i<pot->num;i++)
	{
		for(z=0;z<pot->num;z++)
		{
			if(i==z)
				continue;
			//判断1点是否靠近坐标原点
			//if(orig_D[i]>=orig_D[z])
			//	continue;
			if(D[i][z]<Len_line_min)
				continue;
			if(D[i][z]>nr/3)
				continue;
			if(D[i][z]<height_limit_min||D[i][z]>height_limit_max)
				continue;
			//if(Sim[i][z]<56)
			//	continue;
			for(j=0;j<pot->num;j++)
			{
				if(i!=j&&j!=z&&i!=z)
				{
					//判断1点是否靠近坐标原点
				//	if(orig_D[i]>orig_D[j])
				//		continue;

					//判断三个角的相似度:1-3大于1-2和2-3
					if(Sim[i][z]<Sim[i][j]||Sim[i][z]<Sim[j][z])
						continue;
					//if(Sim[i][j]>24||Sim[j][z]>24)
					//	continue;

					//判断长度比例 L1<L2
					
					L1=D[i][j];
					L2=D[j][z];
					if (L1>L2)
						continue;
					if(L1/L2<mult_min||L1/L2>mult_max)
						continue;
					//判断1-2和2-3的长度
					if(D[i][j]<Len_height_min)
				          continue;
					if(D[z][j]<Len_width_min)
				          continue;
				



					//判断向量的夹角
					Lab1[0]=pot->X1[i],Lab1[1]=pot->Y1[i];
					Lab2[0]=pot->X1[j],Lab2[1]=pot->Y1[j];
					Lab3[0]=pot->X1[z],Lab3[1]=pot->Y1[z];
					V1[0]=(Lab1[0]-Lab2[0])/L1;
					V1[1]=(Lab1[1]-Lab2[1])/L1;
					V2[0]=(Lab2[0]-Lab3[0])/L2;
					V2[1]=(Lab2[1]-Lab3[1])/L2;
					Vsum=V1[0]*V2[0]+V1[1]*V2[1];
					Vsum=(Vsum>=0?Vsum:-Vsum);
					if(Vsum>Vsum_max)
						continue;

					//找到第4个点
					Lab4[0]=Lab1[0]+(Lab3[0]-Lab2[0]);
					Lab4[1]=Lab1[1]+(Lab3[1]-Lab2[1]);
					if(Lab4[0]>nr-1||Lab4[0]<0||Lab4[1]>nc-1||Lab4[1]<0)
						continue;

					//储存有效点
					pot->core[pot->core_num][0]=Lab1[0];
					pot->core[pot->core_num][1]=Lab1[1];
					pot->core[pot->core_num][2]=Lab2[0];
					pot->core[pot->core_num][3]=Lab2[1];
					pot->core[pot->core_num][4]=Lab3[0];
					pot->core[pot->core_num][5]=Lab3[1];
					pot->core[pot->core_num][6]=Lab4[0];
					pot->core[pot->core_num][7]=Lab4[1];
					//储存坐标

					pot->core_num++;
					


				}
			}
		}
	}



	//释放空间
	delete Im;
	delete []image;
	delete Sim_ptr;
	delete Sim;
	delete D_ptr;
	delete D;
	delete orig_D;

}
