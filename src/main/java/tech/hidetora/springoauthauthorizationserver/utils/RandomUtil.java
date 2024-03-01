package tech.hidetora.springoauthauthorizationserver.utils;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

@NoArgsConstructor
public class RandomUtil {
    private static final int DEF_COUNT = 20;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateRandomAlphanumericString() {
        return RandomStringUtils.random(20, 0, 0, true, true, (char[])null, SECURE_RANDOM);
    }

    public static String generatePassword() {
        return generateRandomAlphanumericString();
    }

    public static String generateActivationKey() {
        return generateRandomAlphanumericString();
    }

    public static String generateResetKey() {
        return generateRandomAlphanumericString();
    }

    static {
        SECURE_RANDOM.nextBytes(new byte[64]);
    }
}
