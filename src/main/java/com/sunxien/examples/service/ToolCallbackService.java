package com.sunxien.examples.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
@Service
public class ToolCallbackService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


}
