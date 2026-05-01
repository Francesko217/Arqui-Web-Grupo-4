package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.ProfileRequestDTO;
import pe.edu.upc.tutrade.DTOs.ProfileResponseDTO;

import java.util.List;

public interface IProfileService {
    ProfileResponseDTO create(ProfileRequestDTO dto, String email);
    ProfileResponseDTO getByUserId(int userId);
    ProfileResponseDTO getMyProfile(String email);
    ProfileResponseDTO update(int profileId, ProfileRequestDTO dto, String email);
    void delete(int profileId, String email);
    List<ProfileResponseDTO> listAll();
}
