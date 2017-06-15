
#ifndef _MY_POT
#define _MY_POT
typedef struct my_pot
{
	double *X1;
	double *Y1;
	int num;

	double (*core)[8];
	int  (*core_label)[4];
	int  *pot_p;
	int core_num;

};
#endif