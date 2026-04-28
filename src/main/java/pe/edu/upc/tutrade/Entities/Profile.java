package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name="Profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProfile;

    @Column(name = "first_nameProfile",length = 80,nullable = false)
    private String first_nameProfile;

    @Column(name = "last_nameProfile",length = 80,nullable = false)
    private String last_nameProfile;

    @Column(name="phoneProfile",length=20,nullable = false)
    private String phoneProfile;

    @Column(name = "bioProfile",length=255,nullable=false)
    private String bioProfile;

    @Column(name = "birth_dateProfile",nullable=false)
    private LocalDate birth_dateProfile;

    @Column
    private String avatar_urlProfile;
    @ManyToOne
    @JoinColumn(name="idUser")
    private User user;

    public Profile() {
    }

    public Profile(int idProfile, String last_nameProfile, String first_nameProfile, String phoneProfile, String bioProfile, LocalDate birth_dateProfile, String avatar_urlProfile, User user) {
        this.idProfile = idProfile;
        this.last_nameProfile = last_nameProfile;
        this.first_nameProfile = first_nameProfile;
        this.phoneProfile = phoneProfile;
        this.bioProfile = bioProfile;
        this.birth_dateProfile = birth_dateProfile;
        this.avatar_urlProfile = avatar_urlProfile;
        this.user = user;
    }

    public int getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }

    public String getFirst_nameProfile() {
        return first_nameProfile;
    }

    public void setFirst_nameProfile(String first_nameProfile) {
        this.first_nameProfile = first_nameProfile;
    }

    public String getLast_nameProfile() {
        return last_nameProfile;
    }

    public void setLast_nameProfile(String last_nameProfile) {
        this.last_nameProfile = last_nameProfile;
    }

    public String getPhoneProfile() {
        return phoneProfile;
    }

    public void setPhoneProfile(String phoneProfile) {
        this.phoneProfile = phoneProfile;
    }

    public String getBioProfile() {
        return bioProfile;
    }

    public void setBioProfile(String bioProfile) {
        this.bioProfile = bioProfile;
    }

    public LocalDate getBirth_dateProfile() {
        return birth_dateProfile;
    }

    public void setBirth_dateProfile(LocalDate birth_dateProfile) {
        this.birth_dateProfile = birth_dateProfile;
    }

    public String getAvatar_urlProfile() {
        return avatar_urlProfile;
    }

    public void setAvatar_urlProfile(String avatar_urlProfile) {
        this.avatar_urlProfile = avatar_urlProfile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
