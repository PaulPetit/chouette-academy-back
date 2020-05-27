package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.model.UserSession;
import org.springframework.stereotype.Service;

/**
 * Service de gestion des sessions utilisateurs
 */

@Service
public class SessionService extends BaseService {

    // Retourne la session associée au token reçu par le controleur dans l'entête HTTP. Retourne null si la session est invalide
    public UserSession getUserSession(String httpTokenHash) {
        UserSession userSession = new UserSession();
        if (httpTokenHash == null)
            return null;

        Token token = tokenRepository.findByHashKey(Token.trimHttp(httpTokenHash));
        if (token != null) {
            userSession.setToken(token);
            userSession.setUser(token.getUser());
            return userSession;
        }
        return null;
    }

    // Retourne la session associée au token reçu par le controleur dans l'entête HTTP. Une exception de type
    // Unauthorized est générée si le token est invalide ou expiré.
    public UserSession getUserSessionRequired(String httpTokenHash) {
        UserSession session = getUserSession(httpTokenHash);
        if (session != null)
            return session;
        exceptionService.throwExceptionUnauthorized();
        return null;
    }
}
