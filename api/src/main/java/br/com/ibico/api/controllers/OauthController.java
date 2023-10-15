package br.com.ibico.api.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/oauth")
public class OauthController {

    /**
     * {@link br.com.ibico.api.filters.JWTTokenGeneratorFilter} will handle this request
     */
    @PostMapping("token")
    public void getToken() {
    }


}
