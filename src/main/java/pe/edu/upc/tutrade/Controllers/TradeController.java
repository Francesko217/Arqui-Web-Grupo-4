package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.TradeRequestDTO;
import pe.edu.upc.tutrade.ServicesInterfaces.ITradeService;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private ITradeService tradeService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(tradeService.list());
    }

    @GetMapping("/my")
    public ResponseEntity<?> listarMios(Principal principal) {
        return ResponseEntity.ok(tradeService.listMyTrades(principal.getName()));
    }

    // HU39: propuestas enviadas por el usuario (como proposer)
    @GetMapping("/sent")
    public ResponseEntity<?> listarEnviados(Principal principal) {
        return ResponseEntity.ok(tradeService.listSentTrades(principal.getName()));
    }

    // HU39: propuestas recibidas por el usuario (como receiver)
    @GetMapping("/received")
    public ResponseEntity<?> listarRecibidos(Principal principal) {
        return ResponseEntity.ok(tradeService.listReceivedTrades(principal.getName()));
    }

    // HU21: todas las propuestas sobre un ítem específico
    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> listarPorItem(@PathVariable int itemId, Principal principal) {
        try {
            return ResponseEntity.ok(tradeService.listByItem(itemId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // HU16: ranking de usuarios por cantidad de trades
    @GetMapping("/ranking/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rankingPorTrades() {
        List<Object[]> raw = tradeService.rankingByTradeCount();
        List<Map<String, Object>> result = raw.stream().map(row -> {
            pe.edu.upc.tutrade.Entities.User user = (pe.edu.upc.tutrade.Entities.User) row[0];
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("userId", user.getIdUser());
            entry.put("username", user.getUsernameUser());
            entry.put("email", user.getEmailUser());
            entry.put("totalTrades", row[1]);
            return entry;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id, Principal principal) {
        try {
            return tradeService.listId(id, principal.getName())
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(404).body("Trueque no encontrado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TradeRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(tradeService.create(dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> aceptar(@PathVariable int id, Principal principal) {
        try {
            return ResponseEntity.ok(tradeService.accept(id, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rechazar(@PathVariable int id, Principal principal) {
        try {
            return ResponseEntity.ok(tradeService.reject(id, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelar(@PathVariable int id, Principal principal) {
        try {
            return ResponseEntity.ok(tradeService.cancel(id, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
