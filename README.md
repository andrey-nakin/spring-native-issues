# spring-native-issues

This repository demonstrates an error that occurrs when using `@PropertySource` components with a custom factory and
Spring Native.

The problem is caused by a custom `factory` definition in `SampleDataProperties`:

```java

@Component
@ConfigurationProperties("sample-data")
@PropertySource(
        value = "classpath:sample-data.yaml", 
        factory = YamlPropertySourceFactory.class)
public class SampleDataProperties {
    ...
```

When the application is built into a Spring Native image and runs, a `NoSuchMethodException` is thrown because of
absent `YamlPropertySourceFactory()` constructor.

## Steps to reproduce

1. Build Spring Native docker image:

```shell
./gradlew clean bootBuildImage 
```

2. Run application using Docker:

```shell
docker run docker.io/library/sni-property-source:0.0.1-SNAPSHOT
```

An error occurred:

```
2023-03-23T09:49:07.586Z ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed

java.lang.IllegalStateException: Failed to instantiate class com.example.demo.config.YamlPropertySourceFactory
	at org.springframework.core.io.support.PropertySourceProcessor.instantiateClass(PropertySourceProcessor.java:146) ~[na:na]
	at org.springframework.core.io.support.PropertySourceProcessor.processPropertySource(PropertySourceProcessor.java:81) ~[na:na]
	at com.example.demo.DemoApplication__ApplicationContextInitializer.processPropertySources(DemoApplication__ApplicationContextInitializer.java:46) ~[com.example.demo.DemoApplication:na]
	at com.example.demo.DemoApplication__ApplicationContextInitializer.initialize(DemoApplication__ApplicationContextInitializer.java:33) ~[com.example.demo.DemoApplication:na]
	at com.example.demo.DemoApplication__ApplicationContextInitializer.initialize(DemoApplication__ApplicationContextInitializer.java:27) ~[com.example.demo.DemoApplication:na]
	at org.springframework.context.aot.AotApplicationContextInitializer.initialize(AotApplicationContextInitializer.java:72) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.context.aot.AotApplicationContextInitializer.lambda$forInitializerClasses$0(AotApplicationContextInitializer.java:61) ~[com.example.demo.DemoApplication:6.0.6]
	at org.springframework.boot.SpringApplication.applyInitializers(SpringApplication.java:605) ~[com.example.demo.DemoApplication:3.0.4]
	at org.springframework.boot.SpringApplication.prepareContext(SpringApplication.java:385) ~[com.example.demo.DemoApplication:3.0.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:309) ~[com.example.demo.DemoApplication:3.0.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1304) ~[com.example.demo.DemoApplication:3.0.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1293) ~[com.example.demo.DemoApplication:3.0.4]
	at com.example.demo.DemoApplication.main(DemoApplication.java:10) ~[com.example.demo.DemoApplication:na]
Caused by: java.lang.NoSuchMethodException: com.example.demo.config.YamlPropertySourceFactory.<init>()
	at java.base@17.0.6/java.lang.Class.getConstructor0(DynamicHub.java:3585) ~[com.example.demo.DemoApplication:na]
	at java.base@17.0.6/java.lang.Class.getDeclaredConstructor(DynamicHub.java:2754) ~[com.example.demo.DemoApplication:na]
	at org.springframework.core.io.support.PropertySourceProcessor.instantiateClass(PropertySourceProcessor.java:141) ~[na:na]
	... 12 common frames omitted
```

## Make sure the application works under JVM

1. Build application:

```shell
./gradlew clean build 
```

2. Run it:

```shell
java -jar build/libs/sni-property-source-0.0.1-SNAPSHOT.jar  
```

Application starts and works well. Test it (from another console terminal):

````shell
curl localhost:8080/samples
````

Response:

```json
[
  {
    "id": "0643c6eb-e9f3-4537-83fa-69f7f3c807dc",
    "name": "a",
    "val": "b"
  },
  {
    "id": "f95c9c75-91ed-4114-a880-e5a401568fb0",
    "name": "c",
    "val": "d"
  }
]
```