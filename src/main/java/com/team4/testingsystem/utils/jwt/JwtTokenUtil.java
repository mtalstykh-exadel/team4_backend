package com.team4.testingsystem.utils.jwt;

import com.team4.testingsystem.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtTokenUtil {
	private final Key JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	private String extractUsername(String token) throws JwtException {
		return extractClaim(token, Claims::getSubject);
	}

	private Date extractExpirationDate(String token) throws JwtException {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) throws JwtException {
		return Jwts.parserBuilder()
				.setSigningKey(JWT_SECRET)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpirationDate(token).before(new Date());
	}

	public String generateToken(CustomUserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(userDetails.getUsername(), claims);
	}

	private String createToken(String subject, Map<String, Object> claims) {
		return Jwts.builder()
				.setSubject(subject)
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(JWT_SECRET)
				.compact();
	}

	public Boolean validateToken(String token, CustomUserDetails userDetails) {
		try {
			final String username = extractUsername(token);
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		} catch (JwtException e) {
			return false;
		}
	}
}
