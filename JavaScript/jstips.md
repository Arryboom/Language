#逻辑运算小记
```
$scope.editstatus && (!$scope.name.length ||
    !$scope.sex.length ||
    !$scope.password.length）
```
```
>typeof 5
"number"
>typeof !!5
"boolean"
>!!5
true
>!5
false
>!0
true
>!0 || !0 || !0
true
>!0 || !0 || !1
true
>!1 || !1 || !1
false
```

```
>true &&(!5||!3||!2)
false
>true &&(!0||!0||!0)
true
```

true &&(!5||!3||!2)
#js 与或运算符 || && 妙用
>https://www.jb51.net/article/21339.htm

首先出个题：

如图： 
![](data:image/jpeg;base64,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)
假设对成长速度显示规定如下： 
成长速度为5显示1个箭头； 
成长速度为10显示2个箭头； 
成长速度为12显示3个箭头； 
成长速度为15显示4个箭头； 
其他都显示都显示0各箭头。 
用代码怎么实现？ 

差一点的if，else： 
```
var add_level = 0; 
if(add_step == 5){ 
add_level = 1; 
} 
else if(add_step == 10){ 
add_level = 2; 
} 
else if(add_step == 12){ 
add_level = 3; 
} 
else if(add_step == 15){ 
add_level = 4; 
} 
else { 
add_level = 0; 
} 
```
稍好些的switch： 
```
var add_level = 0; 
switch(add_step){ 
case 5 : add_level = 1; 
break; 
case 10 : add_level = 2; 
break; 
case 12 : add_level = 3; 
break; 
case 15 : add_level = 4; 
break; 
default : add_level = 0; 
break;} 
```
如果需求改成： 
成长速度为>12显示4个箭头； 
成长速度为>10显示3个箭头； 
成长速度为>5显示2个箭头； 
成长速度为>0显示1个箭头； 
成长速度为<=0显示0个箭头。 

那么用switch实现起来也很麻烦了。 

那么你有没有想过用一行就代码实现呢？ 
ok，让我们来看看js强大的表现力吧： 
```
var add_level = (add_step==5 && 1) || (add_step==10 && 2) || (add_step==12 && 3) || (add_step==15 && 4) || 0; 
```
更强大的，也更优的： 
```
var add_level={'5':1,'10':2,'12':3,'15':4}[add_step] || 0; 
```
第二个需求： 
```
var add_level = (add_step>12 && 4) || (add_step>10 && 3) || (add_step>5 && 2) || (add_step>0 && 1) || 0; 
```
首先我们来梳理一下一个概念，请你一定要记住：在js逻辑运算中，0、""、null、false、undefined、NaN都会判为false，其他都为true（好像没有遗漏了吧，请各位确认下）。这个一定要记住，不然应用||和&&就会出现问题。 
这里顺便提下：经常有人问我，看到很多代码if(!!attr)，为什么不直接写if(attr)； 
其实这是一种更严谨的写法： 
请测试 typeof 5和typeof !!5的区别。!!的作用是把一个其他类型的变量转成的bool类型。 
下面主要讨论下逻辑运算符&&和||。 
几乎所有语言中||和&&都遵循“短路”原理，如&&中第一个表达式为假就不会去处理第二个表达式，而||正好相反。 
js也遵循上述原则。但是比较有意思的是它们返回的值。 
代码：var attr = true && 4 && “aaa”; 
那么运行的结果attr就不是简单的true或这false，而是”aaa” 
再来看看||： 
代码：var attr = attr || “”;这个运算经常用来判断一个变量是否已定义，如果没有定义就给他一个初始值，这在给函数的参数定义一个默认值的时候比较有用。因为js不像php可以直接在型参数上定义func($attr=5)。再次提醒你记住上面的原则：如果实参需要是0、""、null、false、undefined、NaN的时候也会当false来处理。 

if(a >=5){ 
alert("你好"); 
} 
可以写成： 
a >= 5 && alert("你好"); 
这样只需一行代码就搞定。但是需要注意的一点就是：js中||和&&的特性帮我们精简了代码的同时，也带来了代码可读性的降低。这就需要我们自己来权衡了。 
一方面精简js代码，能实质性的减少网络流量，尤其是大量应用的js公用库。个人比较推荐的做法是：如果是相对复杂的应用，请适当地写一些注释。这个和正在表达式一样，能够精简代码，但是可读性会降低，对读代码的人要求会高些，最好的办法就是写注释。 

我们可以不使用这些技巧，但是我们一定要能看懂，因为这些技巧已经广泛应用，尤其是像JQuery等js框里的代码，不理解这些你就很难看懂别人的代码。 
像var Yahoo = Yahoo || {};这种是非常广泛应用的。 
ok,最后让我们来看一段jQuery中的代码吧： 
```
var wrap = 
    // option or optgroup 
    !tags.indexOf("<opt") && 
    [ 1, "<select multiple='multiple'>", "</select>" ] || 

    !tags.indexOf("<leg") && 
    [ 1, "<fieldset>", "</fieldset>" ] || 

    tags.match(/^<(thead|tbody|tfoot|colg|cap)/) && 
    [ 1, "<table>", "</table>" ] || 

    !tags.indexOf("<tr") && 
    [ 2, "<table><tbody>", "</tbody></table>" ] || 

    // <thead> matched above 
    (!tags.indexOf("<td") || !tags.indexOf("<th")) && 
    [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ] || 

    !tags.indexOf("<col") && 
    [ 2, "<table><tbody></tbody><colgroup>", "</colgroup></table>" ] || 

    // IE can't serialize <link> and <script> tags normally 
    !jQuery.support.htmlSerialize && 
    [ 1, "div<div>", "</div>" ] || 

    [ 0, "", "" ]; 

    // Go to html and back, then peel off extra wrappers 
    div.innerHTML = wrap[1] + elem + wrap[2]; 

    // Move to the right depth 
    while ( wrap[0]-- ) 
        div = div.lastChild; 
```
这段代码是作者用来处理 $(html) 时，有些标签必须要约束的，如```<option>```必须在```<select></select>```之内的。 
可能你也发现了作者还有一个很巧的地方就是 !tags.indexOf("<opt") ，作者很巧很简单的就实现了startWith的功能了，没有一点多余的代码。jquery源代码中还有很多如此精妙的代码，大家可以去学习学习。

--- 

#setTimeout的作用域以及this的指向问题
>https://www.cnblogs.com/hutaoer/p/3423782.html

setTimeout的用法详见：http://www.w3school.com.cn/htmldom/met_win_settimeout.asp

是的，setTimeout的常见用法是让某个方法延迟执行。我们知道，setTimeout方法是挂在window对象下的。《JavaScript高级程序设计》第二版中，写到：“超时调用的代码都是在全局作用域中执行的，因此函数中this的值在非严格模式下指向window对象，在严格模式下是undefined”。在这里，我们只讨论非严格模式。

setTimeout接受两个参数，第一个是要执行的代码或函数，第二个是延迟的时间。

一、先说结论：setTimeout中所执行函数中的this，永远指向window！！注意是要延迟执行的函数中的this哦！！

1. 直接使用，代码1.1：

1
```
setTimeout("alert(this)", 1);   // [object Window]
```
2. 在一个对象中调用setTimeout试试，代码1.2：

```
var obj = {
  say: function() {
    setTimeout("alert('in obj ' + this)", 0)
  }
}
 
obj.say();   // in obj [object Window]
```
3. 将执行的代码换成匿名函数试试，代码1.3：

```
var obj = {
  say: function() {
    setTimeout(function(){alert(this)}, 0)
  }
}
 
obj.say();   //  [object Window]
```
4. 换成函数引用再试试吧，代码1.4：

```
function talk() {
  alert(this);
}
 
var obj = {
  say: function() {
    setTimeout(talk, 0)
  }
}
 
obj.say();   //  [object Window]
```
恩，貌似得到的结论是正确的，setTimeout中的延迟执行函数中的this指向了window。这里我反复的强调，是延迟执行函数中的this，是因为，我们经常会面对两个this。一个是setTimeout调用环境中的this，一个就是延迟执行函数中的this。这两个this有时候是不同的。有些不放心？？再多写一些代码测试一下！　　

 

 二、setTimeout中的两个this到底指向谁？？为了便于区分，我们把setTimeout调用环境下的this称之为第一个this，把延迟执行函数中的this称之为第二个this，并在代码注释中标出来，方便您区分。先说得出的结论：第一个this的指向是需要根据上下文来确定的，默认为window；第二个this就是指向window。然后我们通过代码来验证下。

1. 函数作为方法调用还是构造函数调用，this是不同的。先看代码，代码2.1：

```
function Foo() {
    this.value = 42;
    this.method = function() {
        // this 指向全局对象
        alert(this)   // 输出window  第二个this
        alert(this.value); // 输出：undefined   第二个this
    };
    setTimeout(this.method, 500);  // this指向Foo的实例对象  第一个this
}
new Foo();
```
我们new了一个Foo对象，那么this.method中的this指向的是new的对象，否则无法调用method方法。但是进了method方法后，方法中的this又指向了window，因此this.value的值为undefined。

我们在外层添加一段代码，再看看，代码2.2：

```
var value=33;
 
function Foo() {
    this.value = 42;
    this.method = function() {
        // this 指向全局对象
        alert(this)   // 输出window    第二个this
        alert(this.value); // 输出：33   第二个this
    };
    setTimeout(this.method, 500);  // 这里的this指向Foo的实例对象  第一个this
}
new Foo();
```
从这里，可以明显的看到，method方法中的this指向的是window，因为可以输出外层的value值。那为什么setTimeout中的this指向的是Foo的实例对象呢？

我觉得代码2.2就等价于下面的代码，如代码2.3：

```
var value=33;
 
function Foo() {
    this.value = 42;
    setTimeout(function(){alert(this);alert(this.value)}, 500);  // 先后输出 window   33  这里是第二个this
}
new Foo();
```
setTimeout中的第一个参数就是一个单纯的函数的引用而已，而函数中的this仍然指向的是window。在setTimeout(this.method, time) 中的this是可以根据上下文而改变的，其最终的目的是要得到一个函数指针。我们再来验证一下，看代码2.4:

```
function method() {
  alert(this.value);  // 输出 42  第二个this
}
 
function Foo() {
    this.value = 42;
    setTimeout(this.method, 500);  // 这里this指向window   第一个this
}
 
Foo();
```
这次我们将Foo当成方法直接执行，method方法放到外层，即挂在window上面。而this则指向了window，因此可以调用method方法。method方法中的this仍然指向window，而Foo()执行的时候，对window.value进行了赋值(this.value=42)，因此输出了42。

　

三、实践。知道了得出的结论，我们来阅读一下比较奇葩的一些代码，进行验证。　　

首先在一个函数中，调用setTimeout。代码3.1：

```
var test = "in the window";
 
setTimeout(function() {alert('outer ' + test)}, 0); // 输出 outer in the window ，默认在window的全局作用域下
 
function f() {
  var test = 'in the f!';  // 局部变量，window作用域不可访问
  setTimeout('alert("inner " + test)', 0);  // 输出 outer in the window, 虽然在f方法的中调用，但执行代码(字符串形式的代码)默认在window全局作用域下，test也指向全局的test
}
 
f();
```
在f方法中，setTimeout中的test的值是外层的test，而不是f作用域中的test。再看代码3.2：

```
var test = "in the window";
 
setTimeout(function() {alert('outer' + test)}, 0); // outer in the window  ，没有问题，在全局下调用，访问全局中的test
 
function f() {
  var test = 'in the f!';
  setTimeout(function(){alert('inner '+ test)}, 0);  // inner in the f!  有问题，不是说好了执行函数中的this指向的是window吗？那test也应该对应window下                                                      //  的值才对，怎么test的值却是 f()中的值呢？？？？
}
 
f();
```
呀。。按照前面的经验，f中的setTimeout中的test也应该明明应该是指向外层的test才对吧？？？我们注意到，这个f里面的setTimeout中的第一个参数是一个匿名函数，这是上面两端代码最大的不同。而只要是函数就有它的作用域，我们可以将上面的代码替换成下面的代码3.3：

```
var test = "in the window";
 
setTimeout(function() {alert('outer ' + test)}, 0); // in the window
 
function f() {
  var test = 'in the f!';
 
  function ff() {alert('inner ' + test)} // 能访问到f中的test局部变量
 
  setTimeout(ff, 0);  // inner in the f!
}
 
f();
```
 再看一段更清晰的代码，3.4：

```
var value=33;
 
function Foo() {
    var value = 42;
    setTimeout(function(){alert(value);alert(this.value)}, 500);  // 先后输出 42 然后输出33  这里的this是第二个this
}
new Foo();
```
可以确定，延迟执行函数中的this的确是指向了window，毫无疑问，上面的所有代码都可以验证哈。但是延迟执行函数中的其他变量需要根据上下文来确认。

修改代码3.4为3.5，去掉匿名函数的调用方式，会更加清晰：

```
var value=33;
 
function Foo() {
    var value = 42;
    function ff() {
      alert(value);  // 42
      alert(this.value);  // 33
    }
    setTimeout(ff, 500);  // 先后输出 42   33 
}
Foo(); // 直接执行，跟普通函数没有区别
```
因此，如果去掉Foo中的value=42的话，那么value的值等于多少呢？undefined还是外层的33？？请看3.5：

```
var value=33;
 
function Foo() {
    function ff() {
      alert(value);   // 输出33
      alert(this.value);  // 输出33  this指向window
    }
    setTimeout(ff, 500);  // 先后输出 33  33
}
Foo();
```
没错，就是外层的33，因为ff可以访问到window下的value值，就如同setTimeout中的匿名函数一样。　　　　

最后，我们通过对象的方式进行调用，代码3.6：

```
var obj = {
  name: 'hutaoer',
  say: function() {
    var self = this;
    setTimeout(function(){
      alert(self);   // 输出 object ，指向obj
      alert(this);   // 第二个this，指向window，我心永恒，从未改变
      alert(self.name)  // 输出 hutaoer
    }, 0)
  }
}
 
obj.say();
```

最后，如果您到看懂了上面的例子，那么我们可以回顾一下得出的一些结论咯：

一、setTimeout中的延迟执行代码中的this永远都指向window

二、setTimeout(this.method, time)这种形式中的this，即上文中提到的第一个this，是根据上下文来判断的，默认为全局作用域，但不一定总是处于全局下，具体问题具体分析。

三、setTimeout(匿名函数, time)这种形式下，匿名函数中的变量也需要根据上下文来判断，具体问题具体分析。### 重新编辑start  谢谢一楼@白夜说 同学的回复，在这里匿名函数的使用形成了一个闭包，从而能访问到外层函数的局部变量。这样子去理解，我觉得挺好的！只是这种闭包，跟常见的闭包不同，因为函数式放在setTimeout里面。 ### —— 于2013.11.29下午15:50分 重新编辑 end

今天就到这里，上面的结论都是本人自己总结出来。鄙人才疏学浅，难免有误，若有纰漏、错误或欠妥之处，还望大家指出，在下不吝赐教，力图互相学习，共同进步。

接下来的一篇，还是谈setTimeout的用法，以及需要注意的问题，敬请期待。

---
#JavaScript中停止执行setInterval和setTimeout事件的方法
>https://www.jb51.net/article/66067.htm

js 代码中执行循环事件时，经常会用到 setInterval 和 setTimeout 这两个方法，关于这两个方法的细节这里不详细讨论了，简要分享下在需要停止循环事件的时候该如何操作。

（1）setInterval 方法可按照指定的周期（以毫秒计）来调用函数或计算表达式，停止该方法可使用 clearInterval 方法。具体示例如下：
复制代码 代码如下:
```
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<body>
<input type="text" id="clock" size="50" />
<script language=javascript>
var int=self.setInterval("clock()",50);//每隔 50 毫秒调用 clock() 函数
function clock(){
 var t=new Date();
 document.getElementById("clock").value=t;
}
</script>
<button onclick="window.clearInterval(int)">停止 interval</button>
</body>
</html>
```
语法 clearInterval(id_of_setinterval)

参数 id_of_setinterval 表示由 setInterval() 返回的 ID 值。

clearInterval() 方法可取消由 setInterval() 设置的 timeout；clearInterval() 方法的参数必须是由 setInterval() 返回的 ID 值。

（2）setTimeout 方法用于在指定的毫秒数后调用函数或计算表达式。停止该方法可使用 clearTimeout 方法。具体示例如下：

提示：setTimeout() 只执行 code 一次。如果要多次调用，请使用 setInterval() 或者让 code 自身再次调用 setTimeout()。

复制代码 代码如下:
```
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
var c=0;
var t;
function timedCount(){
 document.getElementById('txt').value=c;
 c=c+1;
 t=setTimeout("timedCount()",1000);
}
function stopCount(){
 clearTimeout(t);
}
</script>
</head>
<body>
<input type="button" value="开始计数" onClick="timedCount()">
<input type="text" id="txt">
<input type="button" value="停止计数" onClick="stopCount()">
</body>
</html>
```
clearTimeout() 方法可取消由 setTimeout() 方法设置的 timeout。
语法 clearTimeout(id_of_settimeout)

参数 id_of_setinterval 表示由 setTimeout() 返回的 ID 值。该值标识要取消的延迟执行代码块。

#H5 document.querySelector
document.querySelector()
document.querySelectorAll()
Somebody say document.body.querySelector
H5新增功能，等价于jquery的$。

#chrome控制台高级功能详解
>http://www.cnblogs.com/ranyonsue/p/9266795.html


#判断网页是否使用了jquery
window.jQuery
window.$
看是否undefined

#Jquery获取元素各种尺寸
```
$('#cheese').width()
```
>似乎也可以直接赋值尺寸
width() - 返回元素的宽度。
height() - 返回元素的高度。
innerWidth() 方法返回元素的宽度（包括内边距）。                     
innerHeight() 方法返回元素的高度（包括内边距）。                    
outerWidth() 方法返回元素的宽度（包括内边距和边框）。               
outerHeight() 方法返回元素的高度（包括内边距和边框）。              
outerWidth(true) 方法返回元素的宽度（包括内边距、边框和外边距）。   
outerHeight(true) 方法返回元素的高度（包括内边距、边框和外边距）。  
返回文档（HTML 文档）$(document).height()的高度
返回窗口（浏览器视口）$(window).height()的高度
```
var o = document.getElementById("view");var h = o.offsetHeight; //高度var w = o.offsetWidth; //宽度
```
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>jquery动态改变div宽度和高度</title>
<script type="text/javascript" src="jquery-1.11.2.min.js"></script>
</head>
<body>
<div>
<input id="addwidthkeleyi" value="增加宽度" type="button" />
<input id="reducewidthkeleyi" value="减少宽度" type="button" />
<input id="addheightkeleyi" value="增加高度" type="button" />
<input id="reduceheightkeleyi" value="减少高度" type="button" />
<a href="http://keleyi.com/a/bjad/tar6g4m7.htm" target="_blank">原文</a> <a href="http://keleyi.com/">首页</a> <a href="http://keleyi.com/keleyi/phtml/">特效库</a> 点击按钮，注意下方div宽高的变化</div>
<div style="border:1px solid #999;width:200px;height:200px" id="keleyidiv"></div>
<script type="text/javascript">
$("#addwidthke"+"leyi").on("click", function () {
$("#keleyidiv").width($("#keley" + "idiv").width() + 50);
});
$("#reducewidthk" + "eleyi").on("click", function () {
$("#keleyidiv").width($("#kel"+"eyidiv").width() - 50);
});
$("#addheightkele" + "yi").on("click", function () {
$("#kel" + "eyidiv").height($("#keleyidiv").height() + 50);
});
$("#reduceheightkeley" + "i").on("click", function () {
$("#keleyidiv").height($("#keleyidiv").height() - 50);
});
</script>
</body>
</html>
```


Nojquery
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <style type="text/css">
        body
        {
            margin:0;
            padding:0;
        }
        #header
        {
            height: 70px;
            background-color: Blue;
        }
        #middle
        {
            height: auto;
            background-color: Green;
        }
        #footer
        {
            height: 30px;
            background-color: Gray;
        }
    </style>
</head>
<body>   
    <div>
        <div id="header">
        </div>
        <div id="middle">
 
        </div>
        <div id="footer">
        </div>
    </div>
    <script type="text/javascript">
 
        var winWidth = 0;
        var winHeight = 0;
        function findDimensions() { //函数：获取尺寸
            //获取窗口宽度
            if (window.innerWidth) {
                winWidth = window.innerWidth;
            }
            else if ((document.body) && (document.body.clientWidth)) {
                winWidth = document.body.clientWidth;
            }
            //获取窗口高度
            if (window.innerHeight) {
                winHeight = window.innerHeight;
            }
            else if ((document.body) && (document.body.clientHeight)) {
                winHeight = document.body.clientHeight;
            }
            //通过深入Document内部对body进行检测，获取窗口大小
            if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
                winHeight = document.documentElement.clientHeight-100;
                winWidth = document.documentElement.clientWidth;
            }            
            //设置div的具体宽度=窗口的宽度的%
            if (document.getElementById("middle")) {
                document.getElementById("middle").style.height = winHeight + "px";
            }
        }
        findDimensions();
        window.onresize = findDimensions;
    </script>
</body>
</html>

```

***获取DOM元素宽高度***

一、获取css的大小

1.第一种通过内联样式
```
    var box = document.getElementById('box');
    var w = box.style.width;
    var h = box.style.height;```
2.通过计算元素的大小（但是在ie情况下有一个问题，那就没写widht和height的css就返回auto）;
```
    var style = window.getComputedStyle ? window.getComputedStyle(box,null) : null || box.currentStyle;
    var w = style.width;
    var h = style.height;```
3.通过CSSStyleSheet对象中的cssRules(或rules)属性获取元素大小（但是无法获得计算的样式）
```
    var sheet = document.styleSheets[0];
    var rule = (sheet.cssRules || sheet.rules)[0];
    var w = rule.style.width;
    var h = rule.style.height;```
以上三种方法都不行。

二、获取实际的大小

1.clientWidth和clientHeight
```
    var w = box.clientWidth;
    var h = box.clientHeight;```
说明：padding和scroll变动，才有变化

2.scrollWidth 和box.scrollHeight;
```
    var w = box.scrollWidth;
    var h = box.scrollHeight;```
说明，1）border变化，不同浏览器有不同变化2）padding变化，有变化3)margin变化，无变化

3.offsetWidth和offsetHeight
```
    var w = box.scrollWidth;
    var h = box.scrollHeight;```
说明，padding和border有变动，才有变化

三、获取元素周变的距离（原本只能从左边和上边）

1.clientLeft 和 clientTop
这组属性可以获取元素设置了左边框和上边框的大小。
```
    var l = box.clientLeft;
    var t = box.clientTop;```
2.获取相对父级元素的位置
```
    var l = box.offsetLeft;
    var t = box.offsetTop;
    var parent = box.offsetParent;  //获取伏击元素，返回body```
说明，如果没有position：absolute;如果每个浏览器有不同解释

那么获取多层中的元素到body或html的距离，代码如下：
```
    function offsetLeft(element){
        var left = element.offsetLeft;
        var parent = element.offsetParent;
        
        while(parent!== null){
            left += parent.offsetLeft;
            parent = parent.offsetParent;
        }
        return left;
    }
```
3.

//这组属性可以获取滚动条被隐藏的区域大小，也可设置定位到该区域。
```
box.scrollTop; //获取滚动内容上方的位置
box.scrollLeft; //获取滚动内容左方的位置
```
#HTML DOM elements 集合
定义和用法
elements 集合可返回包含表单中所有元素的数组。

元素在数组中出现的顺序和它们在表单的HTML 源代码中出现的顺序相同。

每个元素都有一个 type 属性，其字符串值说明了元素的类型。

**语法**
```
formObject.elements[].property
```

提示和注释
提示：如果 elements[] 数组具有名称（input 标签的 id 或 name 属性），那么该元素的名称就是 formObject 的一个属性，因此可以使用名称而不是数字来引用 input 对象。

举例，假设 x 是一个 form 对象，其中的一个 input 对象的名称是 fname，则可以使用 x.fname 来引用该对象。
实例
下面的例子输出了所有表单元素的值和类型：
```
<html>
<body>

<form id="myForm">
Firstname: <input id="fname" type="text" value="Mickey" />
Lastname: <input id="lname" type="text" value="Mouse" />
<input id="sub" type="button" value="Submit" />
</form>

<p>Get the value of all the elements in the form:<br />
<script type="text/javascript">
var x=document.getElementById("myForm");
for (var i=0;i<x.length;i++)
  {
  document.write(x.elements[i].value);
  document.write("<br />");
  document.write(x.elements[i].type);
  document.write("<br />");
  }
</script>
</p>

</body>
</html>
```

#Dom Elements Object

| 左对齐标题 | 右对齐标题 | 居中对齐标题 |
| :------| ------: | :------: |
| 短文本 | 中等文本 | 稍微长一点的文本 |
| 稍微长一点的文本 | 短文本 | 中等文本 |

| 一个普通标题 | 一个普通标题 | 一个普通标题 |
| ------ | ------ | ------ |
| 短文本 | 中等文本 | 稍微长一点的文本 |
| 稍微长一点的文本 | 短文本 | 中等文本 |

```
1）|、-、:之间的多余空格会被忽略，不影响布局。
2）默认标题栏居中对齐，内容居左对齐。
3）-:表示内容和标题栏居右对齐，:-表示内容和标题栏居左对齐，:-:表示内容和标题栏居中对齐。
4）内容和|之间的多余空格会被忽略，每行第一个|和最后一个|可以省略，-的数量至少有一个。

作者：zizi192
链接：https://www.jianshu.com/p/2df05f279331
來源：简书
简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
```



|属性 / 方法	|描述
| ----- |----- |
| element.accessKey | 设置或返回元素的快捷键。 |
| element.appendChild() | 向元素添加新的子节点，作为最后一个子节点。 |
| element.attributes | 返回元素属性的 NamedNodeMap。 |
| element.childNodes | 返回元素子节点的 NodeList。 |
| element.className | 设置或返回元素的 class 属性。 |
| element.clientHeight | 返回元素的可见高度。 |
| element.clientWidth | 返回元素的可见宽度。 |
| element.cloneNode() | 克隆元素。      |
| element.compareDocumentPosition( ) | 比较两个元素的文档位置。
| element.contentEditable | 设置或返回元素的文本方向。 |
| element.dir | 设置或返回元素的内容是否可编辑。   |
| element.firstChild | 返回元素的首个子。   |
| element.getAttribute() | 返回元素节点的指定属性值。 |
| element.getAttributeNode() | 返回指定的属性节点 |。
| element.getElementsByTagName() | | 返回拥有指定标签名的所有子元素的集合。
| element.getFeature() | 返回实现了指定特性的 API 的某个对象。 |
| element.getUserData() | 返回关联元素上键的对象。 |
| element.hasAttribute() | 如果元素拥有指定属性，则返回true，否则 |返回 false。
| element.hasAttributes() | 如果元素拥有属性，则返回 true， |否则返回 false。
| element.hasChildNodes() | 如果元素拥有子节点，则返回 true |，否则 false。
| element.id | 设置或返回元素的 id。        |
| element.innerHTML | 设置或返回元素的内容。  |
| element.insertBefore() | 在指定的已有的子节点之前插入新节点。 |
| element.isContentEditable | 设置或返回元素的内容。 |
| element.isDefaultNamespace() | 如果指 |定的 namespaceURI 是默认的，则返回 true，否则返回 false。
| element.isEqualNode() | 检查两个元素是否相等。 |
| element.isSameNode() | 检查两个元素是否是相同的节点。 |
| element.isSupported() | 如果元素支持指定特性，则返回 true。 |
| element.lang | 设置或返回元素的语言代码。     |
| element.lastChild | 返回元素的最后一个子元素。 |
| element.namespaceURI | 返回元素的 namespace URI |。
| element.nextSibling | 返回位于相同节点树层级的下一个节点。 |
| element.nodeName | 返回元素的名称。      |
| element.nodeType | 返回元素的节点类型。    |
| element.nodeValue | 设置或返回元素值。    |
| element.normalize() | 合并元素中相邻的文本节点，并移除空的文本节点。 |
| element.offsetHeight | 返回元素的高度。  |
| element.offsetWidth | 返回元素的宽度。   |
| element.offsetLeft | 返回元素的水平偏移位置。 |
| element.offsetParent | 返回元素的偏移容器。 |
| element.offsetTop | 返回元素的垂直偏移位置。 |
| element.ownerDocument | 返回元素的根元素（文档对象）。 |
| element.parentNode | 返回元素的父节点。   |
| element.previousSibling | 返回位于相同节点树层级的前一个元素。 |
| element.removeAttribute() | 从元素中移除指定属性。 |
| element.removeAttributeNode() |  |移除指定的属性节点，并返回被移除的节点。
| element.removeChild() | 从元素中移除子节点。 |
| element.replaceChild() | 替换元素中的子节点。 |
| element.scrollHeight | 返回元素的整体高度。 |
| element.scrollLeft | 返回元素左边缘与视图之间的距离。 |
| element.scrollTop | 返回元素上边缘与视图之间的距离。 |
| element.scrollWidth | 返回元素的整体宽度。 |
| element.setAttribute() | 把指定属性设置或更改为指定值。 |
| element.setAttributeNode() | 设置或更改指定属性 |节点。
| element.setIdAttribute() |       |
| element.setIdAttributeNode() |   |
| element.setUserData() | 把对象关联到元素上的键。 |
| element.style | 设置或返回元素的 style 属性。 |
| element.tabIndex | 设置或返回元素的 tab 键控制次序。 |
| element.tagName | 返回元素的标签名。      |
| element.textContent | 设置或返回节点及其后代的文本内容。 |
| element.title | 设置或返回元素的 title 属性。 |
| element.toString() | 把元素转换为字符串。  |
| nodelist.item() | 返回 NodeList 中位于指定下标的节点 |。
| nodelist.length | 返回 NodeList 中的节点数。 |

#Jquery选择器selector
| 选择器 | 实例 | 选取 |
| :-----: | :-----: | :-----: |
| * | $("*") | 所有元素 |
| #id | $("#lastname") | id="lastname" 的元素 |
| .class | $(".intro") | 所有 class="intro" 的元素 |
| element | $("p") | 所有 <p> 元素 |
| .class.class | $(".intro.demo") | 所有 class="intro" 且 class="demo" 的元素 |
|   |   |   |
| :first | $("p:first") | 第一个 <p> 元素 |
| :last | $("p:last") | 最后一个 <p> 元素 |
| :even | $("tr:even") | 所有偶数 <tr> 元素 |
| :odd | $("tr:odd") | 所有奇数 <tr> 元素 |
|   |   |   |
| :eq(index) | $("ul li:eq(3)") | 列表中的第四个元素（index 从 0 开始） |
| :gt(no) | $("ul li:gt(3)") | 列出 index 大于 3 的元素 |
| :lt(no) | $("ul li:lt(3)") | 列出 index 小于 3 的元素 |
| :not(selector) | $("input:not(:empty)") | 所有不为空的 input 元素 |
|   |   |   |
| :header | $(":header") | 所有标题元素 <h1> - <h6> |
| :animated |   | 所有动画元素 |
|   |   |   |
| :contains(text) | $(":contains('W3School')") | 包含指定字符串的所有元素 |
| :empty | $(":empty") | 无子（元素）节点的所有元素 |
| :hidden | $("p:hidden") | 所有隐藏的 <p> 元素 |
| :visible | $("table:visible") | 所有可见的表格 |
|   |   |   |
| s1,s2,s3 | $("th,td,.intro") | 所有带有匹配选择的元素 |
|   |   |   |
| [attribute] | $("[href]") | 所有带有 href 属性的元素 |
| [attribute=value] | $("[href='#']") | 所有 href 属性的值等于 "#" 的元素 |
| [attribute!=value] | $("[href!='#']") | 所有 href 属性的值不等于 "#" 的元素 |
| [attribute$=value] | $("[href$='.jpg']") | 所有 href 属性的值包含以 ".jpg" 结尾的元素 |
|   |   |   |
| :input | $(":input") | 所有 <input> 元素 |
| :text | $(":text") | 所有 type="text" 的 <input> 元素 |
| :password | $(":password") | 所有 type="password" 的 <input> 元素 |
| :radio | $(":radio") | 所有 type="radio" 的 <input> 元素 |
| :checkbox | $(":checkbox") | 所有 type="checkbox" 的 <input> 元素 |
| :submit | $(":submit") | 所有 type="submit" 的 <input> 元素 |
| :reset | $(":reset") | 所有 type="reset" 的 <input> 元素 |
| :button | $(":button") | 所有 type="button" 的 <input> 元素 |
| :image | $(":image") | 所有 type="image" 的 <input> 元素 |
| :file | $(":file") | 所有 type="file" 的 <input> 元素 |
|   |   |   |
| :enabled | $(":enabled") | 所有激活的 input 元素 |
| :disabled | $(":disabled") | 所有禁用的 input 元素 |
| :selected | $(":selected") | 所有被选取的 input 元素 |
| :checked | $(":checked") | 所有被选中的 input 元素 |

***jquery选择自定义属性***
```
<html>
<head>
<script type="text/javascript" src="./jq/jquery.js"></script>
 
<script type="text/javascript">
 
$(function(){
	$("[cool]").click(function(){
			alert(1);
	});
});
</script>
</head>
 
<body>
<button type="button"  cool="bt">Click me</button>
 
</body>
</html>

```

#jQuery - 设置内容和属性
设置内容 - text()、html() 以及 val()


- text() - 设置或返回所选元素的文本内容
- html() - 设置或返回所选元素的内容（包括 HTML 标记）
- val() - 设置或返回表单字段的值
下面的例子演示如何通过 text()、html() 以及 val() 方法来设置内容：
```
$("#btn1").click(function(){
  $("#test1").text("Hello world!");
});
$("#btn2").click(function(){
  $("#test2").html("<b>Hello world!</b>");
});
$("#btn3").click(function(){
  $("#test3").val("Dolly Duck");
});
```
text()、html() 以及 val() 的回调函数
上面的三个 jQuery 方法：text()、html() 以及 val()，同样拥有回调函数。回调函数由两个参数：被选元素列表中当前元素的下标，以及原始（旧的）值。然后以函数新值返回您希望使用的字符串。

下面的例子演示带有回调函数的 text() 和 html()：
```
$("#btn1").click(function(){
  $("#test1").text(function(i,origText){
    return "Old text: " + origText + " New text: Hello world!
    (index: " + i + ")";
  });
});

$("#btn2").click(function(){
  $("#test2").html(function(i,origText){
    return "Old html: " + origText + " New html: Hello <b>world!</b>
    (index: " + i + ")";
  });
});
```
设置属性 - attr()
jQuery attr() 方法也用于设置/改变属性值。

下面的例子演示如何改变（设置）链接中 href 属性的值：

```
$("button").click(function(){
  $("#w3s").attr("href","http://www.w3school.com.cn/jquery");
});
```
attr() 方法也允许您同时设置多个属性。

下面的例子演示如何同时设置 href 和 title 属性：
```
$("button").click(function(){
  $("#w3s").attr({
    "href" : "http://www.w3school.com.cn/jquery",
    "title" : "W3School jQuery Tutorial"
  });
});
```
attr() 的回调函数
jQuery 方法 attr()，也提供回调函数。回调函数由两个参数：被选元素列表中当前元素的下标，以及原始（旧的）值。然后以函数新值返回您希望使用的字符串。

下面的例子演示带有回调函数的 attr() 方法：
```
$("button").click(function(){
  $("#w3s").attr("href", function(i,origValue){
    return origValue + "/jquery";
  });
});
```
#Jquery引用JSON文件
很少直接引用json文件来获取数据，其实很简单就是路径的引用

如果直接使用jquey，通过getJSON来发送请求，代码则是
```
$.getJSON(json/text.json",function(data){

//你要进行的操作

}
```
此时我的js文件夹和json文件夹是同级，我使用的json数据是json文件夹下面的text.json


---
JSON：
```
 [
	{
		"id": "001",
		"name": "昂昂溪"
	},
	{
		"id": "002",
		"name": "安北"
	},
	{
		"id": "003",
		"name": "敖包"
	},
	{
		"id": "004",
		"name": "敖包沟"
	},
	{
		"id": "005",
		"name": "艾不盖"
	},
	{
		"id": "006",
		"name": "阿巴嘎旗"
	},
	{
		"id": "007",
		"name": "艾比湖"
	},
	{
		"id": "008",
		"name": "安边镇"
	},
	{
		"id": "009",
		"name": "阿城"
	},
	{
		"id": "010",
		"name": "㘷岱"
	},
	{
		"id": "011",
		"name": "安达"
	},
	{
		"id": "012",
		"name": "安定"
	},
	{
		"id": "013",
		"name": "安多"
	},
	{
		"id": "014",
		"name": "安德"
	},
	{
		"id": "015",
		"name": "阿都呼都格"
	},
	{
		"id": "016",
		"name": "阿达日嘎"
	},
	{
		"id": "017",
		"name": "阿尔山"
	},
	{
		"id": "018",
		"name": "阿尔山北"
	},
	{
		"id": "019",
		"name": "阿尔乡"
	},
	{
		"id": "020",
		"name": "安福"
	}
]
```
HTML:
```
<label class="form-label col-xs-1 col-sm-1"><strong>到站：</strong></label>
		<div class="formControls col-xs-2 col-sm-2">
			<select name="shipment.endstation" id="endstation">
			</select>
		</div>
<div class="formControls col-xs-2 col-sm-2">
			<select name="shipment.startstation" id="startstation">
			</select>
		</div>
```
js:
```
$.getJSON("/common/data/station.json",function(data){
	var $startjsontip = $("#startstation");
	$startjsontip.empty();//清空内容
	$("#startstation").append('<option value="">请选择发站</option>');
	$.each(data,function(infoIndex,info){
		$("#startstation").append('<option value="'+ info["id"] + '">' + info["name"] + '</option>');
	}) 
	var $endjsontip = $("#endstation");
	$endjsontip.empty();//清空内容
	$("#endstation").append('<option value="">请选择到站</option>');
	$.each(data,function(infoIndex,info){
		$("#endstation").append('<option value="'+ info["id"] + '">' + info["name"] + '</option>');
	}) 
})

```


son文件是一种轻量级的数据交互格式。一般在jquery中使用getJSON()方法读取。 

----

```
$.getJSON(url,[data],[callback])
```
url：加载的页面地址 
data: 可选项，发送到服务器的数据，格式是key/value 
callback:可选项，加载成功后执行的回调函数 
JSON:
```
[ 
{ 
"name":"张国立", 
"sex":"男", 
"email":"zhangguoli@123.com"
}, 
{ 
"name":"张铁林", 
"sex":"男", 
"email":"zhangtieli@123.com"
}, 
{ 
"name":"邓婕", 
"sex":"女", 
"email":"zhenjie@123.com"
} 
] 

```

其次建一个页面用于获取JSON文件里的用户信息数据，并显示 
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>getJSON获取数据</title> 
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script> 
<style type="text/css"> 
#divframe{ border:1px solid #999; width:500px; margin:0 auto;} 
.loadTitle{ background:#CCC; height:30px;} 
</style> 
< script type = "text/javascript" > 
$(function (){
  $("#btn").click(function ()  {
    $.getJSON("js/userinfo.json", function (data){
      var $jsontip = $("#jsonTip");
      var strHtml = "123";
      //存储数据的变量 
      $jsontip.empty();
      //清空内容 
      $.each(data, function (infoIndex, info){
        strHtml += "姓名：" + info["name"] + "<br>";
        strHtml += "性别：" + info["sex"] + "<br>";
        strHtml += "邮箱：" + info["email"] + "<br>";
        strHtml += "<hr>" 
      }) 
      $jsontip.html(strHtml);
      //显示处理后的数据 
    }) 
  }) 
})
</script> 
</head> 
<body> 
<div id="divframe"> 
<div class="loadTitle"> 
<input type="button" value="获取数据" id="btn"/> 
</div> 
<div id="jsonTip"> 
</div> 
</div> 
</body> 
</html> 
```




#JSONP调用JSON文件

调用本地文件：
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>
 
 
<script type="text/javascript">  
 
    function indexDemo(result){
      //回调函数名称(indexDemo)，需要与 src 中一致，而且要与文件地址中名一致。jsonp格式 名称({})
    //不然无法获取到对应的文件
       console.log(result)     //打印 indexDemo.json 中的数据
    }
 
 
</script>  
 
<!--jsonp中 需要注意的是
     <script  src="jq.json?callback=loa"></script>
 src ? 之前为文件地址，? 之后为回调函数callback名称，
回调函数可以简写为 cb ,  然后 回调函数 名称要与  文件中的名称一致
可以在 对应的文件名中看下，如，jq.json?callback=loa,jq.json的  名字为  loa-->
 
 
<script type="text/javascript" src="index.json?callback=indexDemo"></script>
 
 
</body>
 
 
</html>
```
index.json文件内容
```
indexDemo({
    "a":"lllll",
    "b":"2222" 
     })
```

跨域访问数据举例
 上一个是访问的本地jsonp，这次访问以下百度的关键字部分，访问百度搜索数据，比如返回百度搜索游戏的结果

打开百度首页，同时代开网页debug，切换到Network，在百度搜索框中输入关键字游戏时会发现nerwork下面不断有数据变化，在name那一栏下找到su?wd开头的数据点击打开它的头部信息，把Request URL请求的地址取出，不要wd=XXX和cd=XXX中的XXX分别替换为”旅游”和”demo”，作为jsonp跨域访问的地址，其中wd为百度搜索关键字,cb为回调函数。

```
<script type="text/javascript">
 function demo (res){  
      console.log(res);   //打印从百度获取的 关键字列表
    }
</script>  
<script  src="https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su?wd=旅游&cb=demo"></script>
```
打印出来的内容
![](data:image/png;base64,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)

---
some shitts
```
Javascript 读取外部的本地json文件
方案1
 运行本地web服务器,提供文件服务
方案2  
1.data = '[{"name" : "Ashwin", "age" : "20"},{"name" : "Abhinandan", "age" : "20"}]';  (json文件内部代码)
1.<script type="text/javascript" src="data.json"></script>  
2.var mydata = JSON.parse(data);  

方案3
1.function readTextFile(file, callback) {  
2.    var rawFile = new XMLHttpRequest();  
3.    rawFile.overrideMimeType("application/json");  
4.    rawFile.open("GET", file, true);  
5.    rawFile.onreadystatechange = function() {  
6.        if (rawFile.readyState === 4 && rawFile.status == "200") {  
7.            callback(rawFile.responseText);  
8.        }  
9.    }  
10.    rawFile.send(null);  
11.}  
12.  
13.//usage:  
14.readTextFile("/Users/Documents/workspace/test.json", function(text){  
15.    var data = JSON.parse(text);  
16.    console.log(data);  
17.}); 
另外，这个功能还可以用于加载.html或.txt文件，将mime类型参数覆盖到"text/html"，"text/plain"等。  
方案4
使用FileAPI
1.// 检查文件API的支持  
2.if (window.File && window.FileReader && window.FileList && window.Blob) {  
3.  // 支持.  
4.} else {  
5.  alert('这个浏览器不完全支持文件API');  
6.}  
选择文件
1.使用表单输入进行选择文件
1.<input type="file" id="files" name="files[]" multiple />  
2.<output id="list"></output>  

1.<script>  
2.  function handleFileSelect(evt) {  
3.    var files = evt.target.files; // FileList object  
4.    // files is a FileList of File objects. List some properties.  
5.    var output = [];  
6.    for (var i = 0, f; f = files[i]; i++) {  
7.      output.push('<li><strong>', escape(f.name), '</strong> (', f.type || 'n/a', ') - ',  
8.                  f.size, ' bytes, last modified: ',  
9.                  f.lastModifiedDate.toLocaleDateString(), '</li>');  
10.    }  
11.    document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';  
12.  }  
13.  document.getElementById('files').addEventListener('change', handleFileSelect, false);  
14.</script>  
2.使用拖放进行选择文件
1.<div id="drop_zone">Drop files here</div>  
2.<output id="list"></output> 

1.<script>  
2.  function handleFileSelect(evt) {  
3.    evt.stopPropagation();  
4.    evt.preventDefault();  
5.    var files = evt.dataTransfer.files; // FileList object.  
6.    // files is a FileList of File objects. List some properties.  
7.    var output = [];  
8.    for (var i = 0, f; f = files[i]; i++) {  
9.      output.push('<li><strong>', escape(f.name), '</strong> (', f.type || 'n/a', ') - ',  
10.                  f.size, ' bytes, last modified: ',  
11.                  f.lastModifiedDate.toLocaleDateString(), '</li>');  
12.    }  
13.    document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';  
14.  }  
15.  function handleDragOver(evt) {  
16.    evt.stopPropagation();  
17.    evt.preventDefault();  
18.    evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.  
19.  }  
20.  // Setup the dnd listeners.  
21.  var dropZone = document.getElementById('drop_zone');  
22.  dropZone.addEventListener('dragover', handleDragOver, false);  
23.  dropZone.addEventListener('drop', handleFileSelect, false);  
24.</script> 
读取文件
获得文件后,使用FileReader对象读取文件到内存.读取完成后,将触发onload事件,result属性用于访问文件的数据,
FileReader包括四个异步读取文件的选项.
1.FileReader.readAsBinaryString(Blob|File) - result 属性将包含二进制字符串形式的 file/blob 数据。每个字节均由一个 [0..255] 范围内的整数表示。  
2.FileReader.readAsText(Blob|File, opt_encoding) - result 属性将包含文本字符串形式的 file/blob 数据。该字符串在默认情况下采用“UTF-8”编码。使用可选编码参数可指定其他格式。  
3.FileReader.readAsDataURL(Blob|File) - result 属性将包含编码为数据网址的 file/blob 数据。  
4.FileReader.readAsArrayBuffer(Blob|File) - result 属性将包含 ArrayBuffer 对象形式的 file/blob 数  

1.对您的 FileReader 对象调用其中某一种读取方法后，可使用 onloadstart、onprogress、onload、onabort、onerror 和 onloadend 跟踪其进度。  
读取文件呈现缩略图(例子)
1.<style>  
2.  .thumb {  
3.    height: 75px;  
4.    border: 1px solid #000;  
5.    margin: 10px 5px 0 0;  
6.  }  
7.</style>  
8.  
9.<input type="file" id="files" name="files[]" multiple />  
10.<output id="list"></output>  
11.  
12.<script>  
13.  function handleFileSelect(evt) {  
14.    var files = evt.target.files; // FileList object  
15.  
16.    // Loop through the FileList and render image files as thumbnails.  
17.    for (var i = 0, f; f = files[i]; i++) {  
18.  
19.      // Only process image files.  
20.      if (!f.type.match('image.*')) {  
21.        continue;  
22.      }  
23.  
24.      var reader = new FileReader();  
25.  
26.      // Closure to capture the file information.  
27.      reader.onload = (function(theFile) {  
28.        return function(e) {  
29.          // Render thumbnail.  
30.          var span = document.createElement('span');  
31.          span.innerHTML = ['<img class="thumb" src="', e.target.result,  
32.                            '" title="', escape(theFile.name), '"/>'].join('');  
33.          document.getElementById('list').insertBefore(span, null);  
34.        };  
35.      })(f);  
36.  
37.      // Read in the image file as a data URL.  
38.      reader.readAsDataURL(f);  
39.    }  
40.  }  
41.  
42.  document.getElementById('files').addEventListener('change', handleFileSelect, false);  
43.</script> 
分割文件
1.if (file.webkitSlice) {  //关键代码
2.  var blob = file.webkitSlice(startingByte, endindByte);  
3.} else if (file.mozSlice) {  
4.  var blob = file.mozSlice(startingByte, endindByte);  
5.}  
6.reader.readAsBinaryString(blob);  
如何读取文件块(例子)
1.<style>  
2.  #byte_content {  
3.    margin: 5px 0;  
4.    max-height: 100px;  
5.    overflow-y: auto;  
6.    overflow-x: hidden;  
7.  }  
8.  #byte_range { margin-top: 5px; }  
9.</style>  
10.  
11.<input type="file" id="files" name="file" /> Read bytes:   
12.<span class="readBytesButtons">  
13.  <button data-startbyte="0" data-endbyte="4">1-5</button>  
14.  <button data-startbyte="5" data-endbyte="14">6-15</button>  
15.  <button data-startbyte="6" data-endbyte="7">7-8</button>  
16.  <button>entire file</button>  
17.</span>  
18.<div id="byte_range"></div>  
19.<div id="byte_content"></div>  
20.  
21.<script>  
22.  function readBlob(opt_startByte, opt_stopByte) {  
23.  
24.    var files = document.getElementById('files').files;  
25.    if (!files.length) {  
26.      alert('Please select a file!');  
27.      return;  
28.    }  
29.  
30.    var file = files[0];  
31.    var start = parseInt(opt_startByte) || 0;  
32.    var stop = parseInt(opt_stopByte) || file.size - 1;  
33.  
34.    var reader = new FileReader();  
35.  
36.    // If we use onloadend, we need to check the readyState.  
37.    reader.onloadend = function(evt) {  
38.      if (evt.target.readyState == FileReader.DONE) { // DONE == 2  
39.        document.getElementById('byte_content').textContent = evt.target.result;  
40.        document.getElementById('byte_range').textContent =   
41.            ['Read bytes: ', start + 1, ' - ', stop + 1,  
42.             ' of ', file.size, ' byte file'].join('');  
43.      }  
44.    };  
45.  
46.    if (file.webkitSlice) {  
47.      var blob = file.webkitSlice(start, stop + 1);  
48.    } else if (file.mozSlice) {  
49.      var blob = file.mozSlice(start, stop + 1);  
50.    }  
51.    reader.readAsBinaryString(blob);  
52.  }  
53.    
54.  document.querySelector('.readBytesButtons').addEventListener('click', function(evt) {  
55.    if (evt.target.tagName.toLowerCase() == 'button') {  
56.      var startByte = evt.target.getAttribute('data-startbyte');  
57.      var endByte = evt.target.getAttribute('data-endbyte');  
58.      readBlob(startByte, endByte);  
59.    }  
60.  }, false);  
61.</script>  
监控读取进度
1.onloadstart 和 onprogress 事件可用于监控读取进度  
通过显示进度条来监控读取状态(例子)
1.<style>  
2.  #progress_bar {  
3.    margin: 10px 0;  
4.    padding: 3px;  
5.    border: 1px solid #000;  
6.    font-size: 14px;  
7.    clear: both;  
8.    opacity: 0;  
9.    -moz-transition: opacity 1s linear;  
10.    -o-transition: opacity 1s linear;  
11.    -webkit-transition: opacity 1s linear;  
12.  }  
13.  #progress_bar.loading {  
14.    opacity: 1.0;  
15.  }  
16.  #progress_bar .percent {  
17.    background-color: #99ccff;  
18.    height: auto;  
19.    width: 0;  
20.  }  
21.</style>  
22.  
23.<input type="file" id="files" name="file" />  
24.<button onclick="abortRead();">Cancel read</button>  
25.<div id="progress_bar"><div class="percent">0%</div></div>  
26.  
27.<script>  
28.  var reader;  
29.  var progress = document.querySelector('.percent');  
30.  
31.  function abortRead() {  
32.    reader.abort();  
33.  }  
34.  
35.  function errorHandler(evt) {  
36.    switch(evt.target.error.code) {  
37.      case evt.target.error.NOT_FOUND_ERR:  
38.        alert('File Not Found!');  
39.        break;  
40.      case evt.target.error.NOT_READABLE_ERR:  
41.        alert('File is not readable');  
42.        break;  
43.      case evt.target.error.ABORT_ERR:  
44.        break; // noop  
45.      default:  
46.        alert('An error occurred reading this file.');  
47.    };  
48.  }  
49.  
50.  function updateProgress(evt) {  
51.    // evt is an ProgressEvent.  
52.    if (evt.lengthComputable) {  
53.      var percentLoaded = Math.round((evt.loaded / evt.total) * 100);  
54.      // Increase the progress bar length.  
55.      if (percentLoaded < 100) {  
56.        progress.style.width = percentLoaded + '%';  
57.        progress.textContent = percentLoaded + '%';  
58.      }  
59.    }  
60.  }  
61.  
62.  function handleFileSelect(evt) {  
63.    // Reset progress indicator on new file selection.  
64.    progress.style.width = '0%';  
65.    progress.textContent = '0%';  
66.  
67.    reader = new FileReader();  
68.    reader.onerror = errorHandler;  
69.    reader.onprogress = updateProgress;  
70.    reader.onabort = function(e) {  
71.      alert('File read cancelled');  
72.    };  
73.    reader.onloadstart = function(e) {  
74.      document.getElementById('progress_bar').className = 'loading';  
75.    };  
76.    reader.onload = function(e) {  
77.      // Ensure that the progress bar displays 100% at the end.  
78.      progress.style.width = '100%';  
79.      progress.textContent = '100%';  
80.      setTimeout("document.getElementById('progress_bar').className='';", 2000);  
81.    }  
82.  
83.    // Read in the image file as a binary string.  
84.    reader.readAsBinaryString(evt.target.files[0]);  
85.  }  
86.  
87.  document.getElementById('files').addEventListener('change', handleFileSelect, false);  
88.</script>  
出处1.https://www.html5rocks.com/zh/tutorials/file/dndfiles/
出处2.https://vimsky.com/article/3575.html
```
#Jquery替换整个标签
例如：```<span>test</span> --> ```替换成 ```<div>test</div>```
拿到旧的删掉
```
var content = $('#old').html();
var new = $('<div>'+content+'</div>');
new.after($('#old'));
$('#old').remove();
```

**jQuery replaceWith()替换标签函数用法**
jquery替换选定元素的内容replaceWith()方法

replaceWith，替换元素

本文章来给各位同学介绍jquery中replaceWith()方法的使用方法，有需要了解的朋友不防进入参考，希望此文章对各位同学有所帮助。

replaceWith() 方法将选择的元素的内容替换为其他内容。


我们先在先看一个实例
```
<%@ page language="java"contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC"-//W3C//DTDHTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type"content="text/html;charset=UTF-8">
<title>Inserttitle here</title>
<script type="text/javascript"src="script/jquery-1.7.2.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
       $("button").click(function(){
           $("p").replaceWith("<b>hello</b>");
           //将p元素里面的内容替换为"<b>hello</b>"
       });
    });
</script>
</head>
 

<body>
    <p>1111111111111111111111</p>
    <button>click</button>
</body>
</html>
```

把所有p标签替换为“##”

```
<html>
<head></head>
<body>

<p>a</p>
<p>a</p>
<p>a</p>
<p>a</p>

</body>
</html>

```
--- 
```
$('p').replaceWith('##');替换所有p标签为##
--- 
```
```
<html>
<head></head>
<body>

##
##
##
##

</body>
</html>

```

**替换标签**
利用这个replaceWith，我们可以把所有p标签替换为b标签，内容不变：
```
$('p').each(function(){
    $(this).replaceWith('<b>'+$(this).html()+'</b>');
});
```
```
<html>
<head></head>
<body>

<b>haha</b>
<b>haha</b>
<b>haha</b>
<b>haha</b>

</body>
</html>
```

多语言网站可以利用这个函数轻松完成
如果你开发的是一个多语言的网站，甚至可以利用这个特性，比如，在你需要翻译的文字上加上i标签，然后遍历翻译替换。

假如页面dom结构如下：

```
<html>
<head></head>
<body>

<p><i>苹果</i></p>
<p><i>电脑</i></p>
<p>中文</p>
<p>Chinese</p>

</body>
</html>

```
我们要把页面中的i标签里的文本给翻译，页面中有i标签的分别是苹果、电脑。于是我们实现需要一个翻译库：
```
var translate = {
    '苹果' : 'apple',
    '电脑' : 'PC'
};
```
然后我可以这样执行翻译替换
```
$('i').each(function(){
    $(this).replaceWith(translate[$(this).html()]);
});
```
执行后效果：

```
<html>
<head></head>
<body>

<p>apple</p>
<p>PC</p>
<p>中文</p>
<p>Chinese</p>

</body>
</html>

```
replaceWith和replaceAll

相同点：他们二个都可以进行，查找替换

不同点：写法不同，反正我是没有发现，他们二个有什么功能上的不同。
```
<script type="text/javascript">
 $(document).ready(function(){
 $("strong").replaceAll($("div")); //用strong标签把div标签都替换掉
//$("div").replaceWith($("strong")); //和上面的一样，没什么区别，写法不同
 });
</script>
```

#js获取当前页面url
在WEB开发中，时常会用到javascript来获取当前页面的url网址信息，在这里是我的一些获取url信息的小总结。

下面我们举例一个URL，然后获得它的各个组成部分：http://i.cnblogs.com/EditPosts.aspx?opt=1

1、window.location.href(设置或获取整个 URL 为字符串)

var test = window.location.href;
alert(test);
返回：http://i.cnblogs.com/EditPosts.aspx?opt=1

2、window.location.protocol(设置或获取 URL 的协议部分)

var test = window.location.protocol;
alert(test);
返回：http:

3、window.location.host(设置或获取 URL 的主机部分)

var test = window.location.host;
alert(test);
返回：i.cnblogs.com

4、window.location.port(设置或获取与 URL 关联的端口号码)

var test = window.location.port;
alert(test);
返回：空字符(如果采用默认的80端口(update:即使添加了:80)，那么返回值并不是默认的80而是空字符)

5、window.location.pathname(设置或获取与 URL 的路径部分（就是文件地址）)
var test = window.location.pathname;
alert(test);
返回：/EditPosts.aspx

6、window.location.search(设置或获取 href 属性中跟在问号后面的部分)

var test = window.location.search;
alert(test);
返回：?opt=1

PS：获得查询（参数）部分，除了给动态语言赋值以外，我们同样可以给静态页面，并使用javascript来获得相信应的参数值。

7、window.location.hash(设置或获取 href 属性中在井号“#”后面的分段)

var test = window.location.hash;
alert(test);
返回：空字符(因为url中没有)

js-正则
```
function getQueryString(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}
// 这样调用：
alert(GetQueryString("参数名1"));
  
alert(GetQueryString("参数名2"));
  
alert(GetQueryString("参数名3"));
```

js-split
```
function GetRequest() {
  var url = location.search; //获取url中"?"符后的字串
  var theRequest = new Object();
  if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    strs = str.split("&");
    for(var i = 0; i < strs.length; i ++) {
      theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
    }
  }
  return theRequest;
}
var Request = new Object();
Request = GetRequest();<br>// var id=Request["id"]; 
// var 参数1,参数2,参数3,参数N;
// 参数1 = Request['参数1'];
// 参数2 = Request['参数2'];
// 参数3 = Request['参数3'];
// 参数N = Request['参数N'];
```
指定取

比如说一个url：http://i.cnblogs.com/?j=js,我们想得到参数j的值，可以通过以下函数调用。
```
function GetQueryString(name) { 
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
  var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
  var context = ""; 
  if (r != null) 
     context = r[2]; 
  reg = null; 
  r = null; 
  return context == null || context == "" || context == "undefined" ? "" : context; 
}
alert(GetQueryString("j"));
```
单个参数的获取方法
```
function GetRequest() {
  var url = location.search; //获取url中"?"符后的字串
  if (url.indexOf("?") != -1) {  //判断是否有参数
   var str = url.substr(1); //从第一个字符开始 因为第0个是?号 获取所有除问号的所有符串
   strs = str.split("=");  //用等号进行分隔 （因为知道只有一个参数 所以直接用等号进分隔 如果有多个参数 要用&号分隔 再用等号进行分隔）
   alert(strs[1]);     //直接弹出第一个参数 （如果有多个参数 还要进行循环的）
  }
}
```



#js自定义事件的触发

### 1\. 事件的创建

JS中，最简单的创建事件方法，是使用Event构造器：

var myEvent = new Event('event\_name');

但是为了能够传递数据，就需要使用 CustomEvent 构造器：

[![复制代码](//common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

var myEvent = new CustomEvent('event\_name', {
    detail:{ // 将需要传递的数据写在detail中，以便在EventListener中获取
        // 数据将会在event.detail中得到
 },
});

[![复制代码](//common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

### 2\. 事件的监听

JS的EventListener是根据事件的名称来进行监听的，比如我们在上文中已经创建了一个名称为**‘event\_name’** 的事件，那么当某个元素需要监听它的时候，就需要创建相应的监听器：

[![复制代码](//common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

//假设listener注册在window对象上
window.addEventListener('event\_name', function(event){ // 如果是CustomEvent，传入的数据在event.detail中
    console.log('得到数据为：', event.detail); // ...后续相关操作
});

[![复制代码](//common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

至此，window对象上就有了对**‘event\_name’** 这个事件的监听器，当window上触发这个事件的时候，相关的callback就会执行。

### 3\. 事件的触发

对于一些内置（built-in）的事件，通常都是有一些操作去做触发，比如鼠标单击对应MouseEvent的click事件，利用鼠标（ctrl+滚轮上下）去放大缩小页面对应WheelEvent的resize事件。  
然而，自定义的事件由于不是JS内置的事件，所以我们需要在JS代码中去显式地触发它。方法是使用 **dispatchEvent** 去触发（IE8低版本兼容，使用fireEvent）：

[![复制代码](//common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

// 首先需要提前定义好事件，并且注册相关的EventListener
var myEvent = new CustomEvent('event\_name', { 
    detail: { title: 'This is title!'},
});
window.addEventListener('event\_name', function(event){
    console.log('得到标题为：', event.detail.title);
}); // 随后在对应的元素上触发该事件
if(window.dispatchEvent) {  
    window.dispatchEvent(myEvent);
} else {
    window.fireEvent(myEvent);
} // 根据listener中的callback函数定义，应当会在console中输出 "得到标题为： This is title!"

[![复制代码](//common.cnblogs.com/images/copycode.gif)](javascript:void(0); "复制代码")

需要特别注意的是，当一个事件触发的时候，如果相应的element及其上级元素没有对应的EventListener，就不会有任何回调操作。   
对于子元素的监听，可以对父元素添加事件托管，让事件在事件冒泡阶段被监听器捕获并执行。这时候，使用event.target就可以获取到具体触发事件的元素。



##模拟鼠标事件

```
        var btn = document.querySelector('.button');
        btn.addEventListener('click', function (event) {
            console.log('OH~!You clicked me~!');
        }, false);
        var ev = new MouseEvent('click', {
            cancelable: true,
            bubble: true,
            view: window
        });
        btn.dispatchEvent(ev);
```


You can simulate the mouseover event like this:

HTML
```
<div id="name">My Name</div>
```
JavaScript
```
var element = document.getElementById('name');
element.addEventListener('mouseover', function() {
  console.log('Event triggered');
});

var event = new MouseEvent('mouseover', {
  'view': window,
  'bubbles': true,
  'cancelable': true
});

element.dispatchEvent(event);
```
需要特别注意的是，当一个事件触发的时候，如果相应的element及其上级元素没有对应的EventListener，就不会有任何回调操作。 
对于子元素的监听，可以对父元素添加事件托管，让事件在事件冒泡阶段被监听器捕获并执行。这时候，使用event.target就可以获取到具体触发事件的元素。

#其他常用事件
当然,在构建这个MouseEvent对象的时候还是有很多属性可以填写的,不过,可能就是示例的那几个比较有用,如果像查看更多的属性,请查看如下地址
(由于MouseEvent继承自UIEvent和Event,所以,他也继承了他们的属性)
https://developer.mozilla.org/en-US/docs/Web/API/MouseEvent
https://developer.mozilla.org/en-US/docs/Web/API/UIEvent
https://developer.mozilla.org/en-US/docs/Web/API/Event
想查看MouseEvent()构造器的具体用法,请查看
https://developer.mozilla.org/en-US/docs/Web/API/MouseEvent/MouseEvent
https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent
Event()的详细说明
https://developer.mozilla.org/en-US/docs/Web/API/Event/Event
customEvent() 的详细说明
https://developer.mozilla.org/en-US/docs/Web/API/CustomEvent

KeyboardEvent.key在MDN上的文档被提示为推荐使用的属性,而KeyboardEvent.keyCode却被说成是不推荐使用的,应该使用key属性,然而你去看****KeyboardEvent.key****的文档就会发现,这个属性压根就没得到多少浏览器的支持,如果用这个属性,简直就是掉坑里了.
下图所示,一大片的红字啊


#js中Window和window的区别是什么？

```
> window instanceof Window
< true
```

Window 是接口， window 是实例，全局变量是 window 的属性。


# Promise

> Promise 是异步编程的一种解决方案，比传统的解决方案——回调函数和事件——更合理且更强大。它最早由社区提出并实现，ES6将其写进了语言标准，统一了用法，并原生提供了Promise对象。

### 特点

1. 对象的状态不受外界影响 （3种状态）
    - Pending状态（进行中）
    - Fulfilled状态（已成功）
    - Rejected状态（已失败）
2. 一旦状态改变就不会再变 （两种状态改变：成功或失败）
    - Pending -> Fulfilled
    - Pending -> Rejected

### 用法

#### 创建Promise实例

```jsx
var promise = new Promise(function(resolve, reject){
    // ... some code
    
    if (/* 异步操作成功 */) {
        resolve(value);
    } else {
        reject(error);
    }
})
```

  Promise构造函数接受一个函数作为参数，该函数的两个参数分别是`resolve`和`reject`。它们是两个函数，由JavaScript引擎提供，不用自己部署。  
  resolve作用是将Promise对象状态由“未完成”变为“成功”，也就是`Pending -> Fulfilled`，在异步操作成功时调用，并将异步操作的结果作为参数传递出去；而reject函数则是将Promise对象状态由“未完成”变为“失败”，也就是`Pending -> Rejected`，在异步操作失败时调用，并将异步操作的结果作为参数传递出去。

#### then

  Promise实例生成后，可用`then`方法分别指定两种状态回调参数。then 方法可以接受两个回调函数作为参数：

1. Promise对象状态改为Resolved时调用 （必选）
2. Promise对象状态改为Rejected时调用 （可选）

#### 基本用法示例

```jsx
function sleep(ms) {
    return new Promise(function(resolve, reject) {
        setTimeout(resolve, ms);
    })
}
sleep(500).then( ()=> console.log("finished"));
```

  这段代码定义了一个函数sleep，调用后，等待了指定参数(500)毫秒后执行then中的函数。值得注意的是，Promise新建后就会立即执行。

#### 执行顺序

  接下来我们探究一下它的执行顺序，看以下代码：

```jsx
let promise = new Promise(function(resolve, reject){
    console.log("AAA");
    resolve()
});
promise.then(() => console.log("BBB"));
console.log("CCC")

// AAA
// CCC
// BBB
```

  执行后，我们发现输出顺序总是 `AAA -> CCC -> BBB`。表明，在Promise新建后会立即执行，所以`首先输出 AAA`。然后，then方法指定的回调函数将在当前脚本所有同步任务执行完后才会执行，所以`BBB 最后输出`。

#### 与定时器混用

  首先看一个实例：

```jsx
let promise = new Promise(function(resolve, reject){
    console.log("1");
    resolve();
});
setTimeout(()=>console.log("2"), 0);
promise.then(() => console.log("3"));
console.log("4");

// 1
// 4
// 3
// 2
```

  可以看到，结果输出顺序总是：`1 -> 4 -> 3 -> 2`。1与4的顺序不必再说，而2与3先输出Promise的then，而后输出定时器任务。原因则是Promise属于JavaScript引擎内部任务，而setTimeout则是浏览器API，而引擎内部任务优先级高于浏览器API任务，所以有此结果。

### 拓展 async/await

#### async

  顾名思义，异步。async函数对 Generator 函数的改进，async 函数必定返回 Promise，我们把所有返回 Promise 的函数都可以认为是异步函数。特点体现在以下四点：

- 内置执行器
- 更好的语义
- 更广的适用性
- 返回值是 Promise

#### await

  顾名思义，等待。正常情况下，await命令后面是一个 Promise 对象，返回该对象的结果。如果不是 Promise 对象，就直接返回对应的值。另一种情况是，await命令后面是一个thenable对象（即定义then方法的对象），那么await会将其等同于 Promise 对象。

#### 混合使用

  先看示例：

```jsx
function sleep(ms) {
    return new Promise(function(resolve, reject) {
        setTimeout(resolve,ms);
    })
}
async function handle(){
    console.log("AAA")
    await sleep(5000)
    console.log("BBB")
}

handle();

// AAA
// BBB (5000ms后)

```

  我们定义函数sleep，返回一个Promise。然后在handle函数前加上async关键词，这样就定义了一个async函数。在该函数中，利用await来等待一个Promise。

### Promise优缺点

| 优点 | 缺点 |
| --- | --- |
|解决回调|无法监测进行状态|
|链式调用| 新建立即执行且无法取消|
|减少嵌套|内部错误无法抛出|

>https://www.runoob.com/w3cnote/javascript-promise-object.html


#异步请求进来的js文件怎么打断点？

1.chrome直接控制台

---
2.在需要调试的代码行前添加一句 `debugger`

形如

```
function demo () {
  // 在这里停下
  debugger
  // ...
}
```

这样不论是异步还是同步，都可以非常轻松地在相应位置打开调试器来进行调试了。

---





#ES module工作原理

>https://segmentfault.com/a/1190000020388889

本文参考 [https://hacks.mozilla.org/201...](https://hacks.mozilla.org/2018/03/es-modules-a-cartoon-deep-dive/)，建议大家读原文。

ES6发布了官方的，标准化的Module特性，这一特性花了整整10年的时间。但是，在这之前，大家也都在模块化地编写JS代码。比如在server端的NodeJS，它是对CommonJS的一个实现；Require.js则是可以在浏览器使用，它是对AMD的一个实现。

ES6官方化了模块，使得在浏览器端不再需要引入额外的库来实现模块化的编程（当然浏览器的支持与否，这里暂不讨论）。ES Module的使用也很简单，相关语法也很少，核心是import和export。但是，对于ES module到底是如何工作的，它又和之前的CommonJS和AMD有什么差别呢？这是接下来将要讨论的内容。

**一：没有模块化的编程存在什么问题？**

编写JS代码，主要是对于对变量的操作：给变量赋值或者变量之间进行各种运算。正因为大部分代码都是对变量的操作，所以如何组织代码里面的变量对于如何写好代码和代码维护就显得至关重要了。

当只有少量的变量需要考虑的时候，JavaScript提供了“scope（作用域）”来帮助你。因为在JavaScript里面，一个function不能访问定义在别的function里面的变量。

但是，这同时也带来一个问题，假如functionA想要使用functionB的变量怎么办呢？一个通用的办法就是把functionB的变量放到functionA的上一层作用域。典型的就是jQuery时代，如果要使用jQuery的API，先要保证jQuery在全局作用域。  
但是这样做的问题也很多：

<div class="widget-codetool" style="display:none;">

<div class="widget-codetool--inner"><span class="selectCode code-tool" data-toggle="tooltip" data-placement="top" title="" data-original-title="全选"></span><span type="button" class="copyCode code-tool" data-toggle="tooltip" data-placement="top" data-clipboard-text="1: 所有的script标签必须保证正确的顺序，这使得代码的维护变得异常艰难。
2: 全局作用域被污染。
" title="" data-original-title="复制"></span></div>

</div>

    1: 所有的script标签必须保证正确的顺序，这使得代码的维护变得异常艰难。
    2: 全局作用域被污染。

**二：模块化编程如何解决上面提到的问题？**

模块，把相关的变量和function组织到一起，形成一个所谓的module scope（模块作用域）。在这个作用域里面的变量和function之间彼此是可见的。

与function不同的是，一个模块可以决定自己内部的哪些变量，类，或者function可以被其他模块可见，这个决定我们叫做“export（导出）”。而其他的模块也就可以选择性地使用这个模块导出的内容，我们通过“import（导入）”来实现。

一旦有了导入和导出，我们就可以把我们的程序按照指责划分为一个个模块，大的模块可以继续划分为更小的模块，最终这些模块组合到一起，搭建起了我们整个程序，就像乐高一样。

**三：ES Module的工作原理之Module Instances**

当你在模块化编程的时候，你就会创建一棵依赖树。不同依赖之间的链接来源于你使用的每一条"import"语句。

就是通过这些"import"语句，浏览器和Node才知道它们到底要加载哪些代码。你给浏览器或者Node一个依赖树的入口文件，从这个入口文件开始，浏览器或者Node就沿着每一条"import"语句找到下面的代码。  
<span class="img-wrap">![图片描述](/img/bVbxIjv?w=996&h=584 "图片描述")</span>

但是，浏览器却使用不了这些文件。所有的文件都必须要转变为一系列被叫做“Module Records（模块记录）的数据结构，这样浏览器才能明白这些文件的内容。  
<span class="img-wrap">![图片描述](/img/bVbxIjn?w=844&h=580 "图片描述")</span>

在这之后，module record需要被转化为“module instance（模快实例）”。一个module instance包含2种东西：code和state。

code就是一系列的操作指令，就像菜单一样。但是，光有菜单，并不能作出菜，你还需要原材料。而state就是原材料。State就是变量在每一个特地时间点的值。当然，这些变量只是内存里面一个个保存着值的小盒子的小名而已。

而我们真正需要的就是每一个模块都有一个module instance。模块的加载就是从这个入口文件开始，最后得到包含所有module instance的完整图像。

**四：Module Instances的产生步骤**

对于，ES Module来说，这需要经历三个步骤：

<div class="widget-codetool" style="display: none;">

<div class="widget-codetool--inner"><span class="selectCode code-tool" data-toggle="tooltip" data-placement="top" title="" data-original-title="全选"></span><span type="button" class="copyCode code-tool" data-toggle="tooltip" data-placement="top" data-clipboard-text="1: Construction（构造）- 找到，下载所有的文件并且解析为module records。
2: Instantiation（实例化）- 在内存里找到所有的“盒子”，把所有导出的变量放进去（但是暂时还不求值）。然后，让导出和导入都指向内存里面的这些盒子。这叫做“linking(链接)”。
3: Evaluation（求值）- 执行代码，得到变量的值然后放到这些内存的“盒子”里。
" title="" data-original-title="复制"></span></div>

</div>

    1: Construction（构造）- 找到，下载所有的文件并且解析为module records。
    2: Instantiation（实例化）- 在内存里找到所有的“盒子”，把所有导出的变量放进去（但是暂时还不求值）。然后，让导出和导入都指向内存里面的这些盒子。这叫做“linking(链接)”。
    3: Evaluation（求值）- 执行代码，得到变量的值然后放到这些内存的“盒子”里。

<span class="img-wrap">![图片描述](/img/bVbxJLo?w=1012&h=356 "图片描述")</span>

大家都说ES Module是异步的。你可以认为它是异步的，因为这些工作被分成了三个不同的步骤 - loading（下载），instantiating(实例化)和evaluating（求值） - 并且这些步骤可以单独完成。

这意味着ES Module规范采用了一种在CommonJS里面不存在的异步机制。在CommonJS里面，对于一个模块和它底下的依赖来说，下载，实例化，和求值都是一次性完成的，步骤相互之间没有任何停顿。

然而，这并不意味这这些步骤必须是异步的，它们也可以同步完成。这依赖于“loading（下载）”是由谁去做的。因为，并不是所有的东西都由ES module规范控制。事实上，确实有两部分的工作是由别的规范负责的。

[ES module规范](https://tc39.es/ecma262/#sec-modules) 陈述了你应该怎样把文件解析为module records，和怎样初始化模块以及求值。然而，它却没有说在最开始要怎样得到这些文件。

是loader（下载器）去获取到了文件。而loader对于不同的规范来说是特定的。对于浏览器来说，这个规范是[HTML 规范](https://html.spec.whatwg.org/#fetch-a-module-script-tree)。你可以根据你所使用的平台来得到不同的loader。

<span class="img-wrap">![图片描述](/img/bVbxJPK?w=884&h=594 "图片描述")</span>

loader也控制着模块如何加载。它会调用ES module的方法--ParseModule, Module.Instantiate,和Module.Evaluate。loader就像傀儡师，操纵着JS引擎的线。

现在让我们来具体聊一聊每一个步骤。  
**五：Module Instances的产生步骤之Construction**

对于每一个模块来说，在这一步会经历以下几个步骤

<div class="widget-codetool" style="display:none;">

<div class="widget-codetool--inner"><span class="selectCode code-tool" data-toggle="tooltip" data-placement="top" title="" data-original-title="全选"></span><span type="button" class="copyCode code-tool" data-toggle="tooltip" data-placement="top" data-clipboard-text="1: 弄清楚去哪里下载包含模块的文件（又叫“ module resolution（模块识别）”）
2: 获取文件（通过从一个URL下载或者从文件系统加载）
3: 把文件解析为module record（模块记录）
" title="" data-original-title="复制"></span></div>

</div>

    1: 弄清楚去哪里下载包含模块的文件（又叫“ module resolution（模块识别）”）
    2: 获取文件（通过从一个URL下载或者从文件系统加载）
    3: 把文件解析为module record（模块记录）

**step1： Finding the file and fetching it 找到文件并获取文件**

loader会负责找到文件并下载。首先，需要找到入口文件，在HTML文件里，我们通过使用<script>标签告诉loader哪里去找到入口文件。

<span class="img-wrap">![图片描述](/img/bVbxJTZ?w=878&h=380 "图片描述")</span>

但是，loader如何找到接下来的一系列模块 - 也就是main.js所直接依赖的哪些模块呢？这就轮到import语句登场了。import语句的某一部分又被叫做“模块说明符”。它告诉loader在哪儿可以找到下一个模块。  
<span class="img-wrap">![图片描述](/img/bVbxJU6?w=970&h=242 "图片描述")</span>

关于“模块说明符”，有一点需要说明：某些时候，不同的浏览器和Node之间，需要不同的处理方式。每一个平台都有它们自己的方法去诠释“模块说明符”字符串。而这通过“模块识别算法”完成，不同的平台不一样。就目前来说，一些在Node环境工作的模块识别符在浏览器里面并不工作，但是这一情况[正在被处理修复](https://github.com/domenic/package-name-maps)。

而在修复之前，浏览器只接受URL作为模块标识符。浏览器会从那个URL下载模块文件。但是，对于整个依赖图来说，在同一时间是不可能的。因为直到解析了这个文件，你才知道这个模块需要哪些依赖。。。但是，你又不能解析这个文件除非你获取了它。

这意味着，要解析一个文件，我们必须一层一层地遍历这颗依赖树，理清楚他所有的依赖，然后找到并且下载这些依赖。但是，假如主线程一直在等待这些文件下载，那么大量的其他的任务就被卡在队列里面。这是因为，在浏览器里面进行下载工作，会耗费大量的时间。

像这样阻塞主线程，会导致使用了模块的app太慢了，这也是ES module规范把算法分割成多个步骤的其中一个原因。把construction（构建）单独划分到一个步骤，这就允许浏览器可以在进入到instantiating（实例化）的一系列同步工作之前可以先获取文件并且建立模块之间的依赖树。

把这个算法分割到不同的步骤--正是ES Module和CommonJS module之间的其中一个关键区别。

CommonJS可以做不同于ES Module的处理，是因为从文件系统里面加载文件比从网络上下载文件要花少得多的时间。这就意味着，Node可以在加载文件的时候阻塞主线程。又因为文件已经加载好了，那么实例化和求值（这两步在CommomJS里面是没有分开的）也显得很有道理。这意味着，在你返回这个模块之前，其依赖树上所有的依赖都完成了loading(加载)，instantiating(实例化)和evaluating（求值）。  
<span class="img-wrap">![图片描述](/img/bVbxJ9W?w=950&h=562 "图片描述")</span>

CommonJS的方法会带来一些后果，后面会解释。但是，其中有一点是在Node里面的CommomJS module， 你可以在模块说明符里面使用`变量`。在你寻找下一个模块之前，你会执行完本模块的所有代码。这就意味着当你去做模块识别的时候，这个变量已经有值了。

但是，在ES Module里面，你是在任何求值之前先建立了完整的依赖树。这说明，你不能在模块说明符里面使用变量，因为这个变量目前还没有值。

<span class="img-wrap">![图片描述](/img/bVbxKb8?w=888&h=286 "图片描述")</span>

但是动态模块，在实际生产中又是有用的。所以有一个提议叫做[动态导入](https://github.com/tc39/proposal-dynamic-import)，可以用来满足类似这样的需求：`import(`${path}/foo.js`).`

动态导入的工作原理是，任何使用`import()`来导入的文件，都会作为一个入口文件从而创建一棵单独的依赖树，被单独处理。

<span class="img-wrap">![图片描述](/img/bVbxKeK?w=958&h=746 "图片描述")</span>

但有一点需要注意的是 - 任何同时存在于两棵依赖树的模块都指向同一个模块实例。这是因为loader把模块实例缓存起来了。对于每一个模块来说，在一个特定的全局作用域内，只会有一个模版实例。

这对JS引擎来说，就意味着更少的工作量。举个例子，无论多少模块依赖着某一个模块，但是这个模块文件都只会被获取一次。loader使用[module map](https://html.spec.whatwg.org/multipage/webappapis.html#module-map)来管理这些缓存，每一个全局作用域使用独立的module map来管理各自的缓存。

当loader通过一个URL去获取文件的时候，它会把这个URL放入module map并且做上“正在获取”的标志。然后它发出请求，进而继续下一个文件的获取工作。

当别的模块也依赖同一个文件的时候，会发生什么呢？Loader会查询module map里面的每一个URL，如果它看到这个URL有“正在获取“的标志，那它就不管了，继续下一个URL的处理。

module map不只是看哪个文件正在被下载，它同时也管理这模块的缓存，这就是下面的内容。

**step2: Parsing**

现在我们已经获取到了文件，我们需要把它解析为一个module record。这有助于浏览器理解模块的不同之处是什么。

<span class="img-wrap">![图片描述](/img/bVbxKGV?w=964&h=406 "图片描述")</span>

一旦module record创建完成，它就会被放到module map里面去。这意味着无论何时被请求，loader都可以从module map里面提取它。

<span class="img-wrap">![图片描述](/img/bVbxKHx?w=944&h=470 "图片描述")</span>

在解析的时候，有一个看起来琐碎但是却会产生巨大影响的细节：所有的模块都是在相当于在文件顶部使用了“`use strict`”（严格模式）下被解析的。除此之外，也还有其他的一些不同，例如：关键字`await`被保留在模块的最高层的代码里；`this`的值是`undefined`。

不同的解析方法被称作“解析目标”。假如你用不同的解析目标解析同一个文件，你将会得到不同的解析结果。因为，在解析之前，你需要知道将要被解析的文件是否是模块。

在浏览器里面，这十分简单。你只需要给<script>标签加一个`type="module"`。这就告诉了浏览器这个文件需要被当成是一个模块来解析。因为只有模块才可以被导入，所以浏览器知道导入的文件也是模块。

但是Node不使用HTML相关的标签，所以无法使用type来表示。而在Node里面是通过文件的扩展名".mjs"来表明这是一个ES Module的。

不管是哪种方式，最终都是loader来决定这个文件是否当作一个模块来解析。假如它是一个module或者有`import`，那就会开始这个进程，直到所有的文件被下载和解析。

这一步骤就结束了。在加载进程结束之后，我们就从拥有一个入口文件到最后拥有一系列的module record。

<span class="img-wrap">![图片描述](/img/bVbxKKx?w=800&h=738 "图片描述")</span>

下一步就是实例化这些模块，并且把所有的实例链接起来。  
**六：Module Instances的产生步骤之Instantiation**

如我之前提过的那样，一个实例结合了code和state。state存在于内存中，因此实例化这一步就是关于怎样把东西链接到内存里面的。

首先，JS引擎创建了一个“模块环境记录（module environment record）”。它管理着module record的变量，然后它在内存里面找到所有导出（export）的变量的“盒子”。module environment record会一直监控着内存里面的哪个盒子和哪个export是相关联的。

这些内存里面的盒子还没有获得它们的值，只有在求值这一步骤完成之后，真正的值才会被填充进去。但是这里有个小小的警告：任何导出的function定义，都是在这一步初始化的，这使得求值变得相对简单一些。

为了实例化模块图（module graph），JS引擎会做一个所谓的“深度优先后序遍历”的操作。意思就是说，JS引擎会先走到模块图的最底层--找到不依赖任何其他模块的那些模块，并且设置好它们的导出（export）。  
<span class="img-wrap">![图片描述](/img/bVbxKRf?w=952&h=378 "图片描述")</span>

当JS引擎完成一个模块的所有导出的链接，它就会返回上一个层级去设置来自于这个模块的导入（import）。需要注意的是，导出和导入都是指向同一片内存地址。先链接导出保证了所有的导入都能找到对应的导出。  
<span class="img-wrap">![图片描述](/img/bVbxKSS?w=954&h=362 "图片描述")</span>

这和CommonJS的模块不同。在CommonJS，导入的对象是基于导出拷贝的。这就意味着导出的任何的数值（例如数字）都是拷贝。这就意味着，如果导出模块在之后修改了一些值，导入的模块并不会被同步到这些修改。  
<span class="img-wrap">![图片描述](/img/bVbxKTd?w=950&h=222 "图片描述")</span>

于此相反的是，ES module使用所谓的“实时绑定”，导出的模块和导入的模块都指向同一段内存地址。如果，导出模块修改了一个值，那么这个修改会在导入模块里面也得到体现。

导出值的模块可以在任何时间修改这些值，但是导入模块却不能修改它们导入的值。意思就是，如果一个模块导出了一个对象（object），那它可以修改这个对象的属性值。

“实时绑定”的好处是，不需要跑任何的代码，就可以链接起所有的模块。这有助于当存在循环依赖情况下的求值。

在这一步的最后，我们使得所有的模块实例导出/导入的变量的内存地址链接起来了。

接下来，我们就开始对代码求值，并且把得到的值填入对应的内存地址中。

**七：Module Instances的产生步骤之Evaluation**

最后一步是把值都填入内存地址中。JS引擎通过执行最上层的代码-也就是function以外的代码，来实现这一目的。  
<span class="img-wrap">![图片描述](/img/bVbxKUL?w=790&h=308 "图片描述")</span>

除了往内存地址里面填值，对代码求值有可能也会触发副作用。举个例子，一个模块有可能会向server做请求。因为这个副作用，你只想求模块求值一次。和在实例化阶段的链接无论执行多少次都会得到同一个结果不同，求值会根据你进行了多少次求值操作而得到不同的结果。

这也是为什么需要module map。Module map根据URL来缓存模块，因为每一个模块都只有一个module record，这也保证了每一个模块只会被执行一次。和实例化一样，求值也是按照深度优先倒序的规则来的。

在一个循环依赖的情况下，最终会在依赖树里得到一个环，为了仅仅是说明问题，这里就用一个最简单的例子：  
<span class="img-wrap">![图片描述](/img/bVbxKXb?w=956&h=422 "图片描述")</span>  
我们先来看看CommonJS，它是怎么工作的。首先，main模块会执行到require语句，然后进入到counter模块。Counter模块尝试去从访问导出的对象里面的message变量。但是，因为这个变量还没有在main模块里面被求值，所以会返回undefined。JS引擎会在内存里面为这个本地变量开辟一段地址并把它的值设置为undefined。  
<span class="img-wrap">![图片描述](/img/bVbxKXQ?w=966&h=228 "图片描述")</span>

求值一直继续到counter模块的最底部。我们想知道最终是否能得到message的值（也就是main模块求值之后），于是我们设置了一个timeout。然后，同样的求值过程在main模块重新开始。

<span class="img-wrap">![图片描述](/img/bVbxKZL?w=960&h=414 "图片描述")</span>

message变量会被初始化并且放到内存中。但是，因为这两者之间已经没有任何链接，所以在counter模块里，message变量会保持为undefined。  
<span class="img-wrap">![图片描述](/img/bVbxKZ4?w=966&h=406 "图片描述")</span>

假如这个导出是用“实时绑定”处理的，counter模块最终就能得到正确的值。到timeout执行的时候，main模块的求值就完成了并且得到最终的值。

支持循环依赖，是ES module设计的一个重要基础。正是前面的“三个阶段”使得这一切成为可能。





---


# 1模块化历史

## 1.1前言

参照[前端模块化开发的价值](https://link.jianshu.com?t=https%3A%2F%2Fgithub.com%2Fseajs%2Fseajs%2Fissues%2F547)

## 1.2无模块化

每次说到JavaScript都会想到**Brendan Eich**花了十来天就发明了它，那就是JS的鸿蒙时期，混沌初开。  
 就像当年在校初学前端时写的代码，没有那么多的套路，就是从上往下码代码，没有想着去声明函数神马的，甚至多少代码都写在一个JS里。现在想来真是惨不忍睹。虽然本人入坑前端距那个鸿蒙时代实在久远，但是据各种典籍记载，那时候的代码风格就差不多这样子，从上往下一直堆着就好了。

<div class="_2Uzcx_">

    var a = 0;
    if (xxx) {
      // 省略100L
    }
    document.getElementById('id').onclick = function(event) {
      // 省略若干行
    }
    ......

</div>

## 1.3模块化冒泡

每个行业都有梗，现在和同事聊天有时候还会吐槽十几年前的老网站，真是有幸见过。前辈的聊天更有意思了，当年的登录居然是写死在前端代码里，就像这样子

<div class="_2Uzcx_">

    if (username === 'xxxxx' && password === 'xxxxxx') {
      // 登录成功
    }

</div>

是不是觉得很无语。当年的前端都是静态页面，没有现在这样子通过ajax和后端交互神马的，内容更是丰富多彩，更新及时。  
 前端代码愈发庞大，那么自然而然会暴露很多问题。  
 无非就俩个：

*   命名冲突
*   文件依赖

### 1.3.1命名冲突解决

1.  java风格的namespace，这个很好理解，在此不赘诉，缺点的话，自行想象，不堪
2.  自执行函数(内部变量不可见不被污染)

<div class="_2Uzcx_">

    // jQuery式的匿名自执行函数
    // 缺点是增添了全局变量、依赖需要提前提供
    (function(root) {
        root.jQuery = window.$ = jQuery; // 挂载到window之上
    })(window)

</div>

<div class="_2Uzcx_">

    // 普通自执行函数
    // 缺点就是暴露了全局变量，而且随着模块的增加，全局变量会很多
    module = function() {
        function module() {

        }
        return module;
    }()

</div>

1.  _YUI3的沙箱机制_，这个表示不晓得。

### 1.3.2文件依赖解决

这个没有解决方法，乖乖自行保证顺序和不缺漏吧

<div class="_2Uzcx_">

    <script src="https://cdn.bootcss.com/underscore.js/1.8.3/underscore-min.js"></script>
    <script src="https://cdn.bootcss.com/backbone.js/1.3.3/backbone-min.js"></script>

</div>

## 1.4CommonJS

### 1.4.1前言

随着前端的发展，到node.js被创，js可以用来写server端代码。  
 做过后端的同学肯定知道没有模块化怎么能忍呢？  
 我们通过上节所得，我们可以得出以下几点需待解决

*   模块代码安全，不可被污染也不可污染别人，沙箱呀
*   把模块接口暴露出去（得优雅呀，不能增添全局变量）
*   这个依赖顺序管理

### 1.4.2发展

这个还真没有经历了解过。度娘一番，幸好看到[seajs下的一个issues](https://link.jianshu.com?t=https%3A%2F%2Fgithub.com%2Fseajs%2Fseajs%2Fissues%2F588)。  
 大致就是大牛很牛，推出了[Modules/1.0规范](https://link.jianshu.com?t=http%3A%2F%2Fwiki.commonjs.org%2Fwiki%2FModules)。  
 之后为了推广到浏览器端，大牛产生分歧，分为三大流派：

*   Modules/1.x 流派（[](https://link.jianshu.com?t=http%3A%2F%2Fwiki.commonjs.org%2Fwiki%2FModules%2FTransport)[Modules/Transport](https://link.jianshu.com?t=http%3A%2F%2Fwiki.commonjs.org%2Fwiki%2FModules%2FTransport)  
     通过工具转换现有的CommonJS）
*   Modules/Async 流派（自立门派）
*   Modules/2.0 流（[](https://link.jianshu.com?t=http%3A%2F%2Fwiki.commonjs.org%2Fwiki%2FModules%2FWrappings)[Modules/Wrappings](https://link.jianshu.com?t=http%3A%2F%2Fwiki.commonjs.org%2Fwiki%2FModules%2FWrappings)  
     对1.0的升级）

**这里说下为什么不能用在浏览器**

*   服务端代码在硬盘，加载模块时间几乎忽略不计。浏览器端就不成了。
*   模块引用未被function，所以暴露在了全局之下。

### 1.4.3番外（AMD、CMD）

[AMD](https://link.jianshu.com?t=https%3A%2F%2Fgithub.com%2Famdjs%2Famdjs-api%2Fwiki%2FAMD-%28%25E4%25B8%25AD%25E6%2596%2587%25E7%2589%2588%29)是 RequireJS 在推广过程中对模块定义的规范化产出。  
 [CMD](https://link.jianshu.com?t=https%3A%2F%2Fgithub.com%2Fseajs%2Fseajs%2Fissues%2F242)是 SeaJS 在推广过程中对模块定义的规范化产出。

## 1.5ES Module

这个是ECMA搞得一套。和之前的区别在于人家是官方的，根正苗红，上文的是社区搞得，野生。

# 2 ES Module/CommonJS/AMD/CMD差异

## 2.1 ES Module与CommonJS的差异

### 编译时和运行时

首先说下编译时和运行时。JavaScript有俩种声明方法（声明变量和声明方法）。var/const/let和function  
 编译时，对于声明变量会在内存中开辟一块内存空间并指向变量名，且指向变量名，赋值为undefined。对于函数声明会一样的开启空间。不过赋值为声明的函数体。PS：无论顺序如何，都会先声明变量  
 运行时，执行变量初始化之类的。

<div class="_2Uzcx_">

    // 源码
    var a = 3;
    function f() {
        return 'f';
    }

    // 编译时
    var a = undefined;
    var f = function() {
        return 'f';
    }
    // 运行时
    a = 3;

</div>

CommonJS模块是对象，是运行时加载，运行时才把模块挂载在exports之上（加载整个模块的所有），加载模块其实就是查找对象属性。  
 ES Module不是对象，是使用export显示指定输出，再通过import输入。此法为编译时加载，编译时遇到import就会生成一个只读引用。等到运行时就会根据此引用去被加载的模块取值。所以不会加载模块所有方法，仅取所需。

*   CommonJS 模块输出的是一个值的拷贝，ES6 模块输出的是值的引用。
*   CommonJS 模块是运行时加载，ES6 模块是编译时输出接口。  
     [详情参见](https://link.jianshu.com?t=http%3A%2F%2Fes6.ruanyifeng.com%2F%23docs%2Fmodule-loader%23ES6-%25E6%25A8%25A1%25E5%259D%2597%25E4%25B8%258E-CommonJS-%25E6%25A8%25A1%25E5%259D%2597%25E7%259A%2584%25E5%25B7%25AE%25E5%25BC%2582)

## 2.2CommonJS与AMD/CMD的差异

AMD/CMD是CommonJS在浏览器端的解决方案。CommonJS是同步加载（代码在本地，加载时间基本等于硬盘读取时间）。AMD/CMD是异步加载（浏览器必须这么干，代码在服务端）

## 2.3AMD与CMD的差异

*   AMD是提前执行（RequireJS2.0开始支持延迟执行，不过只是支持写法，实际上还是会提前执行），CMD是延迟执行
*   AMD推荐依赖前置，CMD推荐依赖就近

# 3 用法

## 3.1 CommonJS

<div class="_2Uzcx_">

    // 导出使用module.exports，也可以exports。exports指向module.exports；即exports = module.exports
    // 就是在此对象上挂属性
    // commonjs
    module.exports.add = function add(params) {
        return ++params;
    }
    exports.sub = function sub(params) {
        return --params;
    }

    // 加载模块使用require('xxx')。相对、绝对路径均可。默认引用js，可以不写.js后缀
    // index.js
    var common = require('./commonjs');
    console.log(common.sub(1));
    console.log(common.add(1));

</div>

## 3.2 AMD/RequireJS

*   定义模块：**define(id?, dependencies?, factory)**
*   加载模块：**require([module], factory)**

<div class="_2Uzcx_">

    // a.js
    // 依赖有三个默认的，即"require", "exports", "module"。顺序个数均可视情况
    // 如果忽略则factory默认此三个传入参数
    // id一般是不传的，默认是文件名
    define(["b", "require", "exports"], function(b, require, exports) {
        console.log("a.js执行");
        console.log(b);
    // 暴露api可以使用exports、module.exports、return
        exports.a = function() {
            return require("b");
        }
    })
    // b.js
    define(function() {
        console.log('b.js执行');
        console.log(require);
        console.log(exports);
        console.log(module);
        return 'b';
    })
    // index.js
    // 支持Modules/Wrappings写法，注意dependencies得是空的，且factory参数不可空
    define(function(require, exports, module) {
        console.log('index.js执行');
        var a = require('a');
        var b = require('b');
    })
    // index.js
    require(['a', 'b'], function(a, b) {
        console.log('index.js执行');
    })

</div>

## 3.3 CMD/SeaJS

SeaJS平时没有到，不过了解了下，丰富用法看[CMD定义规范](https://link.jianshu.com?t=https%3A%2F%2Fgithub.com%2Fseajs%2Fseajs%2Fissues%2F242)。

*   定义模块：define(factory)

<div class="_2Uzcx_">

    // a.js
    // require, exports, module参数顺序不可乱
    // 暴露api方法可以使用exports、module.exports、return
    // 与requirejs不同的是，若是未暴露，则返回{}，requirejs返回undefined
    define(function(require, exports, module) {
        console.log('a.js执行');
        console.log(require);
        console.log(exports);
        console.log(module);
    })
    // b.js
    // 
    define(function(require, module, exports) {
        console.log('b.js执行');
        console.log(require);
        console.log(exports);
        console.log(module);
    })
    // index.js
    define(function(require) {
        var a = require('a');
        var b = require('b');
        console.log(a);
        console.log(b);
    })

</div>

定义模块无需列依赖，它会调用factory的toString方法对其进行正则匹配以此分析依赖。**预先下载，延迟执行**

## 3.4 ES Module

### 输出/export

<div class="_2Uzcx_">

    // 报错1
    export 1;
    // 报错2
    const m = 1;
    export m;

    // 接口名与模块内部变量之间，建立了一一对应的关系
    // 写法1
    export const m = 1;
    // 写法2
    const m = 1；
    export { m };
    // 写法3
    const m = 1；
    export { m as module };

</div>

**PS：这个有点不是很明白，大致理解就是不能直接导出变量，但是可以导出声明（函数、变量声明）。这里的接口理解是export之后的变量，它和变量建立了映射关系。总的而言，export之后只能接声明或者语句**

### 输入/import

#### 基本用法

<div class="_2Uzcx_">

    // 类似于对象解构
    // module.js
    export const m = 1;
    // index.js
    // 注意，这里的m得和被加载的模块输出的接口名对应
    import { m } from './module';
    // 若是想为输入的变量取名
    import { m as m1 }  './module';
    // 值得注意的是，import是编译阶段，所以不能动态加载，比如下面写法是错误的。因为'a' + 'b'在运行阶段才能取到值，运行阶段在编译阶段之后
    import { 'a' + 'b' } from './module';
    // 若是只是想运行被加载的模块，如下
    // 值得注意的是，即使加载两次也只是运行一次
    import './module';
    // 整体加载
    import * as module from './module';

</div>

PS：CommonJS和ES Module是可以写一起的，但是最好不要。毕竟一个是编译阶段一个是运行阶段。就在项目中入过坑，自行体会。

#### 赋值

首先输入的模块变量是不可重新赋值的，它只是个可读引用，不过却可以改写属性

<div class="_2Uzcx_">

    // 单例
    // module.js
    export const a = {};
    // module2.js
    export { a } from './module';
    import { a as a1 } from './module';
    import { a } from './module2';
    a1.e = 3;
    console.log(a1) // { e: 3 }
    console.log(a) // { e: 3 }

</div>

### 输出/export default

<div class="_2Uzcx_">

    // module.js
    // 其实export default就是export { xxx as default }
    const m = 1；
    export default m;
    ===
    export { m as default }
    // index.js
    // 对应的输入也得相应变化
    import module from './module';
    ===
    import { default as module } from './module';

</div>

还记得之前export小结处的俩报错么？如下写法正确，因为提供了default接口

<div class="_2Uzcx_">

    // 写法1
    export default 1;
    // 写法2
    const m = 1;
    export default m;
    // 错误写法
    export default const m = 1;

</div>

PS：export default只能一次

### [复合写法](https://link.jianshu.com?t=http%3A%2F%2Fes6.ruanyifeng.com%2F%23docs%2Fmodule%23export-%25E4%25B8%258E-import-%25E7%259A%2584%25E5%25A4%258D%25E5%2590%2588%25E5%2586%2599%25E6%25B3%2595)

可用于模块间继承。比如在a模块写下如下，那么a模块不就有了./module的方法了

<div class="_2Uzcx_">

    export { a } from './module';
    export { a as a1 } from './module';
    export * from './module';

</div>

## 动态加载/import()

因为编译时加载，所以不能动态加载模块。不过幸好有import()方法。  
 这家伙返回值是一个Promise对象，所以then、catch你开心就好

<div class="_2Uzcx_">

    // 普通写法
    import('./module').then(({ a }) => {})
    // async、await
    const { a } = await import('./module');

</div>

# 4 番外自实现

<div class="_2Uzcx_">

    var MyModules = (function(){
        var modules = [];
        function define(name, deps, cb) {
            deps.forEach(function(dep, i) {
                deps[i] = modules[dep];
            });
            modules[name] = cb.apply(cb, deps);
        }
        function get(name) {
            return modules[name];
        }
        return {
            define: define,
            get: get
        };
    })();
    MyModules.define('add', [], function() {
        return function(a, b) {
                return a + b;
            };
    })
    MyModules.define('foo', ['add'], function(add) {
        var a = 3;
        var b = 4;
        return {
            doSomething: function() {
                return add(a, b) + a;;
            }
        };
    })
    var add = MyModules.get('add');
    var foo = MyModules.get('foo');
    console.log(add(1, 2));
    console.log(foo.doSomething());

</div>


>https://www.jianshu.com/p/da2ac9ad2960


---


#JS获得一个对象的所有属性和方法实例
```
function displayProp(obj){   
  var names="";    
  for(var name in obj){    
    names+=name+": "+obj[name]+", ";  
  }  
  alert(names);  
} 
```


#javascript获取原型对象的三种方法

## 代码：

<div class="_2Uzcx_">

    function Person(name){
        this.name=name;
    }
    var p = new Person("abc");

</div>

## 获取p的原型对象

## 代码：

```jsx
function Person(name){
    this.name=name;
}
var p = new Person("abc");
```

## 获取p的原型对象

- 方法一：

```css
p.__proto__
```

- 方法二：

```css
p.constructor.prototype
```

- 方法三：

```css
Object.getPrototypeOf(p)
```


#JavaScript prototype 使用介绍


用过JavaScript的同学们肯定都对prototype如雷贯耳，但是这究竟是个什么东西却让初学者莫衷一是，只知道函数都会有一个prototype属性，可以为其添加函数供实例访问，其它的就不清楚了，最近看了一些 JavaScript高级程序设计，终于揭开了其神秘面纱
用过JavaScript的同学们肯定都对prototype如雷贯耳，但是这究竟是个什么东西却让初学者莫衷一是，只知道函数都会有一个prototype属性，可以为其添加函数供实例访问，其它的就不清楚了，最近看了一些 JavaScript高级程序设计，终于揭开了其神秘面纱。
每个函数都有一个prototype属性，这个属性是指向一个对象的引用，这个对象称为原型对象，原型对象包含函数实例共享的方法和属性，也就是说将函数用作构造函数调用（使用new操作符调用）的时候，新创建的对象会从原型对象上继承属性和方法。
私有变量、函数
在具体说prototype前说几个相关的东东，可以更好的理解prototype的设计意图。之前写的一篇JavaScript 命名空间文章中提到过JavaScript的函数作用域，在函数内定义的变量和函数如果不对外提供接口，那么外部将无法访问到，也就是变为私有变量和私有函数。
复制代码 代码如下:
```
function Obj(){
                var a=0; //私有变量
                var fn=function(){ //私有函数

                }
            }```
这样在函数对象Obj外部无法访问变量a和函数fn，它们就变成私有的，只能在Obj内部使用，即使是函数Obj的实例仍然无法访问这些变量和函数
复制代码 代码如下:
```
var o=new Obj();
            console.log(o.a); //undefined
            console.log(o.fn); //undefined```
静态变量、函数
当定义一个函数后通过 “.”为其添加的属性和函数，通过对象本身仍然可以访问得到，但是其实例却访问不到，这样的变量和函数分别被称为静态变量和静态函数，用过Java、C#的同学很好理解静态的含义。
复制代码 代码如下:
```
function Obj(){

            }

            Obj.a=0; //静态变量

            Obj.fn=function(){ //静态函数

            }

            console.log(Obj.a); //0
            console.log(typeof Obj.fn); //function

            var o=new Obj();
            console.log(o.a); //undefined
            console.log(typeof o.fn); //undefined```
实例变量、函数
在面向对象编程中除了一些库函数我们还是希望在对象定义的时候同时定义一些属性和方法，实例化后可以访问，JavaScript也能做到这样
复制代码 代码如下:
```
function Obj(){
                this.a=[]; //实例变量
                this.fn=function(){ //实例方法

                }
            }

            console.log(typeof Obj.a); //undefined
            console.log(typeof Obj.fn); //undefined

            var o=new Obj();
            console.log(typeof o.a); //object
            console.log(typeof o.fn); //function```
这样可以达到上述目的，然而
复制代码 代码如下:
```
function Obj(){
                this.a=[]; //实例变量
                this.fn=function(){ //实例方法

                }
            }

            var o1=new Obj();
            o1.a.push(1);
            o1.fn={};
            console.log(o1.a); //[1]
            console.log(typeof o1.fn); //object
            var o2=new Obj();
            console.log(o2.a); //[]
            console.log(typeof o2.fn); //function```
上面的代码运行结果完全符合预期，但同时也说明一个问题，在o1中修改了a和fn，而在o2中没有改变，由于数组和函数都是对象，是引用类型，这就说明o1中的属性和方法与o2中的属性与方法虽然同名但却不是一个引用，而是对Obj对象定义的属性和方法的一个复制。
这个对属性来说没有什么问题，但是对于方法来说问题就很大了，因为方法都是在做完全一样的功能，但是却又两份复制，如果一个函数对象有上千和实例方法，那么它的每个实例都要保持一份上千个方法的复制，这显然是不科学的，这可肿么办呢，prototype应运而生。
prototype
无论什么时候，只要创建了一个新函数，就会根据一组特定的规则为该函数创建一个prototype属性，默认情况下prototype属性会默认获得一个constructor(构造函数)属性，这个属性是一个指向prototype属性所在函数的指针，有些绕了啊，写代码、上图！
复制代码 代码如下:
```
function Person(){

            }
```

image
根据上图可以看出Person对象会自动获得prototyp属性，而prototype也是一个对象，会自动获得一个constructor属性，该属性正是指向Person对象。
当调用构造函数创建一个实例的时候，实例内部将包含一个内部指针（很多浏览器这个指针名字为__proto__）指向构造函数的prototype，这个连接存在于实例和构造函数的prototype之间，而不是实例与构造函数之间。
复制代码 代码如下:
```
function Person(name){
                this.name=name;
            }

            Person.prototype.printName=function(){
                alert(this.name);
            }

            var person1=new Person('Byron');
            var person2=new Person('Frank');```
image
Person的实例person1中包含了name属性，同时自动生成一个__proto__属性，该属性指向Person的prototype，可以访问到prototype内定义的printName方法，大概就是这个样子的
image
写段程序测试一下看看prototype内属性、方法是能够共享
复制代码 代码如下:
```
function Person(name){
                this.name=name;
            }

            Person.prototype.share=[];

            Person.prototype.printName=function(){
                alert(this.name);
            }

            var person1=new Person('Byron');
            var person2=new Person('Frank');

            person1.share.push(1);
            person2.share.push(2);
            console.log(person2.share); //[1,2]```
果不其然！实际上当代码读取某个对象的某个属性的时候，都会执行一遍搜索，目标是具有给定名字的属性，搜索首先从对象实例开始，如果在实例中找到该属性则返回，如果没有则查找prototype，如果还是没有找到则继续递归prototype的prototype对象，直到找到为止，如果递归到object仍然没有则返回错误。同样道理如果在实例中定义如prototype同名的属性或函数，则会覆盖prototype的属性或函数。
复制代码 代码如下:
```
function Person(name){
                this.name=name;
            }

            Person.prototype.share=[];
            var person=new Person('Byron');
            person.share=0;

            console.log(person.share); //0而不是prototype中的[]```
构造简单对象
当然prototype不是专门为解决上面问题而定义的，但是却解决了上面问题。了解了这些知识就可以构建一个科学些的、复用率高的对象，如果希望实例对象的属性或函数则定义到prototype中，如果希望每个实例单独拥有的属性或方法则定义到this中，可以通过构造函数传递实例化参数。
复制代码 代码如下:
```
function Person(name){
                this.name=name;
            }

            Person.prototype.share=[];

            Person.prototype.printName=function(){
                alert(this.name);
            }```

>https://www.cnblogs.com/socool-hu/p/5665270.html




#js hook

### 简介

`hook`技术是软件系统设计的常用机制，也称它为钩子，使用好`hook`能极大的提高开发体验。也是我开发经常使用的东西，在js中通常使用`defineProperty`进行`hook`开发。

`defineProperty` 函数介绍：

![10ADC7A0-C515-DE67-A6AE-86BB0E73167B]

对象，默认值为true  
`enumerable:` 表示该属性是否可枚举，即是否通过for-in循环或Object.keys()返回属性，如果直接使用字面量定义对象，默认值为true  
`writable:` 能否修改属性的值，如果直接使用字面量定义对象，默认值为true  
`value:`该属性对应的值，默认为undefined

### 使用介绍

仅仅通过上面的例子不足以说明钩子是多么的实用，下面将多种使用方法补充上来。

使用方式一(增加第三方模块功能):

```jsx
class hook_test{
    test(){
        console.dir("test")
    }
}

my_hook = new hook_test
Object.defineProperty(my_hook,"nihao",{
    configurable: true, 
    enumerable: true, 
    writable: true, 
    value: function () {
        console.dir("nihao")
    }
})
my_hook.nihao()
```

输出:  
`nihao`  
解析: 可以自己给第三方模块新增功能，其实是增加在函数原型上面。

使用方式二(扩展第三方模块功能):

```jsx
class hook_test{
    test(){
        console.dir("test")
    }
}

my_hook = new hook_test
_test = my_hook.test  //存放修改前的test
Object.defineProperty(my_hook,"test",{
    configurable: true,
    enumerable: true,
    writable: true,
    value: function () {
        console.dir("test  new!")
        return _test()
    }
})
my_hook.test()
```

输出:  
`test new!`  
`test`

解析: 常用来添加监控点,或函数重封装,如重封装`axios`实现测试，线上场景区分。

使用方式三(带参数的hook)

```jsx
class hook_test{
    test(str){
        console.dir("test:" + str)
    }
}

my_hook = new hook_test
_test = my_hook.test
Object.defineProperty(my_hook,"test",{
    configurable: true,
    enumerable: true,
    writable: true,
    value: function () {
        const config = arguments[0] || {};
        return _test(config + " hook ok!")
    }
})
my_hook.test("renran")
```

输出  
`test:renran hook ok!`  
解析:参数要使用`arguments`来进行获取，它是一个`list`，多个参数用下标取值。

#### 更多信息
![6C81F51F-C535-675F-6227-14D14CACFC33]







#无头浏览器爬虫抢先执行js hook

>https://zhuanlan.zhihu.com/p/72344793?utm_source=wechat_session


## 0x00 背景介绍

最早的爬虫，只需要能够从服务端获取到HTML代码，进行分析即可，随着Web2.0的普及，越来越多的网站都必须JavaScript解析之后才能正常显示。因此这也对爬虫提出了新的要求，当前前人们已经在爬虫中集成Webkit等框架来满足这样的需求。  
本文将从实际漏洞扫描器项目中，爬虫遇到的一个问题作为切入点，简单的介绍一下爬虫过程中一些JavaScript上Mock或者Hook的技巧。

## 0x01 需求

这里需求主要有两个：

### 场景一：弹框阻碍流程

在网页中存在alert，prompt等弹出框，如果没有取消会造成webkit某些API运行异常。当前针对alert的情况，通常的框架都提供一些额外的解决方案，比如PhantomJS的onAlert()函数，Selenium的switch\_to.alert().accept()。但是我们还是想和场景二一起使用Hook的方法来解决。

### 场景二：记录指定函数被调用情况

存储型XSS的验证过程通常分为Payload的注入和执行情况验证。作为一款优秀的扫描器（没错，说的就是[华为云漏洞扫描服务](https://link.zhihu.com/?target=https%3A//www.huaweicloud.com/product/vss.html)），注入的Payload一定不能对目标系统有危害，因此我们通常会选用一些温柔的函数，比如console.log，而非alert或者随机不存在的函数。但是当第二次爬虫在爬取过程中，如何统计Payload触发的情况，就会成为一个难题。

## 0x02 JavaScript Hook

函数的Hook，其实就是在函数被调用前，对函数进行替换。

```text
var old_alert = window.alert;
window.alert = function(message){
    console.log("receive: " + message);
    old_alert(message);
}
```

上面的例子是对alert函数增加一个日志打印的功能。  
Hook很简单，现在唯一的问题就是要在函数执行之前就进行替换，很多函数是在网页加载中（head部分）或者网页加载完成后立即就执行了，没有空隙给我们替换函数。

## 0x03 注入实战

### PhantomJS

它是基于QT和Webkit的无头(Headless)浏览框架，因为其不依赖Xvfb，资源占用比较小，有段时间非常受大家欢迎。其Project的Owner已经宣布不维护了，现在版本定格在2.1.1。PhantomJS良好的接口，使其能够非常方便的支持JS代码注入。  
假如某个网页([http://fake.hack.com/location.html](https://link.zhihu.com/?target=http%3A//fake.hack.com/location.html)) 会获取地理位置，只有指定位置的用户才会进行下一步处理。

```text
var webPage = require('webpage');
var page = webPage.create();
//页面初始化之前插入一段JS
page.onInitialized = function(){
    //模拟地理定位.
    page.injectJs("fake-location.js");
};
```

fake-location.js的代码也非常简单，内容如下：

```text
window.navigator.geolocation = {
    getCurrentPosition: function (success, failure) {
        success({
            coords: {                //模拟华中科技大学产学研基地
                latitude: 22.52902,
                longitude: 113.94376
            }, timestamp: Date.now()
        });
    },
    watchPosition: function(success, failure){
        success({
            coords: {                //模拟华中科技大学产学研基地
                latitude: 22.52902,
                longitude: 113.94376
            }, timestamp: Date.now()
        });
    }
};
```

从上面的例子可以看到我们Hook了getCurrentPosition函数，它只会返回指定的经纬度。当然这一切都依赖于PhantomJS提供了injectJs这个方法以及onInitialized这个事件。

### Selenium

PhantomJS始终是小众的选择，Selenium才是主流，尤其是Chrome推出了Headless模式之后，大大提高了Selenium的效率。  
由于时间原因，本文只研究了ChromeWebDriver的情况。首先看一段代码：

```text
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
# 定义Chrome和ChromeWebDriver的路径
DRIVER_PATH = "/Users/huangjacky/program/tools/chromedriver"
CHROME_PATH = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
URL = "http://127.0.0.1:8080/test.html"
 
def main():
    # 创建一个WebDriver实例
    options = Options()
    options.add_argument("--headless")
    options.binary_location = CHROME_PATH
    driver = webdriver.Chrome(executable_path=DRIVER_PATH, chrome_options=options)
    try:
        driver.get(URL)
        driver.get_screenshot_as_file("test.png")
    except Exception as e:
        print(e)    
    finally:
        driver.close()
        driver.quit()
```

代码很简单就是打开一个网页，然后截图。这个网页的内容就是一个弹框：

```text
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>test</title>
</head>
<body>
<script>alert("hacked by Helen");</script>
</body>
</html>
```

而运行结果并不符合预期：

![](https://pic3.zhimg.com/v2-811a65d8e1dc4bc6b495e3cf783b9546_b.jpg)

![](https://pic3.zhimg.com/80/v2-811a65d8e1dc4bc6b495e3cf783b9546_720w.jpg)

截图失败了，因为有alert弹出框。  
通过查看WebDriver的API，我们发现一个函数execute\_script, 因此我们修改代码如下：

```text
driver.execute_script('window.alert=null;')
driver.get(URL)
driver.get_screenshot_as_file("test.png")
```

结果肯定是不成功的，不然就不会有这篇文章了。

大概是因为driver.get之后才会有window对象，之前执行的js代码都无效。

Google了大半天，无果。看来从WebDriver上面入手是不行了，我们将目光回到Chrome浏览器自身来，它提供Chrome Devtools Protocol来方便我们这些开发者进行定制，通过[官方文档](https://link.zhihu.com/?target=https%3A//chromedevtools.github.io/devtools-protocol/tot/Page) 的查看，我发现了Page.addScriptToEvaluateOnNewDocument这个Method是可以满足我们需求的。

Chrome Devtools Protocol的一些细节，请读者自行Google或者等我下一篇文章吧。

新的代码如下：

```text
resource = "/session/%s/chromium/send_command_and_get_result" % driver.session_id
url = driver.command_executor._url + resource
body = json.dumps({
    'cmd': 'Page.addScriptToEvaluateOnNewDocument',
    'params': {"source": "window.alert=function(msg){console.log(msg)}"}
})
response = driver.command_executor._request('POST', url, body)
if response['status']:
    raise Exception(response.get('value'))
print(response.get('value'))
driver.get(URL)
driver.get_screenshot_as_file("test.png")
```

代码运行成功，截图OK啦。

这里需要注意几点：

1. url的获取以及session，这些WebDriver里面都有方法
2. 请求的通道，这个主要复用WebDriver.command\_executor

## 0x04 结论

遇到问题，我们首先研究框架支持的情况，当框架不支持的时候，我们可以从事情本质出发，也就是框架的底座，Chrome浏览器自身。最重要的就是多Google，技术问题千万别百度。

更多精彩内容，请滑至顶部点击右上角关注小宅哦~

![](https://pic4.zhimg.com/v2-bc7bccf0c30165aa177c0ae6cae29137_b.gif)



#js作用域

>https://stackoverflow.com/questions/500431/what-is-the-scope-of-variables-in-javascript/500459?r=SearchResults#500459

## TLDR

JavaScript has lexical (also called static) scoping and closures. This means you can tell the scope of an identifier by looking at the source code.

The four scopes are:

1. Global - visible by everything
2. Function - visible within a function (and its sub-functions and blocks)
3. Block - visible within a block (and its sub-blocks)
4. Module - visible within a module

Outside of the special cases of global and module scope, variables are declared using `var` (function scope), `let` (block scope) and `const` (block scope). Most other forms of identifier declaration have block scope in strict mode.

## Overview

Scope is the region of the codebase over which an identifier is valid.

A lexical environment is a mapping between identifier names and the values associated with them.

Scope is formed of a linked nesting of lexical environments, with each level in the nesting corresponding to a lexical environment of an ancestor execution context.

These linked lexical environments form a scope "chain". Identifier resolution is the process of searching along this chain for a matching identifier.

Identifier resolution only occurs in one direction: outwards. In this way, outer lexical environments cannot "see" into inner lexical environments.

There are three pertinent factors in deciding the [scope](https://en.wikipedia.org/wiki/Scope_(computer_science)) of an [identifier](https://www.ecma-international.org/ecma-262/10.0/index.html#sec-names-and-keywords) in JavaScript:

1. How an identifier was declared
2. Where an identifier was declared
3. Whether you are in [strict mode](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Strict_mode) or [non-strict mode](https://developer.mozilla.org/en-US/docs/Glossary/Sloppy_mode)

Some of the ways identifiers can be declared:

1. `var`, `let` and `const`
2. Function parameters
3. Catch block parameter
4. Function declarations
5. Named function expressions
6. Implicitly defined properties on the global object (ie. missing out `var` in non-strict mode)
7. `import` statements
8. `eval`

Some of the locations identifiers can be declared:

1. Global context
2. Function body
3. Ordinary block
4. The top of a control structure (eg. loop, if, while etc)
5. Control structure body
6. Modules

## Declaration Styles

### var

Identifiers declared using `var` **have function scope**, apart from when they are declared directly in the global context, in which case they are added as properties on the global object and have global scope. There are separate rules for their use in `eval` functions.

### let and const

Identifiers declared using `let` and `const` **have block scope**, apart from when they are declared directly in the global context, in which case they have global scope.

Note: `let`, `const` and `var` [are all hoisted](https://stackoverflow.com/a/31222689/38522). This means that their logical position of definition is the top of their enclosing scope (block or function). However, variables declared useing `let` and `const` cannot be read or assigned to until control has passed the point of declaration in the source code. The interim period is known as the temporal dead zone.

```
function f() {
    function g() {
        console.log(x)
    }
    let x = 1
    g()
}
f() // 1 because x is hoisted even though declared with `let`!
```

### Function parameter names

Function parameter names are scoped to the function body. Note that there is a slight complexity to this. Functions declared as default arguments close over the [parameter list](https://stackoverflow.com/questions/61208843/where-are-arguments-positioned-in-the-lexical-environment/), and not the body of the function.

### Function declarations

Function declarations have block scope in strict mode and function scope in non-strict mode. Note: non-strict mode is a complicated set of emergent rules based on the quirky historical implementations of different browsers.

### Named function expressions

Named function expressions are scoped to themselves (eg. for the purpose of recursion).

### Implicitly defined properties on the global object

In non-strict mode, implicitly defined properties on the global object have global scope, because the global object sits at the top of the scope chain. In strict mode these are not permitted.

### eval

In `eval` strings, variables declared using `var` will be placed in the current scope, or, if `eval` is used indirectly, as properties on the global object.

## Examples

The following will throw a ReferenceError because the names`x`, `y` and `z` have no meaning outside of the function `f`.

```
function f() {
    var x = 1
    let y = 1
    const z = 1
}
console.log(typeof x) // undefined (because var has function scope!)
console.log(typeof y) // undefined (because the body of the function is a block)
console.log(typeof z) // undefined (because the body of the function is a block)
```

The following will throw a ReferenceError for `y` and `z`, but not for `x`, because the visibility of `x` is not constrained by the block. Blocks that define the bodies of control structures like `if`, `for` and `while`, behave similarly.

```
{
    var x = 1
    let y = 1
    const z = 1
}
console.log(x) // 1
console.log(typeof y) // undefined because `y` has block scope
console.log(typeof z) // undefined because `z` has block scope
```

In the following, `x` is visible outside of the loop because `var` has function scope:

```
for(var x = 0; x < 5; ++x) {}
console.log(x) // 5 (note this is outside the loop!)
```

...because of this behavior you need to be careful about closing over variables declared using `var` in loops. There is only one instance of variable `x` declared here, and it sits logically outside of the loop.

The following prints `5`, five times, and then prints `5` for an sixth time for the `console.log` outside the loop:

```
for(var x = 0; x < 5; ++x) {
    setTimeout(() => console.log(x)) // closes over the `x` which is logically positioned at the top of the enclosing scope, above the loop
}
console.log(x) // note: visible outside the loop
```

The following prints `undefined` because `x` is block-scoped. The callbacks are run one by one asynchronously. New behavior for `let` variables means that each anonymous function closed over a different variable named `x` (unlike it would have done with `var`), and so integers `0` through `4` are printed.:

```
for(let x = 0; x < 5; ++x) {
    setTimeout(() => console.log(x)) // `let` declarations are re-declared on a per-iteration basis, so the closures capture different variables
}
console.log(typeof x) // undefined
```

The following will NOT throw a `ReferenceError` because the visibility of `x` is not constrained by the block; it will, however, print `undefined` because the variable has not been initialised (because of the `if` statement).

```
if(false) {
    var x = 1
}
console.log(x) // here, `x` has been declared, but not initialised
```

A variable declared at the top of a `for` loop using `let` is scoped to the body of the loop:

```
for(let x = 0; x < 10; ++x) {} 
console.log(typeof x) // undefined, because `x` is block-scoped
```

The following will throw a `ReferenceError` because the visibility of `x` is constrained by the block:

```
if(false) {
    let x = 1
}
console.log(typeof x) // undefined, because `x` is block-scoped
```

Variables declared using `var`, `let` or `const` are all scoped to modules:

```
// module1.js

var x = 0
export function f() {}

//module2.js

import f from 'module1.js'

console.log(x) // throws ReferenceError
```

The following will declare a property on the global object, because variables declared using `var` within the global context, are added as properties to the global object:

```
var x = 1
console.log(window.hasOwnProperty('x')) // true
```

`let` and `const` in the global context do not add properties to the global object, but still have global scope:

```
let x = 1
console.log(window.hasOwnProperty('x')) // false
```

Function parameters can be considered to be declared in the function body:

```
function f(x) {}
console.log(typeof x) // undefined, because `x` is scoped to the function
```

Catch block parameters are scoped to the catch-block body:

```
try {} catch(e) {}
console.log(typeof e) // undefined, because `e` is scoped to the catch block
```

Named function expressions are scoped only to the expression itself:

```
(function foo() { console.log(foo) })()
console.log(typeof foo) // undefined, because `foo` is scoped to its own expression
```

In non-strict mode, implicitly defined properties on the global object are globally scoped. In strict mode you get an error.

```
x = 1 // implicitly defined property on the global object (no "var"!)

console.log(x) // 1
console.log(window.hasOwnProperty('x')) // true
```

In non-strict mode, function declarations have function scope. In strict mode they have block scope.

```
'use strict'
{
    function foo() {}
}
console.log(typeof foo) // undefined, because `foo` is block-scoped
```

## How it works under the hood

Scope is defined as the [lexical](https://stackoverflow.com/a/1047479/38522) region of code over which an identifier is valid.

In JavaScript, every function-object has a hidden `[[Environment]]` reference that is a reference to the [lexical environment](https://www.ecma-international.org/ecma-262/10.0/index.html#sec-lexical-environments) of the [execution context](https://www.ecma-international.org/ecma-262/10.0/index.html#sec-execution-contexts) (stack frame) within which it was created.

When you invoke a function, the hidden `[[Call]]` method is called. This method creates a new execution context and establishes a link between the new execution context and the lexical environment of the function-object. It does this by copying the `[[Environment]]` value on the function-object, into an [outer reference](https://www.ecma-international.org/ecma-262/10.0/index.html#sec-lexical-environments) field on the lexical environment of the new execution context.

Note that this link between the new execution context and the lexical environment of the function object is called a [closure](https://stackoverflow.com/a/111114/38522).

Thus, in JavaScript, scope is implemented via lexical environments linked together in a "chain" by outer references. This chain of lexical environments is called the scope chain, and identifier resolution occurs by [searching up the chain](https://www.ecma-international.org/ecma-262/10.0/index.html#sec-getidentifierreference) for a matching identifier.

Find out [more](https://github.com/getify/You-Dont-Know-JS/tree/2nd-ed/scope-closures).






#how to unit-test private methods(access private methods) in jquery plugins?
>https://stackoverflow.com/questions/5750279/how-to-unit-test-private-methods-in-jquery-plugins
>https://stackoverflow.com/questions/2399422/javascript-calling-a-function-written-in-an-anonymous-function-from-string-with
>https://stackoverflow.com/questions/598878/how-can-i-access-local-scope-dynamically-in-javascript
>https://philipwalton.com/articles/how-to-unit-test-private-functions-in-javascript/
>https://stackoverflow.com/questions/31364930/jasmine-how-to-spyon-instance-methods/31365042#31365042

1.

Code written inside a function in JavaScript, or _closure_ as you called it, is not necessarily isolated from the outside of that function.

It is useful to know that functions have visibility of the scope in which they are defined. Any closure you create carries the scope, and therefore functions, of the code that contains it.

This simple example with a jQuery plugin and an artificial "namespace" might serve to prove this assumption:

```
// Initialise this only when running tests
my_public_test_namespace = function(){};

jQuery.fn.makeItBlue = function() {

    makeItBlue(this);

    function makeItBlue(object) {
        object.css('color','blue');
    }

    if(typeof my_public_test_namespace != "undefined") {
        my_public_test_namespace.testHarness = function() {
            return {
                _makeItBluePrivateFn: makeItBlue
            }
        };
    }
};

$("#myElement").makeItBlue(); // make something blue, initialise plugin

console.debug(my_public_test_namespace.testHarness()._makeItBluePrivateFn);
```

But don't forget you shouldn't really test privates. ;)

---


2.
For some time now I’ve been unit testing my JavaScript with qUnit. Not only good practice, it also saves you an incredible amount of time when it comes to crossbrowser testing your scripts. The only problem I was testing private functions that are hidden within closures.


The bulk of the functions you want to test are not (and should not be) accessible from the global scope. So what I used to do is add a function that would expose the private functions I wanted to test, test them, and if all went well I’d comment out the expose-function. A bit of a hassle really…

While I was working on the documentation for TinySort when I had an idea. In the qUnit test page I don’t load the script in the head but do it via an ajax request. Then I alter the script exposing the desired private functions and then start the test… piece of cake really…

Just view the source of the TinySort unit test.
Somewhere around line 22 you’ll see this:

```
$.ajax({
    url:'scripts/jquery.tinysort.js'
    ,dataFilter: function(data) {
        return data.replace(/$.tinysorts*=s*{/g,'$.tinysort={expose:function(){return{toLowerCase:toLowerCase,isNum:isNum,contains:contains};},');
    }
    ,success: startTest
});
```
When using $.ajax() jQuery will automatically inject your code into the DOM. But with that property ‘dataFilter’ you can alter the result right before injection. So with a little regular expression I look for ‘$.tinysort = {‘ to append a function that exposes the private functions that need testing, which is nothing more than this:
```
function(){
    return{
        toLowerCase: toLowerCase
        ,isNum: isNum
        ,contains: contains
    };
}
```
You can test this by calling up the console in both TinySort and it’s unit test and calling the expose function on both pages:
```
$.tinysort.expose()
```
>https://ronvalstar.nl/unit-testing-private-functions

---

3.
I have a similar problem. The solution I came up with is not something I like but it does the job and there's not a better solution I can find.

```
function Graph()
{
    this.Test = function _Test(expressionStr) { return eval(expressionStr); }

    var private_data;
    function draw_legend() { ... }
    function draw_plot() { ... }
    function helper_func() { ... }
    ...
}
```

To test:

```
var g = new Graph();
g.Test("helper_func()") == something;
g.Test("private_data") == something2
```

>https://stackoverflow.com/questions/716207/testing-private-functions-in-javascript











---

[6C81F51F-C535-675F-6227-14D14CACFC33]:data:image/png;base64,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

[10ADC7A0-C515-DE67-A6AE-86BB0E73167B]:data:image/png;base64,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jq+qchABERABERABERABERABEaiKQC2EgSCKgAiIgAiIgAiIgAiIgAg0VQISBk21ZVUvERABERABERABERABEagFAQmDWsBSUhEQAREQAREQAREQARFoqgQkDJpqy6peIiACIiACIiACIiACIlALAhIGtYClpCIgAiIgAiIgAiIgAiLQVAlIGDTVllW9REAEREAEREAEREAERKAWBCQMagFLSUVABERABERABERABESgqRKQMGiqLat6iYAIiIAIiIAIiIAIiEAtCEgY1AKWkoqACIiACIiACIiACIhAUyUgYdBUW1b1EgEREAEREAEREAEREIFaEJAwqAUsJRUBERABERABERABERCBpkpAwqCptqzqJQIiIAIiIAIiIAIiIAK1ICBhUAtYSioCIiACIiACIiACIiACTZWAhEFTbVnVSwREQAREQAREQAREQARqQUDCoBawlFQEREAEREAEREAEREAEmioBCYOm2rKqlwiIgAiIgAiIgAiIgAjUgoCEQS1gKakIiIAIiIAIiIAIiIAINFUCEgZNtWVVLxEQAREQAREQAREQARGoBQEJg1rAUlIREAEREAEREAEREAERaKoEJAyaasuqXiIgAiIgAiIgAiIgAiJQCwISBrWApaQiIAIiIAIiIAIiIAIi0FQJSBg01ZZVvURABERABERABERABESgFgQkDGoBS0lFQAREQAREQAREQAREoKkSkDBoqi2reomACIiACIiACIiACIhALQhIGNQClpKKgAiIgAiIgAiIgAiIQFMlIGHQVFtW9RIBERABERABERABERCBWhCQMKgFLCUVAREQAREQAREQAREQgaZKQMKgqbas6iUCIiACIiACIiACIiACtSAgYVALWEoqAiIgAiIgAiIgAiIgAk2VgIRBU21Z1UsEUiZgVZHSlfITlFAERGB3JaDxY3dtedW7aRKQMGia7apaiUAtCOiLvRawlFQERKAcAY0f6hAi0JQISBg0pdZUXURguwjoi327sOkmERABQOOHuoEINCUCEgZNpTXN2CzPj6bSmrWvR1Xfzds8ySSMJ7b7S02dpqbPa19U3dGACCT3GzV1A2oYFUUEGimBVL6LNNY06MaVMGjQzVOLwiVeRr1wtYDWhJKmMhjbgsCqoAVq02Fqk7YJsW3KVanYb9TETbm166luUpP1BLLpPSal76EU5qOaHplG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QAS2i4CEwXZh000iIAIiIAIiIAIiIAIi0LQIuCzL2t6dR02LhGojAiIgAiIgAiIgAiIgArsxAQmD3bjxVXUREAEREAEREAEREAERSBCQMFBfEAEREAEREAEREAEREAERQMJAnUAEREAEREAEREAEREAEREDCQH1ABERABERABERABERABEQACQN1AhEQAREQAREQAREQAREQAQkD9QEREAEREAEREAEREAEREAEkDNQJREAEREAEREAEREAEREAEJAzUB0RABERABERABERABERABAwBRSVSPxABERABERABERABERABEZAwUB8QAREQAREQAREQAREQARHQioH6gAiIgAiIgAiIgAiIgAiIgFyJ1AdEQAREQAREQAREQAREQAQMAe0xUD8QAREQAREQAREQAREQARGQMFAfEAEREAEREAEREAEREAER0IqB+oAIiIAIiIAIiIAIiIAIiIBcidQHREAEREAEREAEREAEREAEDAHtMVA/EAEREAEREAEREAEREAERkDBQHxABERABERABERABERABEdCKgfqACIiACIiACIiACIiACIiAXInUB0RABERABERABERABERABAwB7TFQPxABERABERABERABERABEZAwUB8QAREQAREQAREQAREQARHQioH6gAiIgAiIgAiIgAiIgAiIgFyJ1AdEQAREQAREQAREQAREQAQMAe0xSLEfFBYWkpWVlWJqJRMBERABERABERABERCBxkVAwqBce1nxn1xbfxuJxHC7IRKJ4Pf7G1frqrQiIAIiIAIiIAIiIAIikCKBnSAMYkDC4DalcpuFihSLl5RsG5vdPDfxvNo/rvI7thUGyemMOPB6vfFfVZ+2vkqk54gQUsllAAAgAElEQVSACIiACIiACIiACIjAziBQD8Igahv70bix79la6oQgMAa0SZMw4j1YuFOSBuZO8xT7mbYOiGGm783vXYTjnxiRUSY0LMvC5XJ+Nuki8b/Nb8xzjCxxPqlMnGxr7D9493D22f83HPd/F5KTERcFW28PAb7tEzo7o3WVhwiIgAiIgAiIgAiIgAikSKAehEGpbWcXutIoDUKzSAxfwAJPBKyoY4NbMfC4wOW3RUEUN56Yy/7MitvntnmftLBgfm/M7uIYuMLQ3G9M/DBRT7pdNUeAGLVQXhgkZvVjsRhfL1nK96sLOPaEQ8k0iiAM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