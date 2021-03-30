package com.trendyol.demo.userconsumer.repository;


import com.trendyol.demo.userconsumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
