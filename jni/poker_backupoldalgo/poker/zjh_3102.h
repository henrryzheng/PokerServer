#ifndef __ZJH__
#define __ZJH__

struct zjh_card{
	unsigned int Value;
	unsigned int Hs;
};

struct zjh_rlt{
	unsigned int first;
	unsigned int second;
};

struct zjh_rlt ZJH_3102_Call(int pepole,volatile zjh_card * cards);

#endif
