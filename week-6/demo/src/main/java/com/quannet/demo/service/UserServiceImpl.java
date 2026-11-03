package com.quannet.demo.service;

import com.quannet.demo.model.User;
import com.quannet.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired 
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    @Override
    public Page<User> searchUser(String username, String password, Double balance, String tier, int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.searchUser(username, password, balance, tier, pageable);
    }
    @Override
    public User login(String username, String password){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("Account Not Found!!!");
        }
        User user = optionalUser.get();
        // if(!user.getPassword().equals(password)){
        //     throw new RuntimeException("Error Password!!!");
        // }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Error Password!!!");
        }
        return user;
    }

    @Transactional
    @Override
    public User topUpBalance(String username, Double amount){
        if(amount == null || amount <= 0){
            throw new RuntimeException("Error amount!!!");
        }
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) throw new RuntimeException("Cant find account!!!");
        User user = optionalUser.get();
        user.setBalance(user.getBalance() + amount);
        if(user.getBalance() >= 200000.0){
            user.setTier("VIP");
        }
        else if(user.getBalance() >= 100000.0){
            user.setTier("Diamond");
        }
        return userRepository.save(user);
    }
}
