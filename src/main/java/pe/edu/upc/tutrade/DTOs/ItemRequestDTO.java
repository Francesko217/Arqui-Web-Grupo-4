package pe.edu.upc.tutrade.DTOs;

public class ItemRequestDTO {
    private String titleItem;
    private String descriptionItem;
    private int conditionItem;
    private int statusItem; // 1=Disponible, 2=Pausado, 3=Intercambiado
    private int categoryId;

    public String getTitleItem() { return titleItem; }
    public void setTitleItem(String titleItem) { this.titleItem = titleItem; }

    public String getDescriptionItem() { return descriptionItem; }
    public void setDescriptionItem(String descriptionItem) { this.descriptionItem = descriptionItem; }

    public int getConditionItem() { return conditionItem; }
    public void setConditionItem(int conditionItem) { this.conditionItem = conditionItem; }

    public int getStatusItem() { return statusItem; }
    public void setStatusItem(int statusItem) { this.statusItem = statusItem; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}
