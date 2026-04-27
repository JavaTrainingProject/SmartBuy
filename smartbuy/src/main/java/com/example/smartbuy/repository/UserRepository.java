package com.example.smartbuy.repository;

import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

        Optional<UserEntity> findByEmail(String email);

        Optional<UserEntity> findByRole(Role role);

        Optional <UserEntity> findByRefreshToken(String refreshtoken);

        boolean existsByEmail(String email);
}
