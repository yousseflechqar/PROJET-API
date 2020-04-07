package services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	
	private @Autowired Environment env;
	
	


	public String generateToken(String login, Integer id, String permissions) {

		return Jwts.builder()
				.setSubject(id.toString())
//				.claim("login", login)
				.claim("permissions", permissions)
				.setExpiration(new Date(System.currentTimeMillis() + env.getProperty("security.token.expiration", Long.class)))
				.signWith(SignatureAlgorithm.HS512, env.getProperty("security.token.secret-key"))
				.compact();
	}
	
	public Claims resolveClaimsFromToken(String token) {
		
		
		return Jwts.parser()
	            .setSigningKey( env.getProperty("security.token.secret-key") )
	            .parseClaimsJws( token.replace(env.getProperty("security.token.prefix"),""))
	            .getBody();

	}

	
	
}
