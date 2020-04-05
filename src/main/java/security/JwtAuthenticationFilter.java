package security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.LoginBean;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private Environment env;
//	private AuthenticationManager authenticationManager;

	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Environment env) {
//		super("/**");
		super.setAuthenticationManager(authenticationManager);
		this.env = env;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		
		try {
			LoginBean credentials = new ObjectMapper().readValue(request.getInputStream(), LoginBean.class);
			
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(credentials.login, credentials.password)
			);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {


		String token = Jwts.builder()
				.setSubject(((User) authResult.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + env.getProperty("security.token.expiration", Long.class)))
				.signWith(SignatureAlgorithm.HS512, env.getProperty("security.token.secret-key"))
				.compact();
		
		response.setHeader(env.getProperty("security.token.header-string"), env.getProperty("security.token.prefix") + token);
		
		response.setStatus(200);

	}

}
