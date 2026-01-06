package com.canesblack.spring_project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//@Component 한마디로 스프링빈으로 등록하기위한 라벨링 작업

import com.canesblack.spring_project1.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
//@Component 한마디로 스프링빈으로 등록하기위한 라벨링 작업
public class PageController {
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/")
	public String Home() {
		return "index";
		
	}
	//페이지를 조회 및 이동할때 위와 같이 @GetMapping을 써서 이동.
	//->localhost:8080/register
	@GetMapping("/registerPage")
	public String registerPage(HttpServletRequest request, org.springframework.ui.Model model) {
		
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		model.addAttribute("_csrf",csrfToken);
		return "register/index";
	}
	
	@GetMapping("/loginPage")
	public String loginPage(HttpServletRequest request, org.springframework.ui.Model model) {
		
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		model.addAttribute("_csrf",csrfToken);
		return "login/index";
	}
	
	//->localhost:8080/loginPage
	
	@GetMapping("/noticeAddPage")
	public String noticeAddPage(Model model, Authentication authentication) {
		String writer = userService.findWriter(authentication.getName());
		model.addAttribute("writer", writer);
			return "noticeAdd/index";
		}
}

	//@PostMapping()
	//@PutMapping()
	//@DeleteMapping()

