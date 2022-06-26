package ru.spb.devclub.spring.web.context.request;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/getter/params")
@RequiredArgsConstructor
public class RequestParametersGetterController {

    private final RequestParametersGetter getter;

    @GetMapping
    public String getParams() {
        return getter.getParams();
    }

    @SneakyThrows
    @GetMapping("/async")
    public String getParamsAsync() {
        return getter.getParamsAsync().get();
    }

    @SneakyThrows
    @GetMapping("/async/with-context")
    public String getParamsAsyncWithContext() {
        return getter.getParamsAsyncWithContext().get();
    }

    @SneakyThrows
    @GetMapping("/executors")
    public String getParamsFromExecutor() {
        return getter.getParamsFromExecutor().get();
    }

    @SneakyThrows
    @GetMapping("/executors/with-context")
    public String getParamsFromExecutorWithContext() {
        return getter.getParamsFromExecutorWithContext().get();
    }

}
