#include "math.h"


//目的：使用弱分类器组合成强分类器的原理判断出角点
void my_core_juige(short*Imm,short *CC,int nr,int nc)
{
	int i,j,i2,j2;
	int Imax;
	int LL,L;
	short im;
	short h;
	double mult,num1,num2;
	double Xcenter1=0,Ycenter1=0,Xcenter2=0,Ycenter2=0;
	double d=0.5;
	double right_per180,right_per90,right_per270;
	//图像寻址
	short **Im=new short *[nr];
	Im[0]=Imm;
	for(i=1;i<nr;i++)
		Im[i]=Im[i-1]+nc;
	/*//将角点图像置0
	Imax=nr*nc-1;
	for(i=0;i<Imax;i++)
		CC[i]=1;*/
	//
	short**C=new short*[nr];
	C[0]=CC;
	for(i=1;i<nr;i++)
		C[i]=C[i-1]+nc;

	//

		//DWORD start_time,end_time0,end_time1,end_time2,end_time3,end_time4,end_time5;
	//start_time=GetTickCount();
	short Min[4],Max[4];
	LL=4;
	for(i=LL;i<nr-LL;i++)
	{
		for(j=LL;j<nc-LL;j++)
		{
			if(C[i][j]>0)
			{
				C[i][j]=0;
				i2=2;

				/*Min[0]=(Im[i+i2][j]<Im[i-i2][j]?Im[i+i2][j]:Im[i-i2][j]);
				Min[1]=(Im[i][j+i2]<Im[i][j-i2]?Im[i][j+i2]:Im[i][j-i2]);
				Min[2]=(Im[i+i2][j+i2]<Im[i-i2][j-i2]?Im[i+i2][j+i2]:Im[i-i2][j-i2]);
				Min[3]=(Im[i+i2][j-i2]<Im[i-i2][j+i2]?Im[i+i2][j-i2]:Im[i-i2][j+i2]);

				Max[0]=(Im[i+i2][j]>Im[i-i2][j]?Im[i+i2][j]:Im[i-i2][j]);
				Max[1]=(Im[i][j+i2]>Im[i][j-i2]?Im[i][j+i2]:Im[i][j-i2]);
				Max[2]=(Im[i+i2][j+i2]>Im[i-i2][j-i2]?Im[i+i2][j+i2]:Im[i-i2][j-i2]);
				Max[3]=(Im[i+i2][j-i2]>Im[i-i2][j+i2]?Im[i+i2][j-i2]:Im[i-i2][j+i2]);*/

				if(Im[i+i2][j]<Im[i-i2][j])
				{
					Min[0]=Im[i+i2][j];
					Max[0]=Im[i-i2][j];
				}
				else
				{
					Max[0]=Im[i+i2][j];
					Min[0]=Im[i-i2][j];
				}
				if(Im[i][j+i2]<Im[i][j-i2])
				{
					Min[1]=Im[i][j+i2];
					Max[1]=Im[i][j-i2];
				}
				else
				{
					Max[1]=Im[i][j+i2];
					Min[1]=Im[i][j-i2];
				}
				if(Im[i+i2][j+i2]<Im[i-i2][j-i2])
				{
					Min[2]=Im[i+i2][j+i2];
					Max[2]=Im[i-i2][j-i2];
				}
				else
				{
					Max[2]=Im[i+i2][j+i2];
					Min[2]=Im[i-i2][j-i2];
				}
				if(Im[i+i2][j-i2]<Im[i-i2][j+i2])
				{
					Min[3]=Im[i+i2][j-i2];
					Max[3]=Im[i-i2][j+i2];

				}
				else
				{
					Max[3]=Im[i+i2][j-i2];
					Min[3]=Im[i-i2][j+i2];
				}
				im=Im[i][j];
				if(!((Max[1]<im&&im<Min[0])||(Max[0]<im&&im<Min[1])||(Max[2]<im&&im<Min[3])||(Max[3]<im&&im<Min[2])))
				{
					continue;
				}


				i2++;
				//while(1)
				while(i2<=3&&((Max[1]<im&&im<Min[0])||(Max[0]<im&&im<Min[1])||(Max[2]<im&&im<Min[3])||(Max[3]<im&&im<Min[2])))
					//while(i2<2&&((Max[1]<Min[0])||(Max[0]<Min[1])||(Max[2]<Min[3])||(Max[3]<Min[2])))
				{

					if(Im[i+i2][j]<Im[i-i2][j])
					{
						Min[0]=(Min[0]<Im[i+i2][j]?Min[0]:Im[i+i2][j]);
						Max[0]=(Max[0]>Im[i-i2][j]?Max[0]:Im[i-i2][j]);
					}
					else
					{
						Min[0]=(Min[0]<Im[i-i2][j]?Min[0]:Im[i-i2][j]);
						Max[0]=(Max[0]>Im[i+i2][j]?Max[0]:Im[i+i2][j]);
					}
					if(Im[i][j+i2]<Im[i][j-i2])
					{
						Min[1]=(Min[1]<Im[i][j+i2]?Min[1]:Im[i][j+i2]);
						Max[1]=(Max[1]>Im[i][j-i2]?Max[1]:Im[i][j-i2]);
					}
					else
					{
						Min[1]=(Min[1]<Im[i][j-i2]?Min[1]:Im[i][j-i2]);
						Max[1]=(Max[1]>Im[i][j+i2]?Max[1]:Im[i][j+i2]);
					}
					if(Im[i+i2][j+i2]<Im[i-i2][j-i2])
					{
						Min[2]=(Min[2]<Im[i+i2][j+i2]?Min[2]:Im[i+i2][j+i2]);
						Max[2]=(Max[2]>Im[i-i2][j-i2]?Max[2]:Im[i-i2][j-i2]);
					}
					else
					{
						Min[2]=(Min[2]<Im[i-i2][j-i2]?Min[2]:Im[i-i2][j-i2]);
						Max[2]=(Max[2]>Im[i+i2][j+i2]?Max[2]:Im[i+i2][j+i2]);
					}
					if(Im[i+i2][j-i2]<Im[i-i2][j+i2])
					{
						Min[3]=(Min[3]<Im[i+i2][j-i2]?Min[3]:Im[i+i2][j-i2]);
						Max[3]=(Max[3]>Im[i-i2][j+i2]?Max[3]:Im[i-i2][j+i2]);

					}
					else
					{
						Min[3]=(Min[3]<Im[i-i2][j+i2]?Min[3]:Im[i-i2][j+i2]);
						Max[3]=(Max[3]>Im[i+i2][j-i2]?Max[3]:Im[i+i2][j-i2]);
					}
					//cout<<i2<<'\t';
					i2++;

				}
				im=Im[i][j];
				if((Max[1]<im&&im<Min[0])||(Max[0]<im&&im<Min[1])||(Max[2]<im&&im<Min[3])||(Max[3]<im&&im<Min[2]))
				{
					C[i][j]=1;
				}

			}
		}
	}/**/
	//	end_time1=GetTickCount();
	//cout<<"end0="<<end_time1-start_time<<endl;
	int (*Num)[3]=new int[3][3];
	int (*Num_LL)=new int[10];
	int Indi2,Indj2,Ind;
	int T_sum,T;
	//使用3*3的矩阵求取进行角点判断,大小符号的变化次数

	for(LL=4;LL<=4;LL=LL+2)
	{
		L=2*LL+1;

		//角点判断
		short *HH=new short[L*L];
		short **H=new short*[L];
		H[0]=HH;
		for(i=1;i<L;i++)
			H[i]=H[i-1]+L;
		//cout<<endl;

		double Lsum=(L*L);
		for(i=4;i<nr-4;i++)
		{
			for(j=4;j<nc-4;j++)
			{
				if(C[i][j]>0)
				{

					///////////////////////////////////////获取差值矩阵//////////////////////////////////////
					//质心与树木
					Xcenter1=0;
					Ycenter1=0;
					Xcenter2=0;
					Ycenter2=0;
					num1=0,num2=0;
					d=1;

					//平均数
					im=0;
					for( i2=-LL;i2<=LL;i2++)
					{
						for( j2=-LL;j2<=LL;j2++)
						{
							im=im+Im[i+i2][j+j2];
						}
					}

					im=im/Lsum;
					//分割
					for( i2=-LL;i2<=LL;i2++)
					{
						for( j2=-LL;j2<=LL;j2++)
						{
							H[i2+LL][j2+LL]=0;
							H[i2+LL][j2+LL]=(Im[i+i2][j+j2]>im?1:-1);
							if(H[i2+LL][j2+LL]<0)
							{
								num1++;
								Xcenter1=Xcenter1+(double)i2;
								Ycenter1=Ycenter1+(double)j2;
							}
							if(H[i2+LL][j2+LL]>0)
							{
								num2++;
								Xcenter2=Xcenter2+(double)i2;
								Ycenter2=Ycenter2+(double)j2;
							}


						}
					}



					///////////////////////////////////////次数比例//////////////////////////////////////
					mult = 1.5;
					if((num1/num2>mult)||(num2/num1>mult))
					{
						C[i][j]=0;
						continue;
					}

					///////////////////////////////////////正负号的中心//////////////////////////////////////

					Xcenter1=Xcenter1/num1;
					Ycenter1=Ycenter1/num1;
					Xcenter2=Xcenter2/num2;
					Ycenter2=Ycenter2/num2;
					Xcenter1=(Xcenter1>=0?Xcenter1:-Xcenter1);
					Ycenter1=(Ycenter1>=0?Ycenter1:-Ycenter1);
					Xcenter2=(Xcenter2>=0?Xcenter2:-Xcenter2);
					Ycenter2=(Ycenter2>=0?Ycenter2:-Ycenter2);

					if(Xcenter1>d||Ycenter1>d||Xcenter2>d||Ycenter2>d)
					{
						C[i][j]=0;
						continue;
					}

					/////////////////////////////////////////分层数///////////////////////////////////////
					//分层数
					for(i2=0;i2<=LL;i2++)
						Num_LL[i2]=0;
					//分层数
					for(i2=-LL;i2<=LL;i2++)
					{
						for( j2=-LL;j2<=LL;j2++)
						{
							if(H[i2+LL][j2+LL]<0)
							{
								Indi2=(i2>=0?i2:-i2);
								Indj2=(j2>=0?j2:-j2);
								Ind=(Indi2>Indj2?Indi2:Indj2);
								Num_LL[Ind]++;
							}
						}
					}
					for(i2=2;i2<=LL;i2++)
					{
						T=(4*(2*i2+1)-3)/4;
						T_sum=(4*(2*i2+1)-3)-T;
						/*if(Num_LL[i2]<T||Num_LL[i2]>T_sum||(Num_LL[i2]-Num_LL[i2-1])<2)
						{
							C[i][j]=0;
							break;
						}*/
						if (Num_LL[i2]<T || Num_LL[i2]>T_sum || (Num_LL[i2] - Num_LL[i2 - 1])<1)
						{
							C[i][j] = 0;
							break;
						}
					}
					if (Num_LL[LL]<2 * LL + 1 + LL || Num_LL[LL]>(4 * (2 * LL + 1) - 3 - (2 * LL + 1 + LL)))
						if (C[i][j] == 0)
							continue;
					if(C[i][j]==0)
						continue;
					/**/
					///////////////////////////////////////转动180度相似度>70,转动90度相似度<30//////////////////////////////////////
					right_per180=0;
					right_per90=0;
					right_per270=0;

					for(i2=-LL;i2<=LL;i2++)
					{
						for( j2=-LL;j2<=LL;j2++)
						{
							if(H[i2+LL][j2+LL]==H[LL-i2][LL-j2])
								right_per180++;

							if(H[i2+LL][j2+LL]==H[-j2+LL][i2+LL])
								right_per90++;
							if(H[i2+LL][j2+LL]==H[j2+LL][-i2+LL])
								right_per270++;
						}
					}

					if(right_per180<right_per90||right_per180<right_per270||right_per180<Lsum*0.5||right_per90>Lsum*0.5||right_per270>Lsum*0.5)
					{
						C[i][j]=0;
						continue;
					}








					/*	///////////////////////////////////////中间部分属于极小区域//////////////////////////////////////
					for(i2=0;i2<=2;i2++)
					{
					for(j2=0;j2<=2;j2++)
					{
					Num[i2][j2]=0;
					}
					}
					for(i2=-1;i2<=1;i2++)
					{
					for(j2=-1;j2<=1;j2++)
					{
					Num[0][0]=(H[1+i2][1+j2]==1?Num[0][0]+1:Num[0][0]);
					Num[0][1]=(H[1+i2][4+j2]==1?Num[0][1]+1:Num[0][1]);
					Num[0][2]=(H[1+i2][7+j2]==1?Num[0][2]+1:Num[0][2]);
					Num[1][0]=(H[4+i2][1+j2]==1?Num[1][0]+1:Num[1][0]);
					Num[1][1]=(H[4+i2][4+j2]==1?Num[1][1]+1:Num[1][1]);
					Num[1][2]=(H[4+i2][7+j2]==1?Num[1][2]+1:Num[1][2]);
					Num[2][0]=(H[7+i2][1+j2]==1?Num[2][0]+1:Num[2][0]);
					Num[2][1]=(H[7+i2][4+j2]==1?Num[2][1]+1:Num[2][1]);
					Num[2][2]=(H[7+i2][7+j2]==1?Num[2][2]+1:Num[2][2]);
					//cout<<H[7+i2][7+j2]<<'\t';
					}
					}
					Min[0]=(Num[1][0]<Num[1][2]?Num[1][0]:Num[1][2]);
					Min[1]=(Num[0][1]<Num[2][1]?Num[0][1]:Num[2][1]);
					Min[2]=(Num[0][0]<Num[2][2]?Num[0][0]:Num[2][2]);
					Min[3]=(Num[0][2]<Num[2][0]?Num[0][2]:Num[2][0]);

					Max[0]=(Num[1][0]>Num[1][2]?Num[1][0]:Num[1][2]);
					Max[1]=(Num[0][1]>Num[2][1]?Num[0][1]:Num[2][1]);
					Max[2]=(Num[0][0]>Num[2][2]?Num[0][0]:Num[2][2]);
					Max[3]=(Num[0][2]>Num[2][0]?Num[0][2]:Num[2][0]);

					im=Num[1][1];
					C[i][j]=0;
					int num_d=1;
					if((Max[1]<im-num_d&&im+num_d<Min[0])||(Max[0]<im-num_d&&im+num_d<Min[1])
					||(Max[2]<im-num_d&&im+num_d<Min[3])||(Max[3]<im-num_d&&im+num_d<Min[2]))
					{
					//cout<<"("<<Min[0]<<","<<Min[1]<<","<<Min[2]<<","<<Min[3]<<","<<")"<<'\t';
					//cout<<"("<<Max[0]<<","<<Max[1]<<","<<Max[2]<<","<<Max[3]<<","<<")"<<'\t';
					//cout<<endl;
					//cout<<"("<<i<<","<<j<<")"<<'\t';
					C[i][j]=1;
					}

					*/
					/*
					false_per180=0;
					false_per90=0;
					false_per270=0;
					for(i2=-LL;i2<=LL;i2++)
					{
					for( j2=-LL;j2<=LL;j2++)
					{
					H[i2+LL][j2+LL]=Im[i+i2][j+j2];
					}
					}
					for(i2=-LL;i2<=LL;i2++)
					{
					for( j2=-LL;j2<=LL;j2++)
					{

					false_per180=false_per180+
					(H[i2+LL][j2+LL]>H[LL-i2][LL-j2]?(H[i2+LL][j2+LL]-H[LL-i2][LL-j2]):(H[LL-i2][LL-j2]-H[i2+LL][j2+LL]));

					false_per90=false_per90+
					(H[i2+LL][j2+LL]>H[-j2+LL][i2+LL]?(H[i2+LL][j2+LL]-H[-j2+LL][i2+LL]):(H[-j2+LL][i2+LL]-H[i2+LL][j2+LL]));

					false_per270=false_per270+
					(H[i2+LL][j2+LL]>H[j2+LL][-i2+LL]?(H[i2+LL][j2+LL]-H[j2+LL][-i2+LL]):(H[j2+LL][-i2+LL]-H[i2+LL][j2+LL]));
					}
					}
					//cout<<false_per180<<","<<false_per90<<endl;

					if(false_per180>false_per90/2||false_per180>false_per270/2)
					{
					CC[i*nc+j]=0;
					continue;
					}
					/**/
				}/**/


			}

		}
		delete HH;
		delete H;
	}
	delete Im;
	delete C;
	delete []Num;
	delete []Num_LL;
			//end_time2=GetTickCount();
	//cout<<"end00="<<end_time2-end_time1<<endl;/**/
}
