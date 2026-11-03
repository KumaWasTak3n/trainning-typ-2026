package com.quannet.demo.service;

import com.quannet.demo.model.Computer;
import com.quannet.demo.model.Session;
import com.quannet.demo.model.User;
import com.quannet.demo.repository.ComputerRepository;
import com.quannet.demo.repository.SessionRepository;
import com.quannet.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ComputerRepository computerRepository;

    @Transactional
    @Override
    public void save(Session session){
        sessionRepository.save(session);
    }

    @Transactional
    @Override
    public Session startSession(String username, Long computerId){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("Account Not Found!!!");
        }
        User user = optionalUser.get();
        if(user.getBalance() <= 0) {
            throw new RuntimeException("Account balance not enough to play! Current balance: " + user.getBalance());
        }

        Optional<Computer> optionalComputer = computerRepository.findById(computerId);
        if(optionalComputer.isEmpty()){
            throw new RuntimeException("Computer id " + computerId + " not found!!!");
        }
        Computer computer = optionalComputer.get();
        if(!computer.getStatus().equals("AVAILABLE")){
            throw new RuntimeException("Computer is already " + computer.getStatus() + "!!!");
        }
        computer.setStatus("OCCUPIED");
        computerRepository.save(computer);
        Session session = Session.builder()
                .user(user)
                .computer(computer)
                .startTime(LocalDateTime.now())
                .status("ACTIVE")
                .totalTime(0L)
                .totalCost(0.0)
                .build();
        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public Session endSession(Long id){
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            throw new RuntimeException("Session Id" + id + " not found!!!");
        }
        Session session = optionalSession.get();
        if (!session.getStatus().equals("ACTIVE")){
            throw new RuntimeException("Session is not ACTIVE!");
        }

        LocalDateTime endTime = LocalDateTime.now();
        session.setEndTime(endTime);
        long minutes = Math.max(1, Duration.between(session.getStartTime(), endTime).toMinutes());
        session.setTotalTime(minutes);
        Computer computer = session.getComputer();
        double costPerHour = computer.getCost();
        double totalCost = (minutes / 60.0) * costPerHour;
        session.setTotalCost(totalCost);
        session.setStatus("ENDED");

        User user = session.getUser();
        user.setBalance(user.getBalance() - totalCost);
        userRepository.save(user);

        computer.setStatus("AVAILABLE");
        computerRepository.save(computer);

        return sessionRepository.save(session);
    }
}
