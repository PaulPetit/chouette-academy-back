package fr.dawan.chouetteacademy;

import org.springframework.beans.factory.annotation.Value;

public class Conf {

    public final static String USER_HOME = System.getProperty("user.home");

    // DEV
    public final static boolean DEV = true;

    // SECU
    public final static String PASSWORD_SALT = "Q=6+Z@Q8=W@o|x"; // salt de génération des passwords
    public final static String CROSS_ORIGIN_HOST = "*";
    public final static String EXPOSED_HEADERS = "X-Token";
    public final static int TOKEN_LIFE = 720; // durée de vie d'un token en minutes
    public final static int TOKEN_SIZE = 128; // taille en caractères d'un tokenHash

}

