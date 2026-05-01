package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.MeetingPointRequestDTO;
import pe.edu.upc.tutrade.ServicesInterfaces.IMeetingPointService;

import java.security.Principal;

@RestController
@RequestMapping("/meeting-points")
public class MeetingPointController {

    @Autowired
    private IMeetingPointService meetingPointService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(meetingPointService.listAll());
    }

    @GetMapping("/trade/{tradeId}")
    public ResponseEntity<?> listarPorTrade(@PathVariable int tradeId, Principal principal) {
        try {
            return ResponseEntity.ok(meetingPointService.getByTrade(tradeId, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody MeetingPointRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(meetingPointService.create(dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody MeetingPointRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(meetingPointService.update(id, dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id, Principal principal) {
        try {
            meetingPointService.delete(id, principal.getName());
            return ResponseEntity.ok("Punto de encuentro eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
