package com.quannet.demo.repository;

import com.quannet.demo.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {
    List<Computer> findByArea(String area);
}
