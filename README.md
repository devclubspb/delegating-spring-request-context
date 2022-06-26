# delegating-spring-request-context

[![Maven Central](https://img.shields.io/maven-central/v/ru.spb.devclub/delegating-spring-request-context.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22ru.spb.devclub%22%20AND%20a:%22delegating-spring-request-context%22)
[![Javadocs](https://www.javadoc.io/badge/ru.spb.devclub/delegating-spring-request-context.svg)](https://www.javadoc.io/doc/ru.spb.devclub/delegating-spring-request-context)
[![GitHub](https://img.shields.io/github/license/devclubspb/delegating-spring-request-context?style=flat&&color=informational)](LICENSE)

Delegating
[RequestContextHolder](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/request/RequestContextHolder.html)
like
[SecurityContextHolder](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/context/SecurityContextHolder.html)
in
[org.springframework.security.concurrent](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/concurrent/package-summary.html)
and
[org.springframework.security.task](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/task/package-summary.html)
.

Using
[RequestContextHolder](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/request/RequestContextHolder.html)
in other threads:

```java
@Bean("asyncTaskExecutorWithRequestContext")
public AsyncTaskExecutor asyncTaskExecutorWithRequestContext() {
    SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("withRequestContext-");
    return new DelegatingRequestContextAsyncTaskExecutor(executor);
}

@Async("asyncTaskExecutorWithRequestContext")
public void runAsync() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    assert attributes != null;
}
```

See full code in [demo](demo).

## Install

### Gradle

```groovy

implementation 'ru.spb.devclub:delegating-spring-request-context:1.0'
```

### Maven

```xml

<dependency>
    <groupId>ru.spb.devclub</groupId>
    <artifactId>delegating-spring-request-context</artifactId>
    <version>1.0</version>
</dependency>
```

## License

This project is [licensed](LICENSE) under [MIT License](https://opensource.org/licenses/MIT).
