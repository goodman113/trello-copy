//package my.project.trellocopy;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import my.project.trellocopy.service.JwtService;
//import org.hibernate.annotations.Filter;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    final JwtService jwtService;
//    final UserDetailsService userDetailsService;
//
//    public JwtFilter(JwtService jwtService,@Qualifier("getUserDetailsService") UserDetailsService userDetailsService) {
//        this.jwtService = jwtService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String header = request.getHeader("Authorization");
//
//        if (header != null && header.startsWith("Bearer ")) {
//            try {
//                String token = header.substring(7);
//                System.out.println("Token: " + token);
//                String email = jwtService.extractEmail(token);
//                UserDetails userDetails = null;
//                if (email != null) {
//                    userDetails = userDetailsService.loadUserByUsername(email);
//                }
//                if (userDetails != null) {
//                    if (jwtService.validateToken(token, userDetails)) {
//                        System.out.println("Token is valid for user: " + email);
//                        SecurityContextHolder.getContext().setAuthentication(
//                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
//                        );
//                    } else {
//                        System.out.println("Token is invalid for user: " + email);
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Error processing JWT: " + e.getMessage());
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
