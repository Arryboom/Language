#1.form status
```
<!DOCTYPE html>
<html>
<head>f
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
</head>
<body>

<form ng-app="" name="myForm" ng-init="myText = 'test@runoob.com'">

Email:
<input type="email" name="myAddress" ng-model="myText" required>
<p>编辑邮箱地址，查看状态的改变。</p>
<h1>状态</h1>
<p>Valid: {{myForm.myAddress.$valid}} (如果输入的值是合法的则为 true)。</p>
<p>Dirty: {{myForm.myAddress.$dirty}} (如果值改变则为 true,原始状态下则为false)。</p>
<p>Touched: {{myForm.myAddress.$touched}} (如果通过触屏点击则为 true)。</p>
<p>pristine:{{myForm.myAddress.$pristine}}(从来没有被改动过则为true，一经改动永久false)</p>

</form>

</body>
</html>
```
angular内置了一套表单校验，其中包含几个状态

①valid、invalid、pristine和dirty 
valid标记表单元素有效； 
invalid标记表单元素无效； 
pristine表示表单元素是纯净的，用户未操作过； 
dirty表示表单元素是已被用户操作过； 
②更改css属性 
.ng-pristine {} 
.ng-dirty {} 
.ng-valid {} 
.ng-invalid {} 

$scope.infoFrom.$setPristine();   //设置表单为未编辑的纯净状态
$scope.infoFrom.name.$setDirty();   //设置表单为用户已经编辑过得【脏】状态
$scope.menuForm.$setPristine();
$scope.menuForm.$setUntouched();


去掉浏览器验证
<form name="infofrom" novalidate>
</from>

四.注意事项 
①novalidate 
        标准浏览器如火狐，谷歌等对HTML5有很好的支持。众所周知，HTML5中input的type属性已经具备了验证功能。如果你要自己定义验证方式，那么请加上novalidate属性，以此避开浏览器自行验证。 
②type类型 
        HTML5的type属性可以包含text、email、number等，但是angular又内部重写了这些属性，所以放心大胆的去用吧，angular完全可以满足你所有的验证。 
③type="number"还是ng-pattern="/^[0-9]{6}$/" 
        你可以使用type="number"来限制输入框只能输入数字，当然你也可以用ng-pattern来验证用户输入，从而过滤掉非数字输入。这完全取决于你的爱好，没有硬性规定，只是选择多一些罢了。

```
ng-model 指令根据表单域的状态添加/移除以下类：

ng-empty
ng-not-empty
ng-touched
ng-untouched
ng-valid
ng-invalid
ng-dirty
ng-pending
ng-pristine
```
```
ng-valid: 验证通过
ng-invalid: 验证失败
ng-valid-[key]: 由$setValidity添加的所有验证通过的值
ng-invalid-[key]: 由$setValidity添加的所有验证失败的值
ng-pristine: 控件为初始状态
ng-dirty: 控件输入值已变更
ng-touched: 控件已失去焦点
ng-untouched: 控件未失去焦点
ng-pending: 任何为满足$asyncValidators的情况
```
--- 

#2.数组有相同元素时ng-repeat 失效

如
```
<div ng-app="" ng-init="xray=[1,2,3,4,5,1]">
<li ng-repeat="num in xray">
</li>
</div>
```
此时会失败，修复方法是
```
<li ng-repeat="num in xray track by $index">
</li>
```
--- 
#base

指令Directive
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
</head>
<body ng-app="myApp">

<runoob-directive></runoob-directive>
<div runoob-directive></div>
<script>
var app = angular.module("myApp", []);
app.directive("runoobDirective", function() {
    return {
        template : "<h1>自定义指令!</h1>"
    };
});
</script>

</body>
</html>
```
---  

```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
</head>
<body ng-app="myApp">

<div class="runoob-directive"></div>

<script>
var app = angular.module("myApp", []);
app.directive("runoobDirective", function() {
    return {
        restrict : "C",
        template : "<h1>自定义指令!</h1>"
    };
});
</script>

<p><strong>注意：</strong> 你必须设置 <b>restrict</b> 的值为 "C" 才能通过类名来调用指令。</p>

</body>
</html>

```
-- 
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
</head>
<body ng-app="myApp">

<!-- directive: runoob-directive -->

<script>
var app = angular.module("myApp", []);
app.directive("runoobDirective", function() {
    return {
        restrict : "M",
        replace : true,
        template : "<h1>自定义指令!</h1>"
    };
});
</script>

<p><strong>注意：</strong> 我们需要在该实例添加 <strong>replace</strong> 属性， 否则评论是不可见的。</p>

<p><strong>注意：</strong> 你必须设置 <b>restrict</b> 的值为 "M" 才能通过注释来调用指令。</p>

</body>
</html>
```

>restrict 值可以是以下几种:
E 作为元素名使用
A 作为属性使用
C 作为类名使用
M 作为注释使用
restrict 默认值为 EA, 即可以通过元素名和属性名来调用指令。


** ng-model **
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
</head>
<body>

<div ng-app="myApp" ng-controller="myCtrl">
名字: <input ng-model="name">
</div>

<script>
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
    $scope.name = "John Doe";
});
</script>

<p>使用 ng-model 指令来绑定输入域的值到控制器的属性。</p>

</body>
</html>
```

** trim input **
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
</head>
<body>

<form ng-app="" name="myForm">
    Email:
    <input type="email" name="myAddress" ng-model="text">
    <span ng-show="myForm.myAddress.$error.email">不是一个合法的邮箱地址</span>
</form>

<p>在输入框中输入你的邮箱地址，如果不是一个合法的邮箱地址，会弹出提示信息。</p>

</body>
</html>
```

** css **
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
<style>
input.ng-invalid {
    background-color: lightblue;
}
</style>
</head>
<body>

<form ng-app="" name="myForm">
    输入你的名字:
    <input name="myName" ng-model="myText" required>
</form>

<p>编辑文本域，不同状态背景颜色会发生变化。</p>
<p>文本域添加了 required 属性，该值是必须的，如果为空则是不合法的。</p>

</body>
</html>
```

```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script> 
<div ng-app="" name="x">
	<input ng-model="text" required>
	</input>
	</div>
<style>
	input.ng-valid{
	background-color:lightblue;
	}
	input.ng-invalid{
	background-color:red;
	}
	</style>
<p>编辑文本域，不同状态背景颜色会发生变化。</p>
<p>文本域添加了 required 属性，该值是必须的，如果为空则是不合法的。</p>

</body>
</html>
```
#onclick

```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
<div data-ng-app="methodtest" data-ng-controller="chefcontroller">
	<input data-ng-model="name">
	<button data-ng-click=sayhi()>
		clickhere
	</button>
	{{guesswhoami}}
	</div>
	<script>
		var methodtest=angular.module("methodtest",[]);
		methodtest.controller("chefcontroller",function($scope){$scope.name="menson";$scope.sayhi=function(){$scope.guesswhoami="<h1>"+"hahaha "+$scope.name+"</h1>"};});
	</script>
	


<p>当你修改输入框中的值时，会影响到模型(model),当然也会影响到控制器对应的属性值。</p>

</body>
</html>
```
#scope
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="whoami" ng-controller="chefcontroller">
	<ul>
		<li ng-repeat="x in rays track by $index">{{x}}
		</li>
	</ul>
	</div>
	<script>
		var whoami=angular.module("whoami",[]);
		whoami.controller("chefcontroller",function($scope){$scope.rays=[1,2,3,4,6,7,8,8,3];});
	</script>
	
</body>
</html>
```
** root scope***
每个应用app都有一个root scope，在该应用内相当于全局变量
所有的应用都有一个 $rootScope，它可以作用在 ng-app 指令包含的所有 HTML 元素中。

$rootScope 可作用于整个应用中。是各个 controller 中 scope 的桥梁。用 rootscope 定义的值，可以在各个 controller 中使用。
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="whoami" ng-controller="chefcontroller">
	<ul>
		<li ng-repeat="x in rays track by $index">{{x}}
			<ul>
				<li ng-repeat="m in arrys track by $index">{{m}}
				</li>
			</ul>
		</li>
	</ul>
	</div>
	<script>
		var whoami=angular.module("whoami",[]);
		whoami.controller("chefcontroller",function($scope,$rootScope){$scope.rays=[1,2,3,4,6,7,8,8,3];$rootScope.arrys=[5,3,6,8,5,2,1,69,9,8,3,3];});
	</script>
	
</body>
</html>
```

```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="myApp" ng-controller="myCtrl">

<h1>姓氏为 {{lastname}} 家族成员:</h1>

<ul>
    <li ng-repeat="x in names">{{x}} {{lastname}}</li>
</ul>

</div>

<script>
var app = angular.module('myApp', []);

app.controller('myCtrl', function($scope, $rootScope) {
    $scope.names = ["Emil", "Tobias", "Linus"];
    $rootScope.lastname = "Refsnes";
});
</script>

<p>注意 $rootScope 在循环对象内外都可以访问。</p>

</body>
</html>
```
#特殊情况变量绑定
```
<!DOCTYPE html>
<html>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>

<body>

<div ng-app="test" ng-controller="tcontroller" ng-init="jak='x'">
	<input type="text" ng-model="fname">
	<input type="text" ng-model="nname">
	<p>Hi,<span ng-bind="jak">{{$jak}}</span></p>
	</div>
	<script>
		var test=angular.module("test",[]);
		test.controller("tcontroller",function($scope){$scope.fname="joe";$scope.nname="bac";$scope.jak="hi "+$scope.fname+","+$scope.nname;});
	</script>
</body>
</html>
```
此处如果<span>内不使用ng-bind绑定jak，会出现jak值为空。
>controller里面和ng-init里同时给同名变量赋值时，渲染用到的应该是ng-init里面的值，估计是先执行了controller然后执行了ng-init导致了变量覆盖。

#控制器js与主js分离
main html
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="myApp" ng-controller="namesCtrl"> 

<ul>
  <li ng-repeat="x in names">
    {{ x.name + ', ' + x.country }}
  </li>
</ul>

</div>

<script src="controller.js"></script>

</body>
</html>

```
controller js
```
angular.module('myApp', []).controller('namesCtrl', function($scope) {
    $scope.names = [
        {name:'Jani',country:'Norway'},
        {name:'Hege',country:'Sweden'},
        {name:'Kai',country:'Denmark'}
    ];
});
```

#Multi controller
```
<div ng-app="myApp">
    <div ng-controller="myCtrl1">
        名:  <input type="text" ng-model="firstName"><br>
        姓:  <input type="text" ng-model="lastName"><br>
        <br>
        姓名: {{firstName + " " + lastName}}
    </div>
    <br><br>
    <div ng-controller="myCtrl2">
        名:  <input type="text" ng-model="firstName"><br>
        姓:  <input type="text" ng-model="lastName"><br>
        <br>
        姓名: {{firstName + " " + lastName}}
    </div>
</div>
```

#关于 controller 中作用域的问题


controller 中，如果局部 $scope 和 $rootScope 都存在，且有相同名字的变量，{{变量名}} 指局部变量而不是全局变量，作用域只有当前 controller；{{$root.变量名}} 是全局变量，在 ng-app="" 下任何一个 controller 中都能使用。如果没有 $scope, 只有 $rootScope，那么 {{变量名}} 和 {{$root.变量名}} 就没区别了。

```
<body ng-app="myApp">
<div ng-controller="myCtrl">       //输出结果
    {{first}}<br>                 //ctrl局部first
    {{$root.first}}<br>           //全局first
    {{second}}<br>                //全局second
    {{$root.second}}<br>          //全局second
</div>
<br>
<br>
<div ng-controller="myCtrl2">
    {{first}}<br>                //全局first
    {{$root.first}}<br>          //全局first
    {{second}}<br>              //ctrl2局部second 
    {{$root.second}}            //全局second
</div>
<script>    
var app = angular.module('myApp', []);    
app.controller('myCtrl', function ($scope,$rootScope) {
        $scope.first = 'ctrl局部first';
        $rootScope.first = '全局first';
    });    
app.controller('myCtrl2', function ($scope,$rootScope) {
        $scope.second = 'ctrl2局部second';
        $rootScope.second = '全局second';    
});
</script>
```

#内置过滤器
ng内置了九种过滤

1. currency (货币处理)

　　使用currency可以将数字格式化为货币，默认是美元符号，你可以自己传入所需的符号，例如我传入人民币：

{{num | currency : '￥'}}

　　2. date (日期格式化)

　　原生的js对日期的格式化能力有限，ng提供的date过滤器基本可以满足一般的格式化要求。用法如下：

{{date | date : 'yyyy-MM-dd hh:mm:ss EEEE'}}

　　参数用来指定所要的格式，y M d h m s E 分别表示 年 月 日 时 分 秒 星期，你可以自由组合它们。也可以使用不同的个数来限制格式化的位数。另外参数也可以使用特定的描述性字符串，例如“shortTime”将会把时间格式为12:05 pm这样的。ng提供了八种描述性的字符串，个人觉得这些有点多余，我完全可以根据自己的意愿组合出想要的格式，不愿意去记这么多单词~

　　3. 
　　4. (匹配子串)

　　这个名叫filter的filter（不得不说这名字起的，真让人容易混淆——！）用来处理一个数组，然后可以过滤出含有某个子串 的元素，作为一个子数组来返回。可以是字符串数组，也可以是对象数组。如果是对象数组，可以匹配属性的值。它接收一个参数，用来定义子串的匹配规则。下面 举个例子说明一下参数的用法，我用现在特别火的几个孩子定义了一个数组：

 

$scope.childrenArray = [
{name:'kimi',age:3},
{name:'cindy',age:4},
{name:'anglar',age:4},
{name:'shitou',age:6},
{name:'tiantian',age:5}
];$scope.func = function(e){return e.age>4;}

 

{{ childrenArray | filter : 'a' }} //匹配属性值中含有a的
{{ childrenArray | filter : 4 }} //匹配属性值中含有4的
{{ childrenArray | filter : {name : 'i'} }} //参数是对象，匹配name属性中含有i的
{{childrenArray | filter : func }} //参数是函数，指定返回age>4的

　　4. json(格式化json对象)

　　json过滤器可以把一个js对象格式化为json字符串，没有参数。这东西有什么用呢，我一般也不会在页面上输出一个json串啊，官网说它 可以用来进行调试，嗯，是个不错的选择。或者，也可以用在js中使用，作用就和我们熟悉的JSON.stringify()一样。用法超级简单：

{{ jsonTest | json}}

　　5. limitTo(限制数组长度或字符串长度)

　　limitTo过滤器用来截取数组或字符串，接收一个参数用来指定截取的长度，如果参数是负值，则从数组尾部开始截取。个人觉得这个filter有点鸡肋，首先只能从数组或字符串的开头/尾部进行截取，其次，js原生的函数就可以代替它了，看看怎么用吧：

{{ childrenArray | limitTo : 2 }} //将会显示数组中的前两项

　　6. lowercase(小写)

　　把数据转化为全部小写。太简单了，不多解释。同样是很鸡肋的一个filter，没有参数，只能把整个字符串变为小写，不能指定字母。怎么用我都懒得写了。

　　7. uppercase(大写)

　　同上。

　　8. number(格式化数字)

　　number过滤器可以为一个数字加上千位分割，像这样，123,456,789。同时接收一个参数，可以指定float类型保留几位小数：

{{ num | number : 2 }}

　　9. orderBy(排序)

　　orderBy过滤器可以将一个数组中的元素进行排序，接收一个参数来指定排序规则，参数可以是一个字符串，表示以该属性名称进行排序。可以是 一个函数，定义排序属性。还可以是一个数组，表示依次按数组中的属性值进行排序（若按第一项比较的值相等，再按第二项比较），还是拿上面的孩子数组举例：

<div>{{ childrenArray | orderBy : 'age' }}</div> //按age属性值进行排序，若是-age，则倒序
<div>{{ childrenArray | orderBy : orderFunc }}</div> //按照函数的返回值进行排序
<div>{{ childrenArray | orderBy : ['age','name'] }}</div> //如果age相同，按照name进行排序

```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
<div ng-app="myApp" ng-controller="namesCtrl">
	<input ng-model="namef">
	<ul>
		<li ng-repeat="x in names|filter:namef|orderBy:country">
			{{(x.name|uppercase)+","+x.country}}
		</li>
	</ul>
	</div>


<script>angular.module('myApp', []).controller('namesCtrl', function($scope) {
    $scope.names = [
        {name:'Jani',country:'Norway'},
        {name:'Hege',country:'Sweden'},
        {name:'Kai',country:'Denmark'}
    ];
});</script>

</body>
</html>
```

** 自定义过滤器 **
```
<!DOCTYPE html>
<html>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>

<body>
<div ng-app="xtest" ng-controller="tcontroller">
	<p>balabala:<span>{{xrr|rever}}</span></p>
	</div>
	<script>
		xtest=angular.module("xtest",[]);
		xtest.controller("tcontroller",function($scope){$scope.xrr="blllsooowllzxllfgai,,azxc";});
		xtest.filter("rever",function(){return function(text){return text.split("").reverse().join("");};});
	</script>
</body>
</html>
```
#service
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
<div ng-app="mapp" ng-controller="tcontroller">
	<p>currenturl:   {{curl}}</p>
	</div>
	<script>mapp=angular.module("mapp",[]);
	mapp.controller("tcontroller",function($scope,$location){$scope.curl=$location.absUrl();});
	</script>


</body>
</html>

```
>$location.path()返回"/actual/path"

```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="h" ng-controller="c">
	<p>wex</p>
	<span>{{wel}}</span>
	<button ng-click=read()>xx</button>
	</div>
	<script>
	h=angular.module("h",[]);
		h.controller("c",function($http,$scope){$scope.wel="xvxzda";$scope.read=function(){$http.get("welcome.htm").then(function(response){$scope.wel=response.data;console.log($scope.wel);console.log("xwwa");});};});
	
	</script>

</body>
</html>
```


** $http**

```
$http({
    method: 'GET',
    url: '/someUrl'
}).then(function successCallback(response) {
        // 请求成功执行代码
    }, function errorCallback(response) {
        // 请求失败执行代码
});

```
```
$http.get('/someUrl', config).then(successCallback, errorCallback);
$http.post('/someUrl', data, config).then(successCallback, errorCallback);
```
简单例子
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="h" ng-controller="c">
	<p>wex</p>
	<span>{{wel}}</span>
	<button ng-click=read()>xx</button>
	</div>
	<script>
	h=angular.module("h",[]);
		h.controller("c",function($http,$scope){$scope.wel="xvxzda";$scope.read=function(){$http.get("welcome.htm").then(function(response){$scope.wel=response.data;console.log($scope.wel);console.log("xwwa");});};});
	
	</script>

</body>
</html>
```
可用方法
```
$http.get
$http.head
$http.post
$http.put
$http.delete
$http.jsonp
$http.patch
```
>https://docs.angularjs.org/api/ng/service/$http

读取json
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
</head>
<body>

<div ng-app="a" ng-controller="b">
	<p>guesswhatwedo</p>
	<ul><li ng-repeat="x in xrays">
		{{(x.Name|uppercase)+","+x.Country+","+x.Url}}
		</li>
		</div>
	<script>
	a=angular.module("a",[]);
	a.controller("b",function($http,$scope){
		$http({
    method: 'GET',
    url: 'https://www.runoob.com/try/angularjs/data/sites.php'
}).then(function successCallback(response) {
        $scope.xrays=response.data.sites;
    }, function errorCallback(response) {
        // 请求失败执行代码
		console.log("Cannot get true back");
});

	});</script>
</body>
	<!-- runnob/sites.php-->
	<!-- { "sites": [ { "Name": "鑿滈笩鏁欑▼", "Url": "www.runoob.com", "Country": "CN" }, { "Name": "Google", "Url": "www.google.com", "Country": "USA" }, { "Name": "Facebook", "Url": "www.facebook.com", "Country": "USA" }, { "Name": "寰崥", "Url": "www.weibo.com", "Country": "CN" } ] }-->
</html>
```
读取json_简洁版
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
</head>
<body>

<div ng-app="a" ng-controller="b">
	<p>guesswhatwedo</p>
	<ul><li ng-repeat="x in xrays">
		{{(x.Name|uppercase)+","+x.Country+","+x.Url}}
		</li>
		</div>
	<script>
	a=angular.module("a",[]);
	a.controller("b",function($http,$scope){
		$http.get("https://www.runoob.com/try/angularjs/data/sites.php").then(function(response){$scope.xrays=response.data.sites;});
		
	});</script>
</body>
	<!-- runnob/sites.php-->
	<!-- { "sites": [ { "Name": "鑿滈笩鏁欑▼", "Url": "www.runoob.com", "Country": "CN" }, { "Name": "Google", "Url": "www.google.com", "Country": "USA" }, { "Name": "Facebook", "Url": "www.facebook.com", "Country": "USA" }, { "Name": "寰崥", "Url": "www.weibo.com", "Country": "CN" } ] }-->
</html>
```


我们可以使用内置的$http服务直接同外部进行通信。$http服务只是简单的封装了浏览器原生的XMLHttpRequest对象。

1、链式调用
$http服务是只能接受一个参数的函数，这个参数是一个对象，包含了用来生成HTTP请求的配置内容。这个函数返回一个promise对象，具有success和error两个方法。
$http({
url:'data.json',
method:'GET'
}).success(function(data,header,config,status){
//响应成功
}).error(function(data,header,config,status){
//处理响应失败
});
2、返回一个promise对象
var promise=$http({
method:'GET',
url:"data.json"
});
由于$http方法返回一个promise对象，我们可以在响应返回时用then方法来处理回调。如果使用then方法，会得到一个特殊的参数，它代表了相应对象的成功或失败信息，还可以接受两个可选的函数作为参数。或者可以使用success和error回调代替。
promise.then(function(resp){
//resp是一个响应对象
},function(resp){
//带有错误信息的resp
});
或者这样：
promise.success(function(data,status,config,headers){
//处理成功的响应
});
promise.error(function(data,status,hedaers,config){
//处理失败后的响应
});
then()方法与其他两种方法的主要区别是，它会接收到完整的响应对象，而success()和error()则会对响应对象进行析构。3、快捷的get请求①$http.get('/api/users.json');
get()方法返回HttpPromise对象。

还可以发送比如：delete/head/jsonp/post/put 函数内可接受参数具体参照148页
②以再发送jsonp请求举例说明： 为了发送JSONP请求，其中url必须包含JSON_CALLBACK字样。jsonp(url,config) 其中config是可选的var promise=$http.jsonp("/api/users.json?callback=JSON_CALLBACK");
4、也可以将$http当做函数来使用，这时需要传入一个设置对象，用来说明如何构造XHR对象。

$http({
method:'GET',
url:'/api/users.json',
params:{
'username':'tan'
});
 
其中设置对象可以包含以下主要的键：①method可以是：GET/DELETE/HEAD/JSONP/POST/PUT②url:绝对的或者相对的请求目标③params(字符串map或者对象)这个键的值是一个字符串map或对象，会被转换成查询字符串追加在URL后面。如果值不是字符串，会被JSON序列化。比如这个：//参数会转为？name=ari的形式$http({params:{'name':'ari'}});④data(字符串或者对象)这个对象中包含了将会被当作消息体发送给服务器的数据。通常在发送POST请求时使用。从AngularJS 1.3开始，它还可以在POST请求里发送二进制数据。要发送一个blob对象，你可以简单地通过使用data参数来传递它。例如：
var blob=new Blob(['Hello world'],{type:'text/plain'});
$http({
method:'POST',
url:'/',
data:blob
});
4、响应对象AngularJS传递给then（）方法的响应对象包含了四个属性。◇data这个数据代表转换过后的响应体（如果定义了转换的话）◇status响应的HTTP状态码◇headers这个函数是头信息的getter函数，可以接受一个参数，用来获取对应名字值例如，用如下代码获取X-Auth-ID的值：
$http({
method: 'GET',
url: '/api/users.json'
}).then (resp) {
// 读取X-Auth-ID
resp.headers('X-Auth-ID');
});
◇config这个对象是用来生成原始请求的完整设置对象。◇statusText（字符串）这个字符串是响应的HTTP状态文本。5、缓存HTTP请求默认情况下，$http服务不会对请求进行本地缓存。在发送单独的请求时，我们可以通过向$http请求传入一个布尔值或者一个缓存实例来启用缓存。$http.get('/api/users.json',{ cache: true }).success(function(data) {}).error(function(data) {});第一次发送请求时，$http服务会向/api/users.json发送一个GET请求。第二次发送同一个GET请求时，$http服务会从缓存中取回请求的结果，而不会真的发送一个HTTP GET请求。在这个例子里，由于设置了启用缓存，AngularJS默认会使用$cacheFactory,这个服务是AngularJS在启动时自动创建的。如果想要对AngularJS使用的缓存进行更多的自定义控制，可以向请求传入一个自定义的缓存实例代替true。

---------------------

本文来自 僅此 的CSDN 博客 ，全文地址请点击：https://blog.csdn.net/w329300817/article/details/51996861?utm_source=copy 




** $timeout **
延时后执行指定功能
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
<div ng-app="a" ng-controller="b">
	<p>xzasf</p>
	<p>{{x}}</p>
	<button ng-click=read()>xfa</button>
	</div>
	<script>
	a=angular.module("a",[]);
		a.controller("b",function($scope,$timeout){$scope.x="welc";$scope.read=function(){$timeout(function(){$scope.x="WELCOME";},3000);};});
	
	</script>

</body>
</html>

```

** interval ***
死循环执行
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
<div ng-app="a" ng-controller="c">
	<p>now the time is :
	</p>
	<p>{{time}}</p>
	</div>
	<script>
	a=angular.module("a",[]);
		a.controller("c",function($scope,$interval){$scope.time=new Date().toLocaleTimeString();$interval(function(){$scope.time=new Date().toLocaleTimeString();},500);});
	</script>

</body>
</html>

```
#自定义服务
当你创建了自定义服务，并连接到你的应用上后，你可以在控制器，指令，过滤器或其他服务中使用它。
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
<div ng-app="a" ng-controller="b">
	<p>266 to hex is:</p>
	<p>{{guess}}</p>
	</div>
	<script>
		a=angular.module("a",[]);
		a.service("www",function(){this.conv=function(xr){return xr.toString(16);};});

		a.controller("b",function($scope,www){$scope.guess=www.conv(266);});
	</script>

</body>
</html>
```
** 过滤器调用实例 **
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
<div ng-app="a" ng-controller="b">
	<p>guess whoami</p>
	<p>{{ppp|below}}</p>
	</div>
<script>
	a=angular.module("a",[]);
	a.service("hextoray",function(){this.h2b=function(xx){return xx.toString(16);};});
	a.filter("below",['hextoray',function(hextoray){return function(xw){return hextoray.h2b(xw)};}]);
	a.controller("b",function($scope){$scope.ppp=666;});
	
	</script>


</body>
</html>

```
#ul style
```
<html>
<body>

<h4>Disc 项目符号列表：</h4>
<ul type="disc">
 <li>苹果</li>
 <li>香蕉</li>
 <li>柠檬</li>
 <li>桔子</li>
</ul>  

<h4>Circle 项目符号列表：</h4>
<ul type="circle">
 <li>苹果</li>
 <li>香蕉</li>
 <li>柠檬</li>
 <li>桔子</li>
</ul>  

<h4>Square 项目符号列表：</h4>
<ul type="square">
 <li>苹果</li>
 <li>香蕉</li>
 <li>柠檬</li>
 <li>桔子</li>
</ul>  

</body>
</html>

```

#ng-options创建下拉框
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>
	<div ng-app="a" ng-controller="b">
		<select ng-init="selectedName=names[0]" ng-model="selectedName" ng-options="x for x in names">
		</select>
	</div>
	<script>
		a=angular.module("a",[]);
		a.controller("b",function($scope){$scope.names=["jack","mole","cell","chink"];});
	</script>

<p>该实例演示了 ng-options 指令的使用。</p>

</body>
</html>

```
** 与repeat方式创建下拉框的不同**
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="myApp" ng-controller="myCtrl">

<p>选择网站:</p>

<select ng-model="selectedSite" ng-options="x.site for x in sites">
</select>

<h1>你选择的是: {{selectedSite.site}}</h1>
<p>网址为: {{selectedSite.url}}</p>

</div>

<script>
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
   $scope.sites = [
	    {site : "Google", url : "http://www.google.com"},
	    {site : "Runoob", url : "https://www.runoob.com"},
	    {site : "Taobao", url : "http://www.taobao.com"}
	];
});
</script>

<p>该实例演示了使用 ng-options  指令来创建下拉列表，选中的值是一个对象。</p>
</body>
</html>

```
repeat
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="myApp" ng-controller="myCtrl">

<p>选择网站:</p>

<select ng-model="selectedSite">
<option ng-repeat="x in sites" value="{{x.url}}">{{x.site}}</option>
</select>

<h1>你选择的是: {{selectedSite}}</h1>

</div>

<script>
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
   $scope.sites = [
	    {site : "Google", url : "http://www.google.com"},
	    {site : "Runoob", url : "https://www.runoob.com"},
	    {site : "Taobao", url : "http://www.taobao.com"}
	];
});
</script>

<p>该实例演示了使用 ng-repeat 指令来创建下拉列表，选中的值是一个字符串。</p>
</body>
</html>
```


#table
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
</head>
<body>
<div ng-app="a" ng-controller="b">
	<table>
	<tr ng-repeat="x in names">
		<td> {{x.Name}}</td>
		<td>{{x.Country}}</td>
	</tr>
	</table>
	</div>
<script>
	a=angular.module("a",[]);
	a.controller("b",function($scope,$http,$timeout){$timeout(function(){console.log("start");$http.get("/try/angularjs/data/Customers_JSON.php").then(function(resp){$scope.names=resp.data.records;});},3000)});
	</script>


</body>
</html>
```
>Customers_json.php
```
{
"records":[
{"Name":"Alfreds Futterkiste","City":"Berlin","Country":"Germany"},
{"Name":"Ana Trujillo Emparedados y helados","City":"México D.F.","Country":"Mexico"},
{"Name":"Antonio Moreno Taquería","City":"México D.F.","Country":"Mexico"},
{"Name":"Around the Horn","City":"London","Country":"UK"},
{"Name":"B's Beverages","City":"London","Country":"UK"},
{"Name":"Berglunds snabbköp","City":"Luleå","Country":"Sweden"},
{"Name":"Blauer See Delikatessen","City":"Mannheim","Country":"Germany"},
{"Name":"Blondel père et fils","City":"Strasbourg","Country":"France"},
{"Name":"Bólido Comidas preparadas","City":"Madrid","Country":"Spain"},
{"Name":"Bon app'","City":"Marseille","Country":"France"},
{"Name":"Bottom-Dollar Marketse","City":"Tsawassen","Country":"Canada"},
{"Name":"Cactus Comidas para llevar","City":"Buenos Aires","Country":"Argentina"},
{"Name":"Centro comercial Moctezuma","City":"México D.F.","Country":"Mexico"},
{"Name":"Chop-suey Chinese","City":"Bern","Country":"Switzerland"},
{"Name":"Comércio Mineiro","City":"São Paulo","Country":"Brazil"}
]
}
```

** add some css style so more beautiful **
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
<style>
table, th , td  {
  border: 1px solid grey;
  border-collapse: collapse;
  padding: 5px;
}
table tr:nth-child(odd)	{
  background-color: #f1f1f1;
}
table tr:nth-child(even) {
  background-color: #ffffff;
}
</style>
</head>
<body>

<div ng-app="myApp" ng-controller="customersCtrl"> 

<table>
  <tr ng-repeat="x in names">
    <td>{{ x.Name }}</td>
    <td>{{ x.Country }}</td>
  </tr>
</table>

</div>

<script>
var app = angular.module('myApp', []);
app.controller('customersCtrl', function($scope, $http) {
    $http.get("/try/angularjs/data/Customers_JSON.php")
    .then(function (result) {
        $scope.names = result.data.records;
    });
});
</script>

</body>
</html>
```

another:
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
<style>
	td,th,table{
	border:2px dotted #00005c;
	border-collapse:collapse;
	padding:2px	
	}
	table tr:nth-child(odd){
	background-color:#f1f1f1;
	}
	table tr:nth-child(even){background-color:#ffffff;}
	</style>
<div ng-app="a" ng-controller="b">
	<table>
		<td>xx</td>
		<td>aa</td>
	<tr ng-repeat="x in names">
	<td>{{ $index + 1 }}</td>
	<td>{{x.Name|uppercase}}</td>
	<td>{{x.Country|uppercase}}</td>
	</tr>
	</table>
	</div>
	<script>
	a=angular.module("a",[]);
	a.controller("b",function($scope,$http){$http.get("/try/angularjs/data/Customers_JSON.php").then(function(resp){$scope.names=resp.data.records});});
	</script>

</body>
</html>
```
another:
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.bootcss.com/angular.js/1.6.3/angular.min.js"></script>
<style>
table, td  {
  border: 1px solid grey;
  border-collapse: collapse;
  padding: 5px;
}
</style>
</head>
<body>

<div ng-app="myApp" ng-controller="customersCtrl"> 

<table>
  <tr ng-repeat="x in names">
    <td ng-if="$odd" style="background-color:#f1f1f1">
    {{ x.Name }}</td>
    <td ng-if="$even">
    {{ x.Name }}</td>
    <td ng-if="$odd" style="background-color:#f1f1f1">
    {{ x.Country }}</td>
    <td ng-if="$even">
    {{ x.Country }}</td>
  </tr>
</table>

</div>

<script>
var app = angular.module('myApp', []);
app.controller('customersCtrl', function($scope, $http) {
    $http.get("/try/angularjs/data/Customers_JSON.php")
    .then(function (result) {
        $scope.names = result.data.records;
    });
});
</script>

</body>
</html>
```

#With SQL backend
以下的 PHP 代码运行使用的网站进行跨域访问。
some need fix angular.js link to https://libs.cdnjs.net/angular.js/1.4.6/angular.min.js
```
header("Access-Control-Allow-Origin: *");
```
front
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.staticfile.com/angular.js/1.4.6/angular.min.js"></script>
<style>
table, th , td  {
  border: 1px solid grey;
  border-collapse: collapse;
  padding: 5px;
}
table tr:nth-child(odd) {
  background-color: #f1f1f1;
}
table tr:nth-child(even) {
  background-color: #ffffff;
}
</style>
</head>
<body>

<div ng-app="myApp" ng-controller="customersCtrl"> 

<table>
<tr ng-repeat="x in names">
	<td>{{ x.Name }}</td>
	<td>{{ x.Country }}</td>
</tr>
</table>

</div>

<script>
var app = angular.module('myApp', []);
app.controller('customersCtrl', function($scope, $http) {
   $http.get("http://www.runoob.com/try/angularjs/data/Customers_SQL.aspx")
   .success(function (response) {$scope.names = response.records;});
});
</script>

</body>
</html>
```
**php**
```
<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("myServer", "myUser", "myPassword", "Northwind");

$result = $conn->query("SELECT CompanyName, City, Country FROM Customers");

$outp = "";
while($rs = $result->fetch_array(MYSQLI_ASSOC)) {
    if ($outp != "") {$outp .= ",";}
    $outp .= '{"Name":"'  . $rs["CompanyName"] . '",';
    $outp .= '"City":"'   . $rs["City"]        . '",';
    $outp .= '"Country":"'. $rs["Country"]     . '"}'; 
}
$outp ='{"records":['.$outp.']}';
$conn->close();

echo($outp);
?>
```
**PHP 和 MS Access 代码实例**
```
<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=ISO-8859-1");

$conn = new COM("ADODB.Connection");
$conn->open("PROVIDER=Microsoft.Jet.OLEDB.4.0;Data Source=Northwind.mdb");

$rs = $conn->execute("SELECT CompanyName, City, Country FROM Customers");

$outp = "";
while (!$rs->EOF) {
    if ($outp != "") {$outp .= ",";}
    $outp .= '{"Name":"'  . $rs["CompanyName"] . '",';
    $outp .= '"City":"'   . $rs["City"]        . '",';
    $outp .= '"Country":"'. $rs["Country"]     . '"}'; 
    $rs->MoveNext();
}
$outp ='{"records":['.$outp.']}';

$conn->close();

echo ($outp);
?>
```
**ASP.NET, VB 和 MS Access 代码实例**
```
<%@ Import Namespace="System.IO"%>
<%@ Import Namespace="System.Data"%>
<%@ Import Namespace="System.Data.OleDb"%>
<%
Response.AppendHeader("Access-Control-Allow-Origin", "*")
Response.AppendHeader("Content-type", "application/json")
Dim conn As OleDbConnection
Dim objAdapter As OleDbDataAdapter
Dim objTable As DataTable
Dim objRow As DataRow
Dim objDataSet As New DataSet()
Dim outp
Dim c
conn = New OledbConnection("Provider=Microsoft.Jet.OLEDB.4.0;data source=Northwind.mdb")
objAdapter = New OledbDataAdapter("SELECT CompanyName, City, Country FROM Customers", conn)
objAdapter.Fill(objDataSet, "myTable")
objTable=objDataSet.Tables("myTable")

outp = ""
c = chr(34)
for each x in objTable.Rows
if outp <> "" then outp = outp & ","
outp = outp & "{" & c & "Name"    & c & ":" & c & x("CompanyName") & c & ","
outp = outp &       c & "City"    & c & ":" & c & x("City")        & c & "," 
outp = outp &       c & "Country" & c & ":" & c & x("Country")     & c & "}"
next

outp ="{" & c & "records" & c & ":[" & outp & "]}"
response.write(outp)
conn.close
%>
```
**ASP.NET, VB Razor 和 SQL Lite 代码实例**
```
@{
Response.AppendHeader("Access-Control-Allow-Origin", "*")
Response.AppendHeader("Content-type", "application/json")
var db = Database.Open("Northwind");
var query = db.Query("SELECT CompanyName, City, Country FROM Customers");
var outp =""
var c = chr(34)
}
@foreach(var row in query)
{
if outp <> "" then outp = outp + ","
outp = outp + "{" + c + "Name"    + c + ":" + c + @row.CompanyName + c + ","
outp = outp +       c + "City"    + c + ":" + c + @row.City        + c + ","
outp = outp +       c + "Country" + c + ":" + c + @row.Country     + c + "}"
}
outp ="{" + c + "records" + c + ":[" + outp + "]}"
@outp
```

#HTML DOM
禁用按钮
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://libs.cdnjs.net/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="" ng-init="mySwitch=true">
<p>
<button ng-disabled="mySwitch">点我!</button>
</p>
<p>
<input type="checkbox" ng-model="mySwitch"/>按钮
</p>
<p>
{{ mySwitch }}
</p>
</div> 

</body>
</html>
```
隐藏显示ng-hide,ng-show
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://libs.cdnjs.net/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="a" ng-controller="b">

<p ng-hide="true">我是不可见的。</p>
	<p ng-hide="trick">x</p>
<p ng-hide="false">我是可见的。</p>

</div> 
<script>
	a=angular.module("a",[]);
	a.controller("b",function($scope){$scope.trick=false;});
	</script>
</body>
</html>
```
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://libs.cdnjs.net/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="a" ng-controller="b">

<p ng-show="true">我是可见的。</p>
	<p>{{guess}}</p>
<p ng-show="trick">我是不可见的。</p>

</div> 
<script>
	a=angular.module("a",[]);
	a.controller("b",function($scope){$scope.trick=true;});
	</script>
</body>
</html>
```
ng-show/hide表达式可以是条件
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://libs.cdnjs.net/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="" ng-init="hour=13">

<p ng-show="hour < 12">我是可见的。</p>

</div>

</body>
</html>

```
#Angular events
demo
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://libs.cdnjs.net/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<div ng-app="a" ng-controller="b">
	<p>{{x}}</p>
	<button ng-click="x=x+1">follow me</button>
	</div>
	<script>
		a=angular.module("a",[]);
		a.controller("b",function($scope){$scope.x=0});</script>

</body>
</html>

```

#ng各种表单验证，限制，设置
常用的表单验证指令 
1. 必填项验证
某个表单输入是否已填写，只要在输入字段元素上添加HTML5标记required即可：

<input type="text" required />

2. 最小长度
验证表单输入的文本长度是否大于某个最小值，在输入字段上使用指令ng-minleng= "{number}"：

<input type="text" ng-minlength="5" /> 
3. 最大长度
验证表单输入的文本长度是否小于或等于某个最大值，在输入字段上使用指令ng-maxlength="{number}"：

<input type="text" ng-maxlength="20" />
4. 模式匹配
使用ng-pattern="/PATTERN/"来确保输入能够匹配指定的正则表达式：

<input type="text" ng-pattern="/[a-zA-Z]/" /> 
5. 电子邮件
验证输入内容是否是电子邮件，只要像下面这样将input的类型设置为email即可：

<input type="email" name="email" ng-model="user.email" /> 
6. 数字
验证输入内容是否是数字，将input的类型设置为number：

<input type="number" name="age" ng-model="user.age" /> 
7. URL
 验证输入内容是否是URL，将input的类型设置为 url：

<input type="url" name="homepage" ng-model="user.facebook_url" />

```
<div class="col-md-6">
        <form role="form" class="form-horizontal">
            <div class="form-group">
                <div class="col-md-4">
                    <label for="name">1.必填项</label>
                </div>
                <div class="col-md-8">
                    <input class="form-control" id="name" type="text" required ng-model='user.name' />
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-4">
                    <label for="minlength">2.最小长度=5</label>
                </div>
                <div class="col-md-8">
                    <input type="text" id="minlength" ng-minlength="5" ng-model="user.minlength" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-4">
                    <label for="minlength">3.最大长度=20</label>
　　　　　　　　　　</div>
                <div class="col-md-8">
                    <input type="text" ng-model="user.maxlength" ng-maxlength="20" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-4">
                    <label for="minlength">4. 模式匹配</label>
               </div>
                <div class="col-md-8">
                 <input type="text" id="minlength" ng-model="user.pattern" ng-pattern="/^[a-zA-Z]*\d$/" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-4">
                    <label for="email">5. 电子邮件</label>
　　　　　　　　　　</div>
                <div class="col-md-8">
                    <input type="email" id="email" name="email" ng-model="user.email" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-4">
                    <label for="age">6. 数字</label>
　　　　　　　　　　</div>
                <div class="col-md-8">
                    <input type="number" id="age" name="age" ng-model="user.age" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-4">
                    <label for="url"> 7. URL</label>
　　　　　　　　　　</div>
                <div class="col-md-8">
                    <input type="url" id="url" name="homepage" ng-model="user.url" class="form-control" />
                </div>
            </div>
            <div class="form-group text-center">
                <input class="btn btn-primary btn-lg" type="submit" value="提交" />
            </div>
        </form>       
    </div>
    <div class="col-md-12">
        1.必填项:{{user.name}}<br>
        2.最小长度=5:{{user.minlength}}<br>
        3.最大长度=20:{{user.maxlength}}<br>
        4.模式匹配:{{user.pattern}}<br>
        5.电子邮件:{{user.email}}<br>
        6.数字:{{user.age}}<br>
        7.URL:{{user.url}}<br>
    </div>
```
>https://www.cnblogs.com/rohelm/p/4033513.html


#载入库的时机

什么时候载入库?
Note	在我们的实例中，所有 AngularJS 库都在 HTML 文档的头部载入。
对于 HTML 应用程序，通常建议把所有的脚本都放置在 <body> 元素的最底部。

这会提高网页加载速度，因为 HTML 加载不受制于脚本加载。

在我们的多个 AngularJS 实例中，您将看到 AngularJS 库是在文档的 <head> 区域被加载。

在我们的实例中，AngularJS 在 <head> 元素中被加载，因为对 angular.module 的调用只能在库加载完成后才能进行。

另一个解决方案是在 <body> 元素中加载 AngularJS 库，但是必须放置在您的 AngularJS 脚本前面

#单选框
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
</head>
<body ng-app="">
	<p>check one you like </p>
	<input type="radio" ng-model="that" value="bark">bark</input>
	<input type="radio" ng-model="that" value="rabbit">rabbit</input>
<input type="radio" ng-model="that" value="fruit">fruit</input>
<div ng-switch="that">
	<div ng-switch-when="bark">
	<h1> take it </h1></div>
	<div ng-switch-when="rabbit">
<h1>how can you eat rabbit it's so cute.</h1>
</div>
<div ng-switch-when="fruit">
<h1>vitamin C is great</h1>
</div>
</div>

</body>
</html>
```
#下拉框
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
</head>
<body ng-app="">

	<form> <select ng-model="name">
		<option value=""></option>
		<option value="dice">DICE</option>
		<option value="ea">EA</option>
		<option value="ubi">UBI</option>
		</select>
	</form>
	<div ng-switch="name">
		<div ng-switch-when="dice">
			<h1>dice is a good place</h1>
		</div>
<div ng-switch-when="ea">
	<h1>ea game is not a good price</h1>
		</div>
		<div ng-switch-when="ubi">
			<h1>ubi has potato servers</h1>
		</div>
	</div>

<p>ng-switch 指令根据下拉菜单的选择结果显示或隐藏 HTML 区域。</p>

</body>
</html>
```

#表单验证
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

<h2>验证实例</h2>
<form ng-app="a" ng-controller="b" novalidate name="tankform">
	<p>User:</p><input ng-model="user" name="user" type="text" required></input>
	<span ng-show="tankform.user.$dirty && tankform.user.$invalid && !tankform.user.$error.required"><span>Not valid</span></span>
	<span ng-show="tankform.user.$error.required" >No value</span>
	<p>Email:</p><input ng-model="email" name="email" type="email" required></input>
	<span ng-show="tankform.email.$dirty && tankform.email.$invalid &&!tankform.email.$error.required"><span style="color:red">Email not valid.</span></span>
	<span ng-show="tankform.email.$error.required">You didn't input the email</span>
	<input type="submit" ng-disabled="!tankform.email.$dirty || tankform.email.$invalid || tankform.user.$invalid">
	</input>
	</form>
	<script>
		a=angular.module("a",[]);
		a.controller("b",function($scope){$scope.user="smith";});
	</script>
	
</body>
</html>

```
>对邮件地址格式的识别是通过input标签内的type="email"来实现的。

#Angular API
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<script src="https://cdn.staticfile.org/angular.js/1.7.0/angular.min.js"></script>
</head>
<body>
<div ng-app="a" ng-controller="b">
	<p>{{x1}}</p>
	<p>{{x2}}</p>
	<p>{{x3}}</p>
	<p>isstring?--{{z1}}</p>
	<P>isnumber?--{{z2}}</p>
	</div>
	<script>
		a=angular.module("a",[]);
		a.controller("b",function($scope){$scope.x1="AwvS";$scope.x2=angular.$$lowercase($scope.x1);$scope.x3=angular.$$uppercase($scope.x1);$scope.z1=angular.isString($scope.x1);$scope.z2=angular.isNumber($scope.x1);});
	</script>

</body>
</html>
```

#Angular判断数据类型
angular.isArray

判断括号内的值是否为数组。

格式：angular.isArray(value);

value: 被判断是否为数组的值。

---------------------------------------------------------------

angular.isDate

判断括号内的值是否是一个时间。

格式：angular.isDate(value);

value：被判断是否为时间的值。

---------------------------------------------------------------

angular.isDefined

判断括号内的值是否存在。

格式：angular.isDefined(value);

value:被判断是否存在的值。

---------------------------------------------------------------

angular.isFunction

判断括号内的值是否是一个函数。

格式：angular.isFunction(value);

value：被判断是否是函数在值。

---------------------------------------------------------------

angular.isElement

判断括号内的值是否是一个Dom元素（或者包装的jQuery元素）

格式：angular.isElement(value);

value:被判断是否是Dom元素/jQuery元素在值。

---------------------------------------------------------------

angular.isNumber

判断括号内的值是否是数字。

格式：angular.isNumber(value);

value:被判断是否是数字的值。

---------------------------------------------------------------

angular.isObject

判断括号内的值是否是一个对象。和Javascript的typeof不相同，它不把null视为对象。需要注意的是：数组也是对象。

格式：angular.isObject(value);

value:被判断是否是对象的值。

---------------------------------------------------------------

angular.isString

判断括号内的值是否是字符串。

格式：angular.isString(value);

value:被判断是否是字符串的值。

---------------------------------------------------------------

angular.isUndefined

判断括号内的值是否是undefined。

格式：angular.isUndefined(value);

value:被判断是否是undefined的值。

#Angular取长度时报length undefined
>https://stackoverflow.com/questions/32909441/angularjs-ui-calendar-typeerror-cannot-read-property-length-of-undefined

需要在使用变量的length之前定义
#安哥拉表格
>下列表格未按照设想禁用提交按钮，原因为{name:"jack",sex:"man",password:1,id:1}中password被javascript当作int处理，无法获取到password的length导致判断长度比较为undefined导致失败。
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="//apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
</head>
<body ng-app="a" ng-controller="b">
<!--<div class="IdontKnowWhyIjustWannaAddThis">-->
	<div class="container">
	<h4>users</h4>
	<table class="table table-striped">
		<thead>
		<tr>
			<td>name</td>
			<td>sex</td>
			<td>edit</td>
			</tr>
		</thead>
		<tbody>
			<tr ng-repeat="x in users track by $index">
				<td>{{x.name}}</td>
				<td>{{x.sex}}</td>
				<td><button class="btn" ng-click="edituser(x.id)"><span class="glyphicon glyphicon-pencil"></span>edit</button><td>
			</tr>
		</tbody>
	</table>
	<hr>
	<h4 ng-show="editstatus">编辑用户</h4>
	<h4 ng-show="!editstatus">创建新用户</h4>
	<form class="form-horizontal">
		<div class="form-group">
		<label class="col-sm-2 control-label">name:</label>
		<input ng-disabled="editstatus" type="text" ng-model="name"></input>
			</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">sex:</label>
		<input type="text" ng-model="sex"></input>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">password:</label>
		<input type="text" ng-model="password"></input>
	</div>
	</form>
	<hr>
	<button ng-disabled="error" ng-click="submit()"><span class="glyphicon glyphicon-save"></span>Submit</button>
	<div>
		<script>
		a=angular.module("a",[]);
			a.controller("b",function($scope){
				$scope.name='';
				$scope.password='';
				$scope.sex='';
				$scope.users=[{name:"jack",sex:"man",password:1,id:1},{name:"jacks",sex:"man",password:1,id:2},{name:"jacak",sex:"woman",password:1,id:3}];
				$scope.edituser=function(id){$scope.editstatus=true;$scope.ondd=true;$scope.name=$scope.users[id-1].name;$scope.sex=$scope.users[id-1].sex;$scope.password=$scope.users[id-1].password;};
				$scope.submit=function(){alert("Success(->pretend)")};
				$scope.check=function(){console.log("check begin");console.log(!!$scope.name.length &&
    !!$scope.sex.length &&
    !!$scope.password.length);console.log("fuck:"+$scope.sex.length);if($scope.editstatus && ($scope.name.length &&
    $scope.sex.length &&
    $scope.password.length)) {
    $scope.error = false;console.log("1");
  }else{$scope.error=true;console.log("2");};if(!$scope.sex.length||!$scope.name.length||!$scope.password.length){$scope.error=true;console.log("3");}else{$scope.error=false;console.log("5");};};
				$scope.$watch("name",function(){$scope.check()});
				$scope.$watch("sex",function(){$scope.check()});
				$scope.$watch("password",function(){$scope.check()});
											 });
		</script>
</body>
</html>
```

**正常版**
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="//apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
</head>
<body ng-app="a" ng-controller="b">
<!--<div class="IdontKnowWhyIjustWannaAddThis">-->
	<div class="container">
	<h4>users</h4>
	<table class="table table-striped">
		<thead>
		<tr>
			<td>name</td>
			<td>sex</td>
			<td>edit</td>
			</tr>
		</thead>
		<tbody>
			<tr ng-repeat="x in users track by $index">
				<td>{{x.name}}</td>
				<td>{{x.sex}}</td>
				<td><button class="btn" ng-click="edituser(x.id)"><span class="glyphicon glyphicon-pencil"></span>edit</button><td>
			</tr>
		</tbody>
	</table>
	<hr>
	<h4 ng-show="editstatus">编辑用户</h4>
	<h4 ng-show="!editstatus">创建新用户</h4>
	<form class="form-horizontal">
		<div class="form-group">
		<label class="col-sm-2 control-label">name:</label>
		<input ng-disabled="editstatus" type="text" ng-model="name"></input>
			</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">sex:</label>
		<input type="text" ng-model="sex"></input>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">password:</label>
		<input type="text" ng-model="password"></input>
	</div>
	</form>
	<hr>
	<button ng-disabled="error" ng-click="submit()"><span class="glyphicon glyphicon-save"></span>Submit</button>
	<div>
		<script>
		a=angular.module("a",[]);
			a.controller("b",function($scope){
				$scope.name='';
				$scope.password='';
				$scope.sex='';
				$scope.users=[{name:"jack",sex:"man",password:"1",id:1},{name:"jacks",sex:"man",password:"1",id:2},{name:"jacak",sex:"woman",password:"1",id:3}];
				$scope.edituser=function(id){$scope.editstatus=true;$scope.ondd=true;$scope.name=$scope.users[id-1].name;$scope.sex=$scope.users[id-1].sex;$scope.password=$scope.users[id-1].password;};
				$scope.submit=function(){alert("Success(->pretend)")};
				$scope.check=function(){console.log("check begin");console.log($scope.editstatus);console.log($scope.name.length+"-"+
    $scope.sex.length+"-"+
    $scope.password.length);console.log("fuck:"+$scope.sex.length);if($scope.editstatus && ($scope.name.length &&
    $scope.sex.length &&
    $scope.password.length)) {
    $scope.error = false;console.log("1");
  }else{$scope.error=true;console.log("2");};if(!$scope.sex.length||!$scope.name.length||!$scope.password.length){$scope.error=true;console.log("3");}else{$scope.error=false;console.log("5");};};
				$scope.$watch("name",function(){$scope.check()});
				$scope.$watch("sex",function(){$scope.check()});
				$scope.$watch("password",function(){$scope.check()});
											 });
		</script>
</body>
</html>

```

#Bootstrap
Bootstrap 类解析
元素	Bootstrap 类	定义
<div>	container	内容容器
<table>	table	表格
<table>	table-striped	带条纹背景的表格
<button>	btn	按钮
<button>	btn-success	成功按钮
<span>	glyphicon	字形图标
<span>	glyphicon-pencil	铅笔图标
<span>	glyphicon-user	用户图标
<span>	glyphicon-save	保存图标
<form>	form-horizontal	水平表格
<div>	form-group	表单组
<label>	control-label	控制器标签
<label>	col-sm-2	跨越 2 列
<div>	col-sm-10	跨越 10 列



#ng-include

html内包含html


```
<!DOCTYPE html>
<html>
<meta charset="utf-8">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
	<body>
		<div ng-app="">
			<div ng-include="'runoob.htm'"></div></div>
		</body>
</html>
```
ng-include 指令除了可以包含 HTML 文件外，还可以包含 AngularJS 代码,其中的Angularjs代码也会正常执行:
如以下例子:
1.html
```
<!DOCTYPE html>
<html>
<meta charset="utf-8">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
<body>

<div ng-app="myApp" ng-controller="sitesCtrl"> 
  <div ng-include="'sites.htm'"></div>
</div>
 
<script>
var app = angular.module('myApp', []);
app.controller('sitesCtrl', function($scope, $http) {
    $http.get("sites.php").then(function (response) {
        $scope.names = response.data.records;
    });
});
</script>

<p>AngularJS 代码包含在 "sites.htm" 文件中。</p>

</body>
</html>
```
2.php
```
{"records":[{"Name":"\u83dc\u9e1f\u6559\u7a0b","Url":"www.runoob.com"},{"Name":"\u83dc\u9e1f\u5de5\u5177","Url":"c.runoob.com"},{"Name":"Google \u641c\u7d22","Url":"www.google.com"},{"Name":"\u6dd8\u5b9d","Url":"www.taobao.com"},{"Name":"\u5fae\u535a","Url":"www.weibo.com"}]}
```
3.htm
```
<table>
<tr ng-repeat="x in names">
<td>{{ x.Name }}</td>
<td>{{ x.Url }}</td>
</tr>
</table>
```

**跨域包含**

默认情况下， ng-include 指令不允许包含其他域名的文件。

如果你需要包含其他域名的文件，你需要设置域名访问白名单：
```
<!DOCTYPE html>
<html>
<meta charset="utf-8">
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
<body ng-app="myApp">
 
<div ng-include="'http://c.runoob.com/runoobtest/angular_include.php'"></div>
 
<script>
var app = angular.module('myApp', [])
app.config(function($sceDelegateProvider) {
    $sceDelegateProvider.resourceUrlWhitelist([
        'http://c.runoob.com/runoobtest/**'
    ]);
});
</script>
<p>你需要设置服务端允许跨域访问，设置方法可参考 <a target="_blank" href="/w3cnote/php-ajax-cross-border.html">PHP Ajax 跨域问题最佳解决方案</a>。
</body>
</html>
```
>此外，要保证访问的目标资源也允许跨域访问

php示例：
```
<?php
// 允许所有域名可以访问
header('Access-Control-Allow-Origin:*');
 
echo '<b style="color:red">我是跨域的内容</b>';
?>
```



#Angular实现表格增删改查

>https://www.jb51.net/article/117829.htm

```
<!DOCTYPE html>
<html>
<head lang="en">
 <meta charset="UTF-8">
 <title>实现表格的增删改查</title>
 
 <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
 <meta http-equiv="description" content="this is my page">
 <meta http-equiv="content-type" content="text/html; charset=UTF-8">
 <link rel="stylesheet" href="//apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css" rel="external nofollow" >
 <link rel="stylesheet" href="css/font-awesome.css" rel="external nofollow" type="text/css"></link>
 <link rel="stylesheet" href="css/ui.css" rel="external nofollow" type="text/css"></link>
 <link rel="stylesheet" href="css/form.css" rel="external nofollow" type="text/css"></link>
 
 <script type="text/javascript" src="js/jquery-1.11.1.js"></script>
 <script type="text/javascript" src="js/bootstrap.js"></script>
 <script src="http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min.js"></script>
 <style>
  .add{
   position:relative;
   top:-40px;
   left:1000px;
  }
 </style>
 </head>
 
 <body>
 <div ng-app="myapp" ng-controller="myCtrl">
  <h2>管理信息：</h2><br>
  <p>搜索：<input type="text" placeholder="请输入关键字" ng-model="test"></p>
  <button class="btn btn-primary add" ng-click="add()">添加</button>
  <table class="table table-bordered" style="text-align: center">
   <thead>
    <tr>
     <td>姓名</td>
     <td>年龄</td>
     <td>城市</td>
     <td>操作</td>
    </tr>
   </thead>
   <tbody>
    <tr ng-repeat="x in texts | filter:test">
     <td>{{x.name}}</td>
     <td>{{x.age}}</td>
     <td>{{x.city}}</td>
     <td>
      <button class="btn btn-warning"" ng-click="update($index)">修改</button> 
      <button class="btn btn-danger" ng-click="del($index)">删除</button>
     </td>
    </tr>
   </tbody>
  </table>
  
  <!-- 添加信息 -->
  <div class="modal" id="modal-1">

    <div class="modal-dialog">

     <div class="modal-content">

      <div class="modal-header">
       <button class="close" data-dismiss="modal">
        <span class="glyphicon glyphicon-remove"></span>
       </button>
       <h3 class="modal-title">添加信息</h3>
      </div>

      <div class="modal-body">
       <div>姓名：</div>
       <input ng-model="newName" type="text">
       <div>年龄：</div>
       <input ng-model="newAge" type="text">
       <div>城市：</div>
       <input ng-model="newCity" type="text">
      </div>

      <div class="modal-footer">
       <button class="btn btn-default" data-dismiss="modal">关闭</button>
       <button class="btn btn-success" ng-click="save()">保存</button>
      </div>

     </div>

    </div>

  </div>
  
  <!-- 修改信息 -->
  <div class="modal" id="modal-2">

    <div class="modal-dialog">

     <div class="modal-content">

      <div class="modal-header">
       <button class="close" data-dismiss="modal">
        <span class="glyphicon glyphicon-remove"></span>
       </button>
       <h3 class="modal-title">修改信息</h3>
      </div>

      <div class="modal-body">
       <div>姓名：</div>
       <input ng-model="prod.name" value="{{prod.name}}" type="text">
       <div>年龄：</div>
       <input ng-model="prod.age" value="{{prod.age}}" type="text">
       <div>城市：</div>
       <input ng-model="prod.city" value="{{prod.city}}" type="text">
      </div>

      <div class="modal-footer">
       <button class="btn btn-default" data-dismiss="modal">关闭</button>
       <button class="btn btn-success" ng-click="ensure()">确定</button>
      </div>

     </div>

    </div>

   </div>
 </div>
 
 <script type="text/javascript">
  var app = angular.module('myapp',[]);
  app.controller('myCtrl',function($scope){
   //定义表格内容
   $scope.texts = [
    {name:"张三",age:"23",city:"海南"},
    {name:"李四",age:"25",city:"香港"},
    {name:"王五",age:"25",city:"济南"},
    {name:"刘六",age:"22",city:"济南"},
    {name:"李七",age:"35",city:"烟台"},
    {name:"张八",age:"32",city:"聊城"},
    {name:"吕九",age:"30",city:"盘锦"}
   ];
   //定义一个空对象，用于保存和修改数据时临时存储
   $scope.prod = {};
   //定义一个单击删除按钮时触发的事件，用于删除选中行
   $scope.del = function ($index) {
    if($index>=0){
     if(confirm("是否删除"+$scope.texts[$index].name) ){
      $scope.texts.splice($index,1);
     }
    }
   };
   
   //定义一个全局变量idx,用于存储选中行的索引，方便执行保存操作。idx取值为0、1、、、、都有用，所以暂取值为-1;
   var idx = -1;
   //定义一个点击添加按钮时触发的事件，用于新增数据
   $scope.add = function(){
    //显示bootstrap中的模块窗口
    $('#modal-1').modal('show');
    
   };
   //定义一个点击保存按钮时触发的事件
   $scope.save = function(){
    //将添加的值赋给数组
    $scope.texts.name = $scope.newName;
    $scope.texts.age = $scope.newAge;
    $scope.texts.city = $scope.newCity;
    $scope.texts.push({name:$scope.newName,age:$scope.newAge,city:$scope.newCity});
    //关闭模块窗口
    $('#modal-1').modal('hide');
   };
   
   
   //定义一个点击修改按钮时出发的事件，用于修改数据
   $scope.update = function($index){
    //显示bootstrap中的模块窗口
    $('#modal-2').modal('show');

    //将选中行的数据绑定到临时对象prod中，在下面的模态窗口中展示出来
    $scope.prod.name = $scope.texts[$index].name;
    $scope.prod.age = $scope.texts[$index].age;
    $scope.prod.city = $scope.texts[$index].city;
    //选中行的索引赋值给全局变量idx
    idx = $index;
   };

   //定义一个点击确定按钮时触发的事件,
   $scope.ensure = function () {
    //将修改后的值赋给数组
    $scope.texts[idx].name = $scope.prod.name;
    $scope.texts[idx].age = $scope.prod.age;
    $scope.texts[idx].city = $scope.prod.city;
    //关闭模块窗口
    $('#modal-2').modal('hide');
   };
   
   
   
  });
 </script>
 </body>
</html>
```
>https://blog.csdn.net/mayn666/article/details/78754996

```
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title></title><script type="text/javascript" src="js/angular.min.js" ></script><script type="text/javascript" src="js/jquery-3.2.1.min.js" ></script></head><body ng-app="myApp" ng-controller="myCtrl">水果：<input type="text" ng-model="name" /><input type="button" value="添加" ng-click="add()" /><input type="button" value="批量删除" ng-click="plsc()" /><table border="1px" cellspacing="0px" cellpadding="0px" width="200px" height="30px"><tr><td><input type="checkbox" ng-click="qx()"/></td><td>水果名称</td><td>操作</td></tr><tr ng-repeat="g in goods"><td><input type="checkbox" name="ck" ng-click="ck($index)"</td><td>{{g.name}}</td><td><input type="button" ng-click="dele($index)" value="删除"/></td></tr></table><script type="text/javascript">
var mb = angular.module("myApp",[]);//创建模板mb.controller("myCtrl",function($scope){//创建控制//定义数组$scope.goods=[];//添加的方法$scope.add = function(){//创建对象var go = {"flag":false,"name":$scope.name}//放进数组$scope.goods.push(go);}//删除一行$scope.dele =function($index){$scope.goods.splice($index,1);}//改变每行chekbox的状态$scope.ck = function($index){$scope.goods[$index].flag=!$scope.goods[$index].flag}//批量删除$scope.plsc = function(){//反着遍历for (var i = $scope.goods.length-1;i>=0;i--) {if ($scope.goods[i].flag) {$scope.goods.splice(i,1);}}}//全选var qq = true;$scope.qx = function(){//获取属性var ck = $("input[name=ck]")for (var i=0;i<ck.length;i++) {ck[i].checked=qq;//给每个数组中的ck赋值$scope.goods[i].flag=qq;}qq=!qq;}});</script></body>
</html>

---------------------
```

#Angular 的表单属性 $valid, $invalid, $pristine, $dirty
属性类 
$valid     ng-valid    Boolean 告诉我们这一项当前基于你设定的规则是否验证通过

$invalid   ng-invalid  Boolean 告诉我们这一项当前基于你设定的规则是否验证未通过

$pristine  ng-pristine     Boolean 如果表单或者输入框没有使用则为True

$dirty     ng-dirty    Boolean 如果表单或者输入框有使用到则为True

# scope中的Dirty Checking(脏数据检查)
scope功能概述

scope是AngularJS中的核心概念之一。它的设计思想和实现方式也是希望深入了解和学习AngularJS的开发人员必须熟知的。

它的功能主要有以下几点： 
1. 通过数据共享连接Controller和View 
2. 事件的监听和响应 
3. 脏数据检查和数据绑定

前两点并没有什么新奇的地方，关键的地方在于第三点。这是AngularJS这一框架和好多其它框架的不同之处，也是AngularJS的一大卖点。

本文也着重描述这一部分。

所谓脏数据检查(Dirty Checking)，实际上是一种概念而不是指具体的某种技术。根据应用的场景，不同的技术都会选择实现这一概念，比如Java中的ORM框架Hibernate也实现了脏数据检查。

在AngularJS中，脏数据检查在scope这一对象中通过$digest方法实现。但是一般认为$digest方法过于底层，更加推荐使用$apply方法。该方法算是对$digest方法的一层包装吧。

第一次接触这两个方法的同学可以参考之前我翻译过的一篇文章来建立基本概念： 
理解Angular中的$apply()以及$digest()

然而，如果仔细阅读AngularJS的源码就能够发现还有很多值得注意和学习的地方，且听我一一道来。

首先，我们需要明白这个$digest方法到底是用来干什么的，为什么需要有这个方法。

使用jQuery这种库开发过Web应用的同学们都知道，每次在获取了所需要的数据之后，需要写不少代码去把DOM折腾一番，让前端的View能够反映出最新的数据。这个过程不仅繁琐易出错而且写多了也很无聊。

在桌面应用开发中，这种数据同步的问题也没少出现，于是就顺理成章的出现了数据绑定。比如.NET框架的WinForm以及最新的WPF，都支持这一技术。

所谓数据绑定，实际上就是为前端部分的占位符和后台的对应数据建立一种关联关系。这个关联关系可以是双向的，也可以是单向的。

单向的数据绑定就是后台数据的动态更新能够实时自动地同步到前端。而双向的数据绑定则是在单向数据绑定的基础之上，实现了前端数据的动态更新也能自动实时地同步到后台。

注意以上提到的后台，指的就是容纳该数据变量的地方。应用于AngularJS这个场景，也就对应着scope。

那么这种能够节省前端开发人员大量时间的神奇技术，是如何实现呢？

首先，很明显的一点是需要有一种办法能够在一方数据发生变化的时候，通知另一方：嗨，我这里的数据更新了，请你也更新一下吧。看到这个需求，很多同学肯定能够不假思索地回答：使用事件不就好了！ 
但是！这样以来，不又走到了jQuery那一套bind/on的老路子上了嘛。最后还是需要开发人员一个个的注册事件，写响应事件的回调函数。什么都没有改变！

所以，AngularJS为了不走寻常路，设计一套截然不同的方案来解决了数据绑定的问题。这种方案不需要开发人员写任何事件相关的注册代码和响应代码。这就导致了Digest Cycle的诞生，也就是$digest方法。

目前看来，$digest方法需要完成两件事情才能实现数据绑定： 
1. 确定此次检查和上一次检查的这段时间内，哪些数据发生了变化 
2. 对于改变了的数据，设法通知到另一端(如果是后台变化了，那么就需要通知到前端)；没改变的数据则不需要处理

可是光看看第一条就觉得挺难的，又不让利用事件系统，如何知道哪些数据发生了变化…… 那就让AngularJS帮你完成事件的注册和监听吧！在AngularJS版的Hello World中不是有这样的代码嘛：

```

// JavaScript Code
$scope.hello = 'Hello World';12



<!-- HTML Code -->
{{ hello }}12
```
利用再HTML中写下{{ hello }}这种表达式，AngularJS就能够帮你自动注册并监听hello这个变量的改变。那么它是如何完成注册工作的呢？实际上，AngularJS会首先将你在{{ }}中声明的表达式编译成函数并调用$watch方法。这里出现了一个新的概念$watch方法，这个方法很接近事件的注册和监听：


```
$scope.$watch(
    function(scope) { return scope.someValue; },
    function(newValue, oldValue, scope) { // listener code defined here }
);1234
```
$watch方法的第一个参数是一个函数，它通常被称为watch函数，它的返回值声明需要监听的变量；第二个参数是listener，在变量发生改变的时候会被调用。这样看来它和传统的事件注册和监听并没有什么本质上的差别，差别仅在于AngularJS能够帮你自动注册绝大多数的change事件并监听它们，只要你按照AngularJS要求的语法来写HTML中的表达式代码，即{{ }}。$watch方法为当前scope注册了一个watcher，这个watcher会被保存到一个scope内部维护的数组中。

那么现在既然已经注册了需要监听的变量并赋予了listener函数，什么时候才会触发listener函数呢？是时候让$digest方法登台亮相了，在$digest函数中，会逐个检查$watch方法中注册的watch函数，如果该函数返回的值和上一次检查中返回的值不一样的话，就会触发对应的listener函数。拿{{ }}表达式作为例子，该表达式编译得到的listener的行为就是将后台的最新变量给同步到前端。这么一来，就完成了一个简单的数据绑定。

好，现在明白了$digest方法用来触发watchers中的listener函数。那么什么时候会触发$digest方法呢？如果没有地方触发$digest方法，那么也没有办法完成整个数据绑定的流程。

答案就是$apply方法。这个方法能够触发$digest方法。$digest方法的执行就标志着一轮Digest Cycle的开始。



>https://blog.csdn.net/dm_vincent/article/details/50344395?utm_source=copy 



#Angular动画
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>
div {
  transition: all linear 0.5s;
  background-color: lightblue;
  height: 100px;
  width: 100%;
  position: relative;
  top: 0;
  left: 0;
}

.ng-hide {
  height: 0;
  width: 0;
  background-color: transparent;
  top:-200px;
  left: 200px;
}

</style>
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular-animate.min.js"></script>
</head>
<body ng-app="ngAnimate">

<h1>隐藏 DIV: <input type="checkbox" ng-model="myCheck"></h1>

<div ng-hide="myCheck"></div>

</body>
</html>
```
已经分配了app时，可以直接在app初始化时集成
```
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>
div {
  transition: all linear 0.5s;
  background-color: lightblue;
  height: 100px;
  width: 100%;
  position: relative;
  top: 0;
  left: 0;
}

.ng-hide {
  height: 0;
  width: 0;
  background-color: transparent;
  top:-200px;
  left: 200px;
}

</style>
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
<script src="https://cdn.staticfile.org/angular.js/1.4.6/angular-animate.min.js"></script>
</head>
<body ng-app="myApp">

<h1>隐藏 DIV: <input type="checkbox" ng-model="myCheck"></h1>

<div ng-hide="myCheck"></div>

<script>
var app = angular.module('myApp', ['ngAnimate']);
</script>

</body>
</html>
```