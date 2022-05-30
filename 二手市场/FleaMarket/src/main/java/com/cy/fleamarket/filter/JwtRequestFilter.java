package com.cy.fleamarket.filter;


import com.cy.fleamarket.util.JwtTokenUtil;
import com.cy.fleamarket.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String username = null;

        String jwtToken = null;

        String url = request.getRequestURI();

        //放行一些不需要登录的
        if(url.contains("/logging")||url.contains("/selectCommodityByName")
                ||url.contains("/logon")||url.contains("/error")||url.contains("/emailVF")
                ||url.contains("/allCommodityOnSale")||url.contains("/allCommodity")
                ||url.contains("/commodityDetail")||url.contains("/showByPrice")){

            chain.doFilter(request, response);

            return;
        }

        //获取token
        final String requestTokenHeader = request.getHeader("Authorization");

        // JWT报文表头的格式是"Bearer token". 去除"Bearer ",直接获取token
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);

            if(jwtToken.equals("null")){
                logger.warn("未登录");
            }

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");

            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
                
            }
        }
        else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // 一旦我们得到token，就验证它。
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // 如果令牌有效，则手动设置Spring Security

            // 身份验证
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 在上下文中设置Authentication之后，我们指定对当前用户进行身份验证。 因此它成功地通过了Spring安全配置。
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}

