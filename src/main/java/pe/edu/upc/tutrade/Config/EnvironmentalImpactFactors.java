package pe.edu.upc.tutrade.Config;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HU08 - Impacto Ambiental (CO2e). Valores de RESPALDO por categoría, usados solo si la llamada
 * a Climatiq falla o está desactivada. NO son inventados: son el resultado real de Climatiq
 * (/estimate, EPA/US, data_version ^6, spend-based) para el precio medio de cada categoría,
 * cacheado aquí para que la feature no muestre 0 si la API no responde.
 *
 * El enfoque del feature es CO2e únicamente (Climatiq no provee agua ni residuos y no hay una
 * API gratuita que los dé por producto, así que esas métricas se eliminaron).
 */
@Component
public class EnvironmentalImpactFactors {

    // CO2e (kg) evitado por artículo reutilizado, por categoría. Snapshot real de Climatiq.
    private static final double DEFAULT_CO2 = 10.0; // categoría desconocida

    // clave (palabra contenida en el nombre de la categoría) -> kg CO2e por artículo
    private final Map<String, Double> table = new LinkedHashMap<>();

    public EnvironmentalImpactFactors() {
        table.put("ropa", 5.64);
        table.put("cloth", 5.64);
        table.put("electr", 16.2);
        table.put("tecn", 16.2);
        table.put("mueble", 28.2);
        table.put("furnitur", 28.2);
        table.put("deporte", 10.02);
        table.put("sport", 10.02);
        table.put("hogar", 6.28);
        table.put("home", 6.28);
        table.put("libro", 3.54);
        table.put("book", 3.54);
    }

    /** CO2e (kg) de respaldo para una categoría, buscando por palabra clave en su nombre. */
    public double co2For(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return DEFAULT_CO2;
        }
        String n = categoryName.toLowerCase();
        for (Map.Entry<String, Double> e : table.entrySet()) {
            if (n.contains(e.getKey())) {
                return e.getValue();
            }
        }
        return DEFAULT_CO2;
    }
}
