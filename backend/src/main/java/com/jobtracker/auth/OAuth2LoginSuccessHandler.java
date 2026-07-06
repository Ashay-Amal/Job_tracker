package com.jobtracker.auth;

import com.jobtracker.config.AppProperties;
import com.jobtracker.users.AuthProvider;
import com.jobtracker.users.UserAccount;
import com.jobtracker.users.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final AppProperties appProperties;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, AuthService authService, AppProperties appProperties) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.appProperties = appProperties;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        if (email == null) {
            throw new ServletException("Google account did not provide an email address");
        }
        UserAccount user = userRepository.findByEmail(email.toLowerCase()).orElseGet(() -> {
            UserAccount created = new UserAccount();
            created.setEmail(email);
            created.setFullName(principal.getAttribute("name"));
            created.setGoogleSubject(principal.getAttribute("sub"));
            created.setProvider(AuthProvider.GOOGLE);
            return userRepository.save(created);
        });
        var tokens = authService.issueTokens(user);
        String redirect = appProperties.getFrontendBaseUrl()
                + "/login#accessToken=" + encode(tokens.accessToken())
                + "&refreshToken=" + encode(tokens.refreshToken());
        response.sendRedirect(redirect);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
