package com.canesblack.spring_project1.config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

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
		http.csrf(csrf-> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
		// 백엔드 영역으로 데이터가 넘어갈때, 자바스크립트에다가 
		.cors(cors -> cors.configurationSource(corsCorsfigurationSource()))
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
		//always if_required never 중에 사용
		
		.authorizeHttpRequests(authz->authz.requestMatchers("/", "/loginPage", "/logout", "/noticeCheckPage", "/register", "/registerPage", "/menu/all")
				.permitAll()
				.requestMatchers(HttpMethod.POST,"/login", "/register").permitAll()
				.requestMatchers("/resources/**","/WEB-INF/**").permitAll()
				.requestMatchers("/noticeAdd","noticeModifyPage").hasAnyAuthority("ADMIN","MANAGER")
				 .requestMatchers(HttpMethod.POST, "/menu/add").hasAnyAuthority("ADMIN","MANAGER")
				 .requestMatchers(HttpMethod.POST, "/menu/update").hasAnyAuthority("ADMIN","MANAGER")
				 .requestMatchers(HttpMethod.DELETE, "/menu/delete").hasAnyAuthority("ADMIN","MANAGER")
				 .anyRequest().authenticated()
				 //로그인을해야하지만 접근이 가능하게끔 그렇기때문에 자동으로 로그인페이지로감.
				)
		
		
		.formLogin(
			login->login.loginPage("/loginPage") //url 작성해서 로그인페이지로 이동할때
			.loginProcessingUrl("/login")
			.failureUrl("/loginPage?error=true")
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler(authenticationSuccessHandler())
			.permitAll()
			)
		
		.logout(logout->logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")//로그아웃성공후 이 url로 리다이렉팅
				.invalidateHttpSession(true)//세션 무효화=> 세션 공간에 있던 데이터 무효화
				.deleteCookies("JSESSIONID")//쿠키삭제
				.permitAll()// 위 기능을 수행하려면 이 메소드 실행
	
				
				);
		//로그 아웃 url을 통해 로그아웃 진행
		
		return http.build();
		//최종 http에 적용시킬때 사용하는 메소드
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				//로그인이 성공했을 때 우리가 특별기능을 넣고 싶을 때, 세션 , 권한 기능
				HttpSession session = request.getSession();//세션 기능을 가지고 온것
				boolean isManager = authentication.getAuthorities().stream()
						.anyMatch(grantedAuthority -> 
						grantedAuthority.getAuthority().equals("ADMIN")||
						grantedAuthority.getAuthority().equals("MANAGER")
								);
				if(isManager) {
					session.setAttribute("MANAGER", true);
				}
				session.setAttribute("username", authentication.getName());
				//세선에 로그인한 아이디 저장
				session.setAttribute("isAuthenticated", true);
				//세션에다가 로그인됬냐 여부를 저장
				//request.getContextPath()=>localhost:8080
				response.sendRedirect(request.getContextPath()+"/");
				// TODO Auto-generated method stub
				super.onAuthenticationSuccess(request, response, authentication);
			}
		};
		
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public CorsConfigurationSource corsCorsfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","https://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
