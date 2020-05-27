package fr.dawan.chouetteacademy;

import fr.dawan.chouetteacademy.dto.LoginDTO;
import fr.dawan.chouetteacademy.entity.Category;
import fr.dawan.chouetteacademy.entity.Chat;
import fr.dawan.chouetteacademy.entity.Course;
import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.model.CourseStatus;
import fr.dawan.chouetteacademy.respository.ChatRepository;
import fr.dawan.chouetteacademy.respository.CourseRepository;
import fr.dawan.chouetteacademy.respository.UserRepository;
import fr.dawan.chouetteacademy.service.*;
import fr.dawan.chouetteacademy.tools.TimeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Application {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private LoginService loginService;

    private User userFrogez;
    private User userPpetit;
    private User userJohn;

    private Category categBusiness;
    private Category categProgrammation;
    private Category categSante;
    private Category categMusique;
    private Category categDevPerso;
    private Category categModeDeVie;

    @Value("{streamserver.baseurl}")
    String streamServerBaseUrl;

    @Value("${app.server.host}")
    String serverHost;

    @PostConstruct
    public void initAll() {
        initUsers();
        initCategories();
        initCourses();
    }

    public void initUsers() {
        userFrogez = new User();
        userFrogez.setCreationDate();
        userFrogez.setFirstName("Fredy");
        userFrogez.setLastName("Rogez");
        userFrogez.setEmail("fredy_NO_SPAM_@email.email");
        userFrogez.setPasswordHash("SUPER_MOT_DE_PASSE");
        userFrogez.setUserName("frogez");
        userFrogez.setBio("Jeune développeur Nantais. J'aime la Guinness et les macaronis au fromage !");
        userFrogez.setPictureUrl("img/stock/fred.jpg");
        userFrogez.setTwitterUrl("https://twitter.com/fredyrogez");
        userFrogez.setLinkedInUrl("https://www.linkedin.com/in/rogez");
        userFrogez.setWebsiteUrl("https://github.com/rogez");
        userService.save(userFrogez);

        userPpetit = new User();
        userPpetit.setCreationDate();
        userPpetit.setFirstName("Paul");
        userPpetit.setLastName("Petit");
        userPpetit.setEmail("paul_NO_SPAM_@email.email");
        userPpetit.setPasswordHash("SUPER_MOT_DE_PASSE");
        userPpetit.setUserName("ppetit");
        userPpetit.setStreamKey("StreamKey");
        userPpetit.setBio("Développeur Normand expatrié à Nantes. J'aime le beurre salé et les chats.");
        userPpetit.setPictureUrl("img/stock/paul.png");
        userPpetit.setTwitterUrl(null);
        userPpetit.setLinkedInUrl("https://www.linkedin.com/in/paul-petit/");
        userPpetit.setWebsiteUrl("https://github.com/PaulPetit");
        userService.save(userPpetit);

        userJohn = new User();
        userJohn.setCreationDate();
        userJohn.setFirstName("John");
        userJohn.setLastName("Doe");
        userJohn.setEmail("john@doe.fr");
        userJohn.setPasswordHash("123456789");
        userJohn.setUserName("jdoe");

        userService.save(userJohn);
    }

    // création d'une session automatique pour les tests
    public void loginJohn() {
        LoginDTO dto = new LoginDTO();
        dto.setLogin("jdoe");
        dto.setPassword("123456789");
        loginService.login(dto);
    }

    public void initCategories() {
        this.categProgrammation = categoryService.create("Informatique", "Programmation, système, réseaux, logiciels...", serverHost + "img/stock/dev.jpg");
        this.categBusiness = categoryService.create("Business", "Création d'entreprise, investisement, marketing...", serverHost + "img/stock/business.png");
        this.categMusique = categoryService.create("Musique", "Instrument, chant, musique électronique...", serverHost + "img/stock/musique.jpg");
        this.categDevPerso = categoryService.create("Développement personnel", "Productivité, bonheur, estime de soi...", serverHost + "img/stock/santé.jpg");
        this.categSante = categoryService.create("Santé et bien-être", "Nutrition, sport, méditation, yoga...", serverHost + "img/stock/salade.jpg");
        this.categModeDeVie = categoryService.create("Mode de vie", "Jeux, voyage, beauté et maquillage, arts...", serverHost + "img/stock/airport.jpg");
    }

    private void initCourses() {

        // Informatique

        newCourse("TEST STREAM",
                "Formation de test pour le stream",
                userFrogez, categProgrammation, CourseStatus.PLANNED, TimeTools.deltaHours(1),
                "img/stock/dev.jpg");

        newCourse("Programmer en Java",
                "Java n'est pas qu'une île perdue au milieu de l'Indonésie. C'est aussi un langage de programmation !",
                userPpetit, categProgrammation, CourseStatus.PLANNED, TimeTools.deltaHours(1),
                "img/stock/dev.jpg");

        newCourse("Programmer en Cobol",
                "Devenez un expert en développement Cobol (attention, formation réservée aux barbus)",
                userPpetit, categProgrammation, CourseStatus.PLANNED, TimeTools.deltaHours(48),
                "img/stock/coding2.jpg");

        newCourse("Développement d'application Android",
                "Apprendre la programmation en partant de zéro et créer des applications mobiles iOS Android avec Xamarin Forms et le C#.",
                userPpetit, categProgrammation, CourseStatus.PLANNED, TimeTools.deltaHours(6),
                "img/stock/coding3.jpg");

        newCourse("Maîtriser Unity et le C# | Guide Complet Développeur",
                "Apprendre à développer des jeux vidéo pour Smartphone, Windows, Mac... facilement avec Unity.",
                userPpetit, categProgrammation, CourseStatus.PLANNED, TimeTools.deltaHours(3),
                "img/stock/coding4.jpg");

        // Business
        newCourse("Placements en bourse : gagner à tous les coups",
                "Les meilleurs trucs et astuces de nos experts : Jérome Kerviel, Bernard Madoff et Christophe Rocancourt.",
                userPpetit, categBusiness, CourseStatus.PLANNED, TimeTools.deltaHours(3),
                "img/stock/business.png");

        // Musique
        newCourse("Jouer de la carotte ou du poivron",
                "Cours exceptionnel pour la maitrise de la musique légumière. Uniquement sur Chouette Academy !",
                userPpetit, categMusique, CourseStatus.PLANNED, TimeTools.deltaHours(2),
                "img/stock/legumes.jpg");

        // Santé
        newCourse("Cuisine végétarienne",
                "Apprendre à cuisiner des trucs verts.",
                userPpetit, categSante, CourseStatus.PLANNED, TimeTools.deltaHours(18),
                "img/stock/santé.jpg");

        newCourse("Devenir le roi du barbecue.",
                "Maîtriser la cuisson des entrecôtes comme les meilleurs chefs !",
                userPpetit, categSante, CourseStatus.PLANNED, TimeTools.deltaHours(18),
                "img/stock/cuisine.jpg");

        // Mode de vie
        /*
        newCourse("",
                "Tout ce que vous devez savoir pour appliquer le maquillage comme un professionnel. Obtenez un look impeccable.",
                userPpetit, categModeDeVie, CourseStatus.PLANNED, TimeTools.deltaHours(8),
                "img/stock/joker.jpg");
*/
        newCourse("Formation maquillage",
                "Tout ce que vous devez savoir pour appliquer le maquillage comme un professionnel. Obtenez un look impeccable.",
                userPpetit, categModeDeVie, CourseStatus.PLANNED, TimeTools.deltaHours(9),
                "img/stock/joker.jpg");

        newCourse("Cuisiner en camping-car",
                "Tout, vraiment tout pour apprendre les meilleurs techniques de cuisine en mobilité.",
                userPpetit, categModeDeVie, CourseStatus.PLANNED, TimeTools.deltaHours(4),
                "img/stock/cook.jpg");

    }

    public Course newCourse(String title,
                            String description,
                            User owner,
                            Category category,
                            CourseStatus status,
                            Long timestampStreamPlanned,
                            String pictureUrl) {
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        Chat chat = new Chat();
        course.setOwner(owner);
        chatRepository.saveAndFlush(chat);
        course.setChat(chat);
        course.setCreationDate();
        course.setCategory(category);
        course.setStatus(status);
        course.setTimestampStreamPlanned(timestampStreamPlanned);
        course.setPictureUrl(pictureUrl);
        courseRepository.saveAndFlush(course);
        return course;
    }
}
