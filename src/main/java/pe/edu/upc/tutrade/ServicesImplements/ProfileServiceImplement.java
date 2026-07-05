package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ProfileRequestDTO;
import pe.edu.upc.tutrade.DTOs.ProfileResponseDTO;
import pe.edu.upc.tutrade.Entities.Profile;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IProfileRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IProfileService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImplement implements IProfileService {

    @Autowired
    private IProfileRepository profileRepo;

    @Autowired
    private IUserRepository userRepo;

    public static ProfileResponseDTO toDTO(Profile profile) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setIdProfile(profile.getIdProfile());
        dto.setFirstName(profile.getFirst_nameProfile());
        dto.setLastName(profile.getLast_nameProfile());
        dto.setPhone(profile.getPhoneProfile());
        dto.setBio(profile.getBioProfile());
        dto.setBirthDate(profile.getBirth_dateProfile());
        dto.setAvatarUrl(profile.getAvatar_urlProfile());
        dto.setAge(Period.between(profile.getBirth_dateProfile(), LocalDate.now()).getYears());
        dto.setUserId(profile.getUser().getIdUser());
        return dto;
    }

    private static void validateProfileFields(ProfileRequestDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        if (dto.getBirthDate() == null) {
            throw new RuntimeException("La fecha de nacimiento es obligatoria");
        }
        if (dto.getBirthDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha de nacimiento no puede ser en el futuro");
        }
        int edad = Period.between(dto.getBirthDate(), LocalDate.now()).getYears();
        if (edad < 13) {
            throw new RuntimeException("Debes tener al menos 13 años para crear un perfil");
        }
        if (edad > 120) {
            throw new RuntimeException("Fecha de nacimiento inválida");
        }
    }

    @Override
    public ProfileResponseDTO create(ProfileRequestDTO dto, String email) {
        User user = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (profileRepo.findByUser_IdUser(user.getIdUser()).isPresent()) {
            throw new RuntimeException("Ya tienes un perfil creado");
        }

        validateProfileFields(dto);

        Profile profile = new Profile();
        profile.setFirst_nameProfile(dto.getFirstName());
        profile.setLast_nameProfile(dto.getLastName());
        profile.setPhoneProfile(dto.getPhone());
        profile.setBioProfile(dto.getBio());
        profile.setBirth_dateProfile(dto.getBirthDate());
        profile.setAvatar_urlProfile(dto.getAvatarUrl());
        profile.setUser(user);

        return toDTO(profileRepo.save(profile));
    }

    @Override
    public ProfileResponseDTO getByUserId(int userId) {
        Profile profile = profileRepo.findByUser_IdUser(userId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado para el usuario " + userId));
        return toDTO(profile);
    }

    @Override
    public ProfileResponseDTO getMyProfile(String email) {
        User user = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Profile profile = profileRepo.findByUser_IdUser(user.getIdUser())
                .orElseThrow(() -> new RuntimeException("Aún no tienes un perfil creado"));
        return toDTO(profile);
    }

    @Override
    public ProfileResponseDTO update(int profileId, ProfileRequestDTO dto, String email) {
        Profile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        User requester = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");

        if (!isAdmin && profile.getUser().getIdUser() != requester.getIdUser()) {
            throw new RuntimeException("No tienes permiso para modificar este perfil");
        }

        validateProfileFields(dto);

        profile.setFirst_nameProfile(dto.getFirstName());
        profile.setLast_nameProfile(dto.getLastName());
        profile.setPhoneProfile(dto.getPhone());
        profile.setBioProfile(dto.getBio());
        profile.setBirth_dateProfile(dto.getBirthDate());
        profile.setAvatar_urlProfile(dto.getAvatarUrl());

        return toDTO(profileRepo.save(profile));
    }

    @Override
    public void delete(int profileId, String email) {
        Profile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        User requester = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");

        if (!isAdmin && profile.getUser().getIdUser() != requester.getIdUser()) {
            throw new RuntimeException("No tienes permiso para eliminar este perfil");
        }

        profileRepo.deleteById(profileId);
    }

    @Override
    public List<ProfileResponseDTO> listAll() {
        return profileRepo.findAll().stream()
                .map(ProfileServiceImplement::toDTO)
                .collect(Collectors.toList());
    }
}
