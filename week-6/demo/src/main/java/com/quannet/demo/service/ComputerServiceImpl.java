package com.quannet.demo.service;

import com.quannet.demo.model.Computer;
import com.quannet.demo.repository.ComputerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComputerServiceImpl implements ComputerService{
    @Autowired
    ComputerRepository computerRepository;

    @Transactional
    @Override
    public void save(Computer computer){
        computerRepository.save(computer);
    }

}
