package com.cg;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;

public class Foo {

    public static void main(String[] args) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("username", "waowao");
        map.put("time", System.currentTimeMillis());
        String compactJws = Jwts.builder()
                .setClaims(map)
                .signWith(SignatureAlgorithm.HS512, "foobar")
                .compact();
        System.out.println(compactJws);
        System.out.println(Jwts.parser().setSigningKey("foobar").parseClaimsJws(compactJws + " ").getBody().get("username").equals("waowao"));
    }
}
