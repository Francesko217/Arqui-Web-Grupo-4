package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.TradeRequestDTO;
import pe.edu.upc.tutrade.ServicesInterfaces.ITradeService;

import java.security.Principal;

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
