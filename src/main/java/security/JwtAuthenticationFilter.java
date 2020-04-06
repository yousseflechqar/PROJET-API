package security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import services.LoginService;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private @Autowired Environment env;
	private @Autowired LoginService loginService;
//	private AuthenticationManager authenticationManager;

	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//		super("/**");
		super.setAuthenticationManager(authenticationManager);
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

		
		UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
		
		response.setHeader(
			env.getProperty("security.token.header-string"), 
			env.getProperty("security.token.prefix") + loginService.generateToken(principal.getUsername())
		);
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("id", principal.getUserEntity().getId());
		userInfo.put("nom", principal.getUserEntity().getNom());
		userInfo.put("prenom", principal.getUserEntity().getPrenom());
		userInfo.put("roles", principal.getUserEntity().getRoles());
		
		
		HttpUtils.constructJsonResponse(response, new ObjectMapper().writeValueAsString(userInfo), 200);
	    		


	}

}
