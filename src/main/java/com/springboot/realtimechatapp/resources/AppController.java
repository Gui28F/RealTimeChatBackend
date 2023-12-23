package com.springboot.realtimechatapp.resources;

import com.springboot.realtimechatapp.resources.chat.ChatService;
import com.springboot.realtimechatapp.resources.message.MessageService;
import com.springboot.realtimechatapp.resources.user.User;
import com.springboot.realtimechatapp.resources.user.UserRequest;
import com.springboot.realtimechatapp.resources.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    private static final String USER_ALREADY_EXISTS = "User already exists!";
    private UserService userService;
    private ChatService chatService;
    private MessageService messageService;

    @Autowired
    public AppController(UserService userService, ChatService chatService, MessageService messageService){
        this.userService = userService;
        this.chatService = chatService;
        this.messageService = messageService;
    }
    @MessageMapping("/users/register")
    @CrossOrigin(origins = {"http://localhost:8080/", "http://192.168.1.11:8080/", "http://109.50.216.198:8080/"})

    public ResponseEntity<Object> register(UserRequest userRequest) {
        User user = new User(userRequest.getUserID(), userRequest.getUsername(), userRequest.getPassword());
        return convertErrorToResponse(userService.addUser(user));
    }


    private ResponseEntity<Object> convertErrorToResponse(Error error){
        switch (error){
            case CONFLICT -> {
                return new ResponseEntity<>("Conflict", HttpStatus.CONFLICT);
            }
            case ALREADY_JOINED -> {
                return new ResponseEntity<>("Already joined", HttpStatus.CONFLICT);
            }
            case OK -> {
                return new ResponseEntity<>("OK", HttpStatus.OK);
            }
            case NOT_FOUND -> {
                return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
            }
            case ALREADY_EXISTS -> {
                return new ResponseEntity<>("Already exists", HttpStatus.CONFLICT);
            }
            default -> {
                return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
