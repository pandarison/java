# 类 Class
* 类是有具有共同属性的许多个体的抽象(模版).

```
public class Person {

   int age;
   String name;
	
	void eat(){
	}
	
	void speak(){
	}
}
```

## 类中的变量
* 局部变量：在函数中定义的变量为局部变量。生命周期到函数结束为止。
* 成员变量：实例变量是在类内但是在方法外的变量。在创建该类的实例等时候被创建。生命周期到实例被销毁为止。
* 静态变量：含有static关键字的成员变量。在程序初始化的时候创建，全局只有一份该变量，生命周期到程序终止。

## 构造函数
```
public class Person {

	public Person(){
	}
	
	public Person(String name){
	}
}
```

## 创建一个类的对象(实例化一个类)
```
public class Person {
   String name;
   public Person(String name){
   		this.name = name;
   }
	
	void speak(){
		System.out.println("My name is " + name);
	}
	public static void main(String []args){
		Person a = new Person("Harry");
		
   }
}
```