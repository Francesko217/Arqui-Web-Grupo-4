package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;

    @Column(name = "emailUser",length = 150, nullable=false)
    private String emailUser;

    @Column(name = "password_hashUser",length=255,nullable = false)
    private String password_hashUser;

    @Column(name="usernameUser", length = 50,nullable = false)
    private String usernameUser;

    @Column(name="is_premiumUser",nullable = false)
    private Boolean is_premiumUser;

    @Column(name = "is_verifiedUser",nullable = false)
    private Boolean is_verifiedUser;

    @Column(name="created_atUser",nullable = false)
    private LocalDate created_atUser;

    @Column(name="updated_atUser",nullable = false)
    private LocalDate updated_atUser;

    @ManyToOne
    @JoinColumn(name="idRole")
    private Role role;

    public User() {
    }

    public User(int idUser, String password_hashUser, String emailUser, String usernameUser, Boolean is_premiumUser, Boolean is_verifiedUser, LocalDate created_atUser, LocalDate updated_atUser, Role role) {
        this.idUser = idUser;
        this.password_hashUser = password_hashUser;
        this.emailUser = emailUser;
        this.usernameUser = usernameUser;
        this.is_premiumUser = is_premiumUser;
        this.is_verifiedUser = is_verifiedUser;
        this.created_atUser = created_atUser;
        this.updated_atUser = updated_atUser;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPassword_hashUser() {
        return password_hashUser;
    }

    public void setPassword_hashUser(String password_hashUser) {
        this.password_hashUser = password_hashUser;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public Boolean getIs_premiumUser() {
        return is_premiumUser;
    }

    public void setIs_premiumUser(Boolean is_premiumUser) {
        this.is_premiumUser = is_premiumUser;
    }

    public Boolean getIs_verifiedUser() {
        return is_verifiedUser;
    }

    public void setIs_verifiedUser(Boolean is_verifiedUser) {
        this.is_verifiedUser = is_verifiedUser;
    }

    public LocalDate getCreated_atUser() {
        return created_atUser;
    }

    public void setCreated_atUser(LocalDate created_atUser) {
        this.created_atUser = created_atUser;
    }

    public LocalDate getUpdated_atUser() {
        return updated_atUser;
    }

    public void setUpdated_atUser(LocalDate updated_atUser) {
        this.updated_atUser = updated_atUser;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
