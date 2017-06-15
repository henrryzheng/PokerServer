#include "stdafx.h"
#include "math.h"

#include "my_space.h"
/*
Ŀ�ģ������ƵĽ��
˼·;1���ö�ȡ������������Ϊ1
2��������4���ߵ�4���
3��������k��b����Ϊ90�ȣ�k����Ϊ1000
4�������Ƶ�����λ��
*/
/*
int *card_p;//�Ƶ����ԣ��Ƿ��Ѿ������Ź�
double (*card_pro_pot)[4][4];//�Ƶ�λ�����ԣ�4���ߵ�4���(ÿ�����2������)
double (*card_pro_line)[4][2];//�Ƶ�λ�����ԣ�4���ߵ�4��ֱ��(ÿ��ֱ������������)
double (*card_pro_center)[2];//�Ƶ�λ�����ԣ��Ƶ�����
int (*card_para)[55];//���Ƶķ�����
int *card_para_num;//�Ƶķֶ���
*/
void my_save_consequence(int nr,int nc,my_space*Consequence,int real_num,double *pot,double center_x,double center_y)
{


	if(real_num<0||real_num>54)
		return;

	int i,j;
	//1���ö�ȡ������������Ϊ1
	Consequence->card_p[real_num]=1;
	//2��������4���ߵ�4���
	//1-2
	double mult1=(double)10/36 ,mult2=(double)10/66;
	Consequence->card_pro_pot[real_num][0][0]=pot[0]+mult2*(pot[0]-pot[6])+mult1*(pot[0]-pot[2]);
	Consequence->card_pro_pot[real_num][0][1]=pot[1]+mult2*(pot[1]-pot[7])+mult1*(pot[1]-pot[3]);
	Consequence->card_pro_pot[real_num][0][2]=pot[2]+mult1*(pot[2]-pot[0])+mult2*(pot[2]-pot[4]);
	Consequence->card_pro_pot[real_num][0][3]=pot[3]+mult1*(pot[3]-pot[1])+mult2*(pot[3]-pot[5]);
	Consequence->card_pro_pot[real_num][0][4]=(Consequence->card_pro_pot[real_num][0][0]+
		Consequence->card_pro_pot[real_num][0][2])/2;
	Consequence->card_pro_pot[real_num][0][5]=(Consequence->card_pro_pot[real_num][0][1]+
		Consequence->card_pro_pot[real_num][0][3])/2;

	//2-3
	Consequence->card_pro_pot[real_num][1][0]=pot[2]+mult1*(pot[2]-pot[0])+mult2*(pot[2]-pot[4]);
	Consequence->card_pro_pot[real_num][1][1]=pot[3]+mult1*(pot[3]-pot[1])+mult2*(pot[3]-pot[5]);
	Consequence->card_pro_pot[real_num][1][2]=pot[4]+mult2*(pot[4]-pot[2])+mult1*(pot[4]-pot[6]);
	Consequence->card_pro_pot[real_num][1][3]=pot[5]+mult2*(pot[5]-pot[3])+mult1*(pot[5]-pot[7]);
	Consequence->card_pro_pot[real_num][1][4]=(Consequence->card_pro_pot[real_num][1][0]+
		Consequence->card_pro_pot[real_num][1][2])/2;
	Consequence->card_pro_pot[real_num][1][5]=(Consequence->card_pro_pot[real_num][1][1]+
		Consequence->card_pro_pot[real_num][1][3])/2;
	//3-4
	Consequence->card_pro_pot[real_num][2][0]=pot[4]+mult2*(pot[4]-pot[2])+mult1*(pot[4]-pot[6]);
	Consequence->card_pro_pot[real_num][2][1]=pot[5]+mult2*(pot[5]-pot[3])+mult1*(pot[5]-pot[7]);
	Consequence->card_pro_pot[real_num][2][2]=pot[6]+mult1*(pot[6]-pot[4])+mult2*(pot[6]-pot[0]);
	Consequence->card_pro_pot[real_num][2][3]=pot[7]+mult1*(pot[7]-pot[5])+mult2*(pot[7]-pot[1]);
	Consequence->card_pro_pot[real_num][2][4]=(Consequence->card_pro_pot[real_num][2][0]+
		Consequence->card_pro_pot[real_num][2][2])/2;
	Consequence->card_pro_pot[real_num][2][5]=(Consequence->card_pro_pot[real_num][2][1]+
		Consequence->card_pro_pot[real_num][2][3])/2;
	//4-1
	Consequence->card_pro_pot[real_num][3][0]=pot[6]+mult1*(pot[6]-pot[4])+mult2*(pot[6]-pot[0]);
	Consequence->card_pro_pot[real_num][3][1]=pot[7]+mult1*(pot[7]-pot[5])+mult2*(pot[7]-pot[1]);
	Consequence->card_pro_pot[real_num][3][2]=pot[0]+mult2*(pot[0]-pot[6])+mult1*(pot[0]-pot[2]);
	Consequence->card_pro_pot[real_num][3][3]=pot[1]+mult2*(pot[1]-pot[7])+mult1*(pot[1]-pot[3]);
	Consequence->card_pro_pot[real_num][3][4]=(Consequence->card_pro_pot[real_num][3][0]+
		Consequence->card_pro_pot[real_num][3][2])/2;
	Consequence->card_pro_pot[real_num][3][5]=(Consequence->card_pro_pot[real_num][3][1]+
		Consequence->card_pro_pot[real_num][3][3])/2;

	// 3��������k��b����Ϊ90�ȣ�k����Ϊ1000
	for(i=0;i<4;i++)
	{
		//��k
		if(Consequence->card_pro_pot[real_num][i][0]==Consequence->card_pro_pot[real_num][i][2])
			Consequence->card_pro_line[real_num][i][0]=1000;
		else
			Consequence->card_pro_line[real_num][i][0]=
			(Consequence->card_pro_pot[real_num][i][3]-Consequence->card_pro_pot[real_num][i][1])/
			(Consequence->card_pro_pot[real_num][i][2]-Consequence->card_pro_pot[real_num][i][0]);
		//��b
		Consequence->card_pro_line[real_num][i][1]=Consequence->card_pro_pot[real_num][i][1]-
			Consequence->card_pro_line[real_num][i][0]*Consequence->card_pro_pot[real_num][i][0];
	}
	//4�������Ƶ�����λ��
	Consequence->card_pro_center[real_num][0]=center_x;
	Consequence->card_pro_center[real_num][1]=center_y;



}