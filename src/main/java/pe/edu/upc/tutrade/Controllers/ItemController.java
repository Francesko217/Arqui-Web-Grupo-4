package pe.edu.upc.tutrade.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.DTOs.ItemRequestDTO;
import pe.edu.upc.tutrade.DTOs.ItemResponseDTO;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.ServicesInterfaces.IItemService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private IItemService iS;

    @Autowired
    private ModelMapper modelMapper;

    private ItemResponseDTO toDTO(Item item) {
        return modelMapper.map(item, ItemResponseDTO.class);
    }

    private List<ItemResponseDTO> toDTOList(List<Item> items) {
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(toDTOList(iS.list()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarId(@PathVariable int id) {
        return iS.listId(id)
                .<ResponseEntity<?>>map(item -> ResponseEntity.ok(toDTO(item)))
                .orElse(ResponseEntity.status(404).body("Item no encontrado"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> listarPorCategoria(@PathVariable int categoryId) {
        return ResponseEntity.ok(toDTOList(iS.listByCategory(categoryId)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> listarPorStatus(@PathVariable int status) {
        return ResponseEntity.ok(toDTOList(iS.listByStatus(status)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable int userId) {
        return ResponseEntity.ok(toDTOList(iS.listByUser(userId)));
    }

    @PostMapping
    public ResponseEntity<?> insertar(@RequestBody ItemRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(toDTO(iS.insert(dto, principal.getName())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody ItemRequestDTO dto, Principal principal) {
        try {
            return ResponseEntity.ok(toDTO(iS.update(id, dto, principal.getName())));
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
}
