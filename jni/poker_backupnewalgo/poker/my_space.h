
#ifndef _MY_SPACE
#define _MY_SPACE
struct my_space
{
	int mult_x,mult_y;
	//��������
	int *card_p_old;//�Ƶľ����ԣ����μ��֮ǰ������
	int *card_p;//�Ƶ����ԣ��Ƿ��Ѿ������Ź������μ��Ľ����

	//�ѷ��Ƶ�������
	double (*card_pro_pot)[4][6];//�Ƶ�λ�����ԣ�4���ߵ�4���(ÿ�����2������)
	double (*card_pro_line)[4][2];//�Ƶ�λ�����ԣ�4���ߵ�4��ֱ��(ÿ��ֱ������������)
	double (*card_pro_center)[2];//�Ƶ�λ�����ԣ��Ƶ�����

	//�ƾ��������
	int para_num;//�Ƶķֶ���
	int *card_para_num;//ÿ���Ƶ���Ŀ
	int (*card_para)[55];//���Ƶķ�����	
	double (*card_para_center)[2];//ÿ���Ƶ�����

	//���Ƶ�����
	int new_card_num;//���Ƶ���Ŀ
	int *new_card;//����
	//���µ���
	int newst_card;
		
};
#endif