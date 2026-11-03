package com.quannet.demo.controller;

import com.quannet.demo.model.Session;
import com.quannet.demo.repository.SessionRepository;
import com.quannet.demo.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    SessionService sessionService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllSession(){
        List<Session> sessionList = sessionRepository.findAll();
        return new ResponseEntity<>(sessionList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Session> session = sessionRepository.findById(id);
        if(session.isPresent()) return new ResponseEntity<>(session, HttpStatus.OK);
        else return new ResponseEntity<>("Id Not Found!", HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestParam String username, @RequestParam Long computerId){
        try{
            Session session = sessionService.startSession(username, computerId);
            return new ResponseEntity<>(session, HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/end/{id}")
    public ResponseEntity<?> endSession(@PathVariable(name = "id") Long id){
        try {
            Session session = sessionService.endSession(id);
            return new ResponseEntity<>(session, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable(name = "username") String username){
        List<Session> list = sessionRepository.findByUserUsername(username);
        if(!list.isEmpty()) return new ResponseEntity<>(list, HttpStatus.OK);
        else return new ResponseEntity<>("Username not found!!!", HttpStatus.BAD_REQUEST);
    }

}
