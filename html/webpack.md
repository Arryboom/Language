webpack是一个js打包工具，不一个完整的前端构建工具。它的流行得益于模块化和单页应用的流行。webpack提供扩展机制，在庞大的社区支持下各种场景基本它都可找到解决方案。本文的目的是教会你用webpack解决实战中常见的问题。

## webpack原理

在深入实战前先要知道webpack的运行原理

### webpack核心概念

*   `entry` 一个可执行模块或库的入口文件。
*   `chunk` 多个文件组成的一个代码块，例如把一个可执行模块和它所有依赖的模块组合和一个 `chunk` 这体现了webpack的打包机制。
*   `loader` 文件转换器，例如把es6转换为es5，scss转换为css。
*   `plugin` 插件，用于扩展webpack的功能，在webpack构建生命周期的节点上加入扩展hook为webpack加入功能。

### webpack构建流程

从启动webpack构建到输出结果经历了一系列过程，它们是：

1.  解析webpack配置参数，合并从shell传入和`webpack.config.js`文件里配置的参数，生产最后的配置结果。
2.  注册所有配置的插件，好让插件监听webpack构建生命周期的事件节点，以做出对应的反应。
3.  从配置的`entry`入口文件开始解析文件构建AST语法树，找出每个文件所依赖的文件，递归下去。
4.  在解析文件递归的过程中根据文件类型和loader配置找出合适的loader用来对文件进行转换。
5.  递归完后得到每个文件的最终结果，根据`entry`配置生成代码块`chunk`。
6.  输出所有`chunk`到文件系统。

需要注意的是，在构建生命周期中有一系列插件在合适的时机做了合适的事情，比如`UglifyJsPlugin`会在loader转换递归完后对结果再使用`UglifyJs`压缩覆盖之前的结果。

## 场景和方案

通过各种场景和对应的解决方案让你深入掌握webpack

### 单页应用

**demo [redemo](https://github.com/gwuhaolin/redemo)**  
 一个单页应用需要配置一个`entry`指明执行入口，webpack会为`entry`生成一个包含这个入口所有依赖文件的`chunk`，但要让它在浏览器里跑起来还需要一个HTML文件来加载`chunk`生成的js文件，如果提取出了css还需要让HTML文件引入提取出的css。[web-webpack-plugin](https://github.com/gwuhaolin/web-webpack-plugin)里的`WebPlugin`可以自动的完成这些工作。

webpack配置文件

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">const</span> <span class="pl-kos">{</span> WebPlugin <span class="pl-kos">}</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'web-webpack-plugin'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">entry</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">app</span>: <span class="pl-s">'./src/doc/index.js'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
  <span class="pl-c1">plugins</span>: <span class="pl-kos">[</span>
    <span class="pl-c">// 一个WebPlugin对应生成一个html文件</span>
    <span class="pl-k">new</span> <span class="pl-v">WebPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
      <span class="pl-c">//输出的html文件名称</span>
      <span class="pl-c1">filename</span>: <span class="pl-s">'index.html'</span><span class="pl-kos">,</span>
      <span class="pl-c">//这个html依赖的`entry`</span>
      <span class="pl-c1">requires</span>: <span class="pl-kos">[</span><span class="pl-s">'app'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
  <span class="pl-kos">]</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="const { WebPlugin } = require('web-webpack-plugin');
module.exports = {
  entry: {
    app: './src/doc/index.js',
  },
  plugins: [
    // 一个WebPlugin对应生成一个html文件
    new WebPlugin({
      //输出的html文件名称
      filename: 'index.html',
      //这个html依赖的`entry`
      requires: ['app'],
    }),
  ],
};" tabindex="0" role="button"></clipboard-copy></div>

</div>

`requires: ['doc']`指明这个HTML依赖哪些`entry`，`entry`生成的js和css会自动注入到HTML里。  
 你还可以配置这些资源的注入方式，支持如下属性：

*   `_dist` 只有在生产环境下才引入该资源
*   `_dev` 只有在开发环境下才引入该资源
*   `_inline` 把该资源的内容潜入到html里
*   `_ie` 只有IE浏览器才需要引入的资源

要设置这些属性可以通过在js里配置

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">new</span> <span class="pl-v">WebPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
    <span class="pl-c1">filename</span>: <span class="pl-s">'index.html'</span><span class="pl-kos">,</span>
    <span class="pl-c1">requires</span>: <span class="pl-kos">{</span>
         <span class="pl-c1">app</span>:<span class="pl-kos">{</span>
              <span class="pl-c1">_dist</span>:<span class="pl-c1">true</span><span class="pl-kos">,</span>
              <span class="pl-c1">_inline</span>:<span class="pl-c1">false</span><span class="pl-kos">,</span>
         <span class="pl-kos">}</span>
    <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="new WebPlugin({
    filename: 'index.html',
    requires: {
         app:{
              _dist:true,
              _inline:false,
         }
    },
})," tabindex="0" role="button"></clipboard-copy></div>

</div>

或者在模版里设置，使用模版的好处是灵活的控制资源注入点。

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">new</span> <span class="pl-v">WebPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
      <span class="pl-c1">filename</span>: <span class="pl-s">'index.html'</span><span class="pl-kos">,</span>
      <span class="pl-c1">template</span>: <span class="pl-s">'./template.html'</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="new WebPlugin({
      filename: 'index.html',
      template: './template.html',
})," tabindex="0" role="button"></clipboard-copy></div>

</div>

<div class="highlight highlight-text-html-basic position-relative overflow-auto">

<pre><span class="pl-c1"><!DOCTYPE html<span class="pl-kos">></span></span>
<span class="pl-kos"><</span><span class="pl-ent">html</span> <span class="pl-c1">lang</span>="<span class="pl-s">zh-cn</span>"<span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">head</span><span class="pl-kos">></span>
    <span class="pl-kos"><</span><span class="pl-ent">link</span> <span class="pl-c1">rel</span>="<span class="pl-s">stylesheet</span>" <span class="pl-c1">href</span>="<span class="pl-s">app?_inline</span>"<span class="pl-kos">></span>
    <span class="pl-kos"><</span><span class="pl-ent">script</span> <span class="pl-c1">src</span>="<span class="pl-s">ie-polyfill?_ie</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">script</span><span class="pl-kos">></span>
<span class="pl-kos"></</span><span class="pl-ent">head</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">body</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">div</span> <span class="pl-c1">id</span>="<span class="pl-s">react-body</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">div</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">script</span> <span class="pl-c1">src</span>="<span class="pl-s">app</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">script</span><span class="pl-kos">></span>
<span class="pl-kos"></</span><span class="pl-ent">body</span><span class="pl-kos">></span>
<span class="pl-kos"></</span><span class="pl-ent">html</span><span class="pl-kos">></span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="<!DOCTYPE html>
<html lang=&quot;zh-cn&quot;>
<head>
    <link rel=&quot;stylesheet&quot; href=&quot;app?_inline&quot;>
    <script src=&quot;ie-polyfill?_ie&quot;></script>
</head>
<body>
<div id=&quot;react-body&quot;></div>
<script src=&quot;app&quot;></script>
</body>
</html>" tabindex="0" role="button"></clipboard-copy></div>

</div>

`WebPlugin`插件借鉴了`fis3`的思想，补足了webpack缺失的以HTML为入口的功能。想了解`WebPlugin`的更多功能，见[文档](https://github.com/gwuhaolin/web-webpack-plugin/blob/master/readme_zh.md#%E8%BE%93%E5%87%BAhtml%E6%96%87%E4%BB%B6-demo)。

### 一个项目里管理多个单页应用

一般项目里会包含多个单页应用，虽然多个单页应用也可以合并成一个但是这样做会导致用户没访问的部分也加载了。如果项目里有很多个单页应用，为每个单页应用配置一个`entry`和`WebPlugin` ？如果项目又新增了一个单页应用，又去新增webpack配置？这样做太麻烦了，[web-webpack-plugin](https://github.com/gwuhaolin/web-webpack-plugin)里的`AutoWebPlugin`可以方便的解决这些问题。

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
    <span class="pl-c1">plugins</span>: <span class="pl-kos">[</span>
        <span class="pl-c">// 所有页面的入口目录</span>
        <span class="pl-k">new</span> <span class="pl-v">AutoWebPlugin</span><span class="pl-kos">(</span><span class="pl-s">'./src/'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-kos">]</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
    plugins: [
        // 所有页面的入口目录
        new AutoWebPlugin('./src/'),
    ]
};" tabindex="0" role="button"></clipboard-copy></div>

</div>

`AutoWebPlugin`会把`./src/`目录下所有每个文件夹作为一个单页页面的入口，自动为所有的页面入口配置一个WebPlugin输出对应的html。要新增一个页面就在`./src/`下新建一个文件夹包含这个单页应用所依赖的代码，`AutoWebPlugin`自动生成一个名叫文件夹名称的html文件。`AutoWebPlugin`的更多功能见[文档](https://github.com/gwuhaolin/web-webpack-plugin/blob/master/readme_zh.md#%E8%87%AA%E5%8A%A8%E6%8E%A2%E6%B5%8Bhtml%E5%85%A5%E5%8F%A3-demo)。

### 代码分割优化

一个好的代码分割对浏览器首屏效果提升很大。比如对于最常见的react体系你可以

1.  先抽出基础库`react` `react-dom` `redux` `react-redux`到一个单独的文件而不是和其它文件放在一起打包为一个文件，这样做的好处是只要你不升级他们的版本这个文件永远不会被刷新。如果你把这些基础库和业务代码打包在一个文件里每次改动业务代码都会导致文件hash值变化从而导致缓存失效浏览器重复下载这些包含基础库的代码。以上的配置为：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-c">// vender.js 文件抽离基础库到单独的一个文件里防止跟随业务代码被刷新</span>
<span class="pl-c">// 所有页面都依赖的第三方库</span>
<span class="pl-c">// react基础</span>
<span class="pl-k">import</span> <span class="pl-s">'react'</span><span class="pl-kos">;</span>
<span class="pl-k">import</span> <span class="pl-s">'react-dom'</span><span class="pl-kos">;</span>
<span class="pl-k">import</span> <span class="pl-s">'react-redux'</span><span class="pl-kos">;</span>
<span class="pl-c">// redux基础</span>
<span class="pl-k">import</span> <span class="pl-s">'redux'</span><span class="pl-kos">;</span>
<span class="pl-k">import</span> <span class="pl-s">'redux-thunk'</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="// vender.js 文件抽离基础库到单独的一个文件里防止跟随业务代码被刷新
// 所有页面都依赖的第三方库
// react基础
import 'react';
import 'react-dom';
import 'react-redux';
// redux基础
import 'redux';
import 'redux-thunk';" tabindex="0" role="button"></clipboard-copy></div>

</div>

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-c">// webpack配置</span>
<span class="pl-kos">{</span>
  <span class="pl-c1">entry</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">vendor</span>: <span class="pl-s">'./path/to/vendor.js'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="// webpack配置
{
  entry: {
    vendor: './path/to/vendor.js',
  },
}" tabindex="0" role="button"></clipboard-copy></div>

</div>

1.  再通过[CommonsChunkPlugin](https://webpack.github.io/docs/list-of-plugins.html#commonschunkplugin)可以提取出多个代码块都依赖的代码形成一个单独的`chunk`。在应用有多个页面的场景下提取出所有页面公共的代码减少单个页面的代码，在不同页面之间切换时所有页面公共的代码之前被加载过而不必重新加载。

### 构建npm包

**demo [remd](https://github.com/gwuhaolin/remd)**  
 除了构建可运行的web应用，webpack也可用来构建发布到npm上去的给别人调用的js库。

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">const</span> <span class="pl-s1">nodeExternals</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'webpack-node-externals'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">entry</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">index</span>: <span class="pl-s">'./src/index.js'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
  <span class="pl-c1">externals</span>: <span class="pl-kos">[</span><span class="pl-s1">nodeExternals</span><span class="pl-kos">(</span><span class="pl-kos">)</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
  <span class="pl-c1">target</span>: <span class="pl-s">'node'</span><span class="pl-kos">,</span>
  <span class="pl-c1">output</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">path</span>: <span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'.npm'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-c1">filename</span>: <span class="pl-s">'[name].js'</span><span class="pl-kos">,</span>
    <span class="pl-c1">libraryTarget</span>: <span class="pl-s">'commonjs2'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="const nodeExternals = require('webpack-node-externals');
module.exports = {
  entry: {
    index: './src/index.js',
  },
  externals: [nodeExternals()],
  target: 'node',
  output: {
    path: path.resolve(__dirname, '.npm'),
    filename: '[name].js',
    libraryTarget: 'commonjs2',
  },
};" tabindex="0" role="button"></clipboard-copy></div>

</div>

这里有几个区别于web应用不同的地方：

*   `externals: [nodeExternals()]`用于排除`node_modules`目录下的代码被打包进去，因为放在`node_modules`目录下的代码应该通过npm安装。
*   `libraryTarget: 'commonjs2'`指出`entry`是一个可供别人调用的库而不是可执行的，输出的js文件按照commonjs规范。

### 构建服务端渲染

服务端渲染的代码要运行在nodejs环境，和浏览器不同的是，服务端渲染代码需要采用commonjs规范同时不应该包含除js之外的文件比如css。webpack配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">target</span>: <span class="pl-s">'node'</span><span class="pl-kos">,</span>
  <span class="pl-c1">entry</span>: <span class="pl-kos">{</span>
    <span class="pl-s">'server_render'</span>: <span class="pl-s">'./src/server_render'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
  <span class="pl-c1">output</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">filename</span>: <span class="pl-s">'./dist/server/[name].js'</span><span class="pl-kos">,</span>
    <span class="pl-c1">libraryTarget</span>: <span class="pl-s">'commonjs2'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
  <span class="pl-c1">module</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">rules</span>: <span class="pl-kos">[</span>
      <span class="pl-kos">{</span>
        <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span>
        <span class="pl-c1">loader</span>: <span class="pl-s">'babel-loader'</span><span class="pl-kos">,</span>
      <span class="pl-kos">}</span><span class="pl-kos">,</span>
      <span class="pl-kos">{</span>
        <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span><span class="pl-kos">(</span>scss<span class="pl-c1">|</span>css<span class="pl-c1">|</span>pdf<span class="pl-kos">)</span><span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span>
        <span class="pl-c1">loader</span>: <span class="pl-s">'ignore-loader'</span><span class="pl-kos">,</span>
      <span class="pl-kos">}</span><span class="pl-kos">,</span>
    <span class="pl-kos">]</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  target: 'node',
  entry: {
    'server_render': './src/server_render',
  },
  output: {
    filename: './dist/server/[name].js',
    libraryTarget: 'commonjs2',
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'babel-loader',
      },
      {
        test: /\.(scss|css|pdf)$/,
        loader: 'ignore-loader',
      },
    ]
  },
};" tabindex="0" role="button"></clipboard-copy></div>

</div>

其中几个关键的地方在于：

*   `target: 'node'` 指明构建出的代码是要运行在node环境里
*   `libraryTarget: 'commonjs2'` 指明输出的代码要是commonjs规范
*   `{test: /\.(scss|css|pdf)$/,loader: 'ignore-loader'}` 是为了防止不能在node里执行服务端渲染也用不上的文件被打包进去。

### 从fis3迁移到webpack

fis3和webpack有相似的地方也有不同的地方。相似在于他们都采用commonjs规范，不同在于导入css这些非js资源的方式。fis3通过`// @require './index.scss'`而webpack通过`require('./index.scss')`。如果想从fis3平滑迁移到webpack可以使用[comment-require-loader](https://github.com/gwuhaolin/comment-require-loader/issues)。比如你想在webpack构建是使用采用了fis3方式的`imui`模块，配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre>loaders:<span class="pl-kos">[</span><span class="pl-kos">{</span>
     <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span>
     <span class="pl-c1">loaders</span>: <span class="pl-kos">[</span><span class="pl-s">'comment-require-loader'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
     <span class="pl-c1">include</span>: <span class="pl-kos">[</span><span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'node_modules/imui'</span><span class="pl-kos">)</span><span class="pl-kos">,</span><span class="pl-kos">]</span>
<span class="pl-kos">}</span><span class="pl-kos">]</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="loaders:[{
     test: /\.js$/,
     loaders: ['comment-require-loader'],
     include: [path.resolve(__dirname, 'node_modules/imui'),]
}]" tabindex="0" role="button"></clipboard-copy></div>

</div>

## 自定义webpack扩展

如果你在社区找不到你的应用场景的解决方案，那就需要自己动手了写loader或者plugin了。  
 在你编写自定义webpack扩展前你需要想明白到底是要做一个`loader`还是`plugin`呢？可以这样判断：

> 如果你的扩展是想对一个个单独的文件进行转换那么就编写`loader`剩下的都是`plugin`。

其中对文件进行转换可以是像：

*   `babel-loader`把es6转换成`es5`
*   `file-loader`把文件替换成对应的URL
*   `raw-loader`注入文本文件内容到代码里去

### 编写 webpack loader

**demo [comment-require-loader](https://github.com/gwuhaolin/comment-require-loader)**  
 编写`loader`非常简单，以comment-require-loader为例：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-en">exports</span> <span class="pl-c1">=</span> <span class="pl-k">function</span> <span class="pl-kos">(</span><span class="pl-s1">content</span><span class="pl-kos">)</span> <span class="pl-kos">{</span>
    <span class="pl-k">return</span> <span class="pl-en">replace</span><span class="pl-kos">(</span><span class="pl-s1">content</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = function (content) {
    return replace(content);
};" tabindex="0" role="button"></clipboard-copy></div>

</div>

`loader`的入口需要导出一个函数，这个函数要干的事情就是转换一个文件的内容。  
 函数接收的参数`content`是一个文件在转换前的字符串形式内容，需要返回一个新的字符串形式内容作为转换后的结果，所有通过模块化倒入的文件都会经过`loader`。从这里可以看出`loader`只能处理一个个单独的文件而不能处理代码块。想编写更复杂的loader可参考[官方文档](https://webpack.github.io/docs/loaders.html)

### 编写 webpack plugin

**demo [end-webpack-plugin](https://github.com/gwuhaolin/end-webpack-plugin)**  
 `plugin`应用场景广泛，所以稍微复杂点。以end-webpack-plugin为例：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">class</span> <span class="pl-v">EndWebpackPlugin</span> <span class="pl-kos">{</span>

    <span class="pl-en">constructor</span><span class="pl-kos">(</span><span class="pl-s1">doneCallback</span><span class="pl-kos">,</span> <span class="pl-s1">failCallback</span><span class="pl-kos">)</span> <span class="pl-kos">{</span>
        <span class="pl-smi">this</span><span class="pl-kos">.</span><span class="pl-c1">doneCallback</span> <span class="pl-c1">=</span> <span class="pl-s1">doneCallback</span><span class="pl-kos">;</span>
        <span class="pl-smi">this</span><span class="pl-kos">.</span><span class="pl-c1">failCallback</span> <span class="pl-c1">=</span> <span class="pl-s1">failCallback</span><span class="pl-kos">;</span>
    <span class="pl-kos">}</span>

    <span class="pl-en">apply</span><span class="pl-kos">(</span><span class="pl-s1">compiler</span><span class="pl-kos">)</span> <span class="pl-kos">{</span>
        <span class="pl-c">// 监听webpack生命周期里的事件，做相应的处理</span>
        <span class="pl-s1">compiler</span><span class="pl-kos">.</span><span class="pl-en">plugin</span><span class="pl-kos">(</span><span class="pl-s">'done'</span><span class="pl-kos">,</span> <span class="pl-kos">(</span><span class="pl-s1">stats</span><span class="pl-kos">)</span> <span class="pl-c1">=></span> <span class="pl-kos">{</span>
            <span class="pl-smi">this</span><span class="pl-kos">.</span><span class="pl-en">doneCallback</span><span class="pl-kos">(</span><span class="pl-s1">stats</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
        <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
        <span class="pl-s1">compiler</span><span class="pl-kos">.</span><span class="pl-en">plugin</span><span class="pl-kos">(</span><span class="pl-s">'failed'</span><span class="pl-kos">,</span> <span class="pl-kos">(</span><span class="pl-s1">err</span><span class="pl-kos">)</span> <span class="pl-c1">=></span> <span class="pl-kos">{</span>
            <span class="pl-smi">this</span><span class="pl-kos">.</span><span class="pl-en">failCallback</span><span class="pl-kos">(</span><span class="pl-s1">err</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
        <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
    <span class="pl-kos">}</span>
<span class="pl-kos">}</span>

<span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-v">EndWebpackPlugin</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="class EndWebpackPlugin {

    constructor(doneCallback, failCallback) {
        this.doneCallback = doneCallback;
        this.failCallback = failCallback;
    }

    apply(compiler) {
        // 监听webpack生命周期里的事件，做相应的处理
        compiler.plugin('done', (stats) => {
            this.doneCallback(stats);
        });
        compiler.plugin('failed', (err) => {
            this.failCallback(err);
        });
    }
}

module.exports = EndWebpackPlugin;" tabindex="0" role="button"></clipboard-copy></div>

</div>

`loader`的入口需要导出一个class, 在`new EndWebpackPlugin()`的时候通过构造函数传入这个插件需要的参数，在webpack启动的时候会先实例化`plugin`再调用`plugin`的`apply`方法，插件需要在`apply`函数里监听webpack生命周期里的事件，做相应的处理。  
 webpack plugin 里有2个核心概念：

*   `Compiler`: 从webpack启动到推出只存在一个`Compiler`，`Compiler`存放着webpack配置
*   `Compilation`: 由于webpack的监听文件变化自动编译机制，`Compilation`代表一次编译。

`Compiler` 和 `Compilation` 都会广播一系列事件。  
 webpack生命周期里有非常多的事件可以在[event-hooks](https://webpack.js.org/api/plugins/compiler/#event-hooks)和[Compilation](https://webpack.js.org/api/plugins/compilation/)里查到。以上只是一个最简单的demo，更复杂的可以查看 [how to write a plugin](https://github.com/webpack/docs/wiki/how-to-write-a-plugin)或参考[web-webpack-plugin](https://github.com/gwuhaolin/web-webpack-plugin)。

## 总结

webpack其实很简单，可以用一句话涵盖它的本质：

> webpack是一个打包模块化js的工具，可以通过loader转换文件，通过plugin扩展功能。

如果webpack让你感到复杂，一定是各种loader和plugin的原因。  
 希望本文能让你明白webpack的原理与本质让你可以在实战中灵活应用webpack。


>https://github.com/gwuhaolin/blog/issues/4






# webpack 终极优化


webpack是当下最流行的js打包工具，这得益于网页应用日益复杂和js模块化的流行。[webpack2](https://webpack.js.org)增加了一些新特性也正式发布了一段时间，是时候告诉大家如何用webpack2优化你的构建让它构建出更小的文件尺寸和更好的开发体验。

# 优化输出

打包结果更小可以让网页打开速度更快以及简约宽带。可以通过这以下几点做到

#### 压缩css

`css-loader` 在webpack2里默认是没有开启压缩的，最后生成的css文件里有很多空格和tab，通过配置  
 `css-loader?minimize`参数可以开启压缩输出最小的css。css的压缩实际是是通过[cssnano](http://cssnano.co)实现的。

#### tree-shaking

tree-shaking 是指借助es6 `import export` 语法静态性的特点来删掉export但是没有import过的东西。要让tree-shaking工作需要注意以下几点：

*   配置babel让它在编译转化es6代码时不把`import export`转换为cmd的`module.export`，配置如下：

<div class="highlight highlight-source-json position-relative overflow-auto">

<pre><span class="pl-ent">"presets"</span>: [
    [
      <span class="pl-s"><span class="pl-pds">"</span>es2015<span class="pl-pds">"</span></span>,
      {
        <span class="pl-ent">"modules"</span>: <span class="pl-c1">false</span>
      }
    ]
]</pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="&quot;presets&quot;: [
    [
      &quot;es2015&quot;,
      {
        &quot;modules&quot;: false
      }
    ]
]
" tabindex="0" role="button"></clipboard-copy></div>

</div>

*   大多数分布到npm的库里的代码都是es5的，但是也有部分库（redux,react-router等等）开始支持tree-shaking。这些库发布到npm里的代码即包含es5的又包含全采用了es6 `import export` 语法的代码。  
     拿redux库来说，npm下载到的目录结构如下：

<div class="snippet-clipboard-content position-relative overflow-auto">

    ├── es
    │   └── utils
    ├── lib
    │   └── utils

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="├── es
│&nbsp;&nbsp; └── utils
├── lib
│&nbsp;&nbsp; └── utils
" tabindex="0" role="button"></clipboard-copy></div>

</div>

其中lib目录里是编译出的es5代码，es目录里是编译出的采用`import export` 语法的es5代码，在redux的`package.json`文件里有这两个配置：

<div class="snippet-clipboard-content position-relative overflow-auto">

    "main": "lib/index.js",
    "jsnext:main": "es/index.js",

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="&quot;main&quot;: &quot;lib/index.js&quot;,
&quot;jsnext:main&quot;: &quot;es/index.js&quot;,
" tabindex="0" role="button"></clipboard-copy></div>

</div>

这是指这个库的入口文件的位置，所以要让webpack去读取es目录下的代码需要使用jsnext:main字段配置的入口，要做到这点webpack需要这样配置：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
	<span class="pl-c1">resolve</span>: <span class="pl-kos">{</span>
            <span class="pl-c1">mainFields</span>: <span class="pl-kos">[</span><span class="pl-s">'jsnext:main'</span><span class="pl-kos">,</span><span class="pl-s">'main'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
        <span class="pl-kos">}</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
	resolve: {
            mainFields: ['jsnext:main','main'],
        }
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

这会让webpack先使用jsnext:main字段，在没有时使用main字段。这样就可以优化支持tree-shaking的库。

#### 优化 UglifyJsPlugin

webpack `--optimize-minimize` 选项会开启 UglifyJsPlugin来压缩输出的js，但是默认的UglifyJsPlugin配置并没有把代码压缩到最小输出的js里还是有注释和空格，需要覆盖默认的配置：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">new</span> <span class="pl-v">UglifyJsPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
    <span class="pl-c">// 最紧凑的输出</span>
    <span class="pl-c1">beautify</span>: <span class="pl-c1">false</span><span class="pl-kos">,</span>
    <span class="pl-c">// 删除所有的注释</span>
    <span class="pl-c1">comments</span>: <span class="pl-c1">false</span><span class="pl-kos">,</span>
    <span class="pl-c1">compress</span>: <span class="pl-kos">{</span>
      <span class="pl-c">// 在UglifyJs删除没有用到的代码时不输出警告</span> 
      <span class="pl-c1">warnings</span>: <span class="pl-c1">false</span><span class="pl-kos">,</span>
      <span class="pl-c">// 删除所有的 `console` 语句</span>
      <span class="pl-c">// 还可以兼容ie浏览器</span>
      <span class="pl-c1">drop_console</span>: <span class="pl-c1">true</span><span class="pl-kos">,</span>
      <span class="pl-c">// 内嵌定义了但是只用到一次的变量</span>
      <span class="pl-c1">collapse_vars</span>: <span class="pl-c1">true</span><span class="pl-kos">,</span>
      <span class="pl-c">// 提取出出现多次但是没有定义成变量去引用的静态值</span>
      <span class="pl-c1">reduce_vars</span>: <span class="pl-c1">true</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span>
<span class="pl-kos">}</span><span class="pl-kos">)</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="new UglifyJsPlugin({
    // 最紧凑的输出
    beautify: false,
    // 删除所有的注释
    comments: false,
    compress: {
      // 在UglifyJs删除没有用到的代码时不输出警告  
      warnings: false,
      // 删除所有的 `console` 语句
      // 还可以兼容ie浏览器
      drop_console: true,
      // 内嵌定义了但是只用到一次的变量
      collapse_vars: true,
      // 提取出出现多次但是没有定义成变量去引用的静态值
      reduce_vars: true,
    }
})
" tabindex="0" role="button"></clipboard-copy></div>

</div>

#### 定义环境变量 NODE_ENV=production

很多库里（比如react）有部分代码是这样的：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">if</span><span class="pl-kos">(</span><span class="pl-s1">process</span><span class="pl-kos">.</span><span class="pl-c1">env</span><span class="pl-kos">.</span><span class="pl-c1">NODE_ENV</span> <span class="pl-c1">!==</span> <span class="pl-s">'production'</span><span class="pl-kos">)</span><span class="pl-kos">{</span>
<span class="pl-c">// 不是生产环境才需要用到的代码，比如控制台里看到的警告</span> 
<span class="pl-kos">}</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="if(process.env.NODE_ENV !== 'production'){
// 不是生产环境才需要用到的代码，比如控制台里看到的警告    
}
" tabindex="0" role="button"></clipboard-copy></div>

</div>

在环境变量 `NODE_ENV` 等于 `production` 的时候UglifyJs会认为if语句里的是死代码在压缩代码时删掉。

#### 使用 CommonsChunkPlugin 抽取公共代码

[CommonsChunkPlugin](https://webpack.github.io/docs/list-of-plugins.html#commonschunkplugin)可以提取出多个代码块都依赖的模块形成一个单独的模块。要发挥CommonsChunkPlugin的作用还需要浏览器缓存机制的配合。在应用有多个页面的场景下提取出所有页面公共的代码减少单个页面的代码，在不同页面之间切换时所有页面公共的代码之前被加载过而不必重新加载。这个方法可以非常有效的提升应用性能。

#### 在生产环境按照文件内容md5打hash

webpack编译在生产环境出来的js、css、图片、字体这些文件应该放到CDN上，再根据文件内容的md5命名文件，利用缓存机制用户只需要加载一次，第二次加载时就直接访问缓存。如果你之后有修改就会为对应的文件生产新的md5值。做到以上你需要这样配置：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-kos">{</span>
  <span class="pl-c1">output</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">publicPath</span>: <span class="pl-c1">CND_URL</span><span class="pl-kos">,</span>
    <span class="pl-c1">filename</span>: <span class="pl-s">'[name]_[chunkhash].js'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="{
  output: {
    publicPath: CND_URL,
    filename: '[name]_[chunkhash].js',
  },
}
" tabindex="0" role="button"></clipboard-copy></div>

</div>

知道以上原理后我们还可以进一步优化：利用CommonsChunkPlugin提取出使用页面都依赖的基础运行环境。比如对于最常见的react体系你可以抽出基础库`react` `react-dom` `redux` `react-redux`到一个单独的文件而不是和其它文件放在一起打包为一个文件，这样做的好处是只要你不升级他们的版本这个文件永远不会被刷新。如果你把这些基础库和业务代码打包在一个文件里每次改动业务代码都会导致浏览器重复下载这些包含基础库的代码。以上的配置为：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-c">// vender.js 文件抽离基础库到单独的一个文件里防止跟随业务代码被刷新</span>
<span class="pl-c">// 所有页面都依赖的第三方库</span>
<span class="pl-c">// react基础</span>
<span class="pl-k">import</span> <span class="pl-s">'react'</span><span class="pl-kos">;</span>
<span class="pl-k">import</span> <span class="pl-s">'react-dom'</span><span class="pl-kos">;</span>
<span class="pl-k">import</span> <span class="pl-s">'react-redux'</span><span class="pl-kos">;</span>
<span class="pl-c">// redux基础</span>
<span class="pl-k">import</span> <span class="pl-s">'redux'</span><span class="pl-kos">;</span>
<span class="pl-k">import</span> <span class="pl-s">'redux-thunk'</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="// vender.js 文件抽离基础库到单独的一个文件里防止跟随业务代码被刷新
// 所有页面都依赖的第三方库
// react基础
import 'react';
import 'react-dom';
import 'react-redux';
// redux基础
import 'redux';
import 'redux-thunk';
" tabindex="0" role="button"></clipboard-copy></div>

</div>

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-c">// webpack配置</span>
<span class="pl-kos">{</span>
  <span class="pl-c1">entry</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">vendor</span>: <span class="pl-s">'./path/to/vendor.js'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="// webpack配置
{
  entry: {
    vendor: './path/to/vendor.js',
  },
}
" tabindex="0" role="button"></clipboard-copy></div>

</div>

#### DedupePlugin 和 OccurrenceOrderPlugin

在webpack1里经常会使用 `DedupePlugin` 插件来消除重复的模块以及使用 `OccurrenceOrderPlugin` 插件让被依赖次数更高的模块靠前分到更小的id 来达到输出更少的代码，在webpack2里这些已经这两个插件已经被移除了因为这些功能已经被内置了。

除了压缩文本代码外还可以：

*   **用[imagemin-webpack-plugin](https://github.com/Klathmon/imagemin-webpack-plugin) 压缩图片**
*   **用[webpack-spritesmith](https://github.com/mixtur/webpack-spritesmith) 合并雪碧图**
*   **对于支持es6的js运行环境使用[babili](https://github.com/babel/babili)**

以上优化点只需要在构建用于生产环境代码的时候才使用，在开发环境时最好关闭因为它们很耗时。

# 优化开发体验

优化开发体验主要从更快的构建和更方便的功能入手。

## 更快的构建

#### 缩小文件搜索范围

webpack的`resolve.modules`配置模块库（通常是指node_modules）所在的位置，在js里出现`import 'redux'`这样不是相对也不是绝对路径的写法时会去node_modules目录下找。但是默认的配置会采用向上递归搜索的方式去寻找node_modules，但通常项目目录里只有一个node_modules在项目根目录，为了减少搜索我们直接写明node_modules的全路径：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
    <span class="pl-c1">resolve</span>: <span class="pl-kos">{</span>
        <span class="pl-c1">modules</span>: <span class="pl-kos">[</span><span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'node_modules'</span><span class="pl-kos">)</span><span class="pl-kos">]</span>
    <span class="pl-kos">}</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
    resolve: {
        modules: [path.resolve(__dirname, 'node_modules')]
    }
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

除此之外webpack配置loader时也可以缩小文件搜索范围。

*   loader的test正则表达式也应该尽可能的简单，比如在你的项目里只有`.js`文件时就不要把test写成`/\.jsx?$/`
*   loader使用include命中只需要处理的文件，比如babel-loader的这两个配置:

只对项目目录下src目录里的代码进行babel编译

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-kos">{</span>
    <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span>
    <span class="pl-c1">loader</span>: <span class="pl-s">'babel-loader'</span><span class="pl-kos">,</span>
    <span class="pl-c1">include</span>: <span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'src'</span><span class="pl-kos">)</span>
<span class="pl-kos">}</span>	</pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="{
    test: /\.js$/,
    loader: 'babel-loader',
    include: path.resolve(__dirname, 'src')
}	
" tabindex="0" role="button"></clipboard-copy></div>

</div>

项目目录下的所有js都会进行babel编译，包括庞大的node_modules下的js

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-kos">{</span>
    <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span>
    <span class="pl-c1">loader</span>: <span class="pl-s">'babel-loader'</span>
<span class="pl-kos">}</span>	</pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="{
    test: /\.js$/,
    loader: 'babel-loader'
}	
" tabindex="0" role="button"></clipboard-copy></div>

</div>

#### 开启 babel-loader 缓存

babel编译过程很耗时，好在babel-loader提供缓存编译结果选项，在重启webpack时不需要创新编译而是复用缓存结果减少编译流程。babel-loader缓存机制默认是关闭的，打开的配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
    <span class="pl-c1">module</span>: <span class="pl-kos">{</span>
         <span class="pl-c1">loaders</span>: <span class="pl-kos">[</span><span class="pl-kos">{</span>
                <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span>
                <span class="pl-c1">loader</span>: <span class="pl-s">'babel-loader?cacheDirectory'</span><span class="pl-kos">,</span>
         <span class="pl-kos">}</span><span class="pl-kos">]</span>
  <span class="pl-kos">}</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
    module: {
         loaders: [{
                test: /\.js$/,
                loader: 'babel-loader?cacheDirectory',
         }]
  }
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

#### 使用 alias

`resolve.alias` 配置路径映射。  
 发布到npm的库大多数都包含两个目录，一个是放着cmd模块化的lib目录，一个是把所有文件合成一个文件的dist目录，多数的入口文件是指向lib里面下的。  
 默认情况下webpack会去读lib目录下的入口文件再去递归加载其它依赖的文件这个过程很耗时，alias配置可以让webpack直接使用dist目录的整体文件减少文件递归解析。配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">resolve</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">alias</span>: <span class="pl-kos">{</span>
      <span class="pl-s">'moment'</span>: <span class="pl-s">'moment/min/moment.min.js'</span><span class="pl-kos">,</span>
      <span class="pl-s">'react'</span>: <span class="pl-s">'react/dist/react.js'</span><span class="pl-kos">,</span>
      <span class="pl-s">'react-dom'</span>: <span class="pl-s">'react-dom/dist/react-dom.js'</span>
    <span class="pl-kos">}</span>
  <span class="pl-kos">}</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  resolve: {
    alias: {
      'moment': 'moment/min/moment.min.js',
      'react': 'react/dist/react.js',
      'react-dom': 'react-dom/dist/react-dom.js'
    }
  }
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

#### 使用 noParse

`module.noParse` 配置哪些文件可以脱离webpack的解析。  
 有些库是自成一体不依赖其他库的没有使用模块化的，比如jquey、momentjs、chart.js，要使用它们必须整体全部引入。  
 webpack是模块化打包工具完全没有必要去解析这些文件的依赖，因为它们都不依赖其它文件体积也很庞大，要忽略它们配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">module</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">noParse</span>: <span class="pl-pds"><span class="pl-c1">/</span>node_modules<span class="pl-cce">\/</span><span class="pl-kos">(</span>jquey<span class="pl-c1">|</span>moment<span class="pl-c1">|</span>chart<span class="pl-cce">\.</span>js<span class="pl-kos">)</span><span class="pl-c1">/</span></span>
  <span class="pl-kos">}</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  module: {
    noParse: /node_modules\/(jquey|moment|chart\.js)/
  }
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

除此以外还有很多可以加速的方法：

*   **使用[happypack](https://github.com/amireh/happypack)多进程并行构建**
*   **使用[DllPlugin](https://github.com/webpack/docs/wiki/list-of-plugins#dllplugin)复用模块**

## 更方便的功能

#### 模块热替换

模块热替换是指在开发的过程中修改代码后不用刷新页面直接把变化的模块替换到老模块让页面呈现出最新的效果。  
 webpack-dev-server内置模块热替换，配置起来也很方便，下面以react应用为例，步骤如下：

*   在启动webpack-dev-server的时候带上`--hot`参数开启模块热替换，在开启`--hot`后针对css的变化是会自动热替换的，但是js涉及到复杂的逻辑还需要进一步配置。
*   配置页面入口文件

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">import</span> <span class="pl-v">App</span> <span class="pl-k">from</span> <span class="pl-s">'./app'</span><span class="pl-kos">;</span>

<span class="pl-k">function</span> <span class="pl-en">run</span><span class="pl-kos">(</span><span class="pl-kos">)</span><span class="pl-kos">{</span>
	<span class="pl-en">render</span><span class="pl-kos">(</span><span class="pl-c1"><</span><span class="pl-ent">App</span><span class="pl-c1">/</span><span class="pl-c1">></span><span class="pl-kos">,</span><span class="pl-smi">document</span><span class="pl-kos">.</span><span class="pl-en">getElementById</span><span class="pl-kos">(</span><span class="pl-s">'app'</span><span class="pl-kos">)</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-kos">}</span>
<span class="pl-en">run</span><span class="pl-kos">(</span><span class="pl-kos">)</span><span class="pl-kos">;</span>

<span class="pl-c">// 只在开发模式下配置模块热替换</span>
<span class="pl-k">if</span> <span class="pl-kos">(</span><span class="pl-s1">process</span><span class="pl-kos">.</span><span class="pl-c1">env</span><span class="pl-kos">.</span><span class="pl-c1">NODE_ENV</span> <span class="pl-c1">!==</span> <span class="pl-s">'production'</span><span class="pl-kos">)</span> <span class="pl-kos">{</span>
  <span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">hot</span><span class="pl-kos">.</span><span class="pl-en">accept</span><span class="pl-kos">(</span><span class="pl-s">'./app'</span><span class="pl-kos">,</span> <span class="pl-s1">run</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-kos">}</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="import App from './app';

function run(){
	render(<App/>,document.getElementById('app'));
}
run();

// 只在开发模式下配置模块热替换
if (process.env.NODE_ENV !== 'production') {
  module.hot.accept('./app', run);
}
" tabindex="0" role="button"></clipboard-copy></div>

</div>

当./app发生变化或者当./app依赖的文件发生变化时会把./app编译成一个模块去替换老的，替换完毕后重新执行run函数渲染出最新的效果。

#### 自动生成html

webpack只做了资源打包的工作还缺少把这些加载到html里运行的功能，在庞大的app里手写html去加载这些资源是很繁琐易错的，我们需要自动正确的加载打包出的资源。  
 webpack原生不支持这个功能于是我做了一个插件 [web-webpack-plugin](https://github.com/gwuhaolin/web-webpack-plugin)  
 具体使用点开链接看[详细文档](https://github.com/gwuhaolin/web-webpack-plugin/blob/master/readme_zh.md)，使用大概如下：

[demo](https://github.com/gwuhaolin/web-webpack-plugin/tree/master/demo/out-html)

webpack配置

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
    <span class="pl-c1">entry</span>: <span class="pl-kos">{</span>
        <span class="pl-c1">A</span>: <span class="pl-s">'./a'</span><span class="pl-kos">,</span>
        <span class="pl-c1">B</span>: <span class="pl-s">'./b'</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">,</span>
    <span class="pl-c1">plugins</span>: <span class="pl-kos">[</span>
        <span class="pl-k">new</span> <span class="pl-v">WebPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
            <span class="pl-c">// 输出的html文件名称，必填，注意不要重名，重名会覆盖相互文件。</span>
            <span class="pl-c1">filename</span>: <span class="pl-s">'index.html'</span><span class="pl-kos">,</span>
            <span class="pl-c">// 该html文件依赖的entry，必须是一个数组。依赖的资源的注入顺序按照数组的顺序。</span>
            <span class="pl-c1">requires</span>: <span class="pl-kos">[</span><span class="pl-s">'A'</span><span class="pl-kos">,</span> <span class="pl-s">'B'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
        <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-kos">]</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
    entry: {
        A: './a',
        B: './b',
    },
    plugins: [
        new WebPlugin({
            // 输出的html文件名称，必填，注意不要重名，重名会覆盖相互文件。
            filename: 'index.html',
            // 该html文件依赖的entry，必须是一个数组。依赖的资源的注入顺序按照数组的顺序。
            requires: ['A', 'B'],
        }),
    ]
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

将会输出一个`index.html`文件，这个文件将会自动引入 entry `A` 和 `B` 生成的js文件，

输出的html:

<div class="highlight highlight-text-html-basic position-relative overflow-auto">

<pre><span class="pl-c1"><!DOCTYPE html<span class="pl-kos">></span></span>
<span class="pl-kos"><</span><span class="pl-ent">html</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">head</span><span class="pl-kos">></span>
    <span class="pl-kos"><</span><span class="pl-ent">meta</span> <span class="pl-c1">charset</span>="<span class="pl-s">UTF-8</span>"<span class="pl-kos">></span>
<span class="pl-kos"></</span><span class="pl-ent">head</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">body</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">script</span> <span class="pl-c1">src</span>="<span class="pl-s">A.js</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">script</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">script</span> <span class="pl-c1">src</span>="<span class="pl-s">B.js</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">script</span><span class="pl-kos">></span>
<span class="pl-kos"></</span><span class="pl-ent">body</span><span class="pl-kos">></span>
<span class="pl-kos"></</span><span class="pl-ent">html</span><span class="pl-kos">></span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="<!DOCTYPE html>
<html>
<head>
    <meta charset=&quot;UTF-8&quot;>
</head>
<body>
<script src=&quot;A.js&quot;></script>
<script src=&quot;B.js&quot;></script>
</body>
</html>
" tabindex="0" role="button"></clipboard-copy></div>

</div>

输出的目录结构

<div class="snippet-clipboard-content position-relative overflow-auto">

    ├── A.js
    ├── B.js
    └── index.html

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="├── A.js
├── B.js
└── index.html
" tabindex="0" role="button"></clipboard-copy></div>

</div>

#### 管理多页面

虽然webpack适用于单页应用，但复杂的系统经常是由多个单页应用组成，每个页面一个功能模块。webpack给出了js打包方案但缺少管理多个页面的功能。 [web-webpack-plugin](https://github.com/gwuhaolin/web-webpack-plugin)的`AutoWebPlugin`会自动的为你的系统里每个单页应用生成一个html入口页，这个入口会自动的注入当前单页应用依赖的资源，使用它你只需如下几行代码：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-s1">plugins</span>: <span class="pl-kos">[</span>
    <span class="pl-c">// ./src/pages/ 代表存放所有页面的根目录，这个目录下的每一个目录被看着是一个单页应用</span>
    <span class="pl-c">// 会为里面的每一个目录生成一个html入口</span>
    <span class="pl-k">new</span> <span class="pl-v">AutoWebPlugin</span><span class="pl-kos">(</span><span class="pl-s">'./src/pages/'</span><span class="pl-kos">,</span> <span class="pl-kos">{</span>
      <span class="pl-c">//使用单页应用的html模版文件，这里你可以自定义配置</span>
      <span class="pl-c1">template</span>: <span class="pl-s">'./src/assets/template.html'</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
<span class="pl-kos">]</span><span class="pl-kos">,</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="plugins: [
    // ./src/pages/ 代表存放所有页面的根目录，这个目录下的每一个目录被看着是一个单页应用
    // 会为里面的每一个目录生成一个html入口
    new AutoWebPlugin('./src/pages/', {
      //使用单页应用的html模版文件，这里你可以自定义配置
      template: './src/assets/template.html',
    }),
],
" tabindex="0" role="button"></clipboard-copy></div>

</div>

查看web-webpack-plugin的[文档了解更多](https://github.com/gwuhaolin/web-webpack-plugin/blob/master/readme_zh.md#%E8%87%AA%E5%8A%A8%E6%8E%A2%E6%B5%8Bhtml%E5%85%A5%E5%8F%A3-demo)

### 分析输出结果

如果你对当前的配置输出或者构建速度不满意，webpack有一个工具叫做[webpack analyze](https://webpack.github.io/analyse/) 以可视化的方式直观的分析构建，来进一步优化构建结果和速度。要使用它你需要在执行webpack的时候带上`--json --profile`2个参数，这代表让webpack把构建结果以json输出并带上构建性能信息，使用如下：

<div class="highlight highlight-source-shell position-relative overflow-auto">

<pre>webpack --json --profile <span class="pl-k">></span> stats.json </pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="webpack --json --profile > stats.json 
" tabindex="0" role="button"></clipboard-copy></div>

</div>

会生产一个`stats.json` 文件，再打开[webpack analyze](https://webpack.github.io/analyse/) 上传这个文件开始分析。

**最后附上这篇文章所讲到的[webpack整体的配置](https://gist.github.com/gwuhaolin/cebd252a23793e742e6acae90ab63e83)，分为开发环境的`webpack.config.js`和生产环境的`webpack-dist.config.js`**

[阅读原文](http://wuhaolin.cn/2017/04/30/webpack2%20%E7%BB%88%E6%9E%81%E4%BC%98%E5%8C%96/)


>https://github.com/gwuhaolin/blog/issues/2





#加速Webpack


> 本文首发于[IBM Dev社区](https://www.ibm.com/developerworks/cn/web/wa-lo-expedite-webpack/index.html)

Web 应用日益复杂，相关开发技术也百花齐放，这对前端构建工具提出了更高的要求。 Webpack 从众多构建工具中脱颖而出成为目前最流行的构建工具，几乎成为目前前端开发里的必备工具之一。 大多数人在使用 Webpack 的过程中都会遇到构建速度慢的问题，在项目大时显得尤为突出，这极大的影响了我们的开发体验，降低了我们的开发效率。

本文将传授你一些加速 Webpack 构建的技巧，下面来一一介绍。

# 通过多进程并行处理

由于有大量文件需要解析和处理，构建是文件读写和计算密集型的操作，特别是当文件数量变多后，Webpack 构建慢的问题会显得严重。 运行在 Node.js 之上的 Webpack 是单线程模型的，也就是说 Webpack 需要处理的任务需要一件件挨着做，不能多个事情一起做。

文件读写和计算操作是无法避免的，那能不能让 Webpack 同一时刻处理多个任务，发挥多核 CPU 电脑的威力，以提升构建速度呢？

### 使用 HappyPack

HappyPack 就能让 Webpack 做到上面抛出的问题，它把任务分解给多个子进程去并发的执行，子进程处理完后再把结果发送给主进程。

接入 HappyPack 的相关代码如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">const</span> <span class="pl-s1">path</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'path'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
    <span class="pl-k">const</span>  <span class="pl-v">ExtractTextPlugin</span> <span class="pl-c1">=</span>  <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'extract-text-webpack-plugin'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
    <span class="pl-k">const</span>  <span class="pl-v">HappyPack</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'happypack'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
    <span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span> 
        <span class="pl-c1">module</span>: <span class="pl-kos">{</span> 
            <span class="pl-c1">rules</span>: <span class="pl-kos">[</span> 
                <span class="pl-kos">{</span>    <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span> 
                    <span class="pl-c">// 把对 .js 文件的处理转交给 id 为 babel 的 HappyPack 实例</span> 
                    <span class="pl-c1">use</span>:<span class="pl-kos">[</span><span class="pl-s">'happypack/loader?id=babel'</span><span class="pl-kos">]</span><span class="pl-kos">,</span> 
                    <span class="pl-c">// 排除 node_modules 目录下的文件，node_modules目录下的文件都是采用的 ES5 语法，没必要再通过 Babel 去转换</span> 
                    <span class="pl-c1">exclude</span>: <span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'node_modules'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
                 <span class="pl-kos">}</span><span class="pl-kos">,</span> 
                <span class="pl-kos">{</span> 
                    <span class="pl-c">// 把对 .css 文件的处理转交给 id 为 css 的 HappyPack 实例</span>
                     <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>css<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span> 
                     <span class="pl-c1">use</span>:<span class="pl-v">ExtractTextPlugin</span><span class="pl-kos">.</span><span class="pl-en">extract</span><span class="pl-kos">(</span><span class="pl-kos">{</span> 
                        <span class="pl-c1">use</span>: <span class="pl-kos">[</span><span class="pl-s">'happypack/loader?id=css'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
             <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span> 
        <span class="pl-kos">}</span><span class="pl-kos">,</span> 
<span class="pl-kos">]</span> <span class="pl-kos">}</span><span class="pl-kos">,</span>
    <span class="pl-c1">plugins</span>: <span class="pl-kos">[</span> 
        <span class="pl-k">new</span> <span class="pl-v">HappyPack</span><span class="pl-kos">(</span><span class="pl-kos">{</span> 
            <span class="pl-c">// 用唯一的标识符 id 来代表当前的HappyPack 是用来处理一类特定的文件</span> 
        <span class="pl-c1">id</span>: <span class="pl-s">'babel'</span><span class="pl-kos">,</span> 
            <span class="pl-c">// 如何处理 .js 文件，用法和 Loader配置中一样</span> 
        <span class="pl-c1">loaders</span>: <span class="pl-kos">[</span><span class="pl-s">'babel-loader?cacheDirectory'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
     <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
        <span class="pl-k">new</span> <span class="pl-v">HappyPack</span><span class="pl-kos">(</span><span class="pl-kos">{</span> 
                <span class="pl-c1">id</span>: <span class="pl-s">'css'</span><span class="pl-kos">,</span> 
                    <span class="pl-c">// 如何处理 .css 文件，用法和Loader 配置中一样</span> 
                <span class="pl-c1">loaders</span>: <span class="pl-kos">[</span><span class="pl-s">'css-loader'</span><span class="pl-kos">]</span><span class="pl-kos">,</span> <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span> 
                <span class="pl-k">new</span> <span class="pl-v">ExtractTextPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span> 
                    <span class="pl-c1">filename</span>: <span class="pl-s">`[name].css`</span><span class="pl-kos">,</span> 
            <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span> 
        <span class="pl-kos">]</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="const path = require('path');
    const  ExtractTextPlugin =  require('extract-text-webpack-plugin');
    const  HappyPack = require('happypack');
    module.exports = { 
        module: { 
            rules: [ 
                {    test: /\.js$/, 
                    // 把对 .js 文件的处理转交给 id 为 babel 的 HappyPack 实例 
                    use:['happypack/loader?id=babel'], 
                    // 排除 node_modules 目录下的文件，node_modules目录下的文件都是采用的 ES5 语法，没必要再通过 Babel 去转换 
                    exclude: path.resolve(__dirname, 'node_modules'),
                 }, 
                { 
                    // 把对 .css 文件的处理转交给 id 为 css 的 HappyPack 实例
                     test: /\.css$/, 
                     use:ExtractTextPlugin.extract({ 
                        use: ['happypack/loader?id=css'],
             }), 
        }, 
] },
    plugins: [ 
        new HappyPack({ 
            // 用唯一的标识符 id 来代表当前的HappyPack 是用来处理一类特定的文件 
        id: 'babel', 
            // 如何处理 .js 文件，用法和 Loader配置中一样 
        loaders: ['babel-loader?cacheDirectory'],
     }),
        new HappyPack({ 
                id: 'css', 
                    // 如何处理 .css 文件，用法和Loader 配置中一样 
                loaders: ['css-loader'], }), 
                new ExtractTextPlugin({ 
                    filename: `[name].css`, 
            }), 
        ],
    };
" tabindex="0" role="button"></clipboard-copy></div>

</div>

以上代码有两点重要的修改：

*   在 Loader 配置中，所有文件的处理都交给了 happypack/loader 去处理，使用紧跟其后的 querystring ?id=babel 去告诉 happypack/loader 去选择哪个 HappyPack 实例去处理文件。
*   在 Plugin 配置中，新增了两个 HappyPack 实例分别用于告诉 happypack/loader 去如何处理 .js 和 .css 文件。选项中的 id 属性的值和上面 querystring 中的 ?id=babel 相对应，选项中的 loaders 属性和 Loader 配置中一样。

接入 HappyPack 后，你需要给项目安装新的依赖：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-s1">npm</span> <span class="pl-s1">i</span> <span class="pl-c1">-</span><span class="pl-v">D</span> <span class="pl-s1">happypack</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="npm i -D happypack
" tabindex="0" role="button"></clipboard-copy></div>

</div>

安装成功后重新执行构建，你就会看到以下由 HappyPack 输出的日志：

<div class="snippet-clipboard-content position-relative overflow-auto">

    Happy[babel]: Version: 4.0.0-beta.5\. Threads: 3
    Happy[babel]: All set; signaling webpack to proceed.Happy[css]: Version: 4.0.0-beta.5\. Threads: 3Happy[css]: All set; signaling webpack to proceed.

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="Happy[babel]: Version: 4.0.0-beta.5\. Threads: 3
Happy[babel]: All set; signaling webpack to proceed.Happy[css]: Version: 4.0.0-beta.5\. Threads: 3Happy[css]: All set; signaling webpack to proceed.
" tabindex="0" role="button"></clipboard-copy></div>

</div>

说明你的 HappyPack 配置生效了，并且可以得知 HappyPack 分别启动了3个子进程去并行的处理任务。

在整个 Webpack 构建流程中，最耗时的流程可能就是 Loader 对文件的转换操作了，因为要转换的文件数据巨多，而且这些转换操作都只能一个个挨着处理。 HappyPack 的核心原理就是把这部分任务分解到多个进程去并行处理，从而减少了总的构建时间。

从前面的使用中可以看出所有需要通过 Loader 处理的文件都先交给了 happypack/loader 去处理，收集到了这些文件的处理权后 HappyPack 就好统一分配了。

每通过 new HappyPack() 实例化一个 HappyPack 其实就是告诉 HappyPack 核心调度器如何通过一系列 Loader 去转换一类文件，并且可以指定如何给这类转换操作分配子进程。

核心调度器的逻辑代码在主进程中，也就是运行着 Webpack 的进程中，核心调度器会把一个个任务分配给当前空闲的子进程，子进程处理完毕后把结果发送给核心调度器，它们之间的数据交换是通过进程间通信 API 实现的。

核心调度器收到来自子进程处理完毕的结果后会通知 Webpack 该文件处理完毕。

### 使用 ParallelUglifyPlugin

在使用 Webpack 构建出用于发布到线上的代码时，都会有压缩代码这一流程。 最常见的 JavaScript 代码压缩工具是 UglifyJS，并且 Webpack 也内置了它。

用过 UglifyJS 的你一定会发现在构建用于开发环境的代码时很快就能完成，但在构建用于线上的代码时构建一直卡在一个时间点迟迟没有反应，其实卡住的这个时候就是在进行代码压缩。

由于压缩 JavaScript 代码需要先把代码解析成用 Object 抽象表示的 AST 语法树，再去应用各种规则分析和处理 AST，导致这个过程计算量巨大，耗时非常多。

为什么不把多进程并行处理的思想也引入到代码压缩中呢？

ParallelUglifyPlugin 就做了这个事情。 当 Webpack 有多个 JavaScript 文件需要输出和压缩时，原本会使用 UglifyJS 去一个个挨着压缩再输出， 但是 ParallelUglifyPlugin 则会开启多个子进程，把对多个文件的压缩工作分配给多个子进程去完成，每个子进程其实还是通过 UglifyJS 去压缩代码，但是变成了并行执行。 所以 ParallelUglifyPlugin 能更快的完成对多个文件的压缩工作。

使用 ParallelUglifyPlugin 也非常简单，把原来 Webpack 配置文件中内置的 UglifyJsPlugin 去掉后，再替换成 ParallelUglifyPlugin，相关代码如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">const</span> <span class="pl-s1">path</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'path'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-k">const</span>  <span class="pl-v">ParallelUglifyPlugin</span> <span class="pl-c1">=</span>  <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'webpack-parallel-uglify-plugin'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span> 
        <span class="pl-c1">plugins</span>: <span class="pl-kos">[</span> 
            <span class="pl-c">// 使用 ParallelUglifyPlugin 并行压缩输出的 JS 代码</span>
            <span class="pl-k">new</span> <span class="pl-v">ParallelUglifyPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span> 
                <span class="pl-c">// 传递给 UglifyJS 的参数</span>
                <span class="pl-c1">uglifyJS</span>: <span class="pl-kos">{</span>
                 <span class="pl-kos">}</span><span class="pl-kos">,</span> 
            <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span> 
        <span class="pl-kos">]</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="const path = require('path');
const  ParallelUglifyPlugin =  require('webpack-parallel-uglify-plugin');
module.exports = { 
        plugins: [ 
            // 使用 ParallelUglifyPlugin 并行压缩输出的 JS 代码
            new ParallelUglifyPlugin({ 
                // 传递给 UglifyJS 的参数
                uglifyJS: {
                 }, 
            }), 
        ],
    };
" tabindex="0" role="button"></clipboard-copy></div>

</div>

接入 ParallelUglifyPlugin 后，项目需要安装新的依赖：

<div class="highlight highlight-source-shell position-relative overflow-auto">

<pre>npm i -D webpack-parallel-uglify-plugin</pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="npm i -D webpack-parallel-uglify-plugin
" tabindex="0" role="button"></clipboard-copy></div>

</div>

安装成功后，重新执行构建你会发现速度变快了许多。如果设置 cacheDir 开启了缓存，在之后的构建中会变的更快。

### 缩小文件搜索范围

Webpack 启动后会从配置的 Entry 出发，解析出文件中的导入语句，再递归的解析。 在遇到导入语句时 Webpack 会做两件事情：

1.  根据导入语句去寻找对应的要导入的文件。例如 require('react') 导入语句对应的文件是 ./node_modules/react/react.js，而require('./util')导入语句 对应的文件是 ./util.js。
2.  根据找到的要导入文件的后缀，使用配置中的 Loader 去处理文件。例如使用 ES6 开发的 JavaScript 文件需要使用 babel-loader 去处理。

以上两件事情虽然对于处理一个文件非常快，但是当项目大了以后文件量会变的非常多，这时候构建速度慢的问题就会暴露出来。 虽然以上两件事情无法避免，但需要尽量减少以上两件事情的发生，以提高速度。

接下来一一介绍可以优化它们的途径。

##### 缩小 resolve.modules 的范围

Webpack的resolve.modules 用于配置 Webpack 去哪些目录下寻找第三方模块。

resolve.modules 的默认值是 ['node_modules']，含义是先去当前目录下的 ./node_modules 目录下去找想找的模块，如果没找到就去上一级目录 ../node_modules 中找，再没有就去 ../../node_modules 中找，以此类推，这和 Node.js 的模块寻找机制很相似。

当安装的第三方模块都放在项目根目录下的 ./node_modules 目录下时，没有必要按照默认的方式去一层层的寻找，可以指明存放第三方模块的绝对路径，以减少寻找，配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
            <span class="pl-c1">resolve</span>: <span class="pl-kos">{</span>
                <span class="pl-c">// 使用绝对路径指明第三方模块存放的位置，以减少搜索步骤</span>
                <span class="pl-c">// 其中 __dirname 表示当前工作目录，也就是项目根目录</span>
                <span class="pl-c1">modules</span>: <span class="pl-kos">[</span><span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'node_modules'</span><span class="pl-kos">)</span><span class="pl-kos">]</span>
         <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
            resolve: {
                // 使用绝对路径指明第三方模块存放的位置，以减少搜索步骤
                // 其中 __dirname 表示当前工作目录，也就是项目根目录
                modules: [path.resolve(__dirname, 'node_modules')]
         },
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

##### 缩小 Loader 的命中范围

除此之外在使用 Loader 时可以通过 test 、 include 、 exclude 三个配置项来命中 Loader 要应用规则的文件。 为了尽可能少的让文件被 Loader 处理，可以通过 include 去命中只有哪些文件需要被处理。

以采用 ES6 的项目为例，在配置 babel-loader 时，可以这样：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">module</span>: <span class="pl-kos">{</span>
    <span class="pl-c1">rules</span>: <span class="pl-kos">[</span>
      <span class="pl-kos">{</span>
        <span class="pl-c">// 如果项目源码中只有 js 文件就不要写成 /\.jsx?$/，提升正则表达式性能</span>
        <span class="pl-c1">test</span>: <span class="pl-pds"><span class="pl-c1">/</span><span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">,</span>
        <span class="pl-c">// babel-loader 支持缓存转换出的结果，通过 cacheDirectory 选项开启</span>
        <span class="pl-c1">use</span>: <span class="pl-kos">[</span><span class="pl-s">'babel-loader?cacheDirectory'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
        <span class="pl-c">// 只对项目根目录下的 src 目录中的文件采用 babel-loader</span>
        <span class="pl-c1">include</span>: <span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'src'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
      <span class="pl-kos">}</span><span class="pl-kos">,</span>
    <span class="pl-kos">]</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  module: {
    rules: [
      {
        // 如果项目源码中只有 js 文件就不要写成 /\.jsx?$/，提升正则表达式性能
        test: /\.js$/,
        // babel-loader 支持缓存转换出的结果，通过 cacheDirectory 选项开启
        use: ['babel-loader?cacheDirectory'],
        // 只对项目根目录下的 src 目录中的文件采用 babel-loader
        include: path.resolve(__dirname, 'src'),
      },
    ]
  },
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

你可以适当的调整项目的目录结构，以方便在配置 Loader 时通过 include 去缩小命中范围。

##### 缩小 resolve.extensions 的数量

在导入语句没带文件后缀时，Webpack 会自动带上后缀后去尝试询问文件是否存在。 Webpack 配置中的 resolve.extensions 用于配置在尝试过程中用到的后缀列表，默认是：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre>extensions: <span class="pl-kos">[</span><span class="pl-s">'.js'</span><span class="pl-kos">,</span> <span class="pl-s">'.json'</span><span class="pl-kos">]</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="extensions: ['.js', '.json']
" tabindex="0" role="button"></clipboard-copy></div>

</div>

也就是说当遇到 require('./data') 这样的导入语句时，Webpack 会先去寻找 ./data.js 文件，如果该文件不存在就去寻找 ./data.json 文件，如果还是找不到就报错。

如果这个列表越长，或者正确的后缀在越后面，就会造成尝试的次数越多，所以 resolve.extensions 的配置也会影响到构建的性能。 在配置 resolve.extensions 时你需要遵守以下几点，以做到尽可能的优化构建性能：

*   后缀尝试列表要尽可能的小，不要把项目中不可能存在的情况写到后缀尝试列表中。
*   频率出现最高的文件后缀要优先放在最前面，以做到尽快的退出寻找过程。
*   在源码中写导入语句时，要尽可能的带上后缀，从而可以避免寻找过程。例如在你确定的情况下把 require('./data') 写成 require('./data.json')。

相关 Webpack 配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">resolve</span>: <span class="pl-kos">{</span>
    <span class="pl-c">// 尽可能的减少后缀尝试的可能性</span>
    <span class="pl-c1">extensions</span>: <span class="pl-kos">[</span><span class="pl-s">'js'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  resolve: {
    // 尽可能的减少后缀尝试的可能性
    extensions: ['js'],
  },
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

##### 缩小 resolve.mainFields 的数量

Webpack 配置中的 resolve.mainFields 用于配置第三方模块使用哪个入口文件。

安装的第三方模块中都会有一个 package.json 文件用于描述这个模块的属性，其中有些字段用于描述入口文件在哪里，resolve.mainFields 用于配置采用哪个字段作为入口文件的描述。

可以存在多个字段描述入口文件的原因是因为有些模块可以同时用在多个环境中，针对不同的运行环境需要使用不同的代码。 以 isomorphic-fetchfetch API 为例，它是 的一个实现，但可同时用于浏览器和 Node.js 环境。

为了减少搜索步骤，在你明确第三方模块的入口文件描述字段时，你可以把它设置的尽量少。 由于大多数第三方模块都采用 main 字段去描述入口文件的位置，可以这样配置 Webpack：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">resolve</span>: <span class="pl-kos">{</span>
    <span class="pl-c">// 只采用 main 字段作为入口文件描述字段，以减少搜索步骤</span>
    <span class="pl-c1">mainFields</span>: <span class="pl-kos">[</span><span class="pl-s">'main'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  resolve: {
    // 只采用 main 字段作为入口文件描述字段，以减少搜索步骤
    mainFields: ['main'],
  },
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

使用本方法优化时，你需要考虑到所有运行时依赖的第三方模块的入口文件描述字段，就算有一个模块搞错了都可能会造成构建出的代码无法正常运行。

# 善用现存的文件

### 通过 module.noParse 忽略文件

Webpack 配置中的 module.noParse 配置项可以让 Webpack 忽略对部分没采用模块化的文件的递归解析处理，这样做的好处是能提高构建性能。 原因是一些库，例如 jQuery 、ChartJS， 它们庞大又没有采用模块化标准，让 Webpack 去解析这些文件耗时又没有意义。

在上面的 _优化 resolve.alias 配置_ 中讲到单独完整的 react.min.js 文件就没有采用模块化，让我们来通过配置 module.noParse 忽略对 react.min.js 文件的递归解析处理， 相关 Webpack 配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">module</span>: <span class="pl-kos">{</span>
    <span class="pl-c">// 独完整的 `react.min.js` 文件就没有采用模块化，忽略对 `react.min.js` 文件的递归解析处理</span>
    <span class="pl-c1">noParse</span>: <span class="pl-kos">[</span><span class="pl-pds"><span class="pl-c1">/</span>react<span class="pl-cce">\.</span>min<span class="pl-cce">\.</span>js<span class="pl-cce">$</span><span class="pl-c1">/</span></span><span class="pl-kos">]</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  module: {
    // 独完整的 `react.min.js` 文件就没有采用模块化，忽略对 `react.min.js` 文件的递归解析处理
    noParse: [/react\.min\.js$/],
  },
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

注意被忽略掉的文件里不应该包含 import 、 require 、 define 等模块化语句，不然会导致构建出的代码中包含无法在浏览器环境下执行的模块化语句。

### 通过 resolve.alias 映射文件

Webpack 配置中的 resolve.alias 配置项通过别名来把原导入路径映射成一个新的导入路径。

在实战项目中经常会依赖一些庞大的第三方模块，以 React 库为例，库中包含两套代码：

*   一套是采用 CommonJS 规范的模块化代码，这些文件都放在 lib 目录下，以 package.json 中指定的入口文件 react.js 为模块的入口。
*   一套是把 React 所有相关的代码打包好的完整代码放到一个单独的文件中，这些代码没有采用模块化可以直接执行。其中 dist/react.js 是用于开发环境，里面包含检查和警告的代码。dist/react.min.js 是用于线上环境，被最小化了。

默认情况下 Webpack 会从入口文件 ./node_modules/react/react.js 开始递归的解析和处理依赖的几十个文件，这会时一个耗时的操作。 通过配置 resolve.alias 可以让 Webpack 在处理 React 库时，直接使用单独完整的 react.min.js 文件，从而跳过耗时的递归解析操作。

相关 Webpack 配置如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">resolve</span>: <span class="pl-kos">{</span>
    <span class="pl-c">// 使用 alias 把导入 react 的语句换成直接使用单独完整的 react.min.js 文件，</span>
    <span class="pl-c">// 减少耗时的递归解析操作</span>
    <span class="pl-c1">alias</span>: <span class="pl-kos">{</span>
      <span class="pl-s">'react'</span>: <span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'./node_modules/react/dist/react.min.js'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="module.exports = {
  resolve: {
    // 使用 alias 把导入 react 的语句换成直接使用单独完整的 react.min.js 文件，
    // 减少耗时的递归解析操作
    alias: {
      'react': path.resolve(__dirname, './node_modules/react/dist/react.min.js'),
    }
  },
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

除了 React 库外，大多数库发布到 Npm 仓库中时都会包含打包好的完整文件，对于这些库你也可以对它们配置 alias。

但是对于有些库使用本优化方法后会影响到后面要讲的使用 Tree-Shaking 去除无效代码的优化，因为打包好的完整文件中有部分代码你的项目可能永远用不上。 一般对整体性比较强的库采用本方法优化，因为完整文件中的代码是一个整体，每一行都是不可或缺的。 但是对于一些工具类的库，例如 lodash，你的项目可能只用到了其中几个工具函数，你就不能使用本方法去优化，因为这会导致你的输出代码中包含很多永远不会执行的代码。

# 使用 DllPlugin

在介绍 DllPlugin 前先给大家介绍下 DLL。 用过 Windows 系统的人应该会经常看到以 .dll 为后缀的文件，这些文件称为**动态链接库**，在一个动态链接库中可以包含给其他模块调用的函数和数据。

要给 Web 项目构建接入动态链接库的思想，需要完成以下事情：

*   把网页依赖的基础模块抽离出来，打包到一个个单独的动态链接库中去。一个动态链接库中可以包含多个模块。
*   当需要导入的模块存在于某个动态链接库中时，这个模块不能再次被打包，而是去动态链接库中获取。
*   页面依赖的所有动态链接库需要被加载。

为什么给 Web 项目构建接入动态链接库的思想后，会大大提升构建速度呢？ 原因在于包含大量复用模块的动态链接库只需要编译一次，在之后的构建过程中被动态链接库包含的模块将不会在重新编译，而是直接使用动态链接库中的代码。 由于动态链接库中大多数包含的是常用的第三方模块，例如 react、react-dom，只要不升级这些模块的版本，动态链接库就不用重新编译。

### 接入 Webpack

Webpack 已经内置了对动态链接库的支持，需要通过2个内置的插件接入，它们分别是：

*   DllPlugin 插件：用于打包出一个个单独的动态链接库文件。
*   DllReferencePlugin 插件：用于在主要配置文件中去引入 DllPlugin 插件打包好的动态链接库文件。

下面以基本的 React 项目为例，为其接入 DllPlugin，在开始前先来看下最终构建出的目录结构：

<div class="snippet-clipboard-content position-relative overflow-auto">

    ├── main.js
    ├── polyfill.dll.js
    ├── polyfill.manifest.json
    ├── react.dll.js
    └── react.manifest.json

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="├── main.js
├── polyfill.dll.js
├── polyfill.manifest.json
├── react.dll.js
└── react.manifest.json
" tabindex="0" role="button"></clipboard-copy></div>

</div>

其中包含两个动态链接库文件，分别是：

*   polyfill.dll.js 里面包含项目所有依赖的 polyfill，例如 Promise、fetch 等 API。
*   react.dll.js 里面包含 React 的基础运行环境，也就是 react 和 react-dom 模块。

以 react.dll.js 文件为例，其文件内容大致如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">var</span> <span class="pl-s1">_dll_react</span> <span class="pl-c1">=</span> <span class="pl-kos">(</span><span class="pl-k">function</span><span class="pl-kos">(</span><span class="pl-s1">modules</span><span class="pl-kos">)</span> <span class="pl-kos">{</span>
  <span class="pl-c">// ... 此处省略 webpackBootstrap 函数代码</span>
<span class="pl-kos">}</span><span class="pl-kos">(</span><span class="pl-kos">[</span>
  <span class="pl-k">function</span><span class="pl-kos">(</span><span class="pl-s1">module</span><span class="pl-kos">,</span> <span class="pl-s1">exports</span><span class="pl-kos">,</span> <span class="pl-s1">__webpack_require__</span><span class="pl-kos">)</span> <span class="pl-kos">{</span>
    <span class="pl-c">// 模块 ID 为 0 的模块对应的代码</span>
  <span class="pl-kos">}</span>
  <span class="pl-c">// ... 此处省略剩下的模块对应的代码</span> 
<span class="pl-kos">]</span><span class="pl-kos">)</span><span class="pl-kos">)</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="var _dll_react = (function(modules) {
  // ... 此处省略 webpackBootstrap 函数代码
}([
  function(module, exports, __webpack_require__) {
    // 模块 ID 为 0 的模块对应的代码
  }
  // ... 此处省略剩下的模块对应的代码 
]));
" tabindex="0" role="button"></clipboard-copy></div>

</div>

可见一个动态链接库文件中包含了大量模块的代码，这些模块存放在一个数组里，用数组的索引号作为 ID。 并且还通过 _dll_react 变量把自己暴露在了全局中，也就是可以通过 window._dll_react 可以访问到它里面包含的模块。

其中 polyfill.manifest.json 和 react.manifest.json 文件也是由 DllPlugin 生成，用于描述动态链接库文件中包含哪些模块， 以 react.manifest.json 文件为例，其文件内容大致如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-kos">{</span>
  <span class="pl-c">// 描述该动态链接库文件暴露在全局的变量名称</span>
  <span class="pl-s">"name"</span>: <span class="pl-s">"_dll_react"</span><span class="pl-kos">,</span>
  <span class="pl-s">"content"</span>: <span class="pl-kos">{</span>
    <span class="pl-s">"./node_modules/process/browser.js"</span>: <span class="pl-kos">{</span>
      <span class="pl-s">"id"</span>: <span class="pl-c1">0</span><span class="pl-kos">,</span>
      <span class="pl-s">"meta"</span>: <span class="pl-kos">{</span><span class="pl-kos">}</span>
    <span class="pl-kos">}</span><span class="pl-kos">,</span>
    <span class="pl-c">// ... 此处省略部分模块</span>
  <span class="pl-kos">}</span>
<span class="pl-kos">}</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="{
  // 描述该动态链接库文件暴露在全局的变量名称
  &quot;name&quot;: &quot;_dll_react&quot;,
  &quot;content&quot;: {
    &quot;./node_modules/process/browser.js&quot;: {
      &quot;id&quot;: 0,
      &quot;meta&quot;: {}
    },
    // ... 此处省略部分模块
  }
}
" tabindex="0" role="button"></clipboard-copy></div>

</div>

可见 manifest.json 文件清楚地描述了与其对应的 dll.js 文件中包含了哪些模块，以及每个模块的路径和 ID。

main.js 文件是编译出来的执行入口文件，当遇到其依赖的模块在 dll.js 文件中时，会直接通过 dll.js 文件暴露出的全局变量去获取打包在 dll.js 文件的模块。 所以在 index.html 文件中需要把依赖的两个 dll.js 文件给加载进去，index.html 内容如下：

<div class="highlight highlight-text-html-basic position-relative overflow-auto">

<pre><span class="pl-c"><!--导入依赖的动态链接库文件--></span>
<span class="pl-kos"><</span><span class="pl-ent">script</span> <span class="pl-c1">src</span>="<span class="pl-s">./dist/polyfill.dll.js</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">script</span><span class="pl-kos">></span>
<span class="pl-kos"><</span><span class="pl-ent">script</span> <span class="pl-c1">src</span>="<span class="pl-s">./dist/react.dll.js</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">script</span><span class="pl-kos">></span>
<span class="pl-c"><!--导入执行入口文件--></span>
<span class="pl-kos"><</span><span class="pl-ent">script</span> <span class="pl-c1">src</span>="<span class="pl-s">./dist/main.js</span>"<span class="pl-kos">></span><span class="pl-kos"></</span><span class="pl-ent">script</span><span class="pl-kos">></span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="<!--导入依赖的动态链接库文件-->
<script src=&quot;./dist/polyfill.dll.js&quot;></script>
<script src=&quot;./dist/react.dll.js&quot;></script>
<!--导入执行入口文件-->
<script src=&quot;./dist/main.js&quot;></script>
" tabindex="0" role="button"></clipboard-copy></div>

</div>

以上就是所有接入 DllPlugin 后最终编译出来的代码，接下来教你如何实现。

### 构建出动态链接库文件

构建输出的以下这四个文件

<div class="snippet-clipboard-content position-relative overflow-auto">

    ├── polyfill.dll.js
    ├── polyfill.manifest.json
    ├── react.dll.js
    └── react.manifest.json

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="├── polyfill.dll.js
├── polyfill.manifest.json
├── react.dll.js
└── react.manifest.json
" tabindex="0" role="button"></clipboard-copy></div>

</div>

和以下这一个文件

<div class="snippet-clipboard-content position-relative overflow-auto">

    ├── main.js

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="├── main.js
" tabindex="0" role="button"></clipboard-copy></div>

</div>

是由两份不同的构建分别输出的。

与动态链接库相关的文件需要由一个独立的构建输出，用于给主构建使用。新建一个 Webpack 配置文件 webpack_dll.config.js 专门用于构建它们，文件内容如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">const</span> <span class="pl-s1">path</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'path'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>
<span class="pl-k">const</span> <span class="pl-v">DllPlugin</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'webpack/lib/DllPlugin'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>

<span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c">// JS 执行入口文件</span>
  <span class="pl-c1">entry</span>: <span class="pl-kos">{</span>
    <span class="pl-c">// 把 React 相关模块的放到一个单独的动态链接库</span>
    <span class="pl-c1">react</span>: <span class="pl-kos">[</span><span class="pl-s">'react'</span><span class="pl-kos">,</span> <span class="pl-s">'react-dom'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
    <span class="pl-c">// 把项目需要所有的 polyfill 放到一个单独的动态链接库</span>
    <span class="pl-c1">polyfill</span>: <span class="pl-kos">[</span><span class="pl-s">'core-js/fn/object/assign'</span><span class="pl-kos">,</span> <span class="pl-s">'core-js/fn/promise'</span><span class="pl-kos">,</span> <span class="pl-s">'whatwg-fetch'</span><span class="pl-kos">]</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
  <span class="pl-c1">output</span>: <span class="pl-kos">{</span>
    <span class="pl-c">// 输出的动态链接库的文件名称，[name] 代表当前动态链接库的名称，</span>
    <span class="pl-c">// 也就是 entry 中配置的 react 和 polyfill</span>
    <span class="pl-c1">filename</span>: <span class="pl-s">'[name].dll.js'</span><span class="pl-kos">,</span>
    <span class="pl-c">// 输出的文件都放到 dist 目录下</span>
    <span class="pl-c1">path</span>: <span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">resolve</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'dist'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-c">// 存放动态链接库的全局变量名称，例如对应 react 来说就是 _dll_react</span>
    <span class="pl-c">// 之所以在前面加上 _dll_ 是为了防止全局变量冲突</span>
    <span class="pl-c1">library</span>: <span class="pl-s">'_dll_[name]'</span><span class="pl-kos">,</span>
  <span class="pl-kos">}</span><span class="pl-kos">,</span>
  <span class="pl-c1">plugins</span>: <span class="pl-kos">[</span>
    <span class="pl-c">// 接入 DllPlugin</span>
    <span class="pl-k">new</span> <span class="pl-v">DllPlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
      <span class="pl-c">// 动态链接库的全局变量名称，需要和 output.library 中保持一致</span>
      <span class="pl-c">// 该字段的值也就是输出的 manifest.json 文件 中 name 字段的值</span>
      <span class="pl-c">// 例如 react.manifest.json 中就有 "name": "_dll_react"</span>
      <span class="pl-c1">name</span>: <span class="pl-s">'_dll_[name]'</span><span class="pl-kos">,</span>
      <span class="pl-c">// 描述动态链接库的 manifest.json 文件输出时的文件名称</span>
      <span class="pl-c1">path</span>: <span class="pl-s1">path</span><span class="pl-kos">.</span><span class="pl-en">join</span><span class="pl-kos">(</span><span class="pl-s1">__dirname</span><span class="pl-kos">,</span> <span class="pl-s">'dist'</span><span class="pl-kos">,</span> <span class="pl-s">'[name].manifest.json'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
  <span class="pl-kos">]</span><span class="pl-kos">,</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="const path = require('path');
const DllPlugin = require('webpack/lib/DllPlugin');

module.exports = {
  // JS 执行入口文件
  entry: {
    // 把 React 相关模块的放到一个单独的动态链接库
    react: ['react', 'react-dom'],
    // 把项目需要所有的 polyfill 放到一个单独的动态链接库
    polyfill: ['core-js/fn/object/assign', 'core-js/fn/promise', 'whatwg-fetch'],
  },
  output: {
    // 输出的动态链接库的文件名称，[name] 代表当前动态链接库的名称，
    // 也就是 entry 中配置的 react 和 polyfill
    filename: '[name].dll.js',
    // 输出的文件都放到 dist 目录下
    path: path.resolve(__dirname, 'dist'),
    // 存放动态链接库的全局变量名称，例如对应 react 来说就是 _dll_react
    // 之所以在前面加上 _dll_ 是为了防止全局变量冲突
    library: '_dll_[name]',
  },
  plugins: [
    // 接入 DllPlugin
    new DllPlugin({
      // 动态链接库的全局变量名称，需要和 output.library 中保持一致
      // 该字段的值也就是输出的 manifest.json 文件 中 name 字段的值
      // 例如 react.manifest.json 中就有 &quot;name&quot;: &quot;_dll_react&quot;
      name: '_dll_[name]',
      // 描述动态链接库的 manifest.json 文件输出时的文件名称
      path: path.join(__dirname, 'dist', '[name].manifest.json'),
    }),
  ],
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

### 使用动态链接库文件

构建出的动态链接库文件用于在其它地方使用，在这里也就是给执行入口使用。

用于输出 main.js 的主 Webpack 配置文件内容如下：

<div class="highlight highlight-source-js position-relative overflow-auto">

<pre><span class="pl-k">const</span> <span class="pl-v">DllReferencePlugin</span> <span class="pl-c1">=</span> <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'webpack/lib/DllReferencePlugin'</span><span class="pl-kos">)</span><span class="pl-kos">;</span>

<span class="pl-smi">module</span><span class="pl-kos">.</span><span class="pl-c1">exports</span> <span class="pl-c1">=</span> <span class="pl-kos">{</span>
  <span class="pl-c1">plugins</span>: <span class="pl-kos">[</span>
    <span class="pl-c">// 告诉 Webpack 使用了哪些动态链接库</span>
    <span class="pl-k">new</span> <span class="pl-v">DllReferencePlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
      <span class="pl-c">// 描述 react 动态链接库的文件内容</span>
      <span class="pl-c1">manifest</span>: <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'./dist/react.manifest.json'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-k">new</span> <span class="pl-v">DllReferencePlugin</span><span class="pl-kos">(</span><span class="pl-kos">{</span>
      <span class="pl-c">// 描述 polyfill 动态链接库的文件内容</span>
      <span class="pl-c1">manifest</span>: <span class="pl-en">require</span><span class="pl-kos">(</span><span class="pl-s">'./dist/polyfill.manifest.json'</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
    <span class="pl-kos">}</span><span class="pl-kos">)</span><span class="pl-kos">,</span>
  <span class="pl-kos">]</span><span class="pl-kos">,</span>
  <span class="pl-c1">devtool</span>: <span class="pl-s">'source-map'</span>
<span class="pl-kos">}</span><span class="pl-kos">;</span></pre>

<div class="zeroclipboard-container position-absolute right-0 top-0"><clipboard-copy aria-label="Copy" class="ClipboardButton btn js-clipboard-copy m-2 p-0 tooltipped-no-delay" data-copy-feedback="Copied!" data-tooltip-direction="w" value="const DllReferencePlugin = require('webpack/lib/DllReferencePlugin');

module.exports = {
  plugins: [
    // 告诉 Webpack 使用了哪些动态链接库
    new DllReferencePlugin({
      // 描述 react 动态链接库的文件内容
      manifest: require('./dist/react.manifest.json'),
    }),
    new DllReferencePlugin({
      // 描述 polyfill 动态链接库的文件内容
      manifest: require('./dist/polyfill.manifest.json'),
    }),
  ],
  devtool: 'source-map'
};
" tabindex="0" role="button"></clipboard-copy></div>

</div>

> 注意：在 webpack_dll.config.js 文件中，DllPlugin 中的 name 参数必须和 output.library 中保持一致。 原因在于 DllPlugin 中的 name 参数会影响输出的 manifest.json 文件中 name 字段的值， 而在 webpack.config.js 文件中 DllReferencePlugin 会去 manifest.json 文件读取 name 字段的值， 把值的内容作为在从全局变量中获取动态链接库中内容时的全局变量名。

### 执行构建

在修改好以上两个 Webpack 配置文件后，需要重新执行构建。 重新执行构建时要注意的是需要先把动态链接库相关的文件编译出来，因为主 Webpack 配置文件中定义的 DllReferencePlugin 依赖这些文件。

执行构建时流程如下：

1.  如果动态链接库相关的文件还没有编译出来，就需要先把它们编译出来。方法是执行 webpack --config webpack_dll.config.js 命令。
2.  在确保动态链接库存在的前提下，才能正常的编译出入口执行文件。方法是执行 webpack 命令。这时你会发现构建速度有了非常大的提升。

相信给你的项目加上以上优化方法后，构建速度会大大提高，赶快去试试把！

# 参考资源

*   [Web 开发中的利器 - Webpack](https://www.ibm.com/developerworks/cn/web/wa-lo-web-develop-edge-tool/index.html)
*   查看[Webpack 迁移的研究](http://www.jianshu.com/p/e92ecf788c4f)，了解 Webpack 的具体配置方式





>https://github.com/gwuhaolin/blog/issues/16