package pe.edu.upc.tutrade.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HU08 - Cliente real de la API de Climatiq para estimar CO2e.
 *
 * La API key se lee de la propiedad climatiq.api.key (var de entorno CLIMATIQ_API_KEY, con un
 * default en application.properties). Si está vacía, el cliente queda DESACTIVADO y el cálculo
 * usa los factores locales ({@link EnvironmentalImpactFactors}) como respaldo.
 *
 * Los activity_id de abajo están VERIFICADOS contra la API real (data_version ^6, region US,
 * source EPA / US EEIO). Son factores spend-based: unidad kg/usd, así que /estimate recibe
 * {money, money_unit:"usd"} con un precio medio del producto nuevo equivalente por categoría.
 * Verificado en vivo (ej.): clothing $30 -> 5.64 kg CO2e; audio_video $200 -> 16.2 kg.
 *
 * Para redescubrir/cambiar IDs:
 *   curl -H "Authorization: Bearer $CLIMATIQ_API_KEY" \
 *        "https://api.climatiq.io/data/v1/search?query=clothing&region=US&data_version=^6"
 * Si una llamada falla, se cae a los factores locales sin romper el endpoint.
 */
@Component
public class ClimatiqClient {

    private static final String ESTIMATE_URL = "https://api.climatiq.io/data/v1/estimate";
    private static final String DEFAULT_DATA_VERSION = "^6";

    @Value("${climatiq.api.key:}")
    private String apiKey;

    // Permite fijar la versión del dataset por entorno sin tocar código.
    @Value("${climatiq.data.version:" + DEFAULT_DATA_VERSION + "}")
    private String dataVersion;

    private final RestTemplate restTemplate = new RestTemplate();

    /** Tipo de parámetro que espera un emission factor. */
    public enum ParamType { MONEY, WEIGHT, NUMBER }

    /** Referencia a un emission factor de Climatiq + cómo invocarlo. */
    public static class FactorRef {
        final String activityId;
        final String source;     // selector para desambiguar en /estimate (ej. "EPA")
        final String region;     // selector (ej. "US")
        final ParamType paramType;
        final double value;      // importe (money) / peso (weight) / cantidad (number)
        final String unit;       // "usd" para money, "kg" para weight; null para number

        FactorRef(String activityId, String source, String region,
                  ParamType paramType, double value, String unit) {
            this.activityId = activityId;
            this.source = source;
            this.region = region;
            this.paramType = paramType;
            this.value = value;
            this.unit = unit;
        }
    }

    /**
     * Mapeo categoría -> emission factor (todos EPA/US, spend-based en USD). El valor es el
     * precio medio en USD del artículo nuevo equivalente. Clave = palabra contenida en el
     * nombre de la categoría (mismo criterio que {@link EnvironmentalImpactFactors}).
     */
    private final Map<String, FactorRef> categoryFactors = new LinkedHashMap<>();

    public ClimatiqClient() {
        categoryFactors.put("ropa",     new FactorRef("consumer_goods-type_clothing",                       "EPA", "US", ParamType.MONEY, 30,  "usd"));
        categoryFactors.put("cloth",    new FactorRef("consumer_goods-type_clothing",                       "EPA", "US", ParamType.MONEY, 30,  "usd"));
        categoryFactors.put("electr",   new FactorRef("electronics-type_audio_video_equipment",             "EPA", "US", ParamType.MONEY, 200, "usd"));
        categoryFactors.put("tecn",     new FactorRef("electronics-type_audio_video_equipment",             "EPA", "US", ParamType.MONEY, 200, "usd"));
        categoryFactors.put("mueble",   new FactorRef("consumer_goods-type_upholstered_household_furniture", "EPA", "US", ParamType.MONEY, 150, "usd"));
        categoryFactors.put("furnitur", new FactorRef("consumer_goods-type_upholstered_household_furniture", "EPA", "US", ParamType.MONEY, 150, "usd"));
        categoryFactors.put("deporte",  new FactorRef("consumer_goods-type_sporting_and_athletic_goods",     "EPA", "US", ParamType.MONEY, 60,  "usd"));
        categoryFactors.put("sport",    new FactorRef("consumer_goods-type_sporting_and_athletic_goods",     "EPA", "US", ParamType.MONEY, 60,  "usd"));
        categoryFactors.put("hogar",    new FactorRef("electrical_equipment-type_small_electrical_appliances","EPA", "US", ParamType.MONEY, 40,  "usd"));
        categoryFactors.put("home",     new FactorRef("electrical_equipment-type_small_electrical_appliances","EPA", "US", ParamType.MONEY, 40,  "usd"));
        categoryFactors.put("libro",    new FactorRef("consumer_goods-type_books_printing",                  "EPA", "US", ParamType.MONEY, 15,  "usd"));
        categoryFactors.put("book",     new FactorRef("consumer_goods-type_books_printing",                  "EPA", "US", ParamType.MONEY, 15,  "usd"));
    }

    /** Climatiq solo se usa si hay una API key configurada. */
    public boolean isEnabled() {
        return apiKey != null && !apiKey.isBlank();
    }

    /** Busca el FactorRef de una categoría por palabra clave contenida en su nombre. */
    private FactorRef refFor(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return null;
        }
        String n = categoryName.toLowerCase();
        for (Map.Entry<String, FactorRef> e : categoryFactors.entrySet()) {
            if (n.contains(e.getKey())) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * Estima los kg de CO2e evitados al reutilizar un artículo de la categoría dada.
     * Optional vacío si Climatiq está desactivado, no hay mapping o la llamada falla
     * (en cuyo caso el servicio usa el factor local).
     */
    public Optional<Double> estimateCo2(String categoryName) {
        if (!isEnabled()) {
            return Optional.empty();
        }
        FactorRef ref = refFor(categoryName);
        if (ref == null) {
            return Optional.empty();
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> emissionFactor = new HashMap<>();
            emissionFactor.put("activity_id", ref.activityId);
            emissionFactor.put("data_version", dataVersion);
            if (ref.source != null) emissionFactor.put("source", ref.source);
            if (ref.region != null) emissionFactor.put("region", ref.region);

            Map<String, Object> body = new HashMap<>();
            body.put("emission_factor", emissionFactor);
            body.put("parameters", buildParameters(ref));

            ResponseEntity<Map> resp = restTemplate.exchange(
                    ESTIMATE_URL, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);

            Object co2e = resp.getBody() != null ? resp.getBody().get("co2e") : null;
            if (co2e instanceof Number) {
                return Optional.of(((Number) co2e).doubleValue());
            }
            return Optional.empty();
        } catch (Exception e) {
            // Ante cualquier error de red/clave/ID/unidad, caemos a los factores locales.
            return Optional.empty();
        }
    }

    /** Construye el bloque "parameters" según el tipo de unidad que espera el factor. */
    private Map<String, Object> buildParameters(FactorRef ref) {
        Map<String, Object> parameters = new HashMap<>();
        switch (ref.paramType) {
            case MONEY -> {
                parameters.put("money", ref.value);
                parameters.put("money_unit", ref.unit);
            }
            case WEIGHT -> {
                parameters.put("weight", ref.value);
                parameters.put("weight_unit", ref.unit);
            }
            case NUMBER -> parameters.put("number", (int) ref.value);
        }
        return parameters;
    }
}
