package investigacionjwt.appjwt.Controller;

import investigacionjwt.appjwt.models.Habito;
import investigacionjwt.appjwt.service.HabitoService;
import investigacionjwt.appjwt.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/habitos")
@CrossOrigin("*")
public class HabitoController {

    @Autowired
    private HabitoService habitoService;


    private Optional<String> extractUsername(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Optional.empty();
        }
        String jwt = token.substring(7); // Remueve "Bearer "
        return JwtUtils.getUsernameFromToken(jwt);
    }

    @GetMapping("/{modulo}")
    public ResponseEntity<?> obtenerHabitos(@RequestHeader("Authorization") String token,
            @PathVariable String modulo) {
        Optional<String> usernameOptional = extractUsername(token);
        if (usernameOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        String username = usernameOptional.get();
        try {
            Habito.Modulo moduloEnum = Habito.Modulo.valueOf(modulo.toUpperCase());
            List<Habito> habitos = habitoService.obtenerHabitosPorUsuarioYModulo(username, moduloEnum);
            return ResponseEntity.ok(habitos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Módulo inválido");
        }
    }

    @PostMapping
    public ResponseEntity<?> agregarHabito(@RequestHeader("Authorization") String token,
            @RequestBody Habito habito) {
        Optional<String> usernameOptional = extractUsername(token);
        if (usernameOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
        String username = usernameOptional.get();
        Habito nuevoHabito = habitoService.agregarHabito(username, habito);
        return ResponseEntity.ok(nuevoHabito);
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<?> marcarHabito(@RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        Optional<String> usernameOptional = extractUsername(token);
        if (usernameOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
        habitoService.marcarHabitoComoCompletado(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosHabitos(@RequestHeader("Authorization") String token) {
        Optional<String> usernameOptional = extractUsername(token);
        if (usernameOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        String username = usernameOptional.get();
        List<Habito> habitos = habitoService.obtenerTodosLosHabitos(username);
        return ResponseEntity.ok(habitos);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHabito(@RequestHeader("Authorization") String token,
                                           @PathVariable Long id) {
        Optional<String> usernameOptional = extractUsername(token);
        if (usernameOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
        habitoService.eliminarHabito(id);
        return ResponseEntity.ok().build();
    }


}
