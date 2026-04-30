package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.Entities.Role;
import pe.edu.upc.tutrade.ServicesInterfaces.IRoleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private IRoleService rS;

    @PostMapping
    public ResponseEntity<?> insertar(@RequestBody Role role) {
        try {
            Role nuevo = rS.insert(role);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Role>> listar() {
        return ResponseEntity.ok(rS.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id) {
        Optional<Role> role = rS.listId(id);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        } else {
            return ResponseEntity.status(404).body("Rol no encontrado");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Role role) {
        try {
            Role actualizado = rS.update(id, role);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        Optional<Role> role = rS.listId(id);
        if (role.isPresent()) {
            rS.delete(id);
            return ResponseEntity.ok("Rol eliminado");
        } else {
            return ResponseEntity.status(404).body("Rol no existe");
        }
    }
}
