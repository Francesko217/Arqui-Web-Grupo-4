package pe.edu.upc.tutrade.DTOs;

import pe.edu.upc.tutrade.Entities.Category;

public class ItemResponseDTO {
    private int idItem;
    private String titleItem;
    private String descriptionItem;
    private int conditionItem;
    private int statusItem;
    private UserResponseDTO user;
    private Category category;

    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public String getTitleItem() { return titleItem; }
    public void setTitleItem(String titleItem) { this.titleItem = titleItem; }

    public String getDescriptionItem() { return descriptionItem; }
    public void setDescriptionItem(String descriptionItem) { this.descriptionItem = descriptionItem; }

    public int getConditionItem() { return conditionItem; }
    public void setConditionItem(int conditionItem) { this.conditionItem = conditionItem; }

    public int getStatusItem() { return statusItem; }
    public void setStatusItem(int statusItem) { this.statusItem = statusItem; }

    public UserResponseDTO getUser() { return user; }
    public void setUser(UserResponseDTO user) { this.user = user; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
