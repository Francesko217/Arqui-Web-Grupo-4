package pe.edu.upc.tutrade.DTOs;

import java.time.LocalDate;

public class ProfileResponseDTO {
    private int idProfile;
    private String firstName;
    private String lastName;
    private String phone;
    private String bio;
    private LocalDate birthDate;
    private String avatarUrl;
    private int age;
    private int userId;

    public int getIdProfile() { return idProfile; }
    public void setIdProfile(int idProfile) { this.idProfile = idProfile; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
