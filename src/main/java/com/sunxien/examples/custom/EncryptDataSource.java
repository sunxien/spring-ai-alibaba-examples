package com.sunxien.examples.custom;

import com.sunxien.examples.utils.CodecUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
public final class EncryptDataSource extends HikariDataSource {

    public EncryptDataSource(){
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    /**
     * Usage:
     * -DsecretKey=xxx
     * @param encryptPassword the password
     */
    @Override
    public void setPassword(String encryptPassword) {
        // String secretKey = System.getProperty("secretKey");
        // checkArgument(StringUtils.isNotBlank(secretKey), "-DsecretKey is required");
        // super.setPassword(CodecUtils.decrypt(encryptPassword, secretKey));
        super.setPassword(encryptPassword);
    }
}
