package investigacionjwt.appjwt.Controller;

import investigacionjwt.appjwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login") // Define que este método manejará solicitudes POST a "/api/auth/login".
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        try {
                // Llama al servicio para autenticar al usuario y generar un token JWT
            var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
    
                // Crea un objeto de respuesta con el token y el estado de éxito
            var authResponseDto = new AuthResponseDto(
                    jwtToken,
                    AuthStatus.LOGIN_SUCCESS,
                    "Inicio de sesion exitoso"
            );
            
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(authResponseDto);
    
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            AuthStatus status = AuthStatus.LOGIN_FAILED;
    
            if (errorMessage.contains("Usuario no encontrado")) {
                errorMessage = "Usuario no encontrado";
            } else if (errorMessage.contains("La cuenta no ha sido verificada")) {
                errorMessage = "La cuenta no ha sido verificada. Por favor, revise su correo electrónico.";
            } else if (errorMessage.contains("Bad credentials")) {
                errorMessage = "Usuario o contraseña incorrectos";
            }
    
            var authResponseDto = new AuthResponseDto(null, status, errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponseDto);
        }
    }
    
    
        /**
         * Endpoint para registrar un nuevo usuario (sign-up).
         *
         * @param authRequestDto Contiene el nombre, nombre de usuario y contraseña
         * del nuevo usuario.
         * @return Respuesta HTTP con un token JWT si el registro fue exitoso, o un
         * mensaje de error si no.
         */
        @PostMapping("/registrar") // Define que este método manejará solicitudes POST 
        public ResponseEntity<AuthResponseDto> signUp(@RequestBody AuthRequestDto authRequestDto) {
            try {
                // Llama al servicio para registrar al usuario y generar un token JWT.
                var jwtToken = authService.signUp(authRequestDto.nombre(), authRequestDto.username(), authRequestDto.password(), authRequestDto.email());
    
                // Crea un objeto de respuesta con el token y el estado de éxito.
                var authResponseDto = new AuthResponseDto(jwtToken,
                        AuthStatus.USER_CREATED_SUCCESSFULLY,
                        "Usuario creado con exito. Por favor, revise, tu correo electronico y verifica tu cuenta para completar el registro");
    
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(authResponseDto);
    
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                AuthStatus status = AuthStatus.USER_NOT_CREATED;
    
                // Personalizar mensajes según el tipo de error
                if (e.getMessage().contains("Username already exists")) {
                    errorMessage = "El nombre de usuario ya está en uso";
                } else if (e.getMessage().contains("Email already exists")) {
                    errorMessage = "El correo electrónico ya está registrado";
                }
    
                var authResponseDto = new AuthResponseDto(null, status, errorMessage);
    
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(authResponseDto);
            }
        }
}
