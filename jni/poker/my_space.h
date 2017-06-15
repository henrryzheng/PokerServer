
#ifndef _MY_SPACE
#define _MY_SPACE
struct my_space
{
	int mult_x,mult_y;
	//发牌属性
	int *card_p_old;//牌的旧属性，本次检测之前的属性
	int *card_p;//牌的属性，是否已经被发放过（本次检测的结果）

	//已发牌的牌属性
	double (*card_pro_pot)[4][6];//牌的位置属性：4条边的4组角(每组角有2对坐标)
	double (*card_pro_line)[4][2];//牌的位置属性：4条边的4条直线(每条直线有两个属性)
	double (*card_pro_center)[2];//牌的位置属性：牌的中心

	//牌聚类的属性
	int para_num;//牌的分段数
	int *card_para_num;//每类牌的数目
	int (*card_para)[55];//对牌的分类结果	
	double (*card_para_center)[2];//每类牌的中心

	//新牌的属性
	int new_card_num;//新牌的数目
	int *new_card;//新牌
	//最新的牌
	int newst_card;
		
};
#endif