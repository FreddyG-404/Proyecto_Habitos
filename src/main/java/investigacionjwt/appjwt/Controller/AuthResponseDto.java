package investigacionjwt.appjwt.Controller;

public record AuthResponseDto(
    String token,        // Contiene el token JWT generado si la autenticación o el registro fueron exitosos.
    AuthStatus authStatus, // Representa el estado de la operación de autenticación o registro.
String message
) {
}