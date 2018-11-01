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
这段代码是作者用来处理 $(html) 时，有些标签必须要约束的，如<option>必须在<select></select>之内的。 
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
setTimeout("alert(this)", 1);   // [object Window]
2. 在一个对象中调用setTimeout试试，代码1.2：

1
2
3
4
5
6
7
var obj = {
  say: function() {
    setTimeout("alert('in obj ' + this)", 0)
  }
}
 
obj.say();   // in obj [object Window]
3. 将执行的代码换成匿名函数试试，代码1.3：

1
2
3
4
5
6
7
var obj = {
  say: function() {
    setTimeout(function(){alert(this)}, 0)
  }
}
 
obj.say();   //  [object Window]
4. 换成函数引用再试试吧，代码1.4：

1
2
3
4
5
6
7
8
9
10
11
function talk() {
  alert(this);
}
 
var obj = {
  say: function() {
    setTimeout(talk, 0)
  }
}
 
obj.say();   //  [object Window]
恩，貌似得到的结论是正确的，setTimeout中的延迟执行函数中的this指向了window。这里我反复的强调，是延迟执行函数中的this，是因为，我们经常会面对两个this。一个是setTimeout调用环境中的this，一个就是延迟执行函数中的this。这两个this有时候是不同的。有些不放心？？再多写一些代码测试一下！　　

 

 二、setTimeout中的两个this到底指向谁？？为了便于区分，我们把setTimeout调用环境下的this称之为第一个this，把延迟执行函数中的this称之为第二个this，并在代码注释中标出来，方便您区分。先说得出的结论：第一个this的指向是需要根据上下文来确定的，默认为window；第二个this就是指向window。然后我们通过代码来验证下。

1. 函数作为方法调用还是构造函数调用，this是不同的。先看代码，代码2.1：

1
2
3
4
5
6
7
8
9
10
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
我们new了一个Foo对象，那么this.method中的this指向的是new的对象，否则无法调用method方法。但是进了method方法后，方法中的this又指向了window，因此this.value的值为undefined。

我们在外层添加一段代码，再看看，代码2.2：

1
2
3
4
5
6
7
8
9
10
11
12
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
从这里，可以明显的看到，method方法中的this指向的是window，因为可以输出外层的value值。那为什么setTimeout中的this指向的是Foo的实例对象呢？

我觉得代码2.2就等价于下面的代码，如代码2.3：

1
2
3
4
5
6
7
var value=33;
 
function Foo() {
    this.value = 42;
    setTimeout(function(){alert(this);alert(this.value)}, 500);  // 先后输出 window   33  这里是第二个this
}
new Foo();
setTimeout中的第一个参数就是一个单纯的函数的引用而已，而函数中的this仍然指向的是window。在setTimeout(this.method, time) 中的this是可以根据上下文而改变的，其最终的目的是要得到一个函数指针。我们再来验证一下，看代码2.4:

1
2
3
4
5
6
7
8
9
10
function method() {
  alert(this.value);  // 输出 42  第二个this
}
 
function Foo() {
    this.value = 42;
    setTimeout(this.method, 500);  // 这里this指向window   第一个this
}
 
Foo();
这次我们将Foo当成方法直接执行，method方法放到外层，即挂在window上面。而this则指向了window，因此可以调用method方法。method方法中的this仍然指向window，而Foo()执行的时候，对window.value进行了赋值(this.value=42)，因此输出了42。

　

三、实践。知道了得出的结论，我们来阅读一下比较奇葩的一些代码，进行验证。　　

首先在一个函数中，调用setTimeout。代码3.1：

1
2
3
4
5
6
7
8
9
10
var test = "in the window";
 
setTimeout(function() {alert('outer ' + test)}, 0); // 输出 outer in the window ，默认在window的全局作用域下
 
function f() {
  var test = 'in the f!';  // 局部变量，window作用域不可访问
  setTimeout('alert("inner " + test)', 0);  // 输出 outer in the window, 虽然在f方法的中调用，但执行代码(字符串形式的代码)默认在window全局作用域下，test也指向全局的test
}
 
f();
在f方法中，setTimeout中的test的值是外层的test，而不是f作用域中的test。再看代码3.2：

1
2
3
4
5
6
7
8
9
10
var test = "in the window";
 
setTimeout(function() {alert('outer' + test)}, 0); // outer in the window  ，没有问题，在全局下调用，访问全局中的test
 
function f() {
  var test = 'in the f!';
  setTimeout(function(){alert('inner '+ test)}, 0);  // inner in the f!  有问题，不是说好了执行函数中的this指向的是window吗？那test也应该对应window下                                                      //  的值才对，怎么test的值却是 f()中的值呢？？？？
}
 
f();
呀。。按照前面的经验，f中的setTimeout中的test也应该明明应该是指向外层的test才对吧？？？我们注意到，这个f里面的setTimeout中的第一个参数是一个匿名函数，这是上面两端代码最大的不同。而只要是函数就有它的作用域，我们可以将上面的代码替换成下面的代码3.3：

1
2
3
4
5
6
7
8
9
10
11
12
13
var test = "in the window";
 
setTimeout(function() {alert('outer ' + test)}, 0); // in the window
 
function f() {
  var test = 'in the f!';
 
  function ff() {alert('inner ' + test)} // 能访问到f中的test局部变量
 
  setTimeout(ff, 0);  // inner in the f!
}
 
f();
 再看一段更清晰的代码，3.4：

1
2
3
4
5
6
7
var value=33;
 
function Foo() {
    var value = 42;
    setTimeout(function(){alert(value);alert(this.value)}, 500);  // 先后输出 42 然后输出33  这里的this是第二个this
}
new Foo();
可以确定，延迟执行函数中的this的确是指向了window，毫无疑问，上面的所有代码都可以验证哈。但是延迟执行函数中的其他变量需要根据上下文来确认。

修改代码3.4为3.5，去掉匿名函数的调用方式，会更加清晰：

1
2
3
4
5
6
7
8
9
10
11
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
因此，如果去掉Foo中的value=42的话，那么value的值等于多少呢？undefined还是外层的33？？请看3.5：

1
2
3
4
5
6
7
8
9
10
var value=33;
 
function Foo() {
    function ff() {
      alert(value);   // 输出33
      alert(this.value);  // 输出33  this指向window
    }
    setTimeout(ff, 500);  // 先后输出 33  33
}
Foo();
没错，就是外层的33，因为ff可以访问到window下的value值，就如同setTimeout中的匿名函数一样。　　　　

最后，我们通过对象的方式进行调用，代码3.6：

1
2
3
4
5
6
7
8
9
10
11
12
13
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
语法 clearInterval(id_of_setinterval)

参数 id_of_setinterval 表示由 setInterval() 返回的 ID 值。

clearInterval() 方法可取消由 setInterval() 设置的 timeout；clearInterval() 方法的参数必须是由 setInterval() 返回的 ID 值。

（2）setTimeout 方法用于在指定的毫秒数后调用函数或计算表达式。停止该方法可使用 clearTimeout 方法。具体示例如下：

提示：setTimeout() 只执行 code 一次。如果要多次调用，请使用 setInterval() 或者让 code 自身再次调用 setTimeout()。

复制代码 代码如下:

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