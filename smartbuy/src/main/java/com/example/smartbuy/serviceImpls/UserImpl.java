package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.*;
import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.enums.Role;
import com.example.smartbuy.exception.InvalidCredentialsException;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.exception.UserAlreadyExistsException;
import com.example.smartbuy.mapper.UserMapper;
import com.example.smartbuy.repository.UserRepository;
import com.example.smartbuy.service.EmailService;
import com.example.smartbuy.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    public UserImpl(UserRepository userRepository,
                    PasswordEncoder passwordEncoder,
                    JWTService jwtService,
                    AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;

    }

    @Override
    public UserResponseDto registerUser(UserRequestDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);// for otp


        UserEntity user = UserMapper.toEntity(dto);
        user.setUser_name(dto.getUser_name());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setVerified(false);

        //userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);


        return UserMapper.toDto( userRepository.save(user));
    }

    @Override
    public UserResponseDto registerAdmin(UserRequestDto dto) {

        if (userRepository.findByRole(Role.ADMIN).isPresent()) {
            throw new UserAlreadyExistsException("Admin already exists");
        }

        UserEntity user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getUser_password()));
        user.setRole(Role.ADMIN);

        return UserMapper.toDto(userRepository.save(user));
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


        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

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


    public MessageResponseDto logout(HttpServletRequest request){

        String authHeader=request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            throw new InvalidCredentialsException("Token is missing or invalid");
        }

        String token=authHeader.substring(7);
        String email=jwtService.extractClaims(token).getSubject();

        UserEntity user=userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User","email",email));

        user.setRefreshToken(null);
        userRepository.save(user);

        return new MessageResponseDto("Logged out successfully");
    }

    @Override
    public String verifyOtp(String email, String otp) {

        System.out.println("VERIFY API CALLED");

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP expired";
        }

        if (!user.getOtp().equals(otp)) {
            return "Invalid OTP";
        }

        user.setVerified(false);
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return "Verified successfully";
    }

    @Override
    public String resendOtp(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);

        return "OTP resent";
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toDto);
    }
    @Override
    public UserResponseDto getUserById(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return UserMapper.toDto(user);
    }


    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto dto) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        user.setUser_name(dto.getName());

        if (dto.getEmail() != null &&
                !dto.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists");
            }

            user.setEmail(dto.getEmail());
        }

        user.setUpdatedAt(LocalDateTime.now());

        return UserMapper.toDto(userRepository.save(user));
    }


    @Override
    public void deleteUser(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

}
