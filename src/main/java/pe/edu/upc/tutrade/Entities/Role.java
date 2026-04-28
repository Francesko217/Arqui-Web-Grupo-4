package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

@Entity
@Table(name="Role")
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idRole;

    @Column(name="nameRole",length = 50,nullable = false)
    private String nameRole;

    @Column(name  ="descriptionRole",length = 255,nullable = false)
    private String descriptionRole;

    public Role() {
    }

    public Role(int idRole, String nameRole, String descriptionRole) {
        this.idRole = idRole;
        this.nameRole = nameRole;
        this.descriptionRole = descriptionRole;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getDescriptionRole() {
        return descriptionRole;
    }

    public void setDescriptionRole(String descriptionRole) {
        this.descriptionRole = descriptionRole;
    }
}
