package investigacionjwt.appjwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import investigacionjwt.appjwt.repository.UserRepo;


@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Se intenta encontrar al usuario en la base de datos mediante el repositorio.
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        // Si se encuentra el usuario, se construye un objeto User de Spring Security con la información del usuario.
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
            .build();
    }
}