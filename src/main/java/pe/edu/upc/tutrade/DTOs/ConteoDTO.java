package pe.edu.upc.tutrade.DTOs;

public class ConteoDTO {
    private String etiqueta;
    private Long cantidad;

    public ConteoDTO(String etiqueta, Long cantidad) {
        this.etiqueta = etiqueta;
        this.cantidad = cantidad;
    }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
}
