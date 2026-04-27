package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.*;
import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.enums.Role;
import com.example.smartbuy.exception.InvalidCredentialsException;
import com.example.smartbuy.exception.UserAlreadyExistsException;
import com.example.smartbuy.exception.UserNotFoundException;
import com.example.smartbuy.mapper.UserMapper;
import com.example.smartbuy.repository.UserRepository;
import com.example.smartbuy.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;



    public UserImpl(UserRepository userRepository,
                    PasswordEncoder passwordEncoder,
                    JWTService jwtService,
                    AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;

    }

    @Override
    public UserResponseDto registerUser(UserRequestDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        UserEntity user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto registerAdmin(UserRequestDto dto) {

        if (userRepository.findByRole(Role.ADMIN).isPresent()) {
            throw new UserAlreadyExistsException("Admin already exists");
        }

        UserEntity user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);

        return userMapper.toDto(userRepository.save(user));
    }


    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        String refreshToken = jwtService.createRefreshToken(user.getEmail());

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setId(user.getId());
        response.setRole(user.getRole());

        return response;
    }

    @Override
    public LoginResponseDto refreshToken(RefreshRequestDto dto) {

        UserEntity user = jwtService.verifyRefreshToken(dto.getRefreshToken());

        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getRole());

        String newRefreshToken = jwtService.createRefreshToken(user.getEmail());

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setId(user.getId());
        response.setRole(user.getRole());

        return response;
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


        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists");
            }

            user.setEmail(dto.getEmail());
        }


        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword()); // later use encoder
        }


        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
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
