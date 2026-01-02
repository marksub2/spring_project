package com.canesblack.spring_project1.controller;

import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//@Component 한마디로 스프링빈으로 등록하기위한 라벨링 작업

@Controller
//@Component 한마디로 스프링빈으로 등록하기위한 라벨링 작업
public class PageController {
	
	@GetMapping("/")
	public String Home() {
		return "index";
		
	}
	//페이지를 조회 및 이동할때 위와 같이 @GetMapping을 써서 이동.
	//->localhost:8080/register
	@GetMapping("/register")
	public String registerPage() {
		return "register/index";
	}
	//->localhost:8080/loginPage
	@GetMapping("/loginPage")
	public String loginPage() {
		return "login/index";
	}
	//@PostMapping()
	//@PutMapping()
	//@DeleteMapping()
}
