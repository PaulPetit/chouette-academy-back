package fr.dawan.chouetteacademy.tools;

import fr.dawan.chouetteacademy.Conf;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Getter
@Setter
public class Security {

    private static int tokenSize = Conf.TOKEN_SIZE;
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static SecureRandom secureRandom = new SecureRandom();

    /**
     * Retourne une chaîne de caractère aléatoire
     *
     * @param len Nombre de caractères souhaité
     * @return String
     */
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARS.charAt(secureRandom.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    // String + SALT -> sah256
    public static String sha256(String str) {
        str += Conf.PASSWORD_SALT;
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes());
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
