package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.Config.GroqClient;
import pe.edu.upc.tutrade.DTOs.ChatbotRequest;
import pe.edu.upc.tutrade.DTOs.ChatbotResponse;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired
    private GroqClient groqClient;

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody ChatbotRequest request) {
        if (!groqClient.isEnabled()) {
            return ResponseEntity.status(503).body("Chatbot no disponible (falta GROQ_API_KEY en el servidor)");
        }
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body("Mensaje vacío");
        }
        try {
            String reply = groqClient.chat(request.getMessage());
            return ResponseEntity.ok(new ChatbotResponse(reply));
        } catch (Exception e) {
            return ResponseEntity.status(502).body("Error al conectar con el chatbot");
        }
    }
}
