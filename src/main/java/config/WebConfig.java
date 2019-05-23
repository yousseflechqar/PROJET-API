package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import interceptor.LoginInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan({"controllers"})
public class WebConfig implements WebMvcConfigurer {

	
	@Bean
	public LoginInterceptor loginInterceptor() {
	    return new LoginInterceptor();
	}
	
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor( loginInterceptor() );
        
//        .addPathPatterns("/**")
//        
//        .excludePathPatterns("/login")
//        .excludePathPatterns("/logout")
//        .excludePathPatterns("/login/connexion");

    }
}
