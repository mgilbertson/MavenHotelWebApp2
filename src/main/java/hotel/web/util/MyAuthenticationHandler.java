/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.web.util;

/**
 *
 * @author Mitch
 */
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 *
 * @author jlombardo
 */
public class MyAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String userTargetUrl = "/user/index.jsp"; // change "memmber" to whatever you use
        String adminTargetUrl = "/admin/index.jsp";  // change "admin" to whatever you use and add more taretURLs if needed
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            getRedirectStrategy().sendRedirect(request, response, adminTargetUrl);
        } else if (roles.contains("ROLE_USER")) {
            getRedirectStrategy().sendRedirect(request, response, userTargetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
    }
}
