package com.sunxien.examples.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
@RestController
public class HelloController {

    /**
     * @param name
     * @return String
     */
    @GetMapping("/api/v1/hello/{name}")
    public String hello(@PathVariable(name = "name") String name) {
        log.info("request hello api success");
        return "Hello, " + name;
    }
}
