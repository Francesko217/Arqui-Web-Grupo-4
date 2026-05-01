package pe.edu.upc.tutrade.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.upc.tutrade.Entities.Role;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IRoleRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmailUser("biney-debug@trade.com").isEmpty()) {
            Role adminRole = roleRepository.findByNameRole("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

            User admin = new User();
            admin.setEmailUser("biney-debug@trade.com");
            admin.setPassword_hashUser(passwordEncoder.encode("biney"));
            admin.setUsernameUser("biney-debug");
            admin.setIs_premiumUser(true);
            admin.setIs_verifiedUser(true);
            admin.setRole(adminRole);
            admin.setCreated_atUser(LocalDate.now());
            admin.setUpdated_atUser(LocalDate.now());
            userRepository.save(admin);
        }
    }
}
