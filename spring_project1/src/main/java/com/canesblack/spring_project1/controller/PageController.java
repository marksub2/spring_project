package com.canesblack.spring_project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.canesblack.spring_project1.entity.Menu;
import com.canesblack.spring_project1.entity.User;
import com.canesblack.spring_project1.service.MenuRestService;
import com.canesblack.spring_project1.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

//@Component 한마디로 스프링빈으로 등록하기위한라벨링작업
@Controller
public class PageController {
	
	@Autowired
	private MenuRestService menuRestService;
	
	@Autowired
	private UserService userService;
	
//	@PostMapping()
//	@PutMapping()
//	@DeleteMapping()
	//  / => localhost:8080
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	//페이지를 조회및이동할때 위와같이 @GetMapping()을 써서 이동합니다.
	//  / => localhost:8080/register
	@GetMapping("/registerPage")
	public String registerPage(HttpServletRequest request,org.springframework.ui.Model model) {
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		model.addAttribute("_csrf",csrfToken);
		return "register/index";
	}
	
	@GetMapping("/loginPage")
	public String loginPage(HttpServletRequest request,org.springframework.ui.Model model) {
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		model.addAttribute("_csrf",csrfToken);
		return "login/index";
	}
	
	@GetMapping("/noticeAddPage")
	public String noticeAddPage(Model model,Authentication authentication) {
		String writer = userService.findWriter(authentication.getName());
		model.addAttribute("writer", writer);
		return "noticeAdd/index";
	}
	
	@GetMapping("/noticeCheckPage")
	public String showNoticeCheckPage(@RequestParam("idx") int idx,Model model) {
		Menu menu = menuRestService.boardContent(idx);
		model.addAttribute("menu",menu);
		return "noticeCheck/index";
	}
	//@GetMapping("/noticeModifyPage")
	//public String showNoticeModifyPage(@RequestParam("idx") int idx,Model model) {
	//	Menu menu = menuRestService.boardContent(idx);
	//	model.addAttribute("menu",menu);
	//	return "noticeModify/index";
	//}
	@GetMapping("/noticeModifyPage")
	public String showNoticeModifyPage(@RequestParam("idx") int idx, Model model) {
	    System.out.println("컨트롤러 도착! 요청받은 idx: " + idx); // 이 로그가 콘솔에 찍히는지 확인
	    
	    Menu menu = menuRestService.boardContent(idx);
	    model.addAttribute("menu", menu);
	    return "noticeModify/index"; 
	}
	
}