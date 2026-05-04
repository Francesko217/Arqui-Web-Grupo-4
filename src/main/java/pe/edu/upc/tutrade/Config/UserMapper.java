package pe.edu.upc.tutrade.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IProfileRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.ServicesImplements.ProfileServiceImplement;

@Component
public class UserMapper {

    @Autowired
    private IProfileRepository profileRepo;

    @Autowired
    private ITradeRepository tradeRepo;

    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(user.getIdUser());
        dto.setEmailUser(user.getEmailUser());
        dto.setUsernameUser(user.getUsernameUser());
        dto.setIs_premiumUser(user.getIs_premiumUser());
        dto.setIs_verifiedUser(user.getIs_verifiedUser());
        dto.setIs_enabledUser(user.getIs_enabledUser());
        dto.setCreated_atUser(user.getCreated_atUser());
        dto.setUpdated_atUser(user.getUpdated_atUser());
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
}
