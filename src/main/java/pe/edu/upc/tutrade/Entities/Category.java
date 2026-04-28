package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

@Entity
@Table(name="Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategory;

    @Column(name = "nameCategory",length=60,nullable = false)
    private String nameCategory;

    @Column(name="parent_idCategory",nullable = false)
    private int parent_idCategory;

    public Category() {
    }

    public Category(int idCategory, String nameCategory, int parent_idCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.parent_idCategory = parent_idCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public int getParent_idCategory() {
        return parent_idCategory;
    }

    public void setParent_idCategory(int parent_idCategory) {
        this.parent_idCategory = parent_idCategory;
    }
}
