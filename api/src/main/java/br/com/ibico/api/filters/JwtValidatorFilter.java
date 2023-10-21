package br.com.ibico.api.filters;

import br.com.ibico.api.constraints.SecurityConstraints;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtValidatorFilter extends OncePerRequestFilter {

    private final RequestMatcher permitRequestMatcher;

    public JwtValidatorFilter(RequestMatcher permitRequestMatcher) {
        this.permitRequestMatcher = permitRequestMatcher;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");

        // check if the header is valid or else return 401
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            if (permitRequestMatcher.matches(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not found!");
            return;
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(
                    SecurityConstraints.JWT_KEY.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt.substring("Bearer ".length()))
                    .getBody();
            String username = String.valueOf(claims.get("username"));
            String authorities = (String) claims.get("authorities");
            Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Token received!");
        }

        filterChain.doFilter(request, response);
    }
}
