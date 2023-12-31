package com.springboot.realtimechatapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private final JwtGenerator jwtGenerator;

    public HandshakeInterceptor() {
        this.jwtGenerator = new JwtGenerator();
    }
    private String extractTokenFromQueryParams(ServerHttpRequest request) {
        // Extract the token from query parameters
        // Modify this based on how you pass the token in your application
        // For example, if the token is passed as "token" query parameter:
        return request.getURI().getQuery().split("=")[1];
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        String token = extractTokenFromQueryParams(request);

        try {
            jwtGenerator.validateToken(token);
            return super.beforeHandshake(request, response, wsHandler, attributes);
        } catch (AuthenticationCredentialsNotFoundException ex) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getBody().write("Authentication failed".getBytes(StandardCharsets.UTF_8));
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        System.out.println("After Handshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
