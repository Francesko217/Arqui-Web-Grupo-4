package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.Entities.Category;
import pe.edu.upc.tutrade.ServicesInterfaces.ICategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService cS;

    @GetMapping
    public ResponseEntity<List<Category>> listar() {
        return ResponseEntity.ok(cS.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id) {
        Optional<Category> category = cS.listId(id);
        if (category.isPresent()) return ResponseEntity.ok(category.get());
        return ResponseEntity.status(404).body("Categoría no encontrada");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> insertar(@RequestBody Category category) {
        try {
            return ResponseEntity.ok(cS.insert(category));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Category category) {
        try {
            return ResponseEntity.ok(cS.update(id, category));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            cS.delete(id);
            return ResponseEntity.ok("Categoría eliminada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
