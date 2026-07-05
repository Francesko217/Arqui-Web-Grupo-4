package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.ItemRequestDTO;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.ServicesImplements.ItemServiceImplement;
import pe.edu.upc.tutrade.ServicesInterfaces.IItemService;

import java.security.Principal;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private IItemService iS;

    @Autowired
    private ItemServiceImplement itemServiceImpl;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(itemServiceImpl.listAsDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id) {
        return itemServiceImpl.listIdAsDTO(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Item no encontrado"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> listarPorCategoria(@PathVariable int categoryId) {
        return ResponseEntity.ok(itemServiceImpl.listByCategoryAsDTO(categoryId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> listarPorStatus(@PathVariable int status) {
        return ResponseEntity.ok(itemServiceImpl.listByStatusAsDTO(status));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable int userId) {
        return ResponseEntity.ok(itemServiceImpl.listByUserAsDTO(userId));
    }

    // HU03: buscar items por texto (título o descripción)
    @GetMapping("/search")
    public ResponseEntity<?> buscar(@RequestParam String q) {
        return ResponseEntity.ok(itemServiceImpl.searchAsDTO(q));
    }

    @PostMapping
    public ResponseEntity<?> insertar(@RequestBody ItemRequestDTO dto, Principal principal) {
        try {
            Item nuevo = iS.insert(dto, principal.getName());
            return ResponseEntity.ok(itemServiceImpl.listIdAsDTO(nuevo.getIdItem()).orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody ItemRequestDTO dto, Principal principal) {
        try {
            Item actualizado = iS.update(id, dto, principal.getName());
            return ResponseEntity.ok(itemServiceImpl.listIdAsDTO(actualizado.getIdItem()).orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id, Principal principal) {
        try {
            iS.delete(id, principal.getName());
            return ResponseEntity.ok("Item eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // HU15: pausar ítem (status 1 → 2)
    @PutMapping("/{id}/pause")
    public ResponseEntity<?> pausar(@PathVariable int id, Principal principal) {
        try {
            return ResponseEntity.ok(itemServiceImpl.listIdAsDTO(iS.pause(id, principal.getName()).getIdItem()).orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // HU15: activar ítem (status 2 → 1)
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activar(@PathVariable int id, Principal principal) {
        try {
            return ResponseEntity.ok(itemServiceImpl.listIdAsDTO(iS.activate(id, principal.getName()).getIdItem()).orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // HU46: ítems recibidos por el usuario autenticado
    @GetMapping("/received")
    public ResponseEntity<?> listarRecibidos(Principal principal) {
        return ResponseEntity.ok(itemServiceImpl.listReceivedAsDTO(principal.getName()));
    }
}
