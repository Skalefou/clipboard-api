package org.skalefou.clipboardapi.feature.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.skalefou.clipboardapi.feature.config.exception.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String id = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                token = authHeader.substring(7);
                id = jwtService.extractId(token);
            } catch (SignatureException e) {
                GlobalExceptionHandler.handlerExceptionSecurity(httpResponse, HttpStatus.BAD_REQUEST, "Invalid token");
                return;
            } catch (ExpiredJwtException e) {
                GlobalExceptionHandler.handlerExceptionSecurity(httpResponse, HttpStatus.UNAUTHORIZED, "Token expired");
                return;
            }
        }

        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails usersDetails = userDetailsServiceImpl.loadUserByUsername(id);
            if(jwtService.validateToken(token, usersDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usersDetails, null, usersDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
