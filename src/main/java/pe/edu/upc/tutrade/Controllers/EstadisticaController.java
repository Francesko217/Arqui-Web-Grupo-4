package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tutrade.ServicesInterfaces.IEstadisticaService;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticaController {

    @Autowired
    private IEstadisticaService estadisticaService;

    @GetMapping("/items-por-categoria")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> itemsPorCategoria() {
        return ResponseEntity.ok(estadisticaService.itemsPorCategoria());
    }

    @GetMapping("/trueques-por-estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> truequesPorEstado() {
        return ResponseEntity.ok(estadisticaService.truequesPorEstado());
    }

    @GetMapping("/ranking-usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rankingUsuarios() {
        return ResponseEntity.ok(estadisticaService.rankingUsuarios());
    }
}
