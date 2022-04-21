package com.project.blog.config.jwt;

import com.project.blog.service.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private String Roles = "roles";
    private final Long accessTokenVaildMillisecond = 60 * 60 * 1000L; //1 hours
    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L; //14 days

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessTokenDto(String email) {

        Claims claims = Jwts.claims().setSubject(email); //토큰의 키가 되는 Subject를 중복되지 않는 고유한 값인 eamail로 지정한다.
//        claims.put("Roles", roles);

        Date now = new Date(); /// 만료 시간은 지금부터 1시간

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenVaildMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey) //서명할때 사용되는 알고리즘은 HS256
                .compact();
    }

    public String createRefreshToken(String value) {

        Claims claims = Jwts.claims().setSubject(String.valueOf(value));
        claims.put("Value", value);

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) { //이메일을 얻기 위해 디코딩 하는 부분이다,
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            //지정한 secretkey를 통해 서명된 jwt를 해석하여 subject를 끌고와 리턴하여 이값을 통해 인증 객체를 끌고 올 수 있다.
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public boolean validateTokenExpiration(String token) { //토큰이 만료되었는지 확인해주는 메서드이다.
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
            //이전과 같이 token을 디코딩하여 만료시간을 끌고와 현재 시간과 비교해 확인해준다,
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveJwtToken(HttpServletRequest request) { //토큰은 HTTP Header에 저장되어 계속적으로 이용되어 진다.
        //토큰을 사용하기 위해 실제로 Header에서 꺼내오는 메소드이다.
        return request.getHeader("X-AUTH-TOKEN");
    }


}
