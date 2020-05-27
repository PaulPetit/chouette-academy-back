package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.dto.LoginDTO;
import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.model.MessageStatus;
import org.springframework.stereotype.Service;

import static fr.dawan.chouetteacademy.tools.Security.sha256;

/**
 * Service de gestion des login/logout
 */

@Service
public class LoginService extends BaseService {

    // Retourne un token si les information fournies par l'utilisateur (dto) sont correctes
    // sinon retourne NULL
    public Token login(LoginDTO dto) {

        boolean loginAccess = false;        // login + password = ok

        User user = userRepository.findByUserNameOrEmail(dto.getLogin(), dto.getLogin());

        if (user != null) {
            String reqPasswordHash = sha256(dto.getPassword());
            String userPasswordHash = user.getPasswordHash();
            if (userPasswordHash.equals(reqPasswordHash)) {
                loginAccess = true;
            }
        }

        // Login + mot de passe = valide
        if (loginAccess) {

            Token token = tokenRepository.findByUser(user);
            if (token != null) {
                tokenRepository.delete(token);
                tokenRepository.flush();
            }

            token = new Token(user);
            tokenRepository.save(token);
            return token;
        } else {
            exceptionService.throwExceptionUnauthorized(MessageStatus.WRONG_LOGIN);
        }
        return null;
    }

    // Déconnexion d'un utilisateur : suppression du token correspondant
    public void logout(String httpTokenHash) {
        long nb = tokenRepository.deleteByHashKey(Token.trimHttp(httpTokenHash));
        tokenRepository.flush();
        if (nb < 1) {
            exceptionService.throwExceptionBadRequest(MessageStatus.INVALID_SESSION);
        }
    }
}
