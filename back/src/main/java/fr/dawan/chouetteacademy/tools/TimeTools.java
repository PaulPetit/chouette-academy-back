package fr.dawan.chouetteacademy.tools;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeTools {

    public final static Long MARS_25 = 1585126800L;

    public static Long now() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public static Long deltaHours(int h) {
        return MARS_25 + (h * 3600);
    }

    public static Long fromNowInMinutes(int m) {
        return MARS_25 + (m * 60);
    }
}
