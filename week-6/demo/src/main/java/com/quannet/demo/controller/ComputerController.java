package com.quannet.demo.controller;

import com.quannet.demo.dto.ComputerDto;
import com.quannet.demo.model.Computer;
import com.quannet.demo.repository.ComputerRepository;
import com.quannet.demo.service.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/computers")

public class ComputerController {
    @Autowired
    ComputerRepository computerRepository;
    @Autowired
    ComputerService computerService;
    @PostMapping("/add")
    //nho them phan quyen
    public ResponseEntity<?> saveComputer(@RequestBody ComputerDto computerDto){
        computerService.save(computerDto.convertToModel());
        return new ResponseEntity<>("oke", HttpStatus.OK);
    }
    @PostMapping("/add-all")
    //nho them phan quyen
    public ResponseEntity<?> saveAllComputers(@RequestBody ComputerDto[] computerDtos){
        for (ComputerDto computerDto : computerDtos) {
            computerService.save(computerDto.convertToModel());
        }
        return new ResponseEntity<>("oke", HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Computer> computer = computerRepository.findById(id);
        if(computer.isPresent()) return new ResponseEntity<>(computer, HttpStatus.OK);
        else return new ResponseEntity<>("Id Not Found!", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllComputer(){
        List<Computer> computerList = computerRepository.findAll();
        return new ResponseEntity<>(computerList, HttpStatus.OK);
    }
    //nen phan trang?
    @GetMapping("/get/{area}")
    public ResponseEntity<?> getAreaComputer(@PathVariable(name = "area") String area){
        List<Computer> list = computerRepository.findByArea(area);
        if(!list.isEmpty()) return new ResponseEntity<>(list, HttpStatus.OK);
        else return new ResponseEntity<>("Area not found!!!", HttpStatus.BAD_REQUEST);
    }
}
