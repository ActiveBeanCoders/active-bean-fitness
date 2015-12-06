package com.activebeancoders.fitness.security.infrastructure;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

/**
 * @author Dan Barrese
 */
public class PasswordHasher {

    private static MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-512");

    public static String encodePassword(String plaintextPassword, String salt) {
        return encoder.encodePassword(plaintextPassword, salt);
    }

}

