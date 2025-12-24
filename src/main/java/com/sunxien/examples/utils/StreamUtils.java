package com.sunxien.examples.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
public final class StreamUtils {

    private static final String TWO_NEW_LINES = "\n\n";

    private static final String OPEN_THINK = "<think>";
    private static final String CLOSE_THINK = "</think>";

    private StreamUtils() {
    }

    /**
     * Process <think>...</think> and final result.
     *
     * @param reasoningContentObj Input reasoning content
     * @param text                Input final result
     * @param finishReasonObj     Finish token
     * @return String Append reasoning content to the final result
     */
    public static String processContent(Object reasoningContentObj, String text, Object finishReasonObj) {
        String safetyText = text == null ? "" : text;
        String reasoningContent = reasoningContentObj == null ? "" : reasoningContentObj.toString();
        // The first token from LLM is always <EMPTY>
        if (StringUtils.isEmpty(reasoningContent) && StringUtils.isEmpty(safetyText)
                && finishReasonObj != null && StringUtils.isBlank(finishReasonObj.toString())) {
            reasoningContent = OPEN_THINK + reasoningContent;
        }
        // The token between reasoning content and content is always contain "\n\n"
        if (StringUtils.isBlank(reasoningContent) && TWO_NEW_LINES.equals(safetyText)
                && finishReasonObj != null && StringUtils.isBlank(finishReasonObj.toString())) {
            reasoningContent = reasoningContent + CLOSE_THINK;
        }
        return reasoningContent + safetyText;
    }
}
