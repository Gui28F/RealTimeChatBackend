package com.springboot.realtimechatapp.resources.user;

import com.springboot.realtimechatapp.Result;
import com.springboot.realtimechatapp.resources.chat.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String userID){
        return userRepository.findById(userID);
    }

    public Result<Void> addUser(User user){
        if(this.userRepository.existsById(user.getID()))
            return Result.error(Result.ErrorCode.CONFLICT);
        userRepository.save(user);
        return Result.ok();
    }

    public Result<Void> addChat(String userId, Chat chat){
        Optional<User> userOp = getUser(userId);
        if(userOp.isEmpty())
            return Result.error(Result.ErrorCode.NOT_FOUND);
        User user = userOp.get();
        Result<Void> res = user.addChat(chat);
        userRepository.save(user);
        return res;
    }

    public List<Chat> getChats(String userID){
        return userRepository.findAllChatsByUserId(userID);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOp = this.getUser(username);
        if(userOp.isEmpty())
            throw new UsernameNotFoundException("");
        User user = userOp.get();
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(UserType.USER.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(userAuthority);
        return new org.springframework.security.core.userdetails.User(user.getID(), user.getHashedPassword(),authorities);
    }
}
