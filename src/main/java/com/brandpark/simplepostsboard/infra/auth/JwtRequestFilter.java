package com.brandpark.simplepostsboard.infra.auth;

import com.brandpark.simplepostsboard.modules.accounts.CustomUser;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        if (isAuthenticatedWithSessionId(requestTokenHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader == null) {
            log.warn("토큰이 존재하지 않습니다.");

        } else if (requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("JWT 토큰을 얻을 수 없습니다.");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token 이 만료되었습니다.");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
            }

        } else {
            log.warn("토큰이 \"Bearer\"로 시작하지 않습니다. : {}", requestTokenHeader);

//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is not exists or token name does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            CustomUser userDetails = (CustomUser) this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAccount().getPassword(), userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticatedWithSessionId(String requestTokenHeader) {
        return SecurityContextHolder.getContext().getAuthentication() != null && requestTokenHeader == null;
    }
}
