package com.springboot.realtimechatapp.resources.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import com.springboot.realtimechatapp.resources.Error;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String userID){
        return userRepository.findById(userID);
    }


    public Error addUser(User user){
        if(this.userRepository.existsById(user.getID()))
            return Error.ALREADY_EXISTS;
        userRepository.save(user);
        return Error.OK;
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
