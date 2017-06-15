#include "zjh_3102.h"
#include <android/log.h>



unsigned int isInsideStraight(zjh_card * cards);//�ж��Ƿ�Ϊ˳��
void cardsOrder(volatile zjh_card * cardsx);//һ���˿�ֵ����
void zjh_bubble(volatile unsigned int *cards_v,volatile unsigned int *card_b,volatile int nn);//�����㷨
#ifndef  _mylog_
#define  _mylog_
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, "ProjectName", __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG , "ProjectName", __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO  , "ProjectName", __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN  , "ProjectName", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , "ProjectName", __VA_ARGS__)
#endif
//��һ���˿�ת������ֵ
unsigned int cards2val(volatile zjh_card * cards){
	unsigned int cards_val=0;
	zjh_card mycards[3];//���¶���һ����ʱ��
	mycards[0].Value=cards[0].Value;
	mycards[1].Value=cards[1].Value;
	mycards[2].Value=cards[2].Value;

	mycards[0].Hs=cards[0].Hs;
	mycards[1].Hs=cards[1].Hs;
	mycards[2].Hs=cards[2].Hs;


	cardsOrder(mycards);//�˿�ֵ���򣬴�С����
	LOGI("mycards[0].Value=%d",mycards[0].Value);
	LOGI("mycards[1].Value=%d",mycards[1].Value);
	LOGI("mycards[2].Value=%d",mycards[2].Value);
	LOGI("mycards[0].Hs=%d",mycards[0].Hs);
	LOGI("mycards[1].Hs=%d",mycards[1].Hs);
	LOGI("mycards[2].Hs=%d",mycards[2].Hs);
	//�ж�����ͷ
	if((mycards[0].Value==mycards[1].Value)&&(mycards[1].Value==mycards[2].Value)&&(mycards[0].Value==mycards[2].Value))//�������ֵһ��
	{
		if(mycards[0].Value==1)
			cards_val=10000000+14; //���������A
		else{
			cards_val=10000000+mycards[0].Value;//���������A֮�������ֵһ��
		}
		return cards_val;
	}

	//�жϽ�
	if((mycards[0].Hs==mycards[1].Hs)&&(mycards[1].Hs==mycards[2].Hs)&&(mycards[0].Hs==mycards[2].Hs)){
	
	   if((mycards[0].Value==1)&&(mycards[1].Value==12)&&(mycards[2].Value==13))//�����ͬ��QKA
	   {
			cards_val = 9500000+mycards[0].Hs;

			return cards_val;
	   }

	   if(isInsideStraight(mycards)){
		   if((mycards[0].Value==1)&&(mycards[1].Value==2)&&(mycards[2].Value==3))//�����ͬ��A23
		   {
				cards_val = 9400000+mycards[0].Hs;

				return cards_val;
		   }

		   cards_val=9300000+(mycards[0].Value+mycards[1].Value+mycards[2].Value)*100 + mycards[0].Hs;//ͬ��˳
		   return cards_val;

	   }
	   //���������ͬ��
		if(mycards[0].Value==1)
		{
			cards_val = 9200000 + mycards[0].Hs;
		}else{
			cards_val = 9100000 + mycards[2].Value*100 +  mycards[2].Hs;//����ƵĻ�ɫ����ֵ
		}
		return cards_val;

	}


	//�ж�˳��
	if((mycards[0].Value==1)&&(mycards[1].Value==12)&&(mycards[2].Value==13))//����Ƿ�ͬ��QKA
   {
		cards_val = 8700000+mycards[0].Hs;//�ж������A�Ļ�ɫ

		return cards_val;
   }

   if(isInsideStraight(mycards)){
	   if((mycards[0].Value==1)&&(mycards[1].Value==2)&&(mycards[2].Value==3))//����Ƿ�ͬ��A23
	   {
			cards_val = 8600000+mycards[0].Hs;//�ж������A�Ļ�ɫ

			return cards_val;
	   }

	   cards_val=8500000+(mycards[0].Value+mycards[1].Value+mycards[2].Value)*100 + mycards[2].Hs;//��ͬ��˳,���һ�����ж�����ƻ�ɫ
	   return cards_val;
	
   }

   //�ж϶���
	if((mycards[0].Value==mycards[1].Value)||(mycards[1].Value==mycards[2].Value))//���������ֵһ��
	{
		if(mycards[0].Value==mycards[1].Value)
		{

			if(mycards[0].Value==1)//����Ƕ�A
				cards_val = 8400000+mycards[2].Value*100+mycards[2].Hs;
			else{//������Ƕ�A
				if(mycards[2].Value==1)//�����һ������A
					//cards_val = 830000 + mycards[2].Hs;
					cards_val = 6000000 +  mycards[0].Value*10000 + 50*100+mycards[2].Hs;
				else
					cards_val = 6000000 +  mycards[0].Value*10000 + mycards[2].Value*100+mycards[2].Hs;
			}
			return cards_val;
		}
		if(mycards[1].Value==mycards[2].Value)
		{

			if(mycards[1].Value==1)//����Ƕ�A
				cards_val = 8400000+mycards[0].Value*100+mycards[0].Hs;
			else{//������Ƕ�A
				if(mycards[0].Value==1)//�����һ������A
					//cards_val = 830000 + mycards[0].Hs;
					cards_val = 6000000  +  mycards[1].Value*10000 + 50*100+mycards[0].Hs;
				else
					cards_val = 6000000  +  mycards[1].Value*10000 + mycards[0].Value*100+mycards[0].Hs;
			}
			return cards_val;
		}
		
	}

	//���������
	/*if(mycards[0].Value==1)
	{
		cards_val = 500000 + mycards[0].Hs;
	}else{
		cards_val = 400000 + mycards[2].Value*1000 +  mycards[2].Hs;//����ƵĻ�ɫ����ֵ
	}*/
	
	if(mycards[0].Value==1)//��A�����
		{
			cards_val = 5000000 + mycards[0].Hs+mycards[2].Value*1000 +mycards[1].Value*10;
		}else{
			cards_val = 4000000 + mycards[2].Value*10000 + mycards[1].Value*500+mycards[0].Value*10+ mycards[2].Hs;//����ƵĻ�ɫ����ֵ
		}

	return cards_val;
}

//ֵ��С��������

void cardsOrder(volatile zjh_card * cardsx){
	volatile unsigned int tempval1=0;
	volatile unsigned int tempval2=0;
	volatile unsigned int tempval3=0;
	volatile unsigned int temphs1=0;
	volatile unsigned int temphs2=0;
	volatile unsigned int temphs3=0;
	if(cardsx[0].Value>cardsx[1].Value )
	{
		tempval1 = cardsx[0].Value;
		cardsx[0].Value = cardsx[1].Value;
		cardsx[1].Value =tempval1;

		temphs1 = cardsx[0].Hs;
		cardsx[0].Hs = cardsx[1].Hs;
		cardsx[1].Hs = temphs1;
	}

	if(cardsx[1].Value>cardsx[2].Value )
	{
		tempval2 = cardsx[1].Value;//
		cardsx[1].Value = cardsx[2].Value;
		cardsx[2].Value =tempval2;

		temphs2 = cardsx[1].Hs;
		cardsx[1].Hs = cardsx[2].Hs;
		cardsx[2].Hs = temphs2;
	}

	if(cardsx[0].Value>cardsx[1].Value )
	{
		tempval3 = cardsx[0].Value;
		cardsx[0].Value = cardsx[1].Value;
		cardsx[1].Value =tempval3;

		temphs3 = cardsx[0].Hs;
		cardsx[0].Hs = cardsx[1].Hs;
		cardsx[1].Hs = temphs3;
	}

}

unsigned int isInsideStraight(zjh_card * cards){

		if((cards[2].Value-cards[1].Value)==1)
		{
			if((cards[1].Value-cards[0].Value)==1)
				return 1;
		}

		return 0;

}

struct zjh_rlt ZJH_3102_Call(int pepole,volatile zjh_card * cards){

	struct zjh_rlt my_rlt;
	unsigned int cards_valuep[20];//���������
	unsigned int cards_xiabiao[20];
	if(pepole<18){

	  for(int ib=0;ib<pepole;ib++)
	  {
			LOGI("mycards[0].Value=%d",cards[0+ib*3].Value);
			LOGI("mycards[1].Value=%d",cards[1+ib*3].Value);
			LOGI("mycards[2].Value=%d",cards[2+ib*3].Value);
			LOGI("mycards[0].Hs=%d",cards[0+ib*3].Hs);
			LOGI("mycards[1].Hs=%d",cards[1+ib*3].Hs);
			LOGI("mycards[2].Hs=%d",cards[2+ib*3].Hs);

			cards_valuep[ib]=cards2val(&cards[ib*3]);
			cards_xiabiao[ib]=ib+1;//��������
			LOGI("cards_valuep[%d]=%d",ib,cards_valuep[ib]);
	  }
	}

	zjh_bubble(cards_valuep,cards_xiabiao,pepole);

	my_rlt.first=cards_xiabiao[pepole-1];

	my_rlt.second=cards_xiabiao[pepole-2];
	return my_rlt;
}

void zjh_bubble(volatile unsigned int *cards_v,volatile unsigned int *card_b,volatile int nn){

	int i,j,temp;
	for(i=0;i<nn-1;i++)
		for(j=i+1;j<nn;j++){
			if(cards_v[i]>cards_v[j])
			{
				temp = cards_v[i];
				cards_v[i]=cards_v[j];
				cards_v[j]=temp;

				temp = card_b[i];
				card_b[i]=card_b[j];
				card_b[j]=temp;
			}

		}

}
