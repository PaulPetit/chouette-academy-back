package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.dto.RegisterDTO;
import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.model.MessageStatus;
import org.springframework.stereotype.Service;

/**
 * Service de gestion de l'enregistrement d'un nouvel utilisateur
 */

@Service
public class RegisterService extends BaseService {

    // Enregistre un nouvel utilisateur et création d'une session si ok
    public Token register(RegisterDTO dto) {

        boolean loginAccess = false;        // si login + password ok -> true

        if (dto.getUserName() == null || !userService.userNameIsValid(dto.getUserName())) {
            exceptionService.throwException(MessageStatus.INVALID_USER_NAME);
        }

        if (dto.getEmail() == null || !userService.emailIsValid(dto.getEmail())) {
            exceptionService.throwException(MessageStatus.INVALID_EMAIL);
        }

        if (dto.getPassword() == null || !userService.passwordIsValid(dto.getPassword())) {
            exceptionService.throwException(MessageStatus.INVALID_PASSWORD);
        }

        User user = userService.findByUserName(dto.getUserName());
        if (user != null) {
            exceptionService.throwException(MessageStatus.USER_NAME_ALREADY_EXISTS);
        }

        user = userService.findByEmail(dto.getEmail());
        if (user != null) {
            exceptionService.throwException(MessageStatus.EMAIL_ALREADY_EXISTS);
        }

        // Création d'un nouvel utilisateur
        user = new User(dto);
        userService.save(user);

        // Création d'un nouveau token (ouvre une session pour le nouvel utilisateur)
        Token token = new Token(user);
        tokenRepository.saveAndFlush(token);

        return token;
    }
}
