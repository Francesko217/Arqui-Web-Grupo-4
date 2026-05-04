package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.Profile;
import pe.edu.upc.tutrade.Entities.Role;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IProfileRepository;
import pe.edu.upc.tutrade.Repositories.IRoleRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImplement implements IUserService {

    @Autowired
    private IUserRepository uR;

    @Autowired
    private IRoleRepository rR;

    @Autowired
    private IProfileRepository profileRepo;

    @Autowired
    private ITradeRepository tradeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(user.getIdUser());
        dto.setEmailUser(user.getEmailUser());
        dto.setUsernameUser(user.getUsernameUser());
        dto.setIs_premiumUser(user.getIs_premiumUser());
        dto.setIs_verifiedUser(user.getIs_verifiedUser());
        dto.setCreated_atUser(user.getCreated_atUser());
        dto.setUpdated_atUser(user.getUpdated_atUser());
        dto.setIs_enabledUser(user.getIs_enabledUser());
        dto.setRole(user.getRole());
        profileRepo.findByUser_IdUser(user.getIdUser())
                .ifPresent(p -> dto.setProfile(ProfileServiceImplement.toDTO(p)));
        long acceptedTrades = tradeRepo.findByProposer_IdUser(user.getIdUser()).stream()
                .filter(t -> t.getStatusTrade().equals("ACCEPTED")).count()
                + tradeRepo.findByReceiver_IdUser(user.getIdUser()).stream()
                .filter(t -> t.getStatusTrade().equals("ACCEPTED")).count();
        dto.setVeteran(acceptedTrades >= 3);
        return dto;
    }

    @Override
    public User insert(User user) {
        if (uR.findByEmailUser(user.getEmailUser()).isPresent()) {
            throw new RuntimeException("Email ya registrado");
        }
        Role clientRole = rR.findByNameRole("CLIENT")
                .orElseThrow(() -> new RuntimeException("Rol CLIENT no encontrado"));

        user.setPassword_hashUser(passwordEncoder.encode(user.getPassword_hashUser()));
        user.setRole(clientRole);
        user.setIs_premiumUser(false);
        user.setIs_verifiedUser(false);
        user.setIs_enabledUser(true);
        user.setCreated_atUser(LocalDate.now());
        user.setUpdated_atUser(LocalDate.now());
        return uR.save(user);
    }

    @Override
    public List<User> list() {
        return uR.findAll();
    }

    @Override
    public Optional<User> listId(int id) {
        return uR.findById(id);
    }

    @Override
    public void delete(int id) {
        uR.deleteById(id);
    }

    @Override
    public List<UserResponseDTO> listActiveUsers() {
        return uR.findActiveUsers().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> listInactiveUsers(int inactiveDays) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(inactiveDays);
        return uR.findInactiveUsers(cutoff).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void disableUser(int userId) {
        User user = uR.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setIs_enabledUser(false);
        user.setUpdated_atUser(LocalDate.now());
        uR.save(user);
    }

    @Override
    public void enableUser(int userId) {
        User user = uR.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setIs_enabledUser(true);
        user.setUpdated_atUser(LocalDate.now());
        uR.save(user);
    }

    @Override
    public List<UserResponseDTO> searchByUsername(String username) {
        return uR.findByUsernameUserContainingIgnoreCase(username).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> listVeteranUsers() {
        return uR.findVeteranUsers().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void verifyUser(int userId) {
        User user = uR.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setIs_verifiedUser(true);
        user.setUpdated_atUser(LocalDate.now());
        uR.save(user);
    }

    @Override
    public void togglePremium(int userId) {
        User user = uR.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setIs_premiumUser(!user.getIs_premiumUser());
        user.setUpdated_atUser(LocalDate.now());
        uR.save(user);
    }
}
