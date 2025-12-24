package com.sunxien.examples.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
@Data
@NoArgsConstructor
public class Output implements Serializable {


    @Serial
    private static final long serialVersionUID = 5577474013953730947L;

    private String agentName;
    private String content;

    public Output(String agentName, String content) {
        this.agentName = agentName;
        this.content = content;
    }
}
