Usage of Log4j

Jar:
commons-logging.jar
log4j.jar


1.Create a new log4j.properties file at src folder 

```
log4j.rootLogger=CONSOLE,stdout,logfile
 
#stdout controller
 
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
 
log4j.appender.stdout.Target=System.out
 
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
#output format
 
log4j.appender.stdout.layout.ConversionPattern=%d %p [%c]:%L - %m%n
 
# output filepath
 
log4j.appender.logfile=org.apache.log4j.RollingFileAppender
log4j.appender.logfile.File=D:/keyservice.log
log4j.appender.logfile.layout=org.apache.log4j.PatternLayout
log4j.appender.logfile.layout.ConversionPattern=%d %p [%c] - %m%n
```

2.Usage in your code
1)Import 
```
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
```
2)declare define of log
```
public static Log log = LogFactory.getLog(YourCurrentClassName.class);
```
3)log
```
package example;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class HelloWorld {
  public static Log log = LogFactory.getLog(HelloWorld.class);
  public static void main(String[] argv) {
             String test="sdibt";
            log.info("this is info:"+test);
            log.error("this is error:"+test);
            log.debug("this is debug:"+test);
  }
}
```
4)Then we can got log output in console like below
```
2018-05-10 10:46:29,566 INFO [example.HelloWorld]:10 - this is info:sdibt
2018-05-10 10:46:29,566 ERROR [example.HelloWorld]:11 - this is error:sdibt
2018-05-10 10:46:29,566 DEBUG [example.HelloWorld]:12 - this is debug:sdibt
 
Process finished with exit code 0
```
and log in the filepath you defined D:/keyservice.log