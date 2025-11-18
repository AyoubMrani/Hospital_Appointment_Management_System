package com.hendisantika.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Plaintext password encoder - does NOT encode passwords
 * Compares passwords directly without hashing
 * WARNING: Only use this for development/testing, never in production!
 */
public class PlaintextPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // Return plaintext password without encoding
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Direct string comparison without any encoding
        return rawPassword.toString().equals(encodedPassword);
    }
}
