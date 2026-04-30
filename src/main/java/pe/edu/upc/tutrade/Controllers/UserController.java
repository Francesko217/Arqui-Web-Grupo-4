package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.ServicesInterfaces.IUserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService uS;

    @PostMapping
    public ResponseEntity<?> insertar(@RequestBody User user) {
        try {
            User nuevo = uS.insert(user);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<User>> listar() {
        return ResponseEntity.ok(uS.list());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id) {

        Optional<User> user = uS.listId(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
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
