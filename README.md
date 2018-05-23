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

## C
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

## Java
目录
┝- package_a
|     ┗- Test.java
┗- package_a.java

package_a.java文件的内容如下:
```java
public class package_a{
    public class Test{
    }
}
```
Test.java文件的内容如下:
```
package package_a;
public class Test{
    
}
```
问这两个文件(`package_a.java`和`Test.java`)会冲突吗?
答: 会。