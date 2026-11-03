package com.quannet.demo.repository;

import com.quannet.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "select * from users u where (u.username like %:username% or :username is null) and (u.password = :password or :password is null) and (u.balance = :balance or :balance is null) and (u.tier like %:tier% or :tier is null)",
            countQuery = "select count(*) from users u where (u.username like %:username% or :username is null) and (u.password = :password or :password is null) and (u.balance = :balance or :balance is null) and (u.tier like %:tier% or :tier is null)", nativeQuery = true)
    Page<User> searchUser(@Param("username") String username, @Param("password") String password, @Param("balance")
    Double balance, @Param("tier") String tier, Pageable pageable);
}
