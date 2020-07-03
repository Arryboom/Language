#1

>https://www.velvetcache.org/2010/08/19/a-simple-javascript-hooks-system

I was looking to add some more extensibility to a project this week and I couldn’t find a hook system for JavaScript. I wanted something similar to the PHP [hook system in MediaWiki](https://www.mediawiki.org/wiki/Manual:Hooks), but Google just wasn’t much help.

I’m sure there is something out there that does what I need, but it’s such a simple thing I went ahead and implemented it.

**hook.js**


```
var Hook = {
  hooks: [],

  register: function ( name, callback ) {
    if( 'undefined' == typeof( Hook.hooks[name] ) )
      Hook.hooks[name] = []
    Hook.hooks[name].push( callback )
  },

  call: function ( name, arguments ) {
    if( 'undefined' != typeof( Hook.hooks[name] ) )
      for( i = 0; i < Hook.hooks[name].length; ++i )
        if( true != Hook.hooks[name][i]( arguments ) ) { break; }
  }
}
```

Extensions can “get in line” for a hook by calling register with the hook name and callback.


```
Hook.register(
  'quit',
  function ( args ) {
    alert( 'Bye!' );
    return true;
  }
);
```

Core code (or even other extensions actually) can call hooks by using the call method, with name and an argument array (think argv). If a hook returns anything other than true, processing of the hook ceases.
```
Hook.call( 'quit', [ 'All Done' ] );```
To do useful things you have to set up the right arguments. Since objects are passed by reference in JavaScript, you can manipulate anything in the argument array from inside of your hook (or even add to the array if you want).

Obviously this is a simplified tool. All code is implicitly trusted, argument specification is non-existent, there is no prioritization (except for insertion order) and hooks are not guaranteed to run. But it works!

You can check out some basic usage code and test it out here.

Suggestions are welcome!



# hook.js Test

* * *

## hook.js Code

```
var Hook = {
  hooks: [],

  register: function ( name, callback ) {
    if( 'undefined' == typeof( Hook.hooks[name] ) )
      Hook.hooks[name] = []
    Hook.hooks[name].push( callback )
  },

  call: function ( name, arguments ) {
    if( 'undefined' != typeof( Hook.hooks[name] ) )
      for( i = 0; i < Hook.hooks[name].length; ++i )
        if( true != Hook.hooks[name][i]( arguments ) ) { break; }
  }
}
```

## test() Code

```
function test () {
  el = document.getElementById( 'test-area' );
  // Set up the hooks
  Hook.register( 'one', function ( args ) { el.innerHTML = el.innerHTML + 'One<br/>'; return true; } );
  Hook.register( 'one', function ( args ) { el.innerHTML = el.innerHTML + 'Two<br/>'; return true; } );
  Hook.register( 'one', function ( args ) { el.innerHTML = el.innerHTML + args[0]; args[0] = 'Six<br/>'; return true; } );
  Hook.register( 'one', function ( args ) { el.innerHTML = el.innerHTML + 'Four<br/>'; return false; } );
  Hook.register( 'one', function ( args ) { el.innerHTML = el.innerHTML + 'Five<br/>'; return true; } );

  // Set up the arguments
  arguments = [ 'Three<br/>' ];
  // Call the hooks
  Hook.call( 'one', [ 'Three<br/>' ] );
  // Prove the arguments were changed
  el.innerHTML = el.innerHTML + arguments[0];
}
```

## Expected

One  
Two  
Three  
Four  
Six  

## Result

One  
Two  
Three  
Four  
Six





---