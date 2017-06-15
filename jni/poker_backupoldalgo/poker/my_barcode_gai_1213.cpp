#include "math.h"

/*
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <iomanip>
#include <fstream>
#include "windows.h"
#include "Itoa.h"
#include "Itoa2.h"
using namespace std;
using namespace cv;
#include "my_imshow_short.h"

*/




/*
Ŀ�ģ���ͼ���ж�ȡ���е�����
˼·��
1����8������ɨ��ͼ��
2����ɨ��ͼ����б���ֵ�ָ�
3����ͼ����������򣬻ָ�ͼ��
4���ϲ��෴�����෴�Ķ�ֵͼ��
5��ȥ����ֱ�߳ɷֵ���ͨ��
6����ͼ�����8����˳���򣬻ָ�Ϊɨ���ֵͼ��
7����ɨ���ֵͼ����ʶ������
8���������ֺ���ȡ��Ч��
*/

#include "my_space.h"
#include "my_bianyuan.h"
#include "my_core_juige.h"
#include "my_pot.h"
#include "my_merge_pot.h"
#include "my_find_pot.h"
#include "my_find_pot3.h"
#include "my_otsu.h"
#include "my_num2num2.h"
#include "my_num2num1.h"
#include "my_core_juige_0307.h"
#include "my_card_para.h"
#include "my_save_consequence.h"
#include "my_real_juige.h"
#include "my_otsu_line_obtain.h"


int my_barcode_gai_1213(short*Im,short *Im_pre,int nr,int nc,my_space *Consequence)
{
	int P_jiance = 0;
	int i,j,k2;
	//ͼ��ָ��
	short *Im2_ptr,*Im_ptr;
	short *Iout2_ptr,*Iout_ptr;


	//////////////////////////////////����////////////////////////////////////
	int k;
	int NrNc=nr*nc;//ͼ�����������Ŀ
	int Nummax=0;//���ֵ����Ƶ��

	//////////////////////////////////����Ƶ��////////////////////////////////////
	int (*card_spot)[2]=new int[56][2];//���ֳ��ֵ���Ƶ��



	//////////////////////////////////����Ƶ��////////////////////////////////////
	int *Num=new int[56];//���ֳ��ֵ���Ƶ��
	for (i=0;i<=55;i++)
		Num[i]=0;



	//DWORD start_time,end_time0,end_time1,end_time2,end_time3,end_time4,end_time5;
	//start_time=GetTickCount();
	//////////////////////////////////////////////�������ǵ�////////////////////////////////////////////
	int nr2=nr/2,nc2=nc/2;
	short *Im2=new short[nr2*nc2];
	short *Iout2=new short[nr2*nc2];
	//����ͼ��
	Im2_ptr=Im2;
	Im_ptr=Im;
	for(i=0;i<nr2;i++)
	{
		Im_ptr=Im+i*nc*2;
		for(j=0;j<nc2;j++)
		{

			*Im2_ptr=*Im_ptr;
			Im2_ptr++;
			Im_ptr=Im_ptr+2;
		}

	}
	my_core_juige_0307(Im2,Iout2,nr2,nc2);
	my_bianyuan(Iout2,nr2,nc2,2);
	//my_imshow_short(Iout2, nr2, nc2, "�ǵ�ͼ2", 1);
	////////////////////////////////////////////////���ǵ�ͼ����0;//////////////////////////////////////////////
	short *Iout=new short[nr*nc];//�ǵ�ͼ
	short *Iout_B=new short[nr*nc];//�ǵ�ͼ
	int Imax=nr*nc;
	for(i=0;i<Imax;i++)
		Iout[i]=0;
	int ii2,jj2;
	Iout2_ptr=Iout2;
	for(i=0;i<nr2;i++)
	{
		for(j=0;j<nc2;j++)
		{

			if(*Iout2_ptr==1)
			{
				Iout_ptr=Iout+i*2*nc+j*2-nc-1;
				Iout_ptr[0]=1;
				Iout_ptr[1]=1;
				Iout_ptr[2]=1;
				Iout_ptr=Iout_ptr+nc;
				Iout_ptr[0]=1;
				Iout_ptr[1]=1;
				Iout_ptr[2]=1;
				Iout_ptr=Iout_ptr+nc;
				Iout_ptr[0]=1;
				Iout_ptr[1]=1;
				Iout_ptr[2]=1;
			}
			Iout2_ptr++;
		}

	}
	//////////////////////////////////////////////���ǵ�////////////////////////////////////////////

	my_core_juige(Im,Iout,nr,nc);
	my_bianyuan(Iout,nr,nc,5);

	//cout<<"�ǵ������"<<endl;
	//my_imshow_short(Im,nr,nc,"ԭʼ�Ҷ�ͼ",1);
	//my_imshow_short(Iout,nr,nc,"�ǵ�ͼ",1);


	//end_time1=GetTickCount();
	//cout<<"end1="<<end_time1-start_time<<endl;
	//waitKey();
	//////////////////////////////////////////////����ǵ�////////////////////////////////////////////
	my_pot pot;
	pot.X1=new double[nr*nc/9];
	pot.Y1=new double[nr*nc/9];
	pot.num=0;

	short *Iout_temp=Iout;
	for (i=0;i<nr;i++)
	{
		for(j=0;j<nc;j++)
		{
			if(*Iout_temp++>0)
			{
				pot.X1[pot.num]=i;
				pot.Y1[pot.num]=j;
				pot.num++;
			}
		}
	}

	//cout<<"�ǵ���ĿΪ��"<<pot.num<<endl;
	////////////////////////////////////////////�ϲ��ǵ�////////////////////////////////////////////
	my_merge_pot(&pot);
	//////////////////////////////////////////////�ҵ�����������4����,���ҽ���С������////////////////////////////////////////////
	pot.core = new double[pot.num*pot.num][8];
	pot.core_num=0;
	pot.core_label = new int[pot.num*pot.num][4];
	pot.pot_p=new int[pot.num];



	for(i=0;i<pot.num;i++)
		pot.pot_p[i]=1;



	int mult_region=2;
	short *Region=new short[48*66*mult_region*mult_region];
	short *region=new short[48*33*mult_region*mult_region];
	short *block;
	short *block1=new short[14*mult_region*mult_region];
	short *block2=new short[14*mult_region*mult_region];
	int block_num0,block_num1;
	int region_start[2]={0*mult_region,33*mult_region};
	int (*Value)[6]=new int[2][6];
	int (*Value0)[6]=new int[2][6];
	int num_Value[2];
	int real_num;
	char aa[100]="һ����ͼ";
	char bb[100]="������ͼ";
	char cc[100];





	char name[100];
	int false_num_limit = 0 * mult_region;
	double len_pos=0;
	double pos_num=0;
	double dimension_mult=sqrt((double)36*36+(double)66*66)/sqrt((double)56*56+(double)86*86);
	for(int II=1;II<=2;II++)
	{
		//cout << endl << "II=" << II; 
		if (II == 1)
		{
			false_num_limit = 0 * mult_region;
		}
		else
		{
			false_num_limit = 2 * mult_region;
		}
		pos_num=0;
		len_pos=0;
		//ÿ�μ��֮ǰ�����ȹ���height�ĸ߶�
		for(int i=0;i<55;i++)
		{
			if(Consequence->card_p[i]==1)
			{
				len_pos+=sqrt((Consequence->card_pro_pot[i][0][0]-Consequence->card_pro_pot[i][2][0])*
					(Consequence->card_pro_pot[i][0][0]-Consequence->card_pro_pot[i][2][0])+
					(Consequence->card_pro_pot[i][0][1]-Consequence->card_pro_pot[i][2][1])*
					(Consequence->card_pro_pot[i][0][1]-Consequence->card_pro_pot[i][2][1]))*dimension_mult;
				pos_num++;
			}
		}
		//cout<<"pos_num="<<pos_num<<endl;
		if(pos_num>0)
			len_pos=len_pos/pos_num;
		//cout<<"���ܵĿ��Ϊ��"<<len_pos<<endl;
		//ȥ���Ѿ������Ľ�
		int new_pot_num=0;
		for(i=0;i<pot.num;i++)
		{
			if(pot.pot_p[i]==1)
			{
				pot.X1[new_pot_num]=pot.X1[i];
				pot.Y1[new_pot_num]=pot.Y1[i];
				new_pot_num++;
			}
		}
		pot.num=new_pot_num;

		//�ҵ��������������
		if(II==1)
		{
			if(pos_num<=1)
				my_find_pot(&pot,Im,nr,nc,0,(double)nr/2);
			else
				my_find_pot(&pot,Im,nr,nc,len_pos/2,len_pos*2);
		}
		else
		{
			if(pos_num<=0)
				my_find_pot3(&pot,Im,nr,nc,0,(double)nr/2);
			else
				my_find_pot3(&pot,Im,nr,nc,len_pos/2,len_pos*2);
		}
		int m,n;
		/////////////////////////////////////////////�ֿ�������///////////////////////////////////////////

		if(pot.core_num>0)
		{

			int PPP;//1Ϊ�����֣�0Ϊ������
			if(pot.core_num>0)
			{

				for(k=0;k<pot.core_num;k++)
				{
					//cout<<"("<<pot.core_num<<","<<k<<")"<<'\t';

					double center_x=(pot.core[k][0]+pot.core[k][2]+pot.core[k][4]+pot.core[k][6])/(double)4;
					double center_y=(pot.core[k][1]+pot.core[k][3]+pot.core[k][5]+pot.core[k][7])/(double)4;
					if(center_x<10||center_x>nr-10||center_y<10||center_y>nc-10)
						continue;

					double a,b;//�任�߶�
					//ʵ�����ϽǺ����½ǵ�����ڹ���̨���Ͻ�ƫ������
					/*double real_dx1=center_x+(pot.core[k][6]-pot.core[k][2])/(double)2+(pot.core[k][6]-pot.core[k][4])/(double)6;
					double real_dy1=center_y+(pot.core[k][7]-pot.core[k][3])/(double)2+(pot.core[k][7]-pot.core[k][5])/(double)6;
					double real_dx2=center_x+(pot.core[k][2]-pot.core[k][6])/(double)2+(pot.core[k][2]-pot.core[k][0])/(double)6;
					double real_dy2=center_y+(pot.core[k][3]-pot.core[k][7])/(double)2+(pot.core[k][3]-pot.core[k][1])/(double)6;

					double origin_x=center_x+(pot.core[k][0]-pot.core[k][4])/(double)2+(pot.core[k][0]-pot.core[k][2])/(double)6;
					double origin_y=center_y+(pot.core[k][1]-pot.core[k][5])/(double)2+(pot.core[k][1]-pot.core[k][3])/(double)6;
					*/
					double real_dx1=pot.core[k][6]+(pot.core[k][6]-pot.core[k][4])/(double)6;
					double real_dy1=pot.core[k][7]+(pot.core[k][7]-pot.core[k][5])/(double)6;
					double real_dx2=pot.core[k][2]+(pot.core[k][2]-pot.core[k][0])/(double)6;
					double real_dy2=pot.core[k][3]+(pot.core[k][3]-pot.core[k][1])/(double)6;

					double origin_x=pot.core[k][0]+(pot.core[k][0]-pot.core[k][2])/(double)6;
					double origin_y=pot.core[k][1]+(pot.core[k][1]-pot.core[k][3])/(double)6;


					real_dx1=(real_dx1>1?real_dx1:1),real_dx1=(real_dx1<nr-2?real_dx1:nr-2);
					real_dy1=(real_dy1>1?real_dy1:1),real_dy1=(real_dy1<nc-2?real_dy1:nc-2);
					real_dx2=(real_dx2>1?real_dx2:1),real_dx2=(real_dx2<nr-2?real_dx2:nr-2);
					real_dy2=(real_dy2>1?real_dy2:1),real_dy2=(real_dy2<nc-2?real_dy2:nc-2);

					origin_x=(origin_x>1?origin_x:1),origin_x=(origin_x<nr-2?origin_x:nr-2);
					origin_y=(origin_y>1?origin_y:1),origin_y=(origin_y<nc-2?origin_y:nc-2);

					real_dx1=real_dx1-origin_x;
					real_dy1=real_dy1-origin_y;
					real_dx2=real_dx2-origin_x;
					real_dy2=real_dy2-origin_y;
					//ͼ�����ϽǺ����½ǵ�����ڹ���̨���Ͻ�ƫ������


					double x1=0;
					double y1=66*(double)mult_region-1;
					double x2=48*(double)mult_region-1;
					double y2=0;
					//cout<<real_dx1<<","<<real_dy1<<","<<real_dx2<<","<<real_dy2<<endl;
					//cout<<x1<<","<<y1<<","<<x2<<","<<y2<<endl;
					double A=(x1*y2-x2*y1),B=(x2*y1-x1*y2);
					int tran_x,tran_y;
					//ͼ���е������ڹ���̨���Ͻ�ƫ������
					double x,y;
					int ind;

					for(i=0;i<48*mult_region;i++)
					{
						ind=i*66*mult_region;
						for(j=0;j<66*mult_region;j++)
						{
							/*x=i;
							y=j;*/
							a=(i*y2-x2*j)/A;
							b=(i*y1-x1*j)/B;
							tran_x=(int)((a*real_dx1+b*real_dx2)+origin_x);
							tran_y=(int)((a*real_dy1+b*real_dy2)+origin_y);
							tran_x=(tran_x>1?tran_x:1),tran_x=(tran_x<nr-2?tran_x:nr-2);
							tran_y=(tran_y>1?tran_y:1),tran_y=(tran_y<nc-2?tran_y:nc-2);
							Region[ind+j]=Im_pre[tran_x*nc+tran_y];
						}
					}
					//my_imshow_short(Region,48*mult_region,66*mult_region,strcat(aa,Itoa(k,cc,10)),1);
					//itoa(II * 100 + k, name, 10);
					//my_imshow_short(Region, 48 * mult_region, 66 * mult_region, name, 1);
					//���ַ����ض���-1
					for(i=0;i<2;i++)
						for(j=0;j<6;j++)
							Value[i][j]=-1;

					//����ֵ������
					PPP=1;
					//�ָ��ӿ�
					int ind1,ind2;
					for(k2=0;k2<2;k2++)
					{
						//ȡ�ӿ�ͼ��
						for(i=0;i<48*mult_region;i++)
						{
							ind1=i*33*mult_region;
							ind2=i*66*mult_region;
							for(j=0;j<33*mult_region;j++)
							{
								region[ind1+j]=Region[ind2+(j+region_start[k2])];
							}
						}
						//my_imshow_short(region,48*mult_region,33*mult_region,strcat(bb,Itoa(k2,cc,10)),1);
						
						//ʹ��otsu�ָ�ͼ��
						short T = my_otsu(region, 48 * mult_region, 33 * mult_region);
						for(i=0;i<48*mult_region;i++)
						{
							ind1=i*33*mult_region;
							for(j=0;j<33*mult_region;j++)
								region[ind1+j]=(region[ind1+j]>T?1:0);
						}
						/*����otsuͼ��*/
						if (II == 1)
						{
							my_otsu_line_obtain(region, 48 * mult_region, 33 * mult_region, 4 * mult_region, 0);
						}
						//my_imshow_short(region,48*mult_region,33*mult_region,strcat(bb,Itoa(k2,cc,10)),1);
						PPP = my_real_juige(region, 48, 33, mult_region, k2, false_num_limit);
						if(PPP==0)
						{
							//cout<<5;
							break;
						}
						//����ӿ�
						for(i=0;i<6;i++)
						{
							if(k2==0)
							{
							     ind1=(3*mult_region-1+(i+1)*6*mult_region)*33*mult_region+16*mult_region;
							     ind2=(3*mult_region+(i+1)*6*mult_region)*33*mult_region+16*mult_region;
							}
							else
							{
								ind1=(3*mult_region-1+(i+1)*6*mult_region)*33*mult_region+3*mult_region;
							    ind2=(3*mult_region+(i+1)*6*mult_region)*33*mult_region+3*mult_region;
							}
							//�������ѡ��һ��
							block_num0=0,block_num1=0;				
							for(j=0;j<14*mult_region;j++)
							{
								block1[j]=region[ind1+j];
								block2[j]=region[ind2+j];
								block_num0=block_num0+block1[j];
								block_num1=block_num1+block2[j];
							}
							if (block_num0>(block_num1 + 2 * mult_region) || block_num1>(block_num0 + false_num_limit))
							{
								PPP=0;
								break;
							}
							block_num0=(block_num0>7*mult_region?block_num0:14*mult_region-block_num0);
							block_num1=(block_num1>7*mult_region?block_num1:14*mult_region-block_num1);
							if(block_num0>=block_num1)
								block=block1;
							else
								block=block2;
							//������
							/*cout<<endl<<i<<"�У�";
							for(j=0;j<14*mult_region;j++)
							cout<<block[j]<<",";	
							cout<<endl;
							*/

							//��ȡ������Ϣ
							block_num0=0;
							for(j=0;j<14*mult_region;j++)
								block_num0=block_num0+block[j];
							if (block_num0 <= false_num_limit)
								Value[k2][i]=0;
							if (block_num0 >= 14 * mult_region - false_num_limit)
								Value[k2][i]=1;	

							if(Value[k2][i]==-1)
							{
								PPP=0;
								break;
							}
						}
						if(PPP==0)
							break;
					}
					while(1)
					{
						if(PPP==0)
							break;
						//�ж��Ƿ�������
						if(PPP==0)
						{
							//cout<<"���Ʒָ��"<<endl;
							break;
						}
						//ֻ�����½Ǻ����Ͻǿ���Ϊ��ɫ��0��
						if(Value[0][0]+Value[0][5]+Value[1][0]+Value[1][5]!=1)
							PPP=0;
						if(PPP==0)
						{
							//cout<<"û�б�־λ"<<endl;
							break;
						}
						//���ݱ�־λ���ַ���������
						if(Value[0][0]==1)
						{
							for(i=0;i<2;i++)
								for(j=0;j<6;j++)
									Value0[i][j]=1-Value[i][j];
						}
						else if(Value[1][0]==1)
						{
							for(i=0;i<2;i++)
								for(j=0;j<6;j++)
									Value0[i][j]=1-Value[1-i][j];
						}
						else if(Value[0][5]==1)
						{
							for(i=0;i<2;i++)
								for(j=0;j<6;j++)
									Value0[i][j]=1-Value[i][5-j];
						}

						else //if(Value[1][5]==1)
						{
							for(i=0;i<2;i++)
								for(j=0;j<6;j++)
									Value0[i][j]=1-Value[1-i][5-j];
						}
						//ʶ��ʮ��λ
						if(Value0[0][1]!=1)
						{
							PPP=0;
							//cout<<"û�б�־λ2"<<endl;
							break;
						}
						num_Value[0]=Value0[0][2]*4+Value0[0][3]*2+Value0[0][4];
						num_Value[1]=Value0[1][1]*8+Value0[1][2]*4+Value0[1][3]*2+Value0[1][4];


						num_Value[0]=my_num2num1(num_Value[0]);
						num_Value[1]=my_num2num2(num_Value[1]);
						if (num_Value[0]<0||num_Value[0]>5||num_Value[1]<0||num_Value[1]>9)
						{
							PPP=0;
							//cout<<"��Ϻ�����ֳ�����Χ"<<endl;
							break;
						}
						break;
					}

					//ʶ������
					if (PPP==1)
					{
						real_num=num_Value[0]*10+num_Value[1];
						if(real_num<0||real_num>54)
							PPP=0;
					}
					//��ʾ���
					if(PPP==1)
					{
						//���ñ�־λ
						P_jiance = 1;
						Num[real_num]++;
						//cout<<"����Ϊ��"<<real_num<<endl;
						//�����Ƶ���������
						card_spot[real_num][0]=center_x;
						card_spot[real_num][1]=center_y;
						//�ѱ����ĵ�Ҫ��0
						if(II==1)
						{
							pot.pot_p[pot.core_label[k][0]]=0;
							pot.pot_p[pot.core_label[k][1]]=0;
							pot.pot_p[pot.core_label[k][2]]=0;
							pot.pot_p[pot.core_label[k][3]]=0;
						}

						//�����ƵĽ��
						my_save_consequence(nr,nc,Consequence,real_num,&pot.core[k][0],center_x,center_y);


					}
					else
					{
						//cout<<"������"<<endl;
					}

				}
			}

		}
	}

	//	my_imshow_short(Consequence->Iout,nr,nc,"��",1);


	delete Region;
	delete region;
	delete block1;
	delete block2;
	delete []Value;
	delete []Value0;

	//���汾�εļ����
	delete []pot.core_label;
	delete pot.pot_p;
	delete []pot.core;
	delete pot.X1;
	delete pot.Y1;

	delete []card_spot;
	delete Num;
	delete Iout;
	delete Iout_B;


	delete Im2;
	delete Iout2;
	return P_jiance;

}



