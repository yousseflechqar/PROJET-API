package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import beans.LoginBean;
import dao.UserDao;
import dto.SimpleDto;
import dto.UserSession;
import entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class LoginService {

	
	
	private @Autowired UserDao userDao;
	private @Autowired Environment env;
	
	


	public String generateToken(String login) {

		return Jwts.builder()
				.setSubject(login)
				.setExpiration(new Date(System.currentTimeMillis() + env.getProperty("security.token.expiration", Long.class)))
				.signWith(SignatureAlgorithm.HS512, env.getProperty("security.token.secret-key"))
				.compact();
	}

	
	
}
