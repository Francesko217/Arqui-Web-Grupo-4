package pe.edu.upc.tutrade.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Cliente del chatbot de la landing page, usando la API de Groq (chat completions,
 * compatible con el formato OpenAI).
 *
 * La API key se lee de la var de entorno GROQ_API_KEY (sin default embebido: si no está
 * seteada, isEnabled() devuelve false y el ChatController responde 503 en vez de romper
 * el arranque del backend).
 */
@Component
public class GroqClient {

    private static final String CHAT_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String SYSTEM_PROMPT =
            "Eres el asistente virtual de Tu Trade, una plataforma de trueque de objetos usados. " +
            "Responde en español, breve y amable. SOLO usa los hechos listados abajo sobre cómo " +
            "funciona la plataforma; si te preguntan algo que no está aquí, dilo con honestidad " +
            "en vez de inventar.\n\n" +
            "Hechos reales de Tu Trade:\n" +
            "- NO existe moneda interna ni sistema de puntos. El trueque es directo: intercambias " +
            "un artículo tuyo por uno de otro usuario, sin dinero ni puntos de por medio.\n" +
            "- Flujo: publicas un artículo -> propones o recibes un trueque -> acuerdan un punto de " +
            "encuentro por chat -> intercambian -> se califican mutuamente.\n" +
            "- Cuentas gratuitas tienen límites: máximo 5 artículos activos publicados y máximo 3 " +
            "trueques pendientes simultáneos.\n" +
            "- Premium es una suscripción de pago SIMULADO (no es dinero real, es una demo) que " +
            "activa el administrador de la plataforma. Quita esos límites de 5 artículos y 3 trueques.\n" +
            "- Para truequear se requiere verificación KYC de la cuenta.\n\n" +
            "Anima a crear una cuenta cuando sea natural, sin inventar funciones que no existen.";

    @Value("${groq.api.key:}")
    private String apiKey;

    @Value("${groq.model:llama-3.3-70b-versatile}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isEnabled() {
        return apiKey != null && !apiKey.isBlank();
    }

    public String chat(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_PROMPT),
                        Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.6
        );

        ResponseEntity<Map> resp = restTemplate.exchange(
                CHAT_URL, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);

        List<?> choices = (List<?>) resp.getBody().get("choices");
        Map<?, ?> first = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) first.get("message");
        return (String) message.get("content");
    }
}
