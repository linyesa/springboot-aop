# **AOP简单使用**

springboot-aop启动依赖

```java
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
```

切入点

注解@Pointcut用来声明把切片往哪里切，这里是往controller层下所有方法中切

例如定义切入点[表达式](https://so.csdn.net/so/search?q=表达式&spm=1001.2101.3001.7020) execution (* com.sample.service.impl..***. \***(..))

execution()是最常用的切点函数，其语法如下所示：

整个表达式可以分为五个部分：

1、execution(): 表达式主体。

2、第一个*号：表示返回类型， *号表示所有的类型。

3、包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包，com.sample.service.impl包、子孙包下所有类的方法。

4、第二个\*号：表示类名，\*号表示所有的类。

5、\*(..):最后这个星号表示方法名，*星号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数



@Before
标识一个前置增强方法，相当于BeforeAdvice的功能

@AfterReturning

后置增强，相当于AfterReturningAdvice，方法执行return退出时执行

@AfterThrowing

异常抛出增强，相当于ThrowsAdvice

@After

final增强，不管是抛出异常或者正常退出都会执行

@Around

环绕增强，相当于MethodInterceptor

----------------------------------------------
各方法参数说明：
除了@Around外，每个方法里都可以加或者不加参数JoinPoint，如果有用JoinPoint的地方就加，不加也可以，JoinPoint里包含了类名、被切面的方法名，参数等属性，可供读取使用。@Around参数必须为ProceedingJoinPoint，pjp.proceed相应于执行被切面的方法。@AfterReturning方法里，可以加returning = “XXX”，XXX即为在controller里方法的返回值，本例中的返回值是“first controller”。@AfterThrowing方法里，可以加throwing = "XXX"，供读取异常信息，

```java
@Aspect
@Component
public class After {
    @Pointcut("execution(public * com.example.aop.controller.*.*(..))")
    public void webLog(){};
    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        System.out.println("URL : " + request.getRequestURL().toString());
        System.out.println("HTTP_METHOD : " + request.getMethod());
        System.out.println("IP : " + request.getRemoteAddr());
        System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("方法的返回值 : " + ret);
    }

    //后置异常通知
    @AfterThrowing("webLog()")
    public void throwss(JoinPoint jp){
        System.out.println("方法异常时执行.....");
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
    @org.aspectj.lang.annotation.After("webLog()")
    public void after(JoinPoint jp){
        System.out.println("方法最后执行.....");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) {
        System.out.println("方法环绕start.....");
        try {
            Object o =  pjp.proceed();
            System.out.println("方法环绕proceed，结果是 :" + o);
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}

```

先访问hello1再访问hello结果如下

![image-20220517094359133](C:\Users\tqn\AppData\Roaming\Typora\typora-user-images\image-20220517094359133.png)
https://github.com/linyesa/springboot-aop/blob/main/1.PNG
