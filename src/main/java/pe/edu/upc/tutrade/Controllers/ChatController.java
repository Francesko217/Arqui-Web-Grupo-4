package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.MessageRequestDTO;
import pe.edu.upc.tutrade.ServicesInterfaces.IChatService;

import java.security.Principal;

@RestController
public class ChatController {

    @Autowired
    private IChatService chatService;

    @GetMapping("/chats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(chatService.listAll());
    }

    @GetMapping("/chats/trade/{tradeId}")
    public ResponseEntity<?> listarPorTrade(@PathVariable int tradeId, Principal principal) {
        try {
            return ResponseEntity.ok(chatService.getByTrade(tradeId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> enviarMensaje(@RequestBody MessageRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(chatService.sendMessage(dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/messages/chat/{chatId}")
    public ResponseEntity<?> listarMensajes(@PathVariable int chatId, Principal principal) {
        try {
            return ResponseEntity.ok(chatService.getMessages(chatId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/messages/{id}/read")
    public ResponseEntity<?> marcarLeido(@PathVariable int id, Principal principal) {
        try {
            return ResponseEntity.ok(chatService.markAsRead(id, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
