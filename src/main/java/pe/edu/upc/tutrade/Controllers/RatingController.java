package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.RatingRequestDTO;
import pe.edu.upc.tutrade.ServicesInterfaces.IRatingService;

import java.security.Principal;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private IRatingService ratingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(ratingService.listAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable int userId) {
        return ResponseEntity.ok(ratingService.listByUser(userId));
    }

    @GetMapping("/trade/{tradeId}")
    public ResponseEntity<?> listarPorTrade(@PathVariable int tradeId, Principal principal) {
        try {
            return ResponseEntity.ok(ratingService.listByTrade(tradeId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RatingRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(ratingService.create(dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
