#黑色斑马纹表格

>http://debug.itxst.com/js/iffrizfr

```
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <script src="http://www.itxst.com/package/bootstrap-table-1.14.1/jquery-3.3.1/jquery.js"></script>
    <link href="http://www.itxst.com/package/bootstrap-table-1.14.1/bootstrap-4.3.1/css/bootstrap.css" rel="stylesheet" />
    <link href="http://www.itxst.com/package/bootstrap-table-1.14.1/bootstrap-table-1.14.1/bootstrap-table.css" rel="stylesheet" />
    <script src="http://www.itxst.com/package/bootstrap-table-1.14.1/bootstrap-table-1.14.1/bootstrap-table.js"></script>
    <title>Bootstrap Table入门例子</title>
    <style>
        .table-demo {
            width: 80%;
            margin: 30px auto 0px auto;
        }
        .fixed-table-header {
            border-right: solid 1px #ddd;
            border-top: solid 1px #ddd;
        }
            .fixed-table-header table  {
                border-top: solid 0px #ddd !important;
                margin-top:-1px;
            }
    </style>
</head>
<body >
    <div class="table-demo">
        <table id="table"></table>
    </div>
    <script>
        //设置需要显示的列
        var columns = [{
            field: 'Id',
            title: '编号'
        }, {
            field: 'ProductName',
            title: '产品名称'
        }, {
            field: 'StockNum',
            title: 'Item 库存'
        }];
 
        //bootstrap table初始化数据
        $('#table').bootstrapTable({
            columns: columns,
            data: getData(),
            classes: "table table-bordered table-striped table-sm table-dark", //这里设置表格样式
            height:400  
        });

        function getData()
        {
            var data = [];

            for (var i = 0; i < 50; i++)
            {
                var item = {
                    Id: i,
                    ProductName: '苹果',
                    StockNum: '200'
                };

                data.push(item);
            };

            return data;

        }
```




#extend

设置bootstrap-table表格样式可以通过设置classess属性进行设置，官方默认支持黑色主题、隔行变色等样式，你也可以自定义样式把你的样式类名传进去。

## table-bordered设置表格边框

```
    $('#table').bootstrapTable({
            columns: columns,
            data: getData(),
            classes: "table table-bordered", //这里设置表格样式
            height:400  
        });
```

## table-striped设置隔行变色

```
    $('#table').bootstrapTable({
            columns: columns,
            data: getData(),
            classes: "table table-bordered table-striped", //table-striped表示隔行变色
            height:400  
        });
```

## table-sm设置表格更精致

```
    $('#table').bootstrapTable({
            columns: columns,
            data: getData(),
            classes: "table table-bordered table-striped table-sm", // 设置table-sm样式
            height:400  
        });
```

## table-dark设置表格为黑色主题

```
    $('#table').bootstrapTable({
            columns: columns,
            data: getData(),
            classes: "table table-bordered table-striped table-sm table-dark",  
            height:400  
        });
```