package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.ProfileRequestDTO;
import pe.edu.upc.tutrade.ServicesInterfaces.IProfileService;

import java.security.Principal;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private IProfileService profileService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProfileRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(profileService.create(dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(profileService.listAll());
    }

    @GetMapping("/me")
    public ResponseEntity<?> miPerfil(Principal principal) {
        try {
            return ResponseEntity.ok(profileService.getMyProfile(principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> porUsuario(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(profileService.getByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody ProfileRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(profileService.update(id, dto, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id, Principal principal) {
        try {
            profileService.delete(id, principal.getName());
            return ResponseEntity.ok("Perfil eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
