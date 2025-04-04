package investigacionjwt.appjwt.service;

import investigacionjwt.appjwt.models.Habito;
import investigacionjwt.appjwt.models.Usuario;
import investigacionjwt.appjwt.repository.HabitoRepository;
import investigacionjwt.appjwt.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HabitoService {

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private UserRepo usuarioRepository;

    public List<Habito> obtenerHabitosPorUsuarioYModulo(String username, Habito.Modulo modulo) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return habitoRepository.findByUsuarioAndModulo(usuario, modulo);
    }

    public Habito agregarHabito(String username, Habito habito) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        habito.setUsuario(usuario);
        return habitoRepository.save(habito);
    }

    public void marcarHabitoComoCompletado(Long id) {
        Habito habito = habitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito no encontrado"));

        habito.setCompletado(true);
        habitoRepository.save(habito);
    }
    
    public List<Habito> obtenerTodosLosHabitos(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return habitoRepository.findByUsuario(usuario);
    }
    
    public void eliminarHabito(Long id) {
        if (!habitoRepository.existsById(id)) {
            throw new RuntimeException("Hábito no encontrado");
        }
        habitoRepository.deleteById(id);
    }


}
