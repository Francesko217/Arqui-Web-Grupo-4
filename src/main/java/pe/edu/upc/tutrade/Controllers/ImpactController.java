package pe.edu.upc.tutrade.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.tutrade.ServicesInterfaces.IImpactService;

import java.security.Principal;

/**
 * HU08 - Impacto Ambiental.
 * Expone el ahorro de CO2, agua y residuos generado por los trueques completados.
 */
@RestController
@RequestMapping("/impact")
public class ImpactController {

    @Autowired
    private IImpactService impactService;

    /** Impacto del usuario autenticado. */
    @GetMapping("/me")
    public ResponseEntity<?> miImpacto(Principal principal) {
        try {
            return ResponseEntity.ok(impactService.getUserImpact(principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Impacto agregado de toda la comunidad (público, para el landing). */
    @GetMapping("/community")
    public ResponseEntity<?> impactoComunidad() {
        return ResponseEntity.ok(impactService.getCommunityImpact());
    }
}
