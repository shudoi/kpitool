package com.cg.app.service;

import com.cg.app.entity.Person;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cg.app.repository.PersonRepository;
import com.cg.myflow.consumer.RouteId;
import com.cg.myflow.consumer.To;
import static com.cg.myflow.core.Components.direct;
import com.cg.myflow.core.Exchange;
import static com.cg.myflow.core.Expression.and;
import static com.cg.myflow.core.Expression.claims;
import static com.cg.myflow.core.Expression.evaluate;
import static com.cg.myflow.core.Expression.header;
import static com.cg.myflow.core.Expression.model;
import static com.cg.myflow.core.Expression.not;
import static com.cg.myflow.core.Expression.page;
import static com.cg.myflow.core.Expression.produceEnd;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional
public class PersonService {

    @Autowired
    PersonRepository repository;

    @Value("${jwt.key}")
    private String jwtKey;

    public PersonService() {

        new RouteId("required_auth")
                .to(direct("get_claims"))
                .when(new Object[]{not(claims()), new To().update(page("login"), produceEnd())})
                .update(model("username", claims("username")));

        new RouteId("get_claims")
                .when(new Object[]{header("jwt"), new To(jwtToClaims())},
                new Object[]{and(header("username"), header("password")),
                    new To(direct("login_action"))});

        new RouteId("login_action")
                .to(loginAction());

        new RouteId("person/get")
                .to(findPersonByUsername(claims("username")));

        new RouteId("user/list")
                .to(findAll());
    }

    public boolean isValidUsernameAndPassword(String username, String password) {
        return !repository.findByUsernameAndPassword(username, password).isEmpty();
    }

    private Function<Exchange, Exchange> jwtToClaims() {
        return (exchange) -> {
            try {
                Claims body = Jwts.parser().setSigningKey(jwtKey)
                        .parseClaimsJws(exchange.getHeader("jwt", String.class))
                        .getBody();
                exchange.setClaims(body);
            } catch (Throwable t) {

            }
            return exchange;
        };
    }

    private Function<Exchange, Exchange> createJwt() {
        return (exchange) -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("username", exchange.getHeader("username"));
            map.put("time", System.currentTimeMillis());
            String createdJwtToken = Jwts.builder()
                    .setClaims(map)
                    .signWith(SignatureAlgorithm.HS512, jwtKey)
                    .compact();
            exchange.setHeader("jwt", createdJwtToken);
            return exchange;
        };
    }

    private Function<Exchange, Exchange> loginAction() {
        return (exchange) -> {
            String username = exchange.getHeader("username", String.class);
            String password = exchange.getHeader("password", String.class);
            if (isValidUsernameAndPassword(username, password)) {
                this.createJwt().apply((exchange));
                this.jwtToClaims().apply(exchange);
            }
            return exchange;
        };
    }

    private Function<Exchange, Exchange> findPersonByUsername(Object expression) {
        return (exchange) -> {
            String username = evaluate(expression, exchange).toString();
            List<Person> findByUsername = repository.findByUsername(username);
            if (!findByUsername.isEmpty()) {
                exchange.setRawData(Person.class, findByUsername.get(0));
            }
            return exchange;
        };
    }

    private Function<Exchange, Exchange> findAll() {
        return (exchange) -> {
            List<Person> findAll = repository.findAll();
            exchange.setHeader("data", findAll);
            return exchange;
        };
    }
}
