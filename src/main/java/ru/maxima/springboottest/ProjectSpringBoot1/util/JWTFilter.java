package ru.maxima.springboottest.ProjectSpringBoot1.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.maxima.springboottest.ProjectSpringBoot1.services.PersonDetailsService;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTutil jwTutil;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public JWTFilter(JWTutil jwTutil, PersonDetailsService personDetailsService) {
        this.jwTutil = jwTutil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization"); //""

        if (header != null && !header.isBlank() && header.startsWith("Bearer ") && header.length() > 7) {
            String jwt = header.substring(7);

            try {
                if (jwt.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                } else {
                    String username = jwTutil.validationToken(jwt);

                    UserDetails userDetails = personDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
