package com.example.smartbuy.repository;

import com.example.smartbuy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.user_email = :email")
    boolean existsByEmail(@Param("email") String email);
}