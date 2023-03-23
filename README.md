# spring-native-issues

Project demonstrates a problem that only occurrs when using Spring Native.

The problem is caused by the following JPA repository method:

```java
public interface SampleRepository extends JpaRepository<SampleEntity, Integer> {
    Stream<SampleEntity> findAllByOrderByName();
    ...
```

and its usage in a controller:

```java
    @GetMapping("/samples/first")
    @Transactional(readOnly = true)
    public SampleEntity first() {
        return repository.findAllByOrderByName().findFirst().orElseThrow();
```

The `findFirst()` invocation in the code above causes `ClassCastException` exception.

## Steps to reproduce

1. Build Spring Native image:

```shell
./gradlew clean bootBuildImage
```

2. Run appplication:

```shell
docker run -p 8080:8080 docker.io/library/sni-stream-first:0.0.1-SNAPSHOT
```

3. Invoke REST API:

```shell
curl localhost:8080/samples/first
```

An error is thrown:

```
2023-03-23T10:57:46.504Z ERROR 1 --- [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: java.lang.ClassCastException: com.example.demo.entity.SampleEntity cannot be cast to java.lang.Object[]] with root cause

java.lang.ClassCastException: com.example.demo.entity.SampleEntity cannot be cast to java.lang.Object[]
	at org.springframework.data.jpa.provider.PersistenceProvider$HibernateScrollableResultsIterator.next(PersistenceProvider.java:394) ~[na:na]
	at java.base@17.0.6/java.util.Spliterators$IteratorSpliterator.tryAdvance(Spliterators.java:1856) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150) ~[na:na]
	at java.base@17.0.6/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:647) ~[com.example.demo.DemoApplication:na]
	at com.example.demo.controller.SampleController.first(SampleController.java:30) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.lang.reflect.Method.invoke(Method.java:568) ~[com.example.demo.DemoApplication:na]
	at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:343) ~[na:na]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:196) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:750) ~[na:na]
	at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123) ~[na:na]
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:390) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:750) ~[na:na]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:702) ~[na:na]
	at com.example.demo.controller.SampleController$$SpringCGLIB$$0.first(<generated>) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.lang.reflect.Method.invoke(Method.java:568) ~[com.example.demo.DemoApplication:na]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:207) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:152) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:884) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1081) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:974) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1011) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:903) ~[com.example.demo.DemoApplication:6.0.6]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:705) ~[com.example.demo.DemoApplication:6.0]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885) ~[com.example.demo.DemoApplication:6.0.6]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:814) ~[com.example.demo.DemoApplication:6.0]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:223) ~[na:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[na:na]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[com.example.demo.DemoApplication:10.1.5]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[na:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[na:na]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[com.example.demo.DemoApplication:6.0.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[na:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[na:na]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[com.example.demo.DemoApplication:6.0.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[na:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[na:na]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[com.example.demo.DemoApplication:6.0.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[na:na]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[na:na]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:177) ~[na:na]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[na:na]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[com.example.demo.DemoApplication:10.1.5]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:119) ~[na:na]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[com.example.demo.DemoApplication:10.1.5]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[na:na]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[na:na]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:400) ~[na:na]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[com.example.demo.DemoApplication:10.1.5]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:859) ~[na:na]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1734) ~[na:na]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[com.example.demo.DemoApplication:10.1.5]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[na:na]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[na:na]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[na:na]
	at java.base@17.0.6/java.lang.Thread.run(Thread.java:833) ~[com.example.demo.DemoApplication:na]
	at com.oracle.svm.core.thread.PlatformThreads.threadStartRoutine(PlatformThreads.java:775) ~[com.example.demo.DemoApplication:na]
	at com.oracle.svm.core.posix.thread.PosixPlatformThreads.pthreadStartRoutine(PosixPlatformThreads.java:203) ~[na:na]
```

## Known workarounds

### Avoid using `Stream<>` as a repository method's return value.

Problem is gone when changed the repository to the following:

```java
public interface SampleRepository extends JpaRepository<SampleEntity, Integer> {
    List<SampleEntity> findAllByOrderByName();
    ...
```
