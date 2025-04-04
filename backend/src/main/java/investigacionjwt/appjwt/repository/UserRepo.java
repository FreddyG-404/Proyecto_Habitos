package investigacionjwt.appjwt.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import investigacionjwt.appjwt.models.Usuario;

@Repository // Anotación que indica que esta interfaz es un componente de acceso a datos en
            // Spring.
public interface UserRepo extends JpaRepository<Usuario, Long> {

    boolean existsByUsername(String username);

    Optional<Usuario> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
