package ru.spb.devclub.spring.web.context.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@ExtendWith(MockitoExtension.class)
public abstract class BaseRequestContextHolderTest {

    @Mock
    RequestAttributes attrs;

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(attrs);
    }

}
