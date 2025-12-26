package com.sunxien.examples.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * @author sunxien
 * @date 2025/12/26
 * @since 1.0.0-SNAPSHOT
 */
public class Benchmark {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test?useSSL=false" +
            "&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&autoReconnect=true" +
            "&failOverReadOnly=false&allowMultiQueries=true&rewriteBatchStatements=true";

    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    private static final int MAX_EXECUTE_TIMES = 1000;

    public static Connection conn;

    static {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("get connection success. elapsed: " + stopwatch);
        } catch (Exception ex) {
            throw new RuntimeException("get connection failed.", ex);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<LinkedHashMap<String, Object>> sqls = loadSqls();
        for (LinkedHashMap<String, Object> temp : sqls) {
            System.out.println("SQL:" + temp.get("id") + " 开始" + MAX_EXECUTE_TIMES + "次测试并记录耗时...");
            Sql sql = new Sql(((int) temp.get("id")), ((String) temp.get("sql")));
            // 执行测试并记录耗时
            List<Long> durations = recordExecutionTimes(sql, MAX_EXECUTE_TIMES);
            // 打印统计报告
            printStatisticsReport(durations);
            System.out.println("\n" + Strings.repeat("-", 80) + "\n");
        }
    }

    /**
     * 记录方法执行耗时
     *
     * @param executionTimes 执行次数
     * @return 耗时列表（单位：毫秒）
     */
    public static List<Long> recordExecutionTimes(Sql sql, int executionTimes) {
        List<Long> durations = new ArrayList<>(executionTimes);
        for (int i = 0; i < executionTimes; i++) {
            long startTime = System.nanoTime();
            targetMethod(sql);
            long endTime = System.nanoTime();
            // 纳秒转毫秒
            long duration = (endTime - startTime) / 1_000_000;
            durations.add(duration);
        }
        return durations;
    }

    private static List<LinkedHashMap<String, Object>> loadSqls() {
        List<Sql> sqls = new ArrayList<>();
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream("benchmark.yaml")) {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            return objectMapper.readValue(is, sqls.getClass());
        } catch (Exception ex) {
            throw new RuntimeException("load benchmark.yaml failed.", ex);
        }
    }

    /**
     * 模拟要测试的方法
     */
    public static void targetMethod(Sql sql) {
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql.sql)) {
                while (rs.next()) {
                    // ignore client process.
                }
            }
        } catch (Exception e) {
            System.err.println("execute SQL: " + sql.id + " failed. error: " + e.getMessage());
        }
    }

    /**
     * 打印统计报告
     *
     * @param durations
     */
    public static void printStatisticsReport(List<Long> durations) {
        System.out.println("\n========== 执行耗时统计报告 ==========");
        System.out.println("总执行次数: " + durations.size());

        // 基本统计
        long min = Collections.min(durations);
        long max = Collections.max(durations);
        double average = durations.stream().mapToLong(Long::longValue).average().orElse(0);
        System.out.printf("最短耗时: %d ms%n", min);
        System.out.printf("最长耗时: %d ms%n", max);
        System.out.printf("平均耗时: %.2f ms%n", average);

        // 计算百分位
        double[] percentiles = {50, 75, 90, 95, 99, 99.9};
        Map<Double, Long> percentileResults = calculatePercentiles(durations, percentiles);

        System.out.println("\n百分位统计:");
        for (Map.Entry<Double, Long> entry : percentileResults.entrySet()) {
            System.out.printf("  P%.1f: %3d ms%n", entry.getKey(), entry.getValue());
        }

        // 计算阈值百分比
        long[] thresholds = {5, 10, 20, 50, 100};
        Map<Long, Double> thresholdResults = calculateThresholdPercentages(durations, thresholds);

        System.out.println("\n耗时分布（低于阈值的百分比）:");
        for (Map.Entry<Long, Double> entry : thresholdResults.entrySet()) {
            System.out.printf("  ≤%3d ms: %6.2f%%%n", entry.getKey(), entry.getValue());
        }

        // 生成直方图
        System.out.println("\n耗时分布直方图:");
        generateHistogram(durations);
    }

    /**
     * 统计低于特定阈值的耗时百分比
     *
     * @param durations  耗时列表
     * @param thresholds 阈值数组，如 [5, 10, 20, 50, 100]
     * @return 阈值统计结果
     */
    public static Map<Long, Double> calculateThresholdPercentages(List<Long> durations, long[] thresholds) {
        Map<Long, Double> result = new LinkedHashMap<>(thresholds.length);
        for (long threshold : thresholds) {
            long countBelowThreshold = durations.stream().filter(duration -> duration <= threshold).count();
            double percentage = (double) countBelowThreshold / durations.size() * 100;
            result.put(threshold, percentage);
        }
        return result;
    }

    /**
     * 计算百分位统计
     *
     * @param durations   耗时列表
     * @param percentiles 要计算的百分位数组，如 [50, 90, 95, 99, 99.9]
     * @return 百分位统计结果
     */
    public static Map<Double, Long> calculatePercentiles(List<Long> durations, double[] percentiles) {
        // 先排序
        List<Long> sortedDurations = new ArrayList<>(durations);
        Collections.sort(sortedDurations);

        Map<Double, Long> result = new LinkedHashMap<>(percentiles.length);
        for (double percentile : percentiles) {
            if (percentile < 0 || percentile > 100) {
                throw new IllegalArgumentException("百分位必须在0-100之间");
            }

            double position = (percentile / 100.0) * (sortedDurations.size() - 1);
            int index = (int) Math.floor(position);
            double fraction = position - index;

            long percentileValue;
            if (index + 1 < sortedDurations.size()) {
                // 线性插值
                percentileValue = (long) (sortedDurations.get(index) * (1 - fraction) + sortedDurations.get(index + 1) * fraction);
            } else {
                percentileValue = sortedDurations.get(index);
            }
            result.put(percentile, percentileValue);
        }
        return result;
    }

    /**
     * 生成简单的文本直方图
     */
    private static void generateHistogram(List<Long> durations) {
        Map<String, Integer> histogram = new TreeMap<>();
        // 定义区间
        long[] ranges = {0, 5, 10, 20, 50, 100, 200, 500, Long.MAX_VALUE};

        for (int i = 0; i < ranges.length - 1; i++) {
            long lower = ranges[i];
            long upper = ranges[i + 1];
            String rangeKey;
            if (upper == Long.MAX_VALUE) {
                rangeKey = String.format("%4d+ ms", lower);
            } else if (i == 0) {
                rangeKey = String.format("%4d ms", upper);
            } else {
                rangeKey = String.format("%4d-%3d ms", lower, upper);
            }

            final long lowerBound = lower;
            final long upperBound = upper;
            long count = durations.stream().filter(d -> d > lowerBound && d <= upperBound).count();
            histogram.put(rangeKey, (int) count);
        }

        // 找到最大值用于缩放
        int maxCount = histogram.values().stream().max(Integer::compare).orElse(1);

        // 打印直方图
        for (Map.Entry<String, Integer> entry : histogram.entrySet()) {
            int barLength = (int) ((double) entry.getValue() / maxCount * 50);
            String bar = "█".repeat(Math.max(0, barLength));
            System.out.printf("  %-15s: %3d次 %s%n", entry.getKey(), entry.getValue(), bar);
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sql implements Serializable {

        @Serial
        private static final long serialVersionUID = 8592841369971657118L;

        private int id;
        private String sql;
    }
}
