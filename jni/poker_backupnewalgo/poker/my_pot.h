
#ifndef _MY_POT
#define _MY_POT
struct my_pot
{
	//�ǵ�
	double *X1;//�ǵ��x����
	double *Y1;//�ǵ��y����
	int num;//�ǵ����Ŀ
	int  *pot_p;//�ǵ㱻ʶ�������
	int *pot_p_card;

	//�ǵ���
	double (*core)[8];//�ǵ��������
	int  *core_p;//�ǵ����Ƿ���Ȼ��Ч������
	int  (*core_label)[4];//�ǵ����нǵ�����
	int core_num;//�ǵ������Ŀ

};
#endif