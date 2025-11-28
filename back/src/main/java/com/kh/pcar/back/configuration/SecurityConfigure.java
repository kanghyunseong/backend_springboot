package com.kh.pcar.back.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.kh.pcar.back.configuration.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigure {

	private final JwtFilter jwtFilter;

	// 우리의 문제점 : 시큐리티의 formLogin필터가 자꾸만 인증이 안됐다고 회원가입도 못하게함
	// 해결방법 : form로그인 안쓸래 하고 fillterChain을 빈으로 등록

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		// return httpSecurity.formLogin().disable().build();
		/*
		 * return httpSecurity.formLogin(new
		 * Customizer<FormLoginConfigurer<HttpSecurity>>() {
		 * 
		 * @Override public void customize(FormLoginConfigurer<HttpSecurity> t) {
		 * t.disable(); } }).build();
		 */

		// formLogin필터를 사용안함으로써 401은 지나갔는데 ==> 403이 뜸
		// CSRF(Cross-Site Request Forgery)필터가 튀어나옴
		// <img src="http://www.naver.com" />

		// Example ) 회원가입, 로그인 => 누구나 다 할 수 있어야함


		// 회원정보수정, 회원탈퇴 => 로그인 된 사용자만 할 수 있어야 함

		return httpSecurity.formLogin(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())

				.authorizeHttpRequests(requests -> {

					requests.requestMatchers(HttpMethod.POST, "/members/login", "/members", "/auth/refresh", "/cars/**" , "/members/**")
							.permitAll();

					requests.requestMatchers(HttpMethod.GET, "/boards/**", "/comments/**", "/uploads/**", "/members/**",

							"/cars/**", "/station/**", "/boards/boards/search", "/boards/boards",
							"/boards/boards/","/station/search", "/comments/**", "/boards/notices", "/boards/notices/**"
							, "/boards/imgBoards", "/boards/imgBoards/**", "/boards/imgBoards/search").permitAll();
					
					requests.requestMatchers(HttpMethod.PUT, "/members", "/boards/**", "/boards/boards/**", "/boards/imgBoards", "/boards/imgBoards/**", "/comments/**")
							.authenticated();

					requests.requestMatchers(HttpMethod.DELETE, "/members", "/boards/**", "/boards/boards/**", "/comments/**")
							.authenticated();

					requests.requestMatchers(HttpMethod.POST, "/boards", "/boards/boards", "/comments",
							"/boards/boards/*/view", "/comments/**").authenticated();

					requests.requestMatchers(HttpMethod.GET, "/boards/boards/*").authenticated();

					requests.requestMatchers(HttpMethod.GET, "/admin/**", "/admin/api/settings/**")
							.hasAuthority("ROLE_ADMIN");

					requests.requestMatchers(HttpMethod.POST, "/admin/**", "/admin/api/settings/**")
							.hasAuthority("ROLE_ADMIN");

					requests.requestMatchers(HttpMethod.PUT, "/admin/**").hasAuthority("ROLE_ADMIN");

					requests.requestMatchers(HttpMethod.DELETE, "/admin/**", "/api/admin/**")
							.hasAuthority("ROLE_ADMIN");
				})

				/*
				 * SessionManagement : 세션을 어떻게 관리할것인지 지정 sessionCreatePolicy : 세션 사용 정책을 설정
				 */
				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();


	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

}
