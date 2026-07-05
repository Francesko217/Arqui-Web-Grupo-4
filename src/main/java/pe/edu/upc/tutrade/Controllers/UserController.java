package pe.edu.upc.tutrade.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IProfileRepository;
import pe.edu.upc.tutrade.ServicesImplements.ProfileServiceImplement;
import pe.edu.upc.tutrade.ServicesInterfaces.IUserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService uS;

    @Autowired
    private IProfileRepository profileRepo;

    @Autowired
    private ModelMapper modelMapper;

    private UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
        profileRepo.findByUser_IdUser(user.getIdUser())
                .ifPresent(p -> dto.setProfile(ProfileServiceImplement.toDTO(p)));
        return dto;
    }

    @PostMapping
    public ResponseEntity<?> insertar(@RequestBody User user) {
        try {
            User nuevo = uS.insert(user);
            return ResponseEntity.ok(modelMapper.map(nuevo, UserResponseDTO.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listar() {
        List<UserResponseDTO> lista = uS.list().stream()
                .map(u -> modelMapper.map(u, UserResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/me")
    public ResponseEntity<?> misDatos(Principal principal) {
        return uS.listByEmail(principal.getName())
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(toDTO(u)))
                .orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id) {
        Optional<User> user = uS.listId(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(toDTO(user.get()));
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        Optional<User> user = uS.listId(id);
        if (user.isPresent()) {
            uS.delete(id);
            return ResponseEntity.ok("Usuario eliminado");
        } else {
            return ResponseEntity.status(404).body("Usuario no existe");
        }
    }

    // HU20: listar usuarios activos
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarActivos() {
        return ResponseEntity.ok(uS.listActiveUsers());
    }

    // HU28: listar candidatos a deshabilitación
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarInactivos(@RequestParam(defaultValue = "90") int days) {
        return ResponseEntity.ok(uS.listInactiveUsers(days));
    }

    // HU28: deshabilitar usuario (soft delete)
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deshabilitar(@PathVariable int id) {
        try {
            uS.disableUser(id);
            return ResponseEntity.ok(toDTO(uS.listId(id).get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // HU28: habilitar usuario
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> habilitar(@PathVariable int id) {
        try {
            uS.enableUser(id);
            return ResponseEntity.ok(toDTO(uS.listId(id).get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // HU38: buscar usuarios por username
    @GetMapping("/search")
    public ResponseEntity<?> buscar(@RequestParam String username) {
        return ResponseEntity.ok(uS.searchByUsername(username));
    }

    // HU50: listar usuarios veteranos
    @GetMapping("/veterans")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarVeteranos() {
        return ResponseEntity.ok(uS.listVeteranUsers());
    }

    // HU06: el usuario activa su propia suscripción premium (pago simulado en el front)
    @PutMapping("/me/premium")
    public ResponseEntity<?> activarMiPremium(Principal principal) {
        try {
            uS.subscribePremium(principal.getName());
            return ResponseEntity.ok("Premium activado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Premium: activar/desactivar suscripción premium
    @PutMapping("/{id}/premium")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> togglePremium(@PathVariable int id) {
        try {
            uS.togglePremium(id);
            return ResponseEntity.ok(toDTO(uS.listId(id).get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // KYC: verificar usuario
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verificar(@PathVariable int id) {
        try {
            uS.verifyUser(id);
            return ResponseEntity.ok(toDTO(uS.listId(id).get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
