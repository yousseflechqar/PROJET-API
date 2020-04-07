package security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import services.JwtService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	
	private @Autowired Environment env;
	private @Autowired JwtService jwtService;

	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String prefix = env.getProperty("security.token.prefix");
		String token = request.getHeader(env.getProperty("security.token.header-string"));
		
		if(token != null && token.startsWith(prefix)) {
			
			Claims claims = jwtService.resolveClaimsFromToken(token);
			UsernamePasswordAuthenticationToken authentication = null;
			
			if( claims.getSubject() != null ) {
				
				final Collection<GrantedAuthority> authorities =
		                	Arrays.stream(claims.get("permissions").toString().split(","))
		                        .map(SimpleGrantedAuthority::new)
		                        	.collect(Collectors.toList());
				
				authentication = new UsernamePasswordAuthenticationToken(
						Integer.parseInt(claims.getSubject()), null, authorities);
			}
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		chain.doFilter(request, response);
		
	}



	

}
