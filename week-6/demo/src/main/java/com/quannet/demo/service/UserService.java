package com.quannet.demo.service;

import com.quannet.demo.model.User;
import org.springframework.data.domain.Page;

public interface UserService {
    public void save(User user);
    public Page<User> searchUser(String username, String password, Double balance, String tier, int pageNo, int pageSize);
    public User login(String username, String password);
    public User topUpBalance(String username, Double amount);
}
