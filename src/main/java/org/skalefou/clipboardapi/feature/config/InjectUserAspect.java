package org.skalefou.clipboardapi.feature.config;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.skalefou.clipboardapi.feature.config.exception.JwtTokenInvalidException;
import org.skalefou.clipboardapi.feature.model.Users;
import org.skalefou.clipboardapi.feature.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class InjectUserAspect {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UsersRepository usersRepository;
    private final HttpServletRequest request;

    @Around("@annotation(org.skalefou.clipboardapi.feature.config.InjectUser)")
    public Object injectUser(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        String token = request.getHeader("Authorization");
        Users user = null;

        if (token != null && token.startsWith("Bearer ")) {
            try {
            String jwtToken = token.substring(7);

                String userEmail = jwtService.extractMail(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.validateToken(jwtToken, userDetails)) {
                    user = usersRepository.findByMail(userEmail).orElse(null);
                }
            } catch (SignatureException e) {
                throw new JwtTokenInvalidException("Invalid JWT token", e);
            }
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Users) {
                args[i] = user;
                break;
            }
        }

        return joinPoint.proceed(args);
    }
}
