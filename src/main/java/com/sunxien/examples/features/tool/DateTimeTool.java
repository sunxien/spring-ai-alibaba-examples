package com.sunxien.examples.features.tool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author sunxien
 * @date 2025/12/25
 * @since 1.0.0-SNAPSHOT
 */
@Slf4j
public class DateTimeTool {

    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * @param timeZone
     * @return String
     */
    @Tool(name = "get_current_date", description = "获取指定时区的当前日期，不包含时间")
    public String getCurrentDate(@ToolParam(description = "服务器的时区名称", required = false) String timeZone) {
        TimeZone tz = getLocalTimeZone(timeZone);
        String currentDate = YYYY_MM_DD.format(LocalDateTime.now().atZone(tz.toZoneId()));
        log.info("get current date: {} ({})", currentDate, tz.getID());
        return currentDate;
    }

    /**
     * @param timeZone
     * @return String
     */
    @Tool(name = "get_current_time", description = "获取指定时区的当前时间，不包含日期")
    public String getCurrentTime(@ToolParam(description = "服务器的时区名称", required = false) String timeZone) {
        TimeZone tz = getLocalTimeZone(timeZone);
        String currentTime = HH_MM_SS.format(LocalDateTime.now().atZone(tz.toZoneId()));
        log.info("get current time: {} ({})", currentTime, tz.getID());
        return currentTime;
    }

    /**
     * @param timeZone
     * @return String
     */
    @Tool(name = "get_current_datetime", description = "获取指定时区的当前日期时间")
    public String getCurrentDateTime(@ToolParam(description = "服务器的时区名称", required = false) String timeZone) {
        TimeZone tz = getLocalTimeZone(timeZone);
        String currentDateTime = YYYY_MM_DD_HH_MM_SS.format(LocalDateTime.now().atZone(tz.toZoneId()));
        log.info("get current datetime: {} ({})", currentDateTime, tz.getID());
        return currentDateTime;
    }

    /**
     * @param timeZone Input timezone string.
     * @return TimeZone
     */
    private static TimeZone getLocalTimeZone(String timeZone) {
        if (StringUtils.isBlank(timeZone)) {
            return TimeZone.getDefault();
        } else {
            return TimeZone.getTimeZone(timeZone);
        }
    }
}
