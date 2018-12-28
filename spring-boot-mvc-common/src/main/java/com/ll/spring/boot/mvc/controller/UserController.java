package com.ll.spring.boot.mvc.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.spring.boot.core.util.MapUtil;
import com.ll.spring.boot.mvc.model.User;

@RequestMapping("/user")
@Controller
public class UserController {

	@RequestMapping("/get/{userId}")
	@ResponseBody
	public Map<String, Object> get(@PathVariable Long userId) {
		User user = new User(1L, "张三", "123456", 1L);
		return MapUtil.createSuccessMap("user", user);
	}
}
