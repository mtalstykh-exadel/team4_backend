package com.team4.testingsystem.security;

import com.team4.testingsystem.services.CustomUserDetailsService;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import com.team4.testingsystem.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    private CustomUserDetails userDetails;
    private static final String JWT_TOKEN = "token";
    private String username;

    @BeforeEach
    void init() {
        userDetails = new CustomUserDetails(EntityCreatorUtil.createUser());
        username = userDetails.getUsername();
    }

    @Test
    void filterNoAuthorizationHeader() throws IOException, ServletException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void filterIncorrectAuthorizationHeader() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Incorrect header");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void filterAlreadyAuthenticated() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + JWT_TOKEN);
        Mockito.when(jwtTokenUtil.tryExtractUsername(JWT_TOKEN)).thenReturn(Optional.of(username));

        try (MockedStatic<SecurityContextHolder> mockContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            Mockito.when(securityContext.getAuthentication())
                    .thenReturn(new UsernamePasswordAuthenticationToken("", ""));

            jwtRequestFilter.doFilterInternal(request, response, filterChain);
            Mockito.verify(filterChain).doFilter(request, response);
        }
        Mockito.verify(securityContext, Mockito.never()).setAuthentication(Mockito.any());
    }

    @Test
    void filterUsernameExtractionFailed() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + JWT_TOKEN);
        Mockito.when(jwtTokenUtil.tryExtractUsername(JWT_TOKEN)).thenReturn(Optional.empty());

        try (MockedStatic<SecurityContextHolder> mockContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            Mockito.when(securityContext.getAuthentication()).thenReturn(null);

            jwtRequestFilter.doFilterInternal(request, response, filterChain);

            Mockito.verify(filterChain).doFilter(request, response);
        }

        Mockito.verify(securityContext, Mockito.never()).setAuthentication(Mockito.any());
    }

    @Test
    void filterInvalidToken() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + JWT_TOKEN);
        Mockito.when(jwtTokenUtil.tryExtractUsername(JWT_TOKEN)).thenReturn(Optional.of(username));

        try (MockedStatic<SecurityContextHolder> mockContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            Mockito.when(securityContext.getAuthentication()).thenReturn(null);

            Mockito.when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
            Mockito.when(jwtTokenUtil.validateToken(JWT_TOKEN, userDetails)).thenReturn(false);

            jwtRequestFilter.doFilterInternal(request, response, filterChain);

            Mockito.verify(filterChain).doFilter(request, response);
        }

        Mockito.verify(securityContext, Mockito.never()).setAuthentication(Mockito.any());
    }

    @Test
    void filterAuthenticationSuccess() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + JWT_TOKEN);
        Mockito.when(jwtTokenUtil.tryExtractUsername(JWT_TOKEN)).thenReturn(Optional.of(username));

        try (MockedStatic<SecurityContextHolder> mockContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            Mockito.when(securityContext.getAuthentication()).thenReturn(null);

            Mockito.when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
            Mockito.when(jwtTokenUtil.validateToken(JWT_TOKEN, userDetails)).thenReturn(true);

            jwtRequestFilter.doFilterInternal(request, response, filterChain);

            Mockito.verify(filterChain).doFilter(request, response);

            ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
            Mockito.verify(securityContext).setAuthentication(captor.capture());

            Assertions.assertEquals(userDetails, captor.getValue().getPrincipal());
            Assertions.assertEquals(userDetails.getAuthorities(), captor.getValue().getAuthorities());
        }
    }
}
