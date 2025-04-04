package investigacionjwt.appjwt.repository;

import investigacionjwt.appjwt.models.Habito;
import investigacionjwt.appjwt.models.Habito.Modulo;
import investigacionjwt.appjwt.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HabitoRepository extends JpaRepository<Habito, Long> {
    List<Habito> findByUsuarioAndModulo(Usuario usuario, Modulo modulo);
    List<Habito> findByUsuario(Usuario usuario);

}
