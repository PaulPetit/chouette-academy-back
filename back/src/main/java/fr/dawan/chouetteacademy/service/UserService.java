package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.dto.ResponseDTO;
import fr.dawan.chouetteacademy.dto.UserDTO;
import fr.dawan.chouetteacademy.dto.UserPrivateDTO;
import fr.dawan.chouetteacademy.dto.UserPutDTO;
import fr.dawan.chouetteacademy.entity.Course;
import fr.dawan.chouetteacademy.entity.Token;
import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static fr.dawan.chouetteacademy.tools.Security.sha256;

/**
 * Service de gestion des utilisateurs
 */

@Service
public class UserService extends BaseService {

    @Value("${streamserver.baseurl.owner}")
    private String streamServerBaseUrlOwner;

    // Enregistrement en base de données
    public void save(User user) {
        userRepository.saveAndFlush(user);
    }

    // Retourne un utilisateur par son ID
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Retourne un utilisateur par son userName
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    // Retourne un utilisateur par son email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Retourne un utilisateur par son TokenHash
    public User findByTokenHash(String tokenHash) {
        Token token = tokenRepository.findByHashKey(tokenHash);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }


    // Test si user est inscrit à un cours
    public boolean isUserSubscribedToCourse(User user, Course course) {
        if (user != null && course != null) {
            //return (userRepository.findUserSubscribedCourse(user.getId(), course.getId()).getId() == user.getId());
            User u = userRepository.findUserSubscribedCourse(user.getId(), course.getId());
            return u != null;
        }
        return false;
    }

    // TODO : iltre pour tester la validité d'un userName
    public boolean userNameIsValid(String userName) {
        return true;
    }

    // TODO : Filtre pour tester la validité d'un email
    public boolean emailIsValid(String email) {
        return true;
    }

    // TODO : Filtre pour tester la validité d'un password
    public boolean passwordIsValid(String password) {
        return true;
    }

    // Retourne une liste de User Id à partir d'une liste de Users pour construire un DTO
    public List<Long> usersToIds(List<User> users) {
        List<Long> listIds = new ArrayList<Long>();
        for (User u : users)
            listIds.add(u.getId());
        return listIds;
    }

    // Affecte au userDTO les informations de base
    public void setBaseDTO(User user, UserDTO dto) {
        if (user == null || dto == null) exceptionService.throwExceptionInternalServerError();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFullName(getFullName(user));
        dto.setUserName(user.getUserName());
        dto.setPictureUrl(pictureService.getPictureFullUrl(user.getPictureUrl()));
        dto.setBio(user.getBio());
        dto.setTwitterUrl(user.getTwitterUrl());
        dto.setLinkedInUrl(user.getLinkedInUrl());
        dto.setWebsiteUrl(user.getWebsiteUrl());
    }

    // Affecte au userDTO les informations privées
    public void setPrivateDTO(User user, UserPrivateDTO dto) {
        if (user == null || dto == null) exceptionService.throwExceptionInternalServerError();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreationDate(user.getCreationDate());
        dto.setOwnedCoursesId(courseService.coursesToIds(user.getOwnedCourses()));
        dto.setSubscribedCoursesId(courseService.coursesToIds(user.getSubscribedCourses()));
    }

    // Retourne un UserDTO d'après un User
    public UserDTO getDto(User user) {
        if (user == null) exceptionService.throwExceptionNotFound();
        UserDTO dto = new UserDTO();
        setBaseDTO(user, dto);
        return dto;
    }

    // Retourne un UserDTO d'après un ID
    public UserDTO getDtoById(Long userID) {
        if (userID == null) exceptionService.throwExceptionBadRequest();
        UserDTO dto = new UserDTO();
        User user = findById(userID);
        return getDto(user);
    }

    // Retourne un DTO avec des informations privées
    public UserPrivateDTO getPrivateDto(User user) {
        UserPrivateDTO dto = new UserPrivateDTO();
        if (user == null) exceptionService.throwExceptionNotFound();
        setBaseDTO(user, dto);
        setPrivateDTO(user, dto);
        return dto;
    }

    // Mise à jour d'un User
    public ResponseDTO update(UserPutDTO dto, UserSession session) {

        if (dto == null) exceptionService.throwExceptionBadRequest();

        User user = session.getUser();

        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBio(dto.getBio());
        user.setTwitterUrl(dto.getTwitterUrl());
        user.setWebsiteUrl(dto.getWebsiteUrl());
        user.setLinkedInUrl(dto.getLinkedInUrl());

        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setMessage(MessageStatus.USER_UPDATED.toString());
        responseDTO.setStatus(HttpStatus.OK.value());

        return responseDTO;
    }

    // Modification de l'image de profil
    public void updateProfileImage(User user, MultipartFile file) {
        String imageName = pictureService.uploadImage(user, file);
        user.setPictureUrl("img/" + imageName);
        save(user);
        System.out.println(user.getPictureUrl());
    }

    public String getUserStreamUrl(User owner) {
        if (owner == null) exceptionService.throwExceptionBadRequest();
        return streamServerBaseUrlOwner + owner.getStreamKey();
    }

    // Retourne le nom complet d'un User
    public String getFullName(User user) {

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String userName = user.getUserName();
        String fullName = "";

        if (firstName != null && lastName != null) {
            fullName = firstName + " " + lastName;
        } else {
            fullName = userName;
        }
        return fullName;
    }

    // Gestion de la mise à jour du mot de passe
    public ResponseDTO updatePassword(User user, String password, String newPassword) {
        if (user == null || password == null || newPassword == null) exceptionService.throwExceptionBadRequest();

        ResponseDTO dto = new ResponseDTO();
        String reqPasswordHash = sha256(password);
        String userPasswordHash = user.getPasswordHash();

        if (userPasswordHash.equals(reqPasswordHash)) {
            if (passwordIsValid(newPassword)) {
                user.setPasswordHash(newPassword);
                userRepository.save(user);
                dto.setMessage(MessageStatus.PASSWORD_UPDATED.toString());
            } else {
                dto.setMessage(MessageStatus.NEW_PASSWORD_INVALID.toString());
            }
        } else {
            dto.setMessage(MessageStatus.INVALID_PASSWORD.toString());
        }
        dto.setStatus(HttpStatus.OK.value());
        return dto;
    }
}
