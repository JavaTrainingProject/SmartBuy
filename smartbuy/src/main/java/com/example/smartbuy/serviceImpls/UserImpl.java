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
import java.util.HashMap;
import java.util.Map;

@Service
public class UserImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private Map<String, UserRequestDto> tempUsers = new HashMap<>();
    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, LocalDateTime> otpExpiryMap = new HashMap<>();


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

        if (userRepository.findByEmail(dto.getEmail()).isPresent() || tempUsers.containsKey(dto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        tempUsers.put(dto.getEmail(),dto);
        otpStorage.put(dto.getEmail(),otp);

        emailService.sendOtpEmail(dto.getEmail(), otp);
        otpExpiryMap.put(dto.getEmail(), LocalDateTime.now().plusMinutes(5));

        UserResponseDto response = new UserResponseDto();

        response.setUser_name(dto.getUser_name());
        response.setEmail(dto.getEmail());
        response.setOtp(otp);

        return response;
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

      String storedOtp = otpStorage.get(email);

      if(storedOtp == null){
          return "No OTP found";
      }

      if(!storedOtp.equals(otp)){
          return "Invalid OTP";
      }

      UserRequestDto dto=tempUsers.get(email);

      if(dto ==null){
          return  "User data not found";
      }

        LocalDateTime expiry = otpExpiryMap.get(email);

        if (expiry == null || expiry.isBefore(LocalDateTime.now())) {
            return "OTP expired";
        }

      UserEntity user = UserMapper.toEntity(dto);
      user.setPassword(passwordEncoder.encode(dto.getUser_password()));
      user.setRole(Role.USER);
      userRepository.save(user);

      tempUsers.remove(email);
      otpStorage.remove(email);
      otpExpiryMap.remove(email);

        return "Verified successfully";
    }

    @Override
    public String resendOtp(String email) {

        if(!tempUsers.containsKey(email))
            throw new RuntimeException("User not found");

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        otpStorage.put(email,otp);

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
