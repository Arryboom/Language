Example a class has a private value you want get but it doesn't have a get,set defination:

```
package org.iteye.bbjava.runtimeinformation;   
 
public class PrivateObject {   
 
   @SuppressWarnings("unused")
   private String privateString = null;   
 
   public PrivateObject(String privateString) {   
        this.privateString = privateString;   
    }   
}  
```

Use Java Reflection to get the value of privateString.
```
package org.iteye.bbjava.runtimeinformation;
 
import java.lang.reflect.Field;
 
public class Test {
	 public static void main(String []args) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException{
		  PrivateObject privateObject = new PrivateObject("The Private Value");
 
		  Field privateStringField = PrivateObject.class.
		              getDeclaredField("privateString");
 
		  privateStringField.setAccessible(true);
 
		  String fieldValue = (String) privateStringField.get(privateObject);
		  System.out.println("fieldValue = " + fieldValue);
	  }
}
```


Change the value of private String.

```
package org.iteye.bbjava.runtimeinformation;
 
import java.lang.reflect.Field;
 
public class Test {
	 public static void main(String []args) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException{
		  PrivateObject privateObject = new PrivateObject("The Private Value");
 
		  Field privateStringField = PrivateObject.class.
		              getDeclaredField("privateString");
 
		 
		  
		  privateStringField.setAccessible(true);
		  String fieldValue = (String) privateStringField.get(privateObject);
		  System.out.println("fieldValue = " + fieldValue);
 
		  
		  privateStringField.setAccessible(true);
		  privateStringField.set(privateObject, "As you see,privateString's value is changed!");
		  String fieldValue1 = (String) privateStringField.get(privateObject);
		  System.out.println("fieldValue = " + fieldValue1);
 
		  privateStringField.setAccessible(false);
		  privateStringField.set(privateObject, "As you see,privateString's value is changed!");
		  String fieldValue2 = (String) privateStringField.get(privateObject);
		  System.out.println("fieldValue = " + fieldValue2);
	  }
}
```