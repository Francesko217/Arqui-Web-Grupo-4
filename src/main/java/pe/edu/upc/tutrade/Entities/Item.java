package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItem;

    @Column(name="titleItem",length=120,nullable = false)
    private String titleItem;

    @Column(name ="descriptionItem",length=255,nullable = false)
    private String descriptionItem;

    @Column(name = "conditionItem",nullable = false)
    private int conditionItem;

    @Column(name="statusItem",nullable= false)
    private int statusItem;
    @ManyToOne
    @JoinColumn(name="idUser")
    private User user;
    @ManyToOne
    @JoinColumn(name="idUCategory")
    private Category category;

    public Item() {
    }

    public Item(int idItem, String titleItem, String descriptionItem, int conditionItem, int statusItem, User user, Category category) {
        this.idItem = idItem;
        this.titleItem = titleItem;
        this.descriptionItem = descriptionItem;
        this.conditionItem = conditionItem;
        this.statusItem = statusItem;
        this.user = user;
        this.category = category;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getTitleItem() {
        return titleItem;
    }

    public void setTitleItem(String titleItem) {
        this.titleItem = titleItem;
    }

    public String getDescriptionItem() {
        return descriptionItem;
    }

    public void setDescriptionItem(String descriptionItem) {
        this.descriptionItem = descriptionItem;
    }

    public int getConditionItem() {
        return conditionItem;
    }

    public void setConditionItem(int conditionItem) {
        this.conditionItem = conditionItem;
    }

    public int getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(int statusItem) {
        this.statusItem = statusItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
