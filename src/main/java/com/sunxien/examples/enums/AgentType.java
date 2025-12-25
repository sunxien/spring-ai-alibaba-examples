package com.sunxien.examples.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum AgentType {


    CHAT_CLIENT("聊天客户端", ""),

    CHAT_AGENT("聊天代理", ""),


    /********************** Hospital *********************/

    GUIDE_EXPERT("分诊专家", ""),

    BLOOD_EXPERT("血液科专家", ""),

    SKIN_EXPERT("皮肤科专家", ""),

    SENSE_EXPERT("五官科专家", ""),

    HEART_EXPERT("心内科专家", ""),

    CHEST_EXPERT("胸外科专家", ""),

    BREATH_EXPERT("呼吸科专家", ""),

    INTER_EXPERT("综合内科", ""),

    TEST_EXPERT("血液检验专家", ""),

    REVIEWER("诊断评估", ""),

    SUMMARIZER("诊断总结", "");

    private final String agentName;
    private final String instruction;


}
