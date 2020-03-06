#include <iostream>
#include <string>
#include <algorithm>
#include <cstdio>
#include <cstdlib>
#include <cstring>
using namespace std;
bool compare(int a,int b)
{
    return a>b;
}
bool cover(int a[],int pos,int len,int comnum)
{
   int n=a[pos];
   /*while(n!=1)
            {//如果n变化后等于关键字，说明n即a[pos]覆盖待比较数，返回真
            if(n%2==0)
            {			n/=2;
            if(n==comnum)
                        return true;
            }		else
        	{			n=(3*n+1)/2;
        				if(n==comnum)
                                return true;
            }
            }	return false;*/
   //************
    while(n!=1)
    {
        if(n%2==1)
        {
            n=(3*n+1)/2;
            if(n==comnum)
                return true;
        }
        else
        {
            n=n/2;
            if(n==comnum)
                return true;
        }
        return false;
    }
}
bool judge(int a[],int i,int length)
{
    for(int j=0;j<length;j++)
    {
        if(j!=i)
        {
            if(cover(a,j,length,a[i]));
            return false;
        }
        //return true;
    }
     return true;
}
main()
{
    int k,a[100],b[100];
    cin>>k;
    for(int i=0;i<k;i++)
        cin>>a[i];
    int length=0;
    for(int i=0;i<k;i++)
    {
        if(judge(a,i,k))
        {
            b[length++]=a[i];
        }

    }
    sort(b,b+length,compare);
    for(int i=0;i<length;i++)//
    {
        cout<<b[i];
		if(i!=length-1)
		cout<<' ';



    }
}
