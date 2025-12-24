package com.sunxien.examples.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
public class CodecUtilsTest {

    /**
     *
     */
    @Test
    public void test() {
        try {
            String secretKey = CodecUtils.generateKey();
            log.info("Secret Key: {}", secretKey);

            String encryptPassword = CodecUtils.encrypt("root", secretKey);
            log.info("Encrypt Password: {}", encryptPassword);

            String plainPassword = CodecUtils.decrypt(encryptPassword, secretKey);
            log.info("Plain Password: {}", plainPassword);
        } catch (Exception ex) {
            log.error("CodecUtils run failed.", ex);
        }
    }
}
