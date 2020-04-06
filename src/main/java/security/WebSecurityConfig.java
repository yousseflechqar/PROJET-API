package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.SpringApplicationContext;
import dao.UserDao;

@EnableWebSecurity
@PropertySource("classpath:security.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private @Autowired UserDao userDetailsService;
	private @Autowired Environment environment;

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http.
			authorizeRequests()
				.anyRequest().authenticated()
			.and().addFilter(getJwtAuthenticationFilter())
//			.and().addFilterBefore(getJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
				.authenticationEntryPoint((request, response, exception) -> {
					HttpUtils.constructJsonResponse(response, exception.getMessage(), 401);
				})	
				.accessDeniedHandler((request, response, exception) -> {
					HttpUtils.constructJsonResponse(response, exception.getMessage(), 403);
				})
			;

		http.
			sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	// @formatter:on

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public JwtAuthenticationFilter getJwtAuthenticationFilter() throws Exception {

		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager());
		
		filter.setFilterProcessesUrl("/api/login");

//		filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
//		    response.setStatus(200);
//		});

		filter.setAuthenticationFailureHandler((request, response, exception) -> {
			HttpUtils.constructJsonResponse(response, exception.getMessage(), 401);
		});

		return filter;
	}




	@Bean
	public SpringApplicationContext SpringApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
