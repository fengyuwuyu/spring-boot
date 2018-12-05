package com.ll.spring.boot.aop.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.spring.boot.aop.model.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/get/{id}")
	public User get(@PathVariable Integer id) {
		return new User(id, "张三", "123456");
	}
}
