package com.nc13techsolutions.fitnesstrackerbackendserver.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.JustUserCredentials;
import com.nc13techsolutions.fitnesstrackerbackendserver.models.User;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.JwtService;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenConfig extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = null;
        if (request.getHeader("Authorization") != null) {
            authHeader = request.getHeader("Authorization");
        } else if (request.getHeader("authorization") != null) {
            authHeader = request.getHeader("authorization");
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // no authetication found. So we'll let the filter handle things
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        JustUserCredentials credentials = jwtService.extractCredentials(jwt);
        if (credentials == null)
            throw new UsernameNotFoundException("User not found");
        String username = credentials.getUSERNAME();
        String password = credentials.getPASSWORD();
        if (username != null && password != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // User is not authenticated
            User user = userService.checkUserCredentials(username, password);
            if (user != null) {
                if (jwtService.isJWTTokenVerified(jwt,
                        new JustUserCredentials(user.getUsername(), user.getPassword()))) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } else
                throw new UsernameNotFoundException("User not valid");
        }
        filterChain.doFilter(request, response);
    }

}
