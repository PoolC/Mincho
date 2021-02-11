package org.poolc.api.member.infra;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHashProvider {
    private static final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();

    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String passwordRaw, String passwordHash) {
        return encoder.matches(passwordRaw, passwordHash);
    }
}
