package investigacionjwt.appjwt.Controller;

public record AuthRequestDto(
        String nombre,
        String username,
        String password,
        String email) {

}
