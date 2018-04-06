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

代码:[xml2json.js](src/xml2json.js)