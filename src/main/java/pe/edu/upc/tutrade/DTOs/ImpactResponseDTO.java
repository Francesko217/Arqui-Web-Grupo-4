package pe.edu.upc.tutrade.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * HU08 - Impacto Ambiental (CO2e). Resumen del CO2e evitado por los trueques completados
 * (ACCEPTED). Solo CO2e: es lo que Climatiq provee de forma real.
 */
public class ImpactResponseDTO {
    private double co2SavedKg;     // kg de CO2e evitados
    private int itemsReused;       // nº de artículos que cambiaron de dueño (reutilizados)
    private int tradesCompleted;   // nº de trueques completados que aportan al cálculo
    private String source;         // origen del cálculo: "climatiq" o "estimado"
    private List<CategoryImpactDTO> breakdown = new ArrayList<>();

    public double getCo2SavedKg() { return co2SavedKg; }
    public void setCo2SavedKg(double co2SavedKg) { this.co2SavedKg = co2SavedKg; }

    public int getItemsReused() { return itemsReused; }
    public void setItemsReused(int itemsReused) { this.itemsReused = itemsReused; }

    public int getTradesCompleted() { return tradesCompleted; }
    public void setTradesCompleted(int tradesCompleted) { this.tradesCompleted = tradesCompleted; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public List<CategoryImpactDTO> getBreakdown() { return breakdown; }
    public void setBreakdown(List<CategoryImpactDTO> breakdown) { this.breakdown = breakdown; }

    /** Desglose del CO2e por categoría de artículo. */
    public static class CategoryImpactDTO {
        private String category;
        private int items;
        private double co2SavedKg;

        public CategoryImpactDTO() {}

        public CategoryImpactDTO(String category) {
            this.category = category;
        }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public int getItems() { return items; }
        public void setItems(int items) { this.items = items; }

        public double getCo2SavedKg() { return co2SavedKg; }
        public void setCo2SavedKg(double co2SavedKg) { this.co2SavedKg = co2SavedKg; }
    }
}
