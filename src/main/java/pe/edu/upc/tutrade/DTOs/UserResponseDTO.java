package pe.edu.upc.tutrade.DTOs;

import pe.edu.upc.tutrade.Entities.Role;

import java.time.LocalDate;

public class UserResponseDTO {
    private int idUser;
    private String emailUser;
    private String usernameUser;
    private Boolean is_premiumUser;
    private Boolean is_verifiedUser;
    private LocalDate created_atUser;
    private LocalDate updated_atUser;
    private Role role;
    private ProfileResponseDTO profile;
    private Boolean is_enabledUser;
    private boolean veteran;

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getEmailUser() { return emailUser; }
    public void setEmailUser(String emailUser) { this.emailUser = emailUser; }

    public String getUsernameUser() { return usernameUser; }
    public void setUsernameUser(String usernameUser) { this.usernameUser = usernameUser; }

    public Boolean getIs_premiumUser() { return is_premiumUser; }
    public void setIs_premiumUser(Boolean is_premiumUser) { this.is_premiumUser = is_premiumUser; }

    public Boolean getIs_verifiedUser() { return is_verifiedUser; }
    public void setIs_verifiedUser(Boolean is_verifiedUser) { this.is_verifiedUser = is_verifiedUser; }

    public LocalDate getCreated_atUser() { return created_atUser; }
    public void setCreated_atUser(LocalDate created_atUser) { this.created_atUser = created_atUser; }

    public LocalDate getUpdated_atUser() { return updated_atUser; }
    public void setUpdated_atUser(LocalDate updated_atUser) { this.updated_atUser = updated_atUser; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public ProfileResponseDTO getProfile() { return profile; }
    public void setProfile(ProfileResponseDTO profile) { this.profile = profile; }

    public Boolean getIs_enabledUser() { return is_enabledUser; }
    public void setIs_enabledUser(Boolean is_enabledUser) { this.is_enabledUser = is_enabledUser; }

    public boolean isVeteran() { return veteran; }
    public void setVeteran(boolean veteran) { this.veteran = veteran; }
}
