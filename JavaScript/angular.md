#1.form status
```
<!DOCTYPE html>
<html>
<head>
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

　　3. filter(匹配子串)

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