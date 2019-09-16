package com.example.helloui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloAnnotationController {

    private static final Logger logger = LoggerFactory.getLogger(HelloAnnotationController.class);

    private final HelloAnnotationService helloAnnotationService;

    public HelloAnnotationController(HelloAnnotationService helloAnnotationService) {
        this.helloAnnotationService = helloAnnotationService;
    }

    @GetMapping("/annotation")
    public String hello(Model model) {
        logger.info("Controller hello() started.");
        HelloResource helloResource = helloAnnotationService.hello();
        model.addAttribute("hello", helloResource);
        logger.info("Controller hello() finished.");
        return "index";
    }
}
