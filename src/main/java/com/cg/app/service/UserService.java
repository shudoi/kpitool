package com.cg.app.service;

import static com.cg.myflow.core.Components.*;

import java.util.List;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.app.entity.User;
import com.cg.app.repository.UserRepository;
import com.cg.myflow.consumer.RouteId;
import com.cg.myflow.core.Exchange;

@Service
@Transactional
public class UserService {

	public UserService() {
		new RouteId("_user/list")
				.to(_findAll())
				.to(log("foo"));

	}

	@Autowired
	UserRepository repository;

	public List<User> findAll() {
		return repository.findAll();
	}

	Function<Exchange, Exchange> _findAll() {
		return (exchange) -> {
			exchange.setHeader("data", repository.findAll());
			return exchange;

		};

	}
}
