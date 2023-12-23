package com.springboot.realtimechatapp.resources.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.springboot.realtimechatapp.resources.Error;

@Service
public class UserService {

    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private Optional<User> getUser(String userID){
        return userRepository.findById(userID);
    }


    public Error addUser(User user){
        if(this.userRepository.exists(Example.of(user)))
            return Error.ALREADY_EXISTS;
        userRepository.save(user);
        return Error.OK;
    }
}
