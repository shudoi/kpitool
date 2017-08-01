package com.cg.myflow;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Components {

    @Value("${jwt.key}")
    private String jwtKey;

    public Function<Exchange, Exchange> direct(String endpointName) {
        return (exchange) -> {
            return Endpoints.sendTo(endpointName, exchange);
        };
    }

    public Function<Exchange, Exchange> auth() {
        return (exchange) -> {
            String jwtToken = exchange.getHeader("jwt", String.class);
            if (exchange.getHeader("jwt") != null) {
                Claims body = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(jwtToken).getBody();
                exchange.setClaims(body);
            } else {
                String username = exchange.getHeader("username", String.class);
                String password = exchange.getHeader("password", String.class);
                if ((username.equals("member") && password.equals("member")) || (username.equals("admin") && password.equals("admin"))) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("username", username);
                    map.put("time", System.currentTimeMillis());
                    String createdJwtToken = Jwts.builder()
                            .setClaims(map)
                            .signWith(SignatureAlgorithm.HS512, jwtKey)
                            .compact();
                    exchange.setHeader("jwt", createdJwtToken);
                    return exchange;
                }
            }
            return exchange;
        };
    }
}
