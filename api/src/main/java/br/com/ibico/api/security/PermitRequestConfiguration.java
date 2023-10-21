package br.com.ibico.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class PermitRequestConfiguration {

    @Bean
    RequestMatcher requestsPermitAll() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/v1/users", HttpMethod.POST.toString()),
                new AntPathRequestMatcher("/v1/oauth/**", HttpMethod.POST.toString()),
                new AntPathRequestMatcher("/v1/swagger.html"),
                new AntPathRequestMatcher("/v1/swagger-ui/**"),
                new AntPathRequestMatcher("/v3/api-docs/**")
        );
    }
}
