package com.cg;

import com.cg.app.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class Main {

    @Autowired
    PersonService personService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
