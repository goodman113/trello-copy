//package my.project.trellocopy.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.function.Function;
//
//@Service
//@RequiredArgsConstructor
//@Getter
//public class JwtService {
//
//    @Value("${jwt.access-token-secret-key}")
//    private String accessToken;
//    @Value("${jwt.refresh.token.secretKey}")
//    private String refreshToken;
//    @Value("${jwt.expiration}")
//    private long accessExpiration;
//    @Value("${jwt.refresh.token.expiration}")
//    private long refreshExpiration;
//
//    public String extractEmail(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
//        Claims claims = extractClaimsJwt(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractClaimsJwt(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private SecretKey getSignKey() {
//        byte[] decode = Decoders.BASE64.decode(accessToken);
//        return Keys.hmacShaKeyFor(decode);
//    }
//    private SecretKey getRefreshSignKey() {
//        byte[] decode = Decoders.BASE64.decode(refreshToken);
//        return Keys.hmacShaKeyFor(decode);
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        String email = extractEmail(token);
//        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        Claims claims = extractClaimsJwt(token);
//        return claims.getExpiration().before(new java.util.Date());
//    }
//
//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String generateRefreshToken(String email) {
//        return Jwts.builder()
//                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setSubject(email)
//                .signWith(getRefreshSignKey(), SignatureAlgorithm.HS256)
//                .claim("type", "refresh")
//                .compact();
//    }
//
//}
