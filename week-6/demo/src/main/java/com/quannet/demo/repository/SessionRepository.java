package com.quannet.demo.repository;

import com.quannet.demo.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
//    Optional<Session> findByUsername(String username);
    List<Session> findByUserUsername(String username);
}
