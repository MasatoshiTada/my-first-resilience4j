package com.example.helloui;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloAnnotationService {

    private static final Logger logger = LoggerFactory.getLogger(HelloAnnotationService.class);

    private final RestTemplate restTemplate;

    public HelloAnnotationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "helloApi", fallbackMethod = "recoverHello")
    public Hello hello() {
        logger.info("Service hello() started.");
        String apiUrl = "http://localhost:8090/hello"; // URLは、本来はEurekaなどのService Discoveryで取得する
        Hello hello = restTemplate.getForObject(apiUrl, Hello.class);
        logger.info("Service hello() finished.");
        return hello;
    }

    private Hello recoverHello(Throwable throwable) {
        logger.error("Recover in Service : " + throwable.getClass().getName());
        return new Hello("default");
    }
}
