package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.respository.*;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {
    @Autowired protected ExceptionService exceptionService;
    @Autowired protected CategoryRepository categoryRepository;
    @Autowired protected ChatMessageRepository chatMessageRepository;
    @Autowired protected ChatRepository chatRepository;
    @Autowired protected CourseRepository courseRepository;
    @Autowired protected CategoryService categoryService;
    @Autowired protected UserService userService;
    @Autowired protected PictureService pictureService;
    @Autowired protected UserRepository userRepository;
    @Autowired protected TokenRepository tokenRepository;
    @Autowired protected CourseService courseService;
}
