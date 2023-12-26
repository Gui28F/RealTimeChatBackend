package com.springboot.realtimechatapp.resources;

import com.springboot.realtimechatapp.config.JwtGenerator;
import com.springboot.realtimechatapp.resources.chat.ChatService;
import com.springboot.realtimechatapp.resources.message.MessageService;
import com.springboot.realtimechatapp.resources.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


@CrossOrigin(origins = {"http://localhost:3000/", "http://192.168.1.11:3000/"})
@Controller
public class AppController {
    private final UserService userService;
    private final ChatService chatService;
    private final MessageService messageService;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppController(UserService userService, ChatService chatService,
                         MessageService messageService,  AuthenticationManager authenticationManager,
                         JwtGenerator jwtGenerator, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.chatService = chatService;
        this.messageService = messageService;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/users/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> register(@RequestBody UserRequest userRequest) {
        if(containsNullFiels(userRequest))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contains null fields");
        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getUserID(), userRequest.getUsername(), hashedPassword);
        return convertErrorToResponse(userService.addUser(user));
    }
    @PostMapping(value = "/users/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> login(@RequestBody UserRequest userRequest) {
        Optional<User> userOp = userService.getUser(userRequest.getUserID());
        if(userOp.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        User user = userOp.get();
        if(!passwordEncoder.matches(userRequest.getPassword(), user.getHashedPassword()))
            return new ResponseEntity<>("Password is wrong", HttpStatus.BAD_REQUEST);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getID(), userRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, UserType.USER.toString());
        UserLoginResponse res = new UserLoginResponse(user.getID(), token);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/user/t")
    public ResponseEntity<String> t(@AuthenticationPrincipal UserDetails userDetails){
        if (userDetails != null) {
            String username = userDetails.getUsername();
            return ResponseEntity.ok("Endpoint /t accessed by logged-in user: " + username);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }

    private boolean containsNullFiels(UserRequest request){
        return request.getPassword() == null || request.getUsername() == null
                || request.getUserID() == null;
    }

    private ResponseEntity<Object> convertErrorToResponse(Error error){
        switch (error){
            case CONFLICT -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict");
            }
            case ALREADY_JOINED -> {
                return  ResponseEntity.status(HttpStatus.CONFLICT).body("Already joined");
            }
            case OK -> {
                return  ResponseEntity.status(HttpStatus.OK).body("OK");
            }
            case NOT_FOUND -> {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
            }
            case ALREADY_EXISTS -> {
                return  ResponseEntity.status(HttpStatus.CONFLICT).body("Already exists");
            }
            default -> {
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        }
    }
}
