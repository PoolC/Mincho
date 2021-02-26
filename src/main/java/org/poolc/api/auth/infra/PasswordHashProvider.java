package org.poolc.api.auth.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHashProvider {
    private final PasswordEncoder encoder;

    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String passwordRaw, String passwordHash) {
        return encoder.matches(passwordRaw, passwordHash);
    }
}
