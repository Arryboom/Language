#go 调用windows dll 的三种方法

参考：https://blog.csdn.net/qq_39584315/article/details/81287669

大部分代码参考：https://studygolang.com/articles/2712

第三种方法是从Go\src\internal\syscall\windows\sysdll源码中找到的，三种方法的具体区别还不是很明晰，

初步判断：lazy应该是相当于动态库，其余两种直接把库加载到内存。

```
package main
 
import (
    "fmt"
    "syscall"
    "time"
    "unsafe"
)
 
const (
    MB_OK                = 0x00000000
    MB_OKCANCEL          = 0x00000001
    MB_ABORTRETRYIGNORE  = 0x00000002
    MB_YESNOCANCEL       = 0x00000003
    MB_YESNO             = 0x00000004
    MB_RETRYCANCEL       = 0x00000005
    MB_CANCELTRYCONTINUE = 0x00000006
    MB_ICONHAND          = 0x00000010
    MB_ICONQUESTION      = 0x00000020
    MB_ICONEXCLAMATION   = 0x00000030
    MB_ICONASTERISK      = 0x00000040
    MB_USERICON          = 0x00000080
    MB_ICONWARNING       = MB_ICONEXCLAMATION
    MB_ICONERROR         = MB_ICONHAND
    MB_ICONINFORMATION   = MB_ICONASTERISK
    MB_ICONSTOP          = MB_ICONHAND
 
    MB_DEFBUTTON1 = 0x00000000
    MB_DEFBUTTON2 = 0x00000100
    MB_DEFBUTTON3 = 0x00000200
    MB_DEFBUTTON4 = 0x00000300
)
 
func abort(funcname string, err syscall.Errno) {
    panic(funcname + " failed: " + err.Error())
}
 
var (
    user32, _     = syscall.LoadLibrary("user32.dll")
    messageBox, _ = syscall.GetProcAddress(user32, "MessageBoxW")
)
 
func IntPtr(n int) uintptr {
    return uintptr(n)
}
 
func StrPtr(s string) uintptr {
    return uintptr(unsafe.Pointer(syscall.StringToUTF16Ptr(s)))
}
 
func MessageBox(caption, text string, style uintptr) (result int) {
    ret, _, callErr := syscall.Syscall9(messageBox,
        ,
        ,
        StrPtr(text),
        StrPtr(caption),
        style,
        , , , , )
     {
        abort("Call MessageBox", callErr)
    }
    result = int(ret)
    return
}
 
//func GetModuleHandle() (handle uintptr) {
//    if ret, _, callErr := syscall.Syscall(getModuleHandle, 0, 0, 0, 0); callErr != 0 {
//        abort("Call GetModuleHandle", callErr)
//    } else {
//        handle = ret
//    }
//    return
//}
 
// windows下的第二种DLL方法调用
func ShowMessage2(title, text string) {
    user32 := syscall.NewLazyDLL("user32.dll")
    MessageBoxW := user32.NewProc("MessageBoxW")
    MessageBoxW.Call(IntPtr(), StrPtr(text), StrPtr(title), IntPtr())
}
 
// windows下的第三种DLL方法调用
func ShowMessage3(title, text string) {
    user32, _ := syscall.LoadDLL("user32.dll")
    MessageBoxW, _ := user32.FindProc("MessageBoxW")
    MessageBoxW.Call(IntPtr(), StrPtr(text), StrPtr(title), IntPtr())
}
 
func main() {
    defer syscall.FreeLibrary(user32)
 
    num := MessageBox("Done Title", "This test is Done.", MB_YESNOCANCEL)
    fmt.Printf("Get Retrun Value Before MessageBox Invoked: %d\n", num)
    ShowMessage2("windows下的另一种DLL方法调用", "HELLO !")
 
    ShowMessage3("windows下的第三种DLL方法调用", "lyslyslys !")
 
    time.Sleep( * time.Second)
}
 
func init() {
    fmt.Print("Starting Up\n")
}
```


#引用包

Go的package不支持相对路径，都是从$GOPATH/src下一层一层找的。

goland报错 can't load package: package main: found packages language (coverage.go) and main (gen.go)

>因为在同一个文件夹下，你的*.go文件，可能存在 package *，声明了不同的包。同一目录下，包名必须唯一。


#go map

- go中的map是hash表的一个引用，类型写为：map[key]value，其中的key, value分别对应一种数据类型，如map[string]string   

- 要求所有的key的数据类型相同，所有value数据类型相同(注：key与value可以有不同的数据类型)

- map中的每个key在keys的集合中是唯一的，而且需要支持 == or != 操作

- key的常用类型：int, rune, string, 结构体(每个元素需要支持 == or != 操作), 指针, 基于这些类型自定义的类型


```
    // m0 可以, key类型为string, 支持 == 比较操作
    {
        var m0 map[string]string // 定义map类型变量m0，key的类型为string，value的类型string
        fmt.Println(m0)
    }

    // m1 不可以, []byte是slice，不支持 == != 操作，不可以作为map key的数据类型
    {
        //var m1 map[[]byte]string // 报错： invalid map key type []byte
        //fmt.Println(m1)

        // 准确说slice类型只能与nil比较，其他的都不可以，可以通过如下测试：
        // var b1,b2 []byte
        // fmt.Println(b1==b2) // 报错： invalid operation: b1 == b2 (slice can only be compared to nil)
    }

    // m2 可以, interface{}类型可以作为key，但是需要加入的key的类型是可以比较的
    {
        var m2 map[interface{}]string
        m2 = make(map[interface{}]string)
        //m2[[]byte("k2")]="v2" // panic: runtime error: hash of unhashable type []uint8
        m2[123] = "123"
        m2[12.3] = "123"
        fmt.Println(m2)
    }

    // m3 可以， 数组支持比较
    {
        a3 := [3]int{1, 2, 3}
        var m3 map[[3]int]string
        m3 = make(map[[3]int]string)
        m3[a3] = "m3"
        fmt.Println(m3)
    }

    // m4 可以，book1里面的元素都是支持== !=
    {
        type book1 struct {
            name string
        }
        var m4 map[book1]string
        fmt.Println(m4)
    }

    // m5 不可以, text元素类型为[]byte, 不满足key的要求
    {
        // type book2 struct {
        //  name string
        //  text []byte //没有这个就可以
        // }
        //var m5 map[book2]string //invalid map key type book2
        //fmt.Println(m5)
    }
```

- map创建

 - 两种创建的方式：一是通过字面值；二是通过make函数  

```  

    // 1 字面值
    {
        m1 := map[string]string{
            "m1": "v1", // 定义时指定的初始key/value, 后面可以继续添加
        }
        _ = m1

    }

    // 2 使用make函数
    {
        m2 := make(map[string]string) // 创建时，里面不含元素，元素都需要后续添加
        m2["m2"] = "v2"               // 添加元素
        _ = m2

    }

    // 定义一个空的map
    {
        m3 := map[string]string{}
        m4 := make(map[string]string)
        _ = m3
        _ = m4
    }

```  

 - map增删改查
```
    // 创建
    m := map[string]string{
        "a": "va",
        "b": "vb",
    }
    fmt.Println(len(m)) // len(m) 获得m中key/value对的个数

    // 增加，修改
    {
        // k不存在为增加，k存在为修改
        m["c"] = ""
        m["c"] = "11"                      // 重复增加(key相同)，使用新的值覆盖
        fmt.Printf("%#v %#v\n", m, len(m)) // map[string]string{"a":"va", "b":"vb", "c":"11"} 3
    }

    // 查
    {
        // v := m[k] // 从m中取键k对应的值给v，如果k在m中不存在，则将value类型的零值赋值给v
        // v, ok := m[k] // 从m中取键k对应的值给v，如果k存在，ok=true,如果k不存在，将value类型的零值赋值给v同时ok=false
        // 查1 - 元素不存在
        v1 := m["x"] //
        v2, ok2 := m["x"]
        fmt.Printf("%#v %#v %#v\n", v1, v2, ok2) // "" "" false

        // 查2 - 元素存在
        v3 := m["a"]
        v4, ok4 := m["a"]
        fmt.Printf("%#v %#v %#v\n", v3, v4, ok4) //"va" "va" true
    }

    // 删， 使用内置函数删除k/v对
    {
        // delete(m, k) 将k以及k对应的v从m中删掉；如果k不在m中，不执行任何操作
        delete(m, "x")                     // 删除不存在的key,原m不影响
        delete(m, "a")                     // 删除存在的key
        fmt.Printf("%#v %#v\n", m, len(m)) // map[string]string{"b":"vb", "c":"11"} 2
        delete(m, "a")                     // 重复删除不报错,m无影响
        fmt.Printf("%#v %#v\n", m, len(m)) /// map[string]string{"b":"vb", "c":"11"} 2
    }

```

 - map遍历

  - 遍历的顺序是随机的
  - 使用for range遍历的时候，k,v使用的同一块内存，这也是容易出现错误的地方

```
    m := map[string]int{
        "a": 1,
        "b": 2,
    }
    for k, v := range m {
        fmt.Printf("k:[%v].v:[%v]\n", k, v) // 输出k,v值
    }
```





#golang调试

>https://www.cnblogs.com/li-peng/p/8522592.html




#GO语言规范

- 数据类型xxx  
http://docscn.studygolang.com/ref/spec
- 库  
http://docscn.studygolang.com/pkg/
- go 参数  
http://docscn.studygolang.com/cmd/go/