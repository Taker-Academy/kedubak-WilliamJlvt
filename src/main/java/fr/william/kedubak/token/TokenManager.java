package fr.william.kedubak.token;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class TokenManager {

    private final static Map<String, String> tokens = new HashMap<>();
    private static final String SALT_ROUNDS = "$2a$10$wSY0nwzrNYPNXz1bwV6Yy.";

    public static String generateToken(String userId) {
        String token = BCrypt.hashpw(userId, SALT_ROUNDS);
        tokens.put(token, userId);
        return token;
    }

    public static boolean isValidToken(String token) {
        return tokens.containsKey(token);
    }

    public static String getUserIdFromToken(String token) {
        return tokens.get(token);
    }

}
