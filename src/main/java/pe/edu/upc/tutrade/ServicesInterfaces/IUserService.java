package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User insert(User user);
    List<User> list();
    Optional<User> listId(int id);
    void delete(int id);

    // HU20
    List<UserResponseDTO> listActiveUsers();
    // HU28
    List<UserResponseDTO> listInactiveUsers(int inactiveDays);
    void disableUser(int userId);
    void enableUser(int userId);
    // HU38
    List<UserResponseDTO> searchByUsername(String username);
    // HU50
    List<UserResponseDTO> listVeteranUsers();
    // KYC
    void verifyUser(int userId);
    // Premium
    void togglePremium(int userId);
    // HU06: el propio usuario activa su suscripción premium
    void subscribePremium(String email);
}
