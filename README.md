# 面试题收集

## JavaScript
做做这道题  
```html
<A>  
  <B>  
    <C/>  
    <D/>  
  </B>  
</A>  
```
如何转成如下  
```
{  
  name: 'A',  
  children: [{  
     name: 'B'  
     children: [{  
       name: 'C'  
    }, {  
            name: 'D'  
        }]  
  }]  
}  
``` 

代码:[xml2json.js](src/wang/relish/xml2json.js)

# C
使用递归倒序输出字符串。
```
#include<stdio.h>
void p(){
	char c = getchar();
	if(c!='\n')	p();
	printf("%c",c);
}
int main(){
	p();
	return 0;
}
```
