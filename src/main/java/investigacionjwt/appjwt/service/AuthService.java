package investigacionjwt.appjwt.service;


public interface AuthService {

    String login(String username, String password);

    String signUp(String nombre, String username, String password, String email);

    String verifyToken(String token);

}