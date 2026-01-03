package com.canesblack.spring_project1.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//어떤 어노태이션을 쓰냐에 따라 기능이 달라짐 + ctrl space ->자동 설정
@Configuration
@EnableWebSecurity
//SpringSecurity 기능을 사용하려면 이 어노테이션을 써야함
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{//스프링시큐리티 기능을 사용하고자 할때 이 메소드안에 작성.
	http.formLogin(
			login->login.loginPage("/loginPage") //url 작성해서 로그인페이지로 이동할때
			.loginProcessingUrl("/login")
			.failureUrl("/loginPage?error=true")
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler(authenticationSuccessHandler())
			.permitAll()
			)
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				//로그인이 성공했을 때 우리가 특별기능을 넣고 싶을 때, 세션 , 권한 기능
				HttpSession session = request.getSession();//세션 기능을 가지고 온것
				boolean isManager = authentication.getAutherities().stream()
						.anyMatch(grantedAuthoirity -> 
						grantedAuthority.getAuthority().equals("ADMIN")||
						grantedAuthority.getAuthority().equals("MANAGER")
								);
				if(isManger) {
					session.setAttribute("MANAGER", true);
				}
				session.setAttribute("username", authentication.getName());
				session.setAttribute("isAuthenticatied", true);
				response.sendRedirect(request.getContextPath()+"/");
				// TODO Auto-generated method stub
				super.onAuthenticationSuccess(request, response, authentication);
			}
		}
	}
}
