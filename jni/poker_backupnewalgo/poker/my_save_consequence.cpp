#include "stdafx.h"
#include "math.h"

#include "my_space.h"
/*
目的：保存牌的结果
思路;1、置读取到的数字属性为1
2、保存其4条边的4组点
3、保存其k和b，若为90度，k保存为1000
4、保存牌的中心位置
*/
/*
int *card_p;//牌的属性，是否已经被发放过
double (*card_pro_pot)[4][4];//牌的位置属性：4条边的4组角(每组角有2对坐标)
double (*card_pro_line)[4][2];//牌的位置属性：4条边的4条直线(每条直线有两个属性)
double (*card_pro_center)[2];//牌的位置属性：牌的中心
int (*card_para)[55];//对牌的分类结果
int *card_para_num;//牌的分段数
*/
void my_save_consequence(int nr,int nc,my_space*Consequence,int real_num,double *pot,double center_x,double center_y)
{


	if(real_num<0||real_num>54)
		return;

	int i,j;
	//1、置读取到的数字属性为1
	Consequence->card_p[real_num]=1;
	//2、保存其4条边的4组点
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

	// 3、保存其k和b，若为90度，k保存为1000
	for(i=0;i<4;i++)
	{
		//求k
		if(Consequence->card_pro_pot[real_num][i][0]==Consequence->card_pro_pot[real_num][i][2])
			Consequence->card_pro_line[real_num][i][0]=1000;
		else
			Consequence->card_pro_line[real_num][i][0]=
			(Consequence->card_pro_pot[real_num][i][3]-Consequence->card_pro_pot[real_num][i][1])/
			(Consequence->card_pro_pot[real_num][i][2]-Consequence->card_pro_pot[real_num][i][0]);
		//求b
		Consequence->card_pro_line[real_num][i][1]=Consequence->card_pro_pot[real_num][i][1]-
			Consequence->card_pro_line[real_num][i][0]*Consequence->card_pro_pot[real_num][i][0];
	}
	//4、保存牌的中心位置
	Consequence->card_pro_center[real_num][0]=center_x;
	Consequence->card_pro_center[real_num][1]=center_y;



}