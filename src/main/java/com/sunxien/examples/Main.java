package com.sunxien.examples;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.sunxien.examples"})
public class Main {

    /**
     *
     * Restful API List:
     * <pre>
     * 1. http://localhost:8088/api/v1/hello/{name}
     * 2. http://localhost:8088/api/v1/chat/client/sync/{question}
     * 3. http://localhost:8088/api/v1/chat/client/stream/{question}
     * 4. http://localhost:8088/api/v1/chat/agent/stream/{question}
     * </pre>
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            SpringApplication.run(Main.class);
            log.info("Spring AI Alibaba Application Started. Elapsed: {}", stopwatch);
        } catch (Exception ex) {
            log.info("Spring AI Alibaba Application Start Failed.", ex);
        }
    }
}
