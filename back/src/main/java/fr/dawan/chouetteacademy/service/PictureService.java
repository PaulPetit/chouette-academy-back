package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.tools.Security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Service de gestion des images uploadés par les utilisateurs
 *
 * todo : gérer la suppresion des images qui ne sont plus utilisées
 */

@Service
public class PictureService extends BaseService {

    @Value("${app.img.dir}")
    public String uploadDir;

    @Value("${app.server.host}")
    private String serverHost;

    // Gestion de l'upload d'une image, génération d'un nom de fichier
    public String uploadImage(User user, MultipartFile file) {
        try {
            String fileName = user.getId() + "-" + Security.randomString(32) + "-" + StringUtils.cleanPath(file.getOriginalFilename());
            Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retourne l'url complète de l'image à afficher dans le navigateur.
    public String getPictureFullUrl(String url) {
        String fullUrl = null;
        if (url != null) {
            fullUrl = serverHost + url;
        }
        return fullUrl;
    }
}
