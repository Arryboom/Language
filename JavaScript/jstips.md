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
![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAWkAAAEVCAIAAACt+YOjAAAgAElEQVR4nO2d+3cT553w+w/0lzc9SQ/b9hy3h3rbTTfv7rvZ7WGTt33ZJuG0myxpaJNADW0hhCQQGxKCSyBgx6QOCSEUA7nZJGAwAuwEczMXX4QNCNtYF0uWLVmWrLusm3UbXUYzz/vD0GGY0YzGYxtp7O/n6HCk5zYjjefD8zzzzHe+9d13jQgAAGCKfAvcAQCABOapOzJZpB7LXBxInu1jv9RjmUy20PsHAEVPUbsjGo2uXbu2ubl5xlvuNadpWTgDWTyLdLYMUx8zvkUAmGPMpDsOHTr0OodDhw4xy/j9/oqKitLS0iVLliiVSpIkzWbztm3bMAzjNijZHQcOHOjt7RUowOxocN1xcSA51S0CwHxjJt3h9XorKiqY4qioqPB6vXSBdDr99ttvnzhxgiAIo9G4bNkys9ks4A7J1NbWqlQqgQLcoQrrNYM7AwBzkhkes5w+fZrpjtOnTzNzzWbzG2+8EYvFqI8NDQ0tLS1UYlVVVWlp6erVq30+H0JIoVCU/B2FQkGV1+l0y5cvX7hw4SuvvOJ0OhFCJEkqlcolS5aUlJQsXbo0GAyqVKoSBlQidz8pQdwYTiczCCGEE2hgLAPuAADxzLA7EolEZWUlJY7KyspEIsHMValUtbW19Meenp7a2lqz2fzkk0+2tbXhOF5fX19XV8esolAoKHeEw+Ht27e7XC6CINrb26uqqtLptNFoXLVqlcViIUmSWWtK/Y6BsUwyA+4AgKkx83OlSqWScodSqWRl8bmDHrPo9fqtW7emUim6DO0OtVr98MMP0x2K8vJyDMMUCkVTUxN3H8AdADDbzLw7CILYtWvXrl27CIJgZen1+s2bN9OdEXrMQrtDrVbX1NTgOE5Xod3R29tbVVXFzKJywR0AUBBm5RqtwWAwGAzc9Hg8vmHDhgsXLlBzpStWrLDZbGazubKyMhaLJRKJd9999/z588wqtDu8Xu/KlStv3brFVFJ/f39ZWZnD4WBtqK6urr6+nisvGmF3wHUWAMjL/V7fYbFYli9fXlJSsnz5cp1OhxByOp1lZWULFy585JFH6uvruT0Leq60t7f36aefZk6g4jje3Ny8aNEi1rQovRW+uVJqfcfAWAZn6CWZQTeG07C+AwDEUNRrw9Lp9I4dO9Rq9Yy3nMmiXnMa1pUCgGSK0R0YhpWXl5eUlDz66KMNDQ2snggAAMVAMboDAIDiB9wBAIAUwB0AAEgB3AEAgBTAHQAASAHcAQCAFOaWO0hkbzad/cf6k9/ep93ek7PIRLfz5Lf3nfz2vrEjOVa+AgAgkjnljpQfu/nHizHLZN6SY0cM4A4AmA7F6I4bH/Xe/kJb/1jj3h9+MnjCiP5+e/2nn366devWdDrNVzEyHLxd0ZFN3F1Lpt3eQ/UylEu/TtijdDrLHakJTLW67fR36u72R0jkuWK79B+Np79Tp35LmQ7CHS4AcA/F6I7Oqp4zay7GfYnJ8cipF89M2iJUuoA7UgHs6v87QWni5Lf3nftpQ2SYcRsLiZyto0Mf3I1CyHLH2BHD8Me3SeJuEJCYZXJgc1cmkiZxYvQL3fDe/hn/mgAga4rUHfYeJ0KIwImrf1E6brpEVmT3O0hkbzZd+JevKKEwZ0BY7ghrJi79vHHog97EeJTq5ji+NtMmEpg9AYB5S1G7I5vOXtrc4er3iKzIckfcGul46lRYO4EQmuh2CrgDIYTHMt728e5lZwx/VZE4YTtuNB2Y+XvwAGDOUKTuGOsYRyRyqFzfrL6QDN2Za5jqfEfUFO7675aEI4q547fWXhJ2B0VI7et/vT2bwCcH/cpnWqIjIURySwEAUKzuqH1g3+6H9p947uugKUSnT9UdJE4M7e49/Z26S//ROFh1g3LH2BEDczAy0e1EjCnVK483+W+4EGKPd6hiAADQFKk7qDELAABFC7gDAAApFKM7AAAofsAdAABIAdwBAIAUwB0AAEhhjrujZ8GDAh/5sgSKAQBAMcfdgfJJIWcuuAMA8jJ33JHxT2iWPBG5cd1aU22tqUYI9Sx4kH6xPtKJ3HRuRYSQt/HIyKvrYhq15tdPYaaRAn1FACgi5o478GDA8IcXMdOIY99eyh1oKiMR4U6Ht+mYtaY6NW7TPfsMuAMAUHG6Awsmr/5F+cGCuv0/+fzk82ewAEalC69Jx0OhoVV/wEwj3qZjrs8/pRL5jMDsU3ALsDodCCHfqZPWmmo8GDCuXZ12u6f9FQFA9hSdOwicaN9+TfPVIIETUVfsm9UXRLojJ8IjEXSvXPiyAADgUnTuwALYmTUXExMY9b51bRvtDgnQ1mD9y33DlwsAQE6K0R20LxIT2Jk1F2fDHXwXXPjmUwEAYFF07sgk8POvXR5rt2XimY53ur964sQ0xyxIXJ8C+h0AMCWKzh0IIcdN1yf/9uWBn9X3fapm9jtmyR0wZgEACRSjO2hmfL6Db7IDgTsAYIrMZXewlCF8UZZVEtwBAMIUtTsAAChawB0AAEgB3AEAgBTAHQAASAHcAQCAFMAdAABIYU65w+PxrF69es+ePTiO5y8tCYVCUVJSUlJSolAoEEIEQQwNDW3ZsuWXv/yl2WymyiSTyT179pSWli5atOjq1askSSKEgsHg0qVLS0pKli5dGgwGhbYBAHLgfrvj0KFDr3M4dOjQjDTe1dWlUCiqqqoCgcCMNMhFoVBQ1qDwer07duzo6elZuXIl7Q6FQvH++++n02mXy/Xaa6/ZbDa6fDAYLC8vB3cAc4D77Q6v11tRUcEUR0VFhdfrZZZJTGCtL7d9sKCu9oF92sa7T43NE78Dxz/++GOz2Xzw4EGVSoUQwjBs69atX3755ZIlS0pLS5ubm0mSVKlUW7ZsqampKS0tLSsr8/l8CCGVSkX1JhYtWtTR0YEQOnLkSENDA9WyWq2mt8tyB0UwGPzTn/5EuQPDsMrKSr1eT1VcvHjx+fPnmSXBHcDcoABjltOnTzPdcfr0aVYBbaPh1v7bJMF+irSwOwKBQFVVVSwWU6lUdXV1JEliGFZRUVFbWxuNRu12+/r168PhsEqlevLJJ/v6+nAc/+yzz1paWpiN2O32TZs2RSIRo9FYXl4ei8UQQgcPHuzq6qIK5HUHZYdAINDd3b1u3bqPPvqIWR7cAcwZCuCORCJRWVlJiaOysjKRSLAKeLUT9Y833vioN2KPin8MvUqlOnjwIELI4/G8+eabkUgEw7Bt27bRQwm6WG1tLauuxWIpKytbuHAhPR+RTqffeecdvV4fDod3794dj8epkmLcsWHDhuPHj7/55puhUIhVHtwBzBkKM1eqVCopdyiVypwF0rGMtXP81AtnenarCJzI2yBJkrt37y75Ow8//LBarRbpjnQ6XVlZeezYMYIgmOd2V1fXl19+efv2beZO5nVHIpFYv379zp07Kd3U1dXBmAWYkxTGHQRB7Nq1a9euXQQh5AWvxte2qT2TuHPRRGDMEggE1q9f7/F4qI8KhaKhoUGkO1Kp1KZNmy5fvpxMJo8ePbps2TLq3A4EAjt27Dhw4EA4HKYL53UHQujIkSOHDx9Op9NGo3HFihUwVwrMSQp2jdZgMBgMhpxZnVU9tQ/sq31g3+HFTY6bLjpdwB39/f3bt2+ns/R6/euvvz4xMSFyzHLjxo1FixY98sgj77333rp166hzmyTJ/fv379+/n7rISsEdg1BXXinKy8sxDIvH4++9915paemSJUt6e3uZGwJ3AHOGObW+Y2bx+/07d+5k9hoQT79DPOAOYM4A7sgBhmHl5eXcXgPirA0TD6wNA+YY4A4AAKQA7gAAQArgDgAApADuAABACuAOoRjIAiXzBkMWEy0ZIioD8gXcgVA+KfA9v1Z8m3QK64lz8AA6QL7I1R1YMHl+/ZXdD+0/vLjJPeDNXwEhhFDGP6FZ8kTkxnVrTbW1phpxzl6+Z0rmPe1zFmBtne9hMazC3sYjI6+ui2nUml8/hZlGJP5AADDLyNIdBE5c23VDfVhHEqRP7z+z5mIylBRTEQ8GDH94ETONOPbtpdyBpjISydvpYDWV0yZ5H0PnbTpmralOjdt0zz4D7gCKFlm6I+HHvll9IeqOIRKZzlvq/ukLV/+dO1nyxPgIhYZW/QEzjXibjrk+/5RKFHjWpPDjoAQKCAxzuNZgNeI7ddJaU40HA8a1q9Nud57fAgAKhCzdERgJXnqzIx3L6I4ZLm5sv7ix3d7jpLKkPbNWeAKCO3LhZjE/8vUpBMY1MN8ByA65uuP8his3P+7rrOrBMbyzqod2hwTEDCXyDjS4KXkbEX99BwCKEFm6IzGBffXEiVv7bxM4kU1nL23uoMcsEsg7GcEtmXM+FeVyB18KXGcB5I4s3UHgRGdVz3CrmcySY+225j+cpedKpY1ZkIg+hZgy3KszwhuCMQsgX2TpDoRQYgI7s+bi7of2H1/aHDSF6PRZckfeMnxjEDFXXribA4DiR67umEH4piGEJyyQ4AkvPPHB2hyMWQA5Mt/dwVKGwPwltyTfCS88h5ozHdwByI757g4AAKQB7gAAQArgDgAApADuAABACuAOoflRgZLcWsKvGd9tACgs4A6E8t1Km/eCa85a4nMBQI7I1R1xb0L1t/4DP6vXNuZ+QFROZjV+h8B26VyIzQHMGWTpDgInej64ZTo/emVL15TcMXvxO4RvbLnrDojNAcwVZOkOms6qHpY7ChW/g+9eFVavBGJzAHOG+eWOnAiMROgC3JLcLNYbvkQAmBvMNXdIQKCbINCtQCI0Ae4A5jDgjpmJ3yFyzAIAc4a55o5Cxe8QOVcKAHMGubqjs6qn9oF91OvgIw2BkTtPli9g/I68mwCAuYRc3TGDsLwgMP0J7gAAmvnuDpYyhC/KskqKcQf30gwAzA3muzsAAJAGuAMAACmAOwAAkAK4AwAAKYA7ZiZ+h5jqEooBQNEC7kBohuJ3sK7v5rw1RmArACAv5OqOmCee89lOwsxe/A66GOsNK1H4RRWDGB+ALJCrO4xfm6xddjJLqg/rLlZczaazYmrNXvwOgQLCHRkuEOMDkAVydQeN3xho29SeSeDUxwLG7xDov+RshM8gEOMDkAUydweJtEf11/f0IvJOQqHid3BL5iwjvo8DAEWOvN1hU9oVv/sm5olPpxHufW7Cd7KImS4V8IXwZAcAyAUZu8N+w6VY9rX4iVI++NzBN08hPJ8qUCBnawAgU+TqDle/59SLZ7jiKGD8DuEsvtYAQKbI0h2ZBN66to2O31H7wD57j5PKKlT8DlbKdK7RAoAskKU7ZhaWF/gmO9C03cFXGADkyHx3B0sZwhdlWSXFT4gIjIAAQKbMd3cAACANcAcAAFIAdwAAIAVwBwAAUgB3zFj8DvHtINHzqQBQtIA7EJqJ+B1IxO1wORetC69GA4CiRa7u0DYaqFVhRRK/Q+QakLyLRxDE7wBkglzdQUES5Mi50bZN7QWP3yFyzajwGlMqC+J3ALJA9u5QH9ap/tZPpxQwfofAforpbtBvIH4HIAtk7A7qkbSNvzk1aYvQiQWP3yEwAwrzHcBcQsbuQAiRBGm5Ym0uO5eKTEEWLJgns/CJLTyXISZFzJgFAGSBvN2BEMICWHPZWWbXY6rwuYNPBGJ6FnxGEKMbAJAF8nYHgRPDreYrlV3Z1J250kLF7xColbMA9DsAuSNXd1CTHXt/+MnVrUosmKTTCx6/Q8yUB7dlAJAdcnXHDMLyQt5LIVPtd7D6GmKqAEDxM9/dwVKG8EVZVkkJ/Y4pFQOAYma+uwMAAGmAOwAAkAK4AwAAKYA7AACQArhjxuJ35G2Kb4oU5koBOQLuQGiG4ndwE/NetREoDABFjrzdMXhiaPdD++kHO+Vl9uJ30MWYb/guAPNtgnoD8TsAWSBjdwRModaX2869ekm8O2YvfkfOAixr5FwYksMdEL8DkANydUc2lb1S2eXq83RW9TDdUcD4HTl7JcyKAtWZuRC/A5AFcnWH8RuTal8/ItGU3JGTKY1E+LKYH3MOUgQah7lSQI7I0h3hsclzr15OhpIIsd0hgZzDCmZW3lxWU3wlucrIWRcAZIEs3UEHOqZfnVU9klvjcwefGvjmUxG/a4Qb5L4HgOJHlu5gMiNjFiSiTyGmjEBHg5Ur7BEAKH7AHahnwYMEQQh4gZUr7A4BawjPxYI7pgNBEIXehXmH7N0xfXoWPDg+bukp+R6693ym33SteiGbzSJBd4ifQxV4zd53nPOMj1uoYwTcN2bRHQSRdTpts9T4TNGz4MFUKtn1+//h6xqkUkmXy069z2bxa//5KGsWg0U2i09MeDweB9cLwnYoEnckEvFUKpm/XDFBH6OiRRbnwlSZLXckEnGLZXh4WDcbjc8sdvsYjmfy5sZiUbPZYDYPCTQ1MeHx+VxDQxqnc3zmd3SWyWazHo9Do7kViYQLvS9TQ+AIjo2NGAxq7ojGYhkeGtKQJDn7e3efzoXpfCNpdWex3xGJhMX8XrGYUIhz4dzpk0olBf5DYOWGQn5hd1A4neNydAeF0agtHneIOfrCR9DhsJpMeu5ZYbePjY4O3R93INHnwnSYzjeSVrfA7iAIwmYzS8udERwOaybDO7HKyp0P7hge1hWJO0QefeEjWCTcB3fcfwrpjkwmY7OZx8Zy37IhnDsjpNMph8MqPhfccd8QefSFjyBJkul0itt5IQgimcQSifj091Mks+2O6XwjyXVn3R3JJGa1msfGRoaHBwOBCTo3HA74fO6xMZPRqPX53D6fOxj0i8mNRieNRu3wsC4ej9ls5vHx0dFRYyjkZ20dxzN6/cDYmElgD51OWzqd4luFQeUys5SLHzebh6hOctcfXzSZDIGAj9Vmz4IHaXewWu7+xx/Z7WMej1O59Nddq17o/ulC5mRq9z/92G4f8/ncLte4wzHm8TgGB2+LHLIRRNblGvd4HD6fa3x8NB6PMnPT6ZTbbff53G633WAYUKtVzPF/JpO2Ws02m5naOu0ODEuYTIbhYV0qlRwft4yPW8zmoVAoIGZ/xNSlpg/t9jGHw2q1mpNJjM4S/ttgwj1GNJFI2Odzm0x6lu79fp/X6zIY1Dn97vd73W4HtdHxcQtr2jidTlFH0Ot15szl+50FzoVYLMrcSZIk3G67Wq1inszCezWdbyRcV5jZdcfgYD998SyTSWu1vaw5Lb/fK/B/C18u1bLb7aA+ZrPZoSFNOHzPn2Y6ndJoekdG9HyNZzJpu32Mes+9vJozNxTyd7xV7nTaSJLsWfAgjmcGB29Ho5Po3uuvXc8/2/X8s6zrLDieGRrSdP/Tj+ndGxrSdC8soT4SBDE0pKH73i7XuNttj0YjApO4NCRJjozoaXtiWFyr7WNesDSZ9PRHgsgODw/Sf0DZbNZgUFNfASGEYQmtto/ud1C/s8MxRp0DOJ7R6fqyWTzvLuWtS5KkyWSgbYJhicHBftbQQ/hvA917jPgIBnN3FZ1OG/ds8Xico6N3T4dodJL590MdQfqno45gJnP3AAn8zsLnAncn9frbtDuE92o630i4bl5m1x0Gg5o5ATM0pInF7vkvUbI7us901H7n7oL0iQnP8PAgq1g2m2VN/zDjd2g/rUulktzLqHfO/xd+S/ULqIpUonLx4+27tveUfJ9O6Vrxu86XVjI3wdfvsNvHmFN6dxSz6gWqnWs//z8jI3f239t4ZPAvm3Xd7SLjd/j9XmZ/OJnEmBoiSVKr7WV2NOLxGP037fW6WH9MzDEL9wgajVpWp4YP4bqhkJ91vCyWYbf7nuused3hdNryXk7mG2bmHFcGg36fz0V/TKdTWm0v/ZF1BKlGxsct1Hvh31n4XODupF4/QLtDeK+m842E6+blvs53cMfSkt3Rc6ZL22igU6LRSbValXeX6Pgdtrp96s8PUoncTgeOZ+x2C7c6NWZhVvH7vUNDGlYxp3O86/lnWYk6Xd/kZIiZcu3//lyn67vz/j/+1Wi881t5m44Nf/TBUP8NkfE7RkeNwgfeZhs1GrV+v5fbZTCbh1yue+qy3JH3CPIhXJcaIjFznU4bZ3Ah9LfBd4xYTMkdFJlMOhic8HgcTqdNo7lFp3OP4ORkkD6CSPB3Fv41hN0hvFfT+UZi6gowR9yRSMSp4aXI+B3GFoWt4QsqkbtO1OUaTyYx7uov2h2I7on81+N6/QAzJeeYhSAItVrV/S8PMxu89q8/o4fEPf/w0OiokRo7+E6dHPjiUGB8TGT8juFhnc+Xp9jkZNBsHtJoblmtJub/1dy698cdo6NDo6PGiQkP/XI4rKxZT+G/DeoY5d2NKbkDxzMWy/D4uAXD4ujOsPfOmUYdQdb5nEjEWJNHfL/zdNwhsFfT+UZ56+ZFxu6ofWDf7of2n3jua4/aF41OajS9SNz9LDiO011NxInf0f2jH3StejHnAnPl4sc7N7zEzFL++gmjUcsqxup3UOlabV8sFmGOjK7957+379p+Z+v/8FDX8mVer9PrdXm9LnoCQgxm8xCrt89HKpV0Osd1un76rDObDR6Pg1nm/rjDajXlXWcp8LfBOoICTMkdJpOemcg606gjyCwfiYRzDgG4v/N03CG8V9P5RsJ181Jgd4RCfotlmP7IOjZ8uZFImOrhEzhh/MZ06oUzjjErc06IgjvfQeF22zEsQX9k3efmdtu7//knzCz6jXLx4yMjemai3T7GnVLN6Q6bzezzuZne8Xgc9PqFeDxqNGoxLJ7zni6SJOPxKN/SHY/HYTIZWIl+v5d6E4mEWSZyuca9Xtff39ut1nsuRen1AyLdIbxXwnX9fq/ZzN5n1v/qAn8brCMogHh3ZDIZtVoVj8foFNaZRh1BZhXmERT+nYV/jcnJkMl0d9aJJAmdro/6NfLu1XS+kUBdMRTYHRiWoOeQwuEg6w+CLzcSCavVqmg0ghBKTGCt6y9obvcyfyPEf50lm83abKPMlHvcUfJ9m22U7/YT5eLHNZpb3T/98Z2rqj/+Yce2zd0//TGzHcTjjnQ6ZTTqun/8Qyqx+0c/6NhS0f2PP7rT7/jedy2WEY2mV61W6fUDo6NDXq+L9ojTaVOrVayJCRocx/X62/RQnCRJt9tO/48Xi0VMJgPzDHc4rNRPhxDKZNKDg/3J5J0f1u/3ajS36KaEj6DwXgnXJQjCaNQyr6z7fG7WGITv6HOPoADi3UGS5OBgP+1chFAgMEGdacHgBPr7EaQnoXEcNxq19BVi4d9Z+NdIp1M6XR/VMkmSLpddq+2l5pXz7tV0vpFAXTHMljuSScxiGdZobtGq9vncGk3v6KiR9Sfi8ThGRvQ2m5n1fQRyqSPh93sdVmtPU/elQxcjwTuHgR6zZDIZvf42t9Pr8ThY/78xTaH87X9TuTnvTLu2+LFQKKBc+muXy+5wjHWuKaOGkVT5nu99V/n0U8pnlnRUbuzYukn5zBLl00/1fO+79IAolUp2rXrB7XZ4va6u5c8xx8PXFj9GXfrNZvF4PBYITJhMBnou0O/3arW93LUkNOl0ilo4YLeP2e0WpkaTSUytVhkMaqfT5vO5PR4nc5UNQgjD4qOjRqvVZLePBYMTJpPBZjNjWDzvERTYKzFHH8dxu91Cjcbt9rGcl29yHn3uERQgGJxgdl4QQn6/j7rnaGhI4/O5/P67+59IxM3mIYfD6nLZXa7xWCxqMhnGx+/+ntRyFeoIsq7yCPzOYn4NatLdbB6yWEZisYjRqB0fH6Vy8+7VdL6RQN28yPIe/EgkfPXIpdoH9u394SfKmhtY8O4hFJ7vIIgsd5kz7Q6CyHb+eQUzkVsGcQYyXATumuW7p7bjLxtZlxuj0QizHwtQ5DyCLJj/+VMn+Szv1DxFru6gZyinRDab5a4IoE/mbDZLjUeY6dw3Au5gXZrhXqlhLRihX51ryiYmPMyS1EJACd9xbpPzCDIJh4M6XT813COI7MjIoOxCCsgF+bmDuqNZrVa53Y7ph3thXvhAPN0NvpLCmhDOYm/oBws8Hic1u+b1uqilzdP5XvOWTCZjNg+Njg5RF31BHLOH/NwBAEAxAO4AAEAK4A4AAKQA7gAAQArgDqH5UYGSwhdohVtmXWRhLoqf0g5Psx2RWwGAnIA7EMonhZy5XC8Iv/hq5dyZvFIQ7zvJiNkNYD4jY3d41L4Tz329+6H9rWvbMglRAWmY8TusNdXo3jME5TphqIrcRL5zSUzfRGBDArVEioZKJ7DEyKvrehY8eL3k+9bqndl4HCFEJLHx9/96veT7vf/2v4OXLyGSRAjhoaBp4+vXf7BA/dSvYuoBgW3dk0WS1uqd1prqyI3rmt8swYOi4okBcwm5umNiKHB6eat/OEgSUwjuTMfvcOzbS7kDiT7bEX+nQ6CkyDKS3ZG3LpHELG9XBs62IoS8Tcdsu94lU6mUyzn88pqk1Uri+Pj7f/Uc+RIRRNxgGH5lLR4KifwRrDXV3qZj0dv9umefAXfMQ+TpDhJ1//Wm81aO1VMi43d4m465Pv+UShQehrBSWLk5U4Q7L3yt5Xyfc+si26HIRqOm11+LadQEljC/sTE+qEMIRW/39z/288DZ1kzAP7zupbTbjUgy2Hah91//OXq7n+/rsDYx/v5fvU3HMNOIqXx9NhZDwDxDlu7IJPC2Te09H9w68LP6vT/8RP3lIIHfueVU2vNohUci3JELN2tK7/Mqhm8/p9QOHgxofrOkZ8GD1ppqIonhwcDIq+syAX9Y2WVc/cfxD96nzvzRys3ZeNzXdNy8+Q3z5jciN67n3A3xfS5gniBLd6Rj6ZZV527u7cMxPOKMnl7e6tPnjqMtBvo8ZP3LfcOXy/041T4FXzti2heGSGK22vecB+rwYGD4lbWeo1+ZKjbgoaC36didXsOmckfd3yi/UPMXOTcB7gBYyNIdmQR+6c2OwEiQ+thZ1WPvcUpujc8d3JOE7397Zq7I93yN5NyutHZoIjeum9/clJ2cHF73kmXbVmre1GYfQNwAABA4SURBVLFvb+Bsa8Y/ofnNEufBAySOk+n06F/eYo5Zcm4d3AFQyNIdCCHV3/rVh3UkQU7aIt/8+UJ47E7IJmljFsTxRc4zRKCMwGRHTr+I7LPkLSPQDkU2ErG+u9PfegYh5D7c4PriMzKVihsMg79/jportdZUB86dRdlsuLNj6I8r8VCI7xsJ7x4w35CrO7Bg8vz6K7sf2l//WKPj5t0o8rPkDvF+4RZjZeXMFakh8e3Qkx39j/3cd1JB4jhCKBuPW6t3Xi/5/sDiX0Ru3Qkrn/FPDL+y9voPFgz+7rfYqJnbVM5vBwBydccMwvKCQB8hrzv4znDWthD//+o5m+WmT2nMMrOAOwCK+e4OljLyDiWYJYU1IZyVd6wxpf2ffjtiNnR/3ATIhfnuDgAApAHuAABACuAOAACkAO4AAEAK4I4pTDeKuRoCAPMEcAdC+aQgZlEWAMw3ZOkOLIB99cSJ2gf2Ua+DjzTQ69OFuQ/xO6QAsTAAGSJLdzCJOKPnX7ucDIl6DMd9i98xVSAWBiA7ZO+O219ob39x9xlxBYzfYa2pplL0L/4+5XQghLxNx9wN9YO/++31ku/bP/6IWhju2LfXfbhhYPEvbv5koe/USSp4F8TCAGSHvN0R88S/+fOFmOfuk40LGL/jDiQZuHDesW8vQsjbdEz7P79JWix4OGxcu5q6RdVaUz38ytrMxETKPm5YuSI1Dk9LBWSJvN2hbTTc3NuHphB1MAes+1lQrp6IcC5CCJGk/8w3/Y/9nJILNSCiYmRQ+a7PP/WdOokQomNkkDg+tv1t+p40AJAXMnZHMpQ8s+Yiffe9ZPjcwXfBJed8amrcpnv2mfjgIEKImotF97qDipeBmO64N14GAMgLGbtjqHmkfds1OtogRaHid2Cjo4O/fy7lcqY9HtPrr9Hu8Hz1JSJJbHTUuPqPSasVIWStqQ53dSKSjPTeGl73Eh0vAwDkhVzdkQwlm8vOckMNFip+B4nj9o8/okJj2Grfo91BdUzUT/2KHptQU6rXf7BA/8LvmPEyAEBeyNUdMwjLC3yTHUhc34QJc8xCw4wJCgDyZb67Y6bid+QE3AHMYea7OwAAkAa4AwAAKYA7AACQArgDAAApgDtmMn6HhAAfedsBgOIE3IHQzMXvyNuO8P11rETuAlZwClA8yNUdQVPoxHNf735o/4Gf1esVRtbqUj5mNX6HyP5Lzuu+Aik5m/I2Hhl5dV1Mo9b8+inMNCLmuwPAzCJLd2TT2UubO8a7HYhEk7ZIy8pzWAATU3FW43fk7SwILC3jK8O3OW/TMWtNNXUTDbgDKAiydAci0c29fR3vdCfDqZFzoxc3tmcSOJVTqPgdYgYjORe2C+QKaMh36qS1phoPBoxrV6fdbp6fCQBmEXm6AyE8mb36trL2gX1Nz7bEvQk6vYDxOwTMwiwgMA5CPH4R7u8AQEGQpztIpDtmOPfqpYQfU+3rP7PmYioyBVmwyPkfPjMrb66YwohHTHl3RmCLAFBAZOmO1GSqZdU5vzGAEErH0l//6byr3yO5NT53CAwrpjSdwdeOwBtwB1D8yNId6Vjm6z+dN5weJgkyYAopln09aYtQWYWK34E4Ux4ihzM52wR3AMWPLN2BEPKofceXNtc+sK/+scaxdhsddrBQ8TtY6XzzJiL7OHxCAXcAxYNc3TGDsM5hkQMKxO+OnB/5tiimIigDKELmuztYyhA4jbklJbiDVZ3bQxEYKwFAUTHf3QEAgDTAHQAASAHcAQCAFMAdAABIAdwx5esjwsWmOq8J86CATAF3IJRPCjlzhd3Bt/A056KPmfgGAHC/kas7Yp74mTUXdz+0//jS5qBJ7KPVZjV+B+Ixi/gFIxQQmwOQBbJ0RzaVvfxWp+WKFZHIp5s4v+FKOpYRU3FW43fwFRCjDOZ7iM0ByAJZugMLYK1r26h4P5kEfmlzR9AcprIKFb+DVZ6v84J4+hrMFIjNAcgCWbojNZlqLjs33u0gs+ToZetXvzoRGAlSWYWK35F3WSq3TE6/AIBckKU7EEKOm676xxo/WFDXVX299eU22h0S4HYHhEcZAiMXkevNwRTAHECu7qBJhpIXK64m/JjkFvjcIV4NrAJ5Pwq8AQC5IG93xL2J9m3XdMcMs30PPrdkzjJ5J0SYWTnbBAC5IFd3aBsNtQ/sO7y4aah5hPmAhQLG7+Bm8U1wCJQBALkgV3fMIKxzOO/IQsx8h3BFvvYBQEbMd3ewlCF8UZZVUnjMkrc1gXkTACh+5rs7AACQBrgDAAApgDsAAJACuAMAACmAOwAAkAK4AwAAKcjVHQ6Vq21je9vG9qBZbPAOYTwDXlffPQ+mHDk7KqHxaIRoORb67eOmvdV3W9Pdxp7/L3Ppt7XbNzjCwewM7C4AFBq5uoNC2umdE4fK5VC5pt94S2PoREPw6CcB2h1+H16+0mbUJQkCNR8NffbRBEkKtwEAMmDuuIMkSb8x0LNbdXlzp/FrUyaeQQg5VK7xbkfvwYHLmzstV20kQSKEQpZwz24V1W1RVl/3aieU1depj1RKzBOnGrdctt7Y09v5TrdX46M3KmbZe0tjiHZHb0+8dqs7m0VJjPxop2ftMmtkEroegOyZO+5I+LGhlhE8mSUJ0n7dOdYxjhByqFy3/tafmEgkw6nbn2sTASwTzwx8oY04owROmM6NerV3pJCz3zFQr0tOpiKOqLpBl0ncCU02VXe0NIZaGkOxKLGjwlm/b2LzS/ZQANwByJ654w6vxkf3Hdo2to+cHUUMI2TTWcOp4ZgnznTHyNlRukMhMGZJxzLao3qRYQ0pWO747KOJbesdHRciQT++5WVwBzAXmDvucPW5bUoHqwDXHQROGE4Nd1Vdv7y5c7jVnE1lWSW5jU/THTeVsSceMfbfiCOEHLZ05To7jFmAOcDccUfUFe37RB33xknGVCTXHTFPXH/CSA9AaLwan7Hlntv5+dwx1TGL141vfsluNadSKXL/e94vPoa5UmAuIFd3jJwdZc1ukiTpUfuuvXeTee02x5glgfd/qqHKXN7cOdJqzqazCKF0NK05or/0RgdzrlSCO1oaQ9/7lpp+9fbEEUK3byZ++7jp4f+l27PDk4gTOSsCgLyQqzsk4zcGtI16PJVFCMU88b5D6kRAerxCAJi3zDt3JCdTmiP6y5s72za239jT6zcGSBhCAMDUmXfuAABgRgB3AAAgBXAHAABSAHcAACAFcAcAAFIAdwAAIIUidcehQ4e8Xm+h9wKNXbWFLOH7uUW9Xl9WVnb06NHZ20RtbW1JSUlJSYlKpUIIkSR59erVRYsWlZaW7tmzJ5lMIoSCweDSpUtLSkqWLl0aDEp/1i8whylSd7z++usVFRWnT59OJBIF3I0ZjA8ikuPHj58/f76mpgbDZmvFWm1tLWUNCrPZvGbNGpfLlU6n9+zZ09raSmcFg8Hy8nJwB5CT4nUHRWVlpVKpJAiJ67iZ8TtGL1tJgsyms4aTxvFrDirSh6vPQ5JkNpUdbjVf3tzZ+U6346aLJMigOcS8K/fm3r50LEMte6fuoxs8YaRChIgnGo3++c9/Pn/+PF+BeDxeU1Pjdrt37dplNpsRQsFgcP369fX19YsWLXr00Ud7enoQQiqVasuWLTU1NaWlpWVlZT6fDzF6EytXrnQ6nfF4fOPGjXq9nmq5rq6OlgLLHQqFoqGhASEUCoU2bNhQWVlJL7cHdwACFLs7KHbt2mUwGCS0Q8fvyCRw9ZeDYVskm85qGw0jZ0dxDE8EMM1Xg+l42nLVZrlqI3AiOZkaqNeGxiap6qx+R9g6qT48mJxMEThhuWobvWyd0prUvO4wm80ffvghjuMKhYI61YPB4LJly44ePZpMJgcGBrZu3ZpKpVQq1ZNPPtnX14fj+GeffdbS0kK3QJJkW1tbXV0dQujIkSOUFCKRyJYtW+gxIMsd1EeXy7Vu3brPP/9827ZtdJcH3AEIMPfdQd9Zb1M6XH1u+qY4ugyexAePD8V9CW4VljuoFqj3MU/ccGqYuo9uplAoFJRZ9Hr9zp070+l0zrNXpVLV1tYyU0iSPHfu3OLFi6muB5Vrs9neeuutWCzW399//PhxujDXHSdOnHj55ZfVarXZbAZ3ACIpdndMf8xCi8ByxerV+IrWHRiGlZeXl/ydX/3qV3a7XaQ77Hb7888/T+mVzsVx/MMPPxwcHDx+/LjLdTc0CcsdTU1NS5cutVgsCCG1Wr19+3YYswBiKF53zMhcqUPlsl93kiQZ9yXUhwcTfozrDpIgh8+YbUo7gRPJcKr/U82kPUplWa5YbV12elziNwbUXw6momkCJ8wXLFRYQ/EIj1nMZvMbb7wRi8UQQiRJ7t69u729XaQ7xsbGVqxY4XK5vF7vpk2b6FyVSrVr1y6FQsEcW7HcYTQa33jjDb/fH41GN23axNw9cAcgQJG6Y6au0dKPYrixp5e62sp1B0IoHU1rGw2XN3d2VV33qH30mRb3JXoPDtydKyVIm9LR+U43K+aYSITd0draSs1TULS3t+/YsWNiYkKMO3Ac379/f2lp6ZIlSz788EM6NxwOr1q1qr+/n1mY5Q5qvLNo0aJHHnmkvr4ex3E6C9wBCFCk7pgpuJEE5w8kSd6+ffv9999nhSliuUMAcAcggCzdwQwaRr26qq93bO9mJVLimFV30P0a+tWxrbuL8cQGZuDl+4nZbP7FL36xYcMGv9/PymKtDcsJrA0D8iJLdwAAUHDAHQAASAHcAQCAFMAdAABIAdwBAIAUwB3FSDQaXbt2bXNzc6F3BAB4KYA75mdsDr/fX1FRQS3fUiqVJEmybh5hItkdBw4c6O3tnYn9BYA8FMAd8zA2Rzqdfvvtt0+cOEEQhNFoXLZsmdlsFnCHZMSv+wKAaVIYd8y32BzMe1UQQg0NDS0tLVRiVVVVaWnp6tWrqTAcCoWCvh1OoVBQ5XU63fLlyxcuXPjKK684nU6EEEmSSqVyyZIl9PItlUpVwgDWdAGzTSHdMX9ic7DuQOnp6amtrTWbzU8++WRbWxuO4/X19cybWRBCCoWCckc4HN6+fbvL5SIIor29vaqqKp1OG43GVatWWSwW1k5CvwO4b8jbHXKJzcHnDnrMotfrqbg+dBnaHWq1+uGHH6Y7FOXl5RiGKRSKpqYm7obAHcB9Q95jFrnE5tDr9Zs3b6bnd+gxC+0OtVpdU1PDvIeVdkdvb29VVRUzi8oFdwCFRcZzpTKKzRGPxzds2HDhwgVqrnTFihU2m81sNldWVsZisUQi8e6777Lq0u7wer0rV668desWU7L9/f1lZWUOh4O1obq6uvr6esk6BgDxyPgarYxicyCELBbL8uXLS0pKli9frtPpEEJOp7OsrGzhwoXcwBmI4Q6EUG9v79NPP82cQMVxvLm5edGiRaxpUXorMFcKzDYyXhs2h2NzpNPpHTt2qNXqQu8IAPBSdO6Yz7E56JCljz76aENDA6snAgBFRdG5AwAAWQDuAABACuAOAACk8K3vvmucBAAAmCLQ7wAAQArgDgAApADuAABACv8fqdaqTFdHA+kAAAAASUVORK5CYIIA)

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