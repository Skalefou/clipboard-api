package org.skalefou.clipboardapi.feature.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    public static Map<String, Object> generateJsonErrorException(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        return body;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e) {
        return new ResponseEntity<>(generateJsonErrorException(e.getReason()), e.getStatusCode());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException e) {
        System.err.println("Handling SignatureException: " + e.getMessage());
        return new ResponseEntity<>(generateJsonErrorException(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<Object> handleJwtTokenInvalidException(JwtTokenInvalidException e) {
        return new ResponseEntity<>(generateJsonErrorException(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    public static void handlerExceptionSecurity(HttpServletResponse response, HttpStatus httpStatus, String message) {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errorResponse = generateJsonErrorException(message);

        try (PrintWriter writer = response.getWriter()) {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            writer.write(jsonResponse);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}