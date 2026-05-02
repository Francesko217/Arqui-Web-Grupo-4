package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.LoginRequest;
import pe.edu.upc.tutrade.DTOs.LoginResponse;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.Security.JwtUtil;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            userRepository.findByEmailUser(auth.getName())
                    .ifPresent(user -> {
                        user.setLast_loginUser(LocalDateTime.now());
                        userRepository.save(user);
                    });
            String token = jwtUtil.generateToken(auth.getName());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (DisabledException e) {
            return ResponseEntity.status(403).body("Cuenta deshabilitada");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
}
