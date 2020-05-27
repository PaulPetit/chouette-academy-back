package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.model.MessageStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service de gestion des exceptions
 */

@Service
public class ExceptionService extends BaseService {

    public void throwException(HttpStatus httpStatus, MessageStatus messageStatus) {
        throw new ResponseStatusException(httpStatus, messageStatus.name());
    }

    public void throwException(MessageStatus messageStatus) {
        throw new ResponseStatusException(HttpStatus.OK, messageStatus.name());
    }

    public void throwExceptionBadRequest(MessageStatus messageStatus) {
        throwException(HttpStatus.BAD_REQUEST, messageStatus);
    }

    public void throwExceptionUnauthorized(MessageStatus messageStatus) {
        throwException(HttpStatus.UNAUTHORIZED, messageStatus);
    }

    public void throwExceptionForbidden(MessageStatus messageStatus) {
        throwException(HttpStatus.FORBIDDEN, messageStatus);
    }

    public void throwExceptionNotFound(MessageStatus messageStatus) {
        throwException(HttpStatus.NOT_FOUND, messageStatus);
    }

    public void throwExceptionBadRequest() {
        throwException(HttpStatus.BAD_REQUEST, MessageStatus.BAD_REQUEST);
    }

    public void throwExceptionUnauthorized() {
        throwException(HttpStatus.UNAUTHORIZED, MessageStatus.UNAUTHORIZED);
    }

    public void throwExceptionForbidden() {
        throwException(HttpStatus.FORBIDDEN, MessageStatus.FORBIDDEN);
    }

    public void throwExceptionNotFound() {
        throwException(HttpStatus.NOT_FOUND, MessageStatus.NOT_FOUND);
    }

    public void throwExceptionInternalServerError() {
        throwException(HttpStatus.INTERNAL_SERVER_ERROR, MessageStatus.INTERNAL_SERVER_ERROR);
    }
}
