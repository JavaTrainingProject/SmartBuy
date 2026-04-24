
package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.UserResponseDto;
import com.example.smartbuy.dtos.UserUpdateRequestDto;
import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.exception.UserAlreadyExistsException;
import com.example.smartbuy.exception.UserNotFoundException;
import com.example.smartbuy.mapper.UserMapper;
import com.example.smartbuy.repository.UserRepository;
import com.example.smartbuy.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }


    @Override
    public UserResponseDto getUserById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toDto(user);
    }


    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto dto) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setUser_name(dto.getName());
        }


        if (dto.getEmail() != null && !dto.getEmail().equals(user.getUser_email())) {

            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists");
            }

            user.setUser_email(dto.getEmail());
        }

        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
    }


    @Override
    public void deleteUser(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
    }
}