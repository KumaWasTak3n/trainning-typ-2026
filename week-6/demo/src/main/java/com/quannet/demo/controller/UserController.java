package com.quannet.demo.controller;

import com.quannet.demo.config.JwtService;
import com.quannet.demo.dto.LoginRequest;
import com.quannet.demo.dto.TopUpRequest;
import com.quannet.demo.dto.UserDto;
import com.quannet.demo.model.User;
import com.quannet.demo.repository.UserRepository;
import com.quannet.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/add")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto){
        userService.save(userDto.convertToModel());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    @PostMapping("/add-all")
    public ResponseEntity<?> saveAllUser(@RequestBody UserDto[] userDtos){
        for (UserDto userDto : userDtos) {
            userService.save(userDto.convertToModel());
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            User user = userService.login(request.getUsername(), request.getPassword());
            user.setPassword("********");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>("Id Not Found!", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getByUserName(@PathVariable(name = "username") String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>("Username Not Found!", HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/nap-tien")
    public ResponseEntity<?> topUp(@RequestBody TopUpRequest request){
        try{
            User updatedUser = userService.topUpBalance(request.getUsername(), request.getAmount());
            updatedUser.setPassword("********");
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam(name = "username", required = false) String username, @RequestParam(name = "password", required = false) String password,
                                        @RequestParam(name = "balance", required = false) Double balance,
                                        @RequestParam(name = "tier", required = false) String tier,
                                        @RequestParam(name = "pageNo")int pageNo, @RequestParam(name = "pageSize")int pageSize){
        Page<User> users = userService.searchUser(username, password, balance, tier, pageNo, pageSize);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
