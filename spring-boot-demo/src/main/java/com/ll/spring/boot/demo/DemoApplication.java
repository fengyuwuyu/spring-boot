package com.ll.spring.boot.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages= {"com.ll.spring.boot"})
@ServletComponentScan
public class DemoApplication {

}
