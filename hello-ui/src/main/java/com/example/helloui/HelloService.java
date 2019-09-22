package com.example.helloui;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloService.class);

    private final RestTemplate restTemplate;
    private final CircuitBreaker circuitBreaker;

    public HelloService(RestTemplate restTemplate, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.restTemplate = restTemplate;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("helloApi");
    }

    public Hello hello() {
        logger.info("Service hello() started.");
        String apiUrl = "http://localhost:8090/hello"; // URLは、本来はEurekaなどのService Discoveryで取得する
        CheckedFunction0<Hello> decorateSupplier =
                circuitBreaker.decorateCheckedSupplier(
                        () -> restTemplate.getForObject(apiUrl, Hello.class));
        Try<Hello> result = Try.of(decorateSupplier)
                .recover(this::recoverHello); // 失敗時の代替処理
        Hello hello = result.get();
        logger.info("Service hello() finished. Circuit = {}", circuitBreaker.getState());
        return hello;
    }

    private Hello recoverHello(Throwable throwable) {
        logger.error("Recover in Service : " + throwable.getClass().getName());
        return new Hello("default");
    }
}
