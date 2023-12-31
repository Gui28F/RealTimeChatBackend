package com.springboot.realtimechatapp.resources;

import com.springboot.realtimechatapp.Result;
import com.springboot.realtimechatapp.config.JwtGenerator;
import com.springboot.realtimechatapp.resources.chat.Chat;
import com.springboot.realtimechatapp.resources.chat.ChatService;
import com.springboot.realtimechatapp.resources.message.Message;
import com.springboot.realtimechatapp.resources.message.MessageRequest;
import com.springboot.realtimechatapp.resources.message.MessageService;
import com.springboot.realtimechatapp.resources.user.*;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.web.bind.annotation.*;

import java.net.http.WebSocketHandshakeException;
import java.util.*;


@CrossOrigin(origins = {"*","http://localhost:3000/", "http://192.168.1.11:3000/"})
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
        Result<Void> res = userService.addUser(user);
        return new ResponseEntity<>(Result.statusCodeFrom(res));

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
            return new ResponseEntity<>("Not logged in",HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping(value = "/user/chats", produces = "application/json")
    public ResponseEntity<List<Chat>> getChats(@AuthenticationPrincipal UserDetails userDetails){
        Optional<User> userOp = userService.getUser(userDetails.getUsername());
        if(userOp.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Chat> chats = userService.getChats(userDetails.getUsername());
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @PostMapping(value="/user/chat")
    public ResponseEntity<Void> addChat(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String chatName){
        Chat chat = new Chat(chatName);
        chatService.addChat(chat);
        Result<Void> res = userService.addChat(userDetails.getUsername(),chat);
        return new ResponseEntity<>(Result.statusCodeFrom(res));
    }

    @MessageMapping("/chat.send/{chatId}")
    @SendTo("/chat/{chatId}")
    public Message sendMessage(@DestinationVariable Long chatId, @Payload MessageRequest message) {
        Optional<User> userOp = userService.getUser(message.getSenderId());
        if(userOp.isEmpty())
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        Optional<Chat> chatOp = chatService.getChat(chatId);
        if(chatOp.isEmpty())
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        Message msg = new Message(userOp.get(), chatOp.get(), message.getMsg());
        messageService.addMsg(msg);
        return msg;
    }
    private boolean containsNullFiels(UserRequest request){
        return request.getPassword() == null || request.getUsername() == null
                || request.getUserID() == null;
    }

}
