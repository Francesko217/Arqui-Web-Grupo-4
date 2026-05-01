package pe.edu.upc.tutrade.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.ServicesInterfaces.IUserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService uS;

    @Autowired
    private ModelMapper modelMapper;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id) {
        Optional<User> user = uS.listId(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(user.get(), UserResponseDTO.class));
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
}
