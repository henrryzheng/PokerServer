
#ifndef _MY_POT
#define _MY_POT
struct my_pot
{
	//角点
	double *X1;//角点的x坐标
	double *Y1;//角点的y坐标
	int num;//角点的数目
	int  *pot_p;//角点被识别的属性
	int *pot_p_card;

	//角点组
	double (*core)[8];//角点组的坐标
	int  *core_p;//角点组是否仍然有效的属性
	int  (*core_label)[4];//角点组中角点的序号
	int core_num;//角点组的数目

};
#endif