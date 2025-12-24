package com.sunxien.examples.configure;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
@Data
@NoArgsConstructor
public class ModelConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -6691101357939601698L;

    private String modelName;

    private String provider;

    private String baseUrl;

    private String apiKey;

    private boolean enableThinking;

    private double temperature;

    private double topK;

    private double topP;



}
