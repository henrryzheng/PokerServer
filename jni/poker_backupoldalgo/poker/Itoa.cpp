//#include "stdafx.h"
#include "math.h"
/*
目的：用于将数字转换为字符串输出
*/
char* Itoa(int n, char *p, int m)
{
	int a[56] = {0};
	int i = 0;
	int j = 0;


	if(n==0)
	{
		p[0]=0+48;
		p[1]='\0';
	}
	else
	{
		while(n >=1)
		{
			int nI = n/m;
			a[i++] = n - nI*m;
			n = nI;
		}

		while(i--)
		{
			p[j++] = a[i] + 48;
		}
		p[j] = '\0';
	}

	return p;
}
