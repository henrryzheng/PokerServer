#include "math.h"



int my_num2num1(int im)
{
	int iout;
	if(im>=0&&im<=5)
	{
		switch(im)
		{
		    case 0:
			{
				iout= 0;
				break;
			}
			case 1:
			{
				iout= 1;
				break;
			}
			case 2:
			{
				iout= 2;
				break;
			}
			case 3:
			{
				iout= 3;
				break;
			}
			case 4:
			{
				iout= 4;
				break;
			}
			case 5:
			{
				iout= 5;
				break;
			}
		}

	}
	else
	{
		iout= -1;
	}
	return iout;
}
