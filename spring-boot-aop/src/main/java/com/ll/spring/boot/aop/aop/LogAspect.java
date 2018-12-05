package com.ll.spring.boot.aop.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	@Pointcut(value="@annotation(com.stylefeng.guns.core.common.annotion.EnumEntityList)")
	public void pointCut() {}
}
