package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.ReportRequestDTO;
import pe.edu.upc.tutrade.ServicesInterfaces.IReportService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ReportRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(reportService.create(dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(reportService.listAll());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> porUsuario(@PathVariable int userId) {
        return ResponseEntity.ok(reportService.listByReportedUser(userId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(reportService.updateStatus(id, body.get("status")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
