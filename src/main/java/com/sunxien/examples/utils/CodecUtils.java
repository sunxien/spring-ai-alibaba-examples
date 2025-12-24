package com.sunxien.examples.utils;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Locale;

import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

/**
 * @author sunxien
 * @date 2025/12/24
 * @since 1.0.0-SNAPSHOT
 */
public final class CodecUtils {

    private CodecUtils() {
    }

    private static final String ALGO_NAME = "SM4";
    private static final String ALGO_ECB_PKCS5PADDING = "SM4/ECB/PKCS5Padding";
    private static final int DEFAULT_KEY_SIZE = 128;

    static {
        if (Security.getProvider(PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * Generate the secret key
     *
     * @return String The secret key
     * @throws Exception
     */
    public static String generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGO_NAME, PROVIDER_NAME);
        kg.init(DEFAULT_KEY_SIZE, new SecureRandom());
        return Hex.encodeHexString(kg.generateKey().getEncoded()).toUpperCase(Locale.getDefault());
    }

    /**
     * Encrypt the plain password string.
     *
     * @param plainString The plain password string
     * @param key         The secret key
     * @return String The encrypted password string
     */
    public static String encrypt(String plainString, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Hex.decodeHex(key), ALGO_NAME);
            Cipher cipher = Cipher.getInstance(ALGO_ECB_PKCS5PADDING, PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] cypherBytes = cipher.doFinal(plainString.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cypherBytes);
        } catch (Exception ex) {
            throw new SecurityException("Encrypt failed.", ex);
        }
    }

    /**
     * Decrypt secret password string
     *
     * @param cipherString The cipher password
     * @param key          The secret key
     * @return String The plain password string
     */
    public static String decrypt(String cipherString, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Hex.decodeHex(key), ALGO_NAME);
            Cipher cipher = Cipher.getInstance(ALGO_ECB_PKCS5PADDING, PROVIDER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] cipherByets = cipher.doFinal(Base64.getDecoder().decode(cipherString));
            return new String(cipherByets, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new SecurityException("Decrypt failed.", ex);
        }
    }
}
