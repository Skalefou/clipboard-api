package org.skalefou.clipboardapi.feature.config.exception;

public class JwtTokenInvalidException extends RuntimeException {
    public JwtTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
