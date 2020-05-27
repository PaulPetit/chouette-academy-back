package fr.dawan.chouetteacademy.service;

import fr.dawan.chouetteacademy.dto.PostCourseDTO;
import fr.dawan.chouetteacademy.dto.courses.*;
import fr.dawan.chouetteacademy.entity.*;
import fr.dawan.chouetteacademy.model.CourseStatus;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import fr.dawan.chouetteacademy.tools.Slug;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Service
public class CourseService extends BaseService {

    @Value("${streamserver.baseurl.student}")
    private String streamServerBaseUrl;

    @Value("${streamserver.baseurl.owner}")
    private String streamServerBaseUrlOwner;

    @Value("${app.server.host}")
    private String serverHost;

    public Course findById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    // retourne le DTO d'un cours pour un owner
    public CourseDTO getOwnerDTO(Course c) {
        if (c == null)
            exceptionService.throwExceptionNotFound();

        CourseDTO dto = new CourseDTO();
        dto.setId(c.getId());
        dto.setDescription(c.getDescription());
        dto.setTitle(c.getTitle());
        dto.setTimestampCreation(c.getTimestampCreation());
        dto.setTimestampStreamPlanned(c.getTimestampStreamPlanned());

        if (c.getPictureUrl() != null) {
            dto.setPictureUrl(serverHost + c.getPictureUrl());
        }

        if (c.getCategory() != null) dto.setCategoryId(c.getCategory().getId());
        if (c.getOwner() != null) dto.setOwnerId(c.getOwner().getId());
        if (c.getChat() != null) dto.setChatId(c.getChat().getId());

        if (c.getStudents() != null) {
            dto.setStudentsId(userService.usersToIds(c.getStudents()));
            dto.setNbSubcribers((long) dto.getStudentsId().size());
        } else {
            dto.setNbSubcribers(0L);
        }

        dto.setStatus(c.getStatus());

        dto.setStreamURL(streamServerBaseUrl + c.getOwner().getStreamKey() + ".m3u8");
        //dto.setOwnerStreamURL(streamServerBaseUrlOwner + c.getOwner().getStreamKey());
        dto.setLive(dto.getStatus() == CourseStatus.LIVE);
        dto.setSubscribed(false);
        dto.setOwned(true);

        return dto;
    }

    public CourseDTO getDTO(Course course, User user) {
        if (course == null) exceptionService.throwExceptionBadRequest();

        CourseDTO dto = new CourseDTO();

        boolean userIsOwner = false;
        boolean userIsSubscribed = false;

        if (user != null) {
            userIsOwner = course.getOwner() == user;
            userIsSubscribed = isUserSubscribed(course, user);
        }

        dto.setId(course.getId());
        dto.setOwnerId(course.getOwner().getId());
        dto.setInstructorId(course.getOwner().getId());
        dto.setInstructorName(userService.getFullName(course.getOwner()));
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setTimestampCreation(course.getTimestampCreation());
        dto.setTimestampStreamPlanned(course.getTimestampStreamPlanned());

        if (course.getCategory() != null) {
            dto.setCategoryName(course.getCategory().getName());
            dto.setCategoryId(course.getCategory().getId());
        }

        if (course.getStudents() != null) {
            dto.setStudentsId(userService.usersToIds(course.getStudents()));
            dto.setNbSubcribers((long) dto.getStudentsId().size());
        } else {
            dto.setNbSubcribers(0L);
        }
        if (course.getPictureUrl() != null) dto.setPictureUrl(serverHost + course.getPictureUrl());
        if (course.getChat() != null) dto.setChatId(course.getChat().getId());

        if (userIsOwner || userIsSubscribed)
            dto.setStreamURL(streamServerBaseUrl + course.getOwner().getStreamKey() + ".m3u8");

        dto.setStatus(course.getStatus());
        dto.setLive(dto.getStatus() == CourseStatus.LIVE);
        dto.setSubscribed(userIsSubscribed);
        dto.setOwned(userIsOwner);

        if (!userIsOwner)
            dto.setStudentsId(null);

        return dto;
    }

    public Course create(String title, User owner) {
        if (owner == null)
            exceptionService.throwExceptionBadRequest();

        StringBuilder slug = new StringBuilder(Slug.make(title));

        if (slug.length() < 1)
            slug = new StringBuilder("untitled");

        if (existsBySlug(slug.toString())) {
            int i = 0;
            do {
                i++;
            } while (existsBySlug(slug.toString() + '-' + i));
            slug.append("-").append(i);
        }

        Course course = new Course();
        course.setOwner(owner);
        course.setTitle(title);
        course.setSlug(slug.toString());
        course.setCreationDate();

        Chat chat = new Chat();
        chatRepository.saveAndFlush(chat);
        course.setChat(chat);
        return courseRepository.saveAndFlush(course);
    }

    // création d'un cours avec le DTO en provenance du controleur
    public CourseCreatedDTO createDTO(PostCourseDTO dto, User owner) {
        if (dto == null || owner == null)
            exceptionService.throwExceptionBadRequest();

        Course course = create(dto.getTitle(), owner);
        CourseCreatedDTO createdDTO = new CourseCreatedDTO();

        createdDTO.setId(course.getId());
        createdDTO.setSlug(course.getSlug());
        createdDTO.setStatus(HttpStatus.CREATED.value());
        createdDTO.setMessage(MessageStatus.CREATED.toString());

        return createdDTO;
    }

    public boolean updateDTO(CourseUpdateDTO dto, User owner) {
        if (dto != null && owner != null) {
            Course course = courseRepository.findByIdWithTags(dto.getId());
            if (course == null)
                exceptionService.throwExceptionBadRequest();

            if ((course.getOwner() != null) && (course.getOwner().getId() != owner.getId())) {
                exceptionService.throwExceptionForbidden();
            }

            course.setTitle(dto.getTitle());
            course.setDescription(dto.getDescription());
            course.setTimestampStreamPlanned(dto.getTimestampStreamPlanned());
            course.setStatus(dto.getStatus());

            Long categId = dto.getCategoryId();
            if (categId != null) {
                Category category = categoryService.findById(categId);
                course.setCategory(category);
            } else {
                course.setCategory(null);
            }
            courseRepository.saveAndFlush(course);
            return true;
        }
        return false;
    }

    // retourne une liste de cours
    public Collection<Course> findAll() {
        return courseRepository.findAll();
    }

    private CourseDTO courseToListItemDTO(Course c) {


        if (c == null)
            exceptionService.throwExceptionBadRequest();

        CourseDTO itemDTO = new CourseDTO();

        itemDTO.setId(c.getId());
        if (c.getOwner() != null) {
            itemDTO.setOwnerId(c.getOwner().getId());
        }
        itemDTO.setTitle(c.getTitle());
        itemDTO.setDescription(c.getDescription());
        itemDTO.setTimestampStreamPlanned(c.getTimestampStreamPlanned());
        itemDTO.setTimestampCreation(c.getTimestampCreation());
        if (c.getCategory() != null) {
            itemDTO.setCategoryId(c.getCategory().getId());
        }
        itemDTO.setStatus(c.getStatus());

        return itemDTO;
    }

    // retourne une liste de cours
    public CourseListDTO findAllDTO() {
        CourseListDTO listDTO = new CourseListDTO();
        List<Course> courses = courseRepository.findAll();
        for (Course c : courses) {
            CourseDTO cdto = courseToListItemDTO(c);
            listDTO.getCourses().add(cdto);
        }
        listDTO.setNbCourses((long) listDTO.getCourses().size());
        return listDTO;
    }

    // recherche un cours par title ou description
    public CourseListDTO searchDTO(String query, UserSession session) {
        User user = null;
        if (session != null)
            user = session.getUser();

        CourseListDTO listDTO = new CourseListDTO();
        List<Course> courses = courseRepository.findByTitleContainsOrDescriptionContains(query, query);

        for (Course c : courses) {
            CourseDTO dto = toCourseQueryDTO(c, user);
            if (dto.getStatus() == CourseStatus.PLANNED || dto.getStatus() == CourseStatus.LIVE)
                listDTO.getCourses().add(dto);
        }
        listDTO.setNbCourses((long) listDTO.getCourses().size());
        return listDTO;
    }

    // retourne une liste de cours par category
    public CourseListDTO findAllByCategoryDTO(Category category) {
        CourseListDTO listDTO = new CourseListDTO();
        List<Course> courses = courseRepository.findAllByCategory(category);
        for (Course c : courses) {
            CourseDTO dto = courseToListItemDTO(c);
            if (dto.getStatus() == CourseStatus.PLANNED || dto.getStatus() == CourseStatus.LIVE)
                listDTO.getCourses().add(dto);
        }
        listDTO.setNbCourses((long) listDTO.getCourses().size());
        return listDTO;
    }

    public CourseListDTO findAllByOwnerDTO(User owner) {
        List<Course> courses = courseRepository.findAllByOwner(owner);
        return toCourseListDTO(courses, owner);
    }

    public CourseListDTO findAllBySubscribedDTO(User user) {
        List<Course> courses = user.getSubscribedCourses();
        return toCourseListDTO(courses, user);
    }

    public CourseListDTO toCourseListDTO(List<Course> courses, User user) {
        CourseListDTO courseListDTO = new CourseListDTO();
        for (Course c : courses) {
            //CourseDTO cdto = courseToListItemDTO(c);
            CourseDTO cdto = getDTO(c, user);
            courseListDTO.getCourses().add(cdto);
        }
        courseListDTO.setNbCourses((long) courseListDTO.getCourses().size());
        return courseListDTO;
    }

    // retourne tous les cours d'une catégorie
    public Collection<Course> findAllByCategory(Category category) {
        return courseRepository.findAllByCategory(category);
    }

    // retourne tous les cours crées par un utilisateur (owner)
    public List<Course> findAllByOwner(User user) {
        return courseRepository.findAllByOwner(user);
    }

    public void subscribe(Long courseID, User user) {
        if (courseID == null || user == null) exceptionService.throwExceptionBadRequest();
        Course course = findById(courseID);
        if (course == null) exceptionService.throwExceptionBadRequest();
        if (course.getOwner() == user) exceptionService.throwException(MessageStatus.OWNER_CAN_NOT_SUBSCRIBE);
        if (isUserSubscribed(course, user)) exceptionService.throwException(MessageStatus.ALREADY_SUBSCRIBED);
        course.getStudents().add(user);
        courseRepository.saveAndFlush(course);
        user.getSubscribedCourses().add(course);
        userRepository.saveAndFlush(user);
    }

    public void unsubscribe(Long courseID, User user) {
        if (courseID == null || user == null) exceptionService.throwExceptionBadRequest();
        Course course = findById(courseID);
        if (course == null) exceptionService.throwExceptionBadRequest();
        if (!isUserSubscribed(course, user))
            exceptionService.throwException(MessageStatus.NOT_SUBSCRIBED);
        course.getStudents().remove(user);
        courseRepository.saveAndFlush(course);
        user.getSubscribedCourses().remove(course);
        userRepository.saveAndFlush(user);
    }

    // test si un utilisateur à souscrit à un cours
    public boolean isUserSubscribed(Course course, User user) {
        return course.getStudents().contains(user);
    }

    // test l'existence d'un cours par le slug
    public boolean existsBySlug(String slug) {
        return courseRepository.existsBySlug(slug);
    }


    public List<Long> coursesToIds(List<Course> courses) {
        if (courses == null)
            exceptionService.throwExceptionBadRequest();

        List<Long> coursesId = new ArrayList<Long>();

        for (Course course : courses) {
            coursesId.add(course.getId());
        }

        return coursesId;
    }

    public CourseDTO goLive(Long courseId, UserSession session) {
        if (courseId == null || session == null) exceptionService.throwExceptionBadRequest();

        Course course = findById(courseId);
        if (course == null) exceptionService.throwExceptionBadRequest();

        User owner = session.getUser();
        if (owner == null) exceptionService.throwExceptionBadRequest();

        if (owner == course.getOwner()) {

            List<Course> courses = findAllByOwner(owner);
            if (courses != null) {
                for (Course c : courses) {
                    if (c.getStatus() == CourseStatus.LIVE)
                        exceptionService.throwException(MessageStatus.INSTRUCTOR_ALREADY_IN_LIVE);
                }
            }

            if (course.getStatus() == CourseStatus.DRAFT)
                exceptionService.throwException(MessageStatus.COURSE_IN_DRAFT_STATUS);

            if (course.getStatus() == CourseStatus.DONE)
                exceptionService.throwException(MessageStatus.COURSE_IN_DONE_STATUS);

            course.setStatus(CourseStatus.LIVE);
            courseRepository.save(course);
        }
        return  getOwnerDTO(course);
    }

    public CourseDTO endLive(Long courseId, UserSession session) {
        if (courseId == null || session == null) exceptionService.throwExceptionBadRequest();

        Course course = findById(courseId);
        if (course == null) exceptionService.throwExceptionBadRequest();

        User owner = session.getUser();
        if (owner == null) exceptionService.throwExceptionUnauthorized();

        if (owner == course.getOwner()) {
            course.setStatus(CourseStatus.DONE);
            courseRepository.save(course);
        }
        return getOwnerDTO(course);
    }

    public void updateCourseImage(User user, Long courseId, MultipartFile file) {
        Course course = findById(courseId);
        if (course == null || user == null || file == null) exceptionService.throwExceptionBadRequest();

        if (course.getOwner() == user) {
            String imageName = pictureService.uploadImage(user, file);
            course.setPictureUrl("img/" + imageName);
            courseRepository.save(course);
        } else {
            exceptionService.throwExceptionUnauthorized();
        }
    }

    public CourseListDTO getCoursesUpcoming(User user) {
        List<Course> courses = courseRepository.findTop6ByStatusOrderByTimestampStreamPlannedAsc(CourseStatus.PLANNED);
        CourseListDTO courseUpcomingDTO = new CourseListDTO();

        if (courses == null) exceptionService.throwExceptionNotFound();

        for (Course c : courses) {
            CourseDTO dto = getDTO(c, user);
            courseUpcomingDTO.getCourses().add(dto);
        }
        courseUpcomingDTO.setNbCourses((long) courseUpcomingDTO.getCourses().size());
        return courseUpcomingDTO;
    }

    public CourseListDTO getCoursesLive(User user) {
        List<Course> courses = courseRepository.findTop6ByStatus(CourseStatus.LIVE);
        CourseListDTO courseLiveDTO = new CourseListDTO();

        if (courses == null) exceptionService.throwExceptionNotFound();
        for (Course c : courses) {
            CourseDTO dto = getDTO(c, user);
            courseLiveDTO.getCourses().add(dto);
        }
        courseLiveDTO.setNbCourses((long) courseLiveDTO.getCourses().size());
        return courseLiveDTO;
    }

    public CourseByCategoryDTO getCoursesByCategory(Long id, UserSession session) {
        User user = null;
        if (id == null) exceptionService.throwExceptionBadRequest();

        if (session != null)
            user = session.getUser();

        Category category = categoryService.findById(id);
        if (category == null) exceptionService.throwExceptionBadRequest();
        List<Course> courses = courseRepository.findAllByCategory(category);
        if (courses == null) exceptionService.throwExceptionNotFound();

        CourseByCategoryDTO courseByCategoryDTO = new CourseByCategoryDTO();

        courseByCategoryDTO.setId(category.getId());
        courseByCategoryDTO.setName(category.getName());
        courseByCategoryDTO.setDescription(category.getDescription());
        courseByCategoryDTO.setPictureUrl(category.getPictureUrl());

        for (Course c : courses) {
            CourseDTO dto = toCourseQueryDTO(c, user);
            if (dto.getStatus() != CourseStatus.DRAFT)
                courseByCategoryDTO.getCourses().add(dto);
        }
        courseByCategoryDTO.setNbCourses((long) courseByCategoryDTO.getCourses().size());

        return courseByCategoryDTO;
    }

    public CourseDTO toCourseQueryDTO(Course course, User user) {
        if (course == null) exceptionService.throwExceptionBadRequest();
        CourseDTO dto = new CourseDTO();

        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorName(userService.getFullName(course.getOwner()));
        dto.setInstructorId(course.getOwner().getId());

        if (course.getCategory() != null) {
            dto.setCategoryId(course.getCategory().getId());
            dto.setCategoryName(course.getCategory().getName());
        }

        if (course.getStudents() != null) {
            dto.setNbSubcribers((long) course.getStudents().size());
        } else {
            dto.setNbSubcribers(0L);
        }

        dto.setTimestampStreamPlanned(course.getTimestampStreamPlanned());

        if (course.getPictureUrl() != null) {
            dto.setPictureUrl(serverHost + course.getPictureUrl());
        }

        dto.setStatus(course.getStatus());

        if (user != null) {
            dto.setSubscribed(isUserSubscribed(course, user));
            dto.setOwned(course.getOwner() == user);
        } else {
            dto.setSubscribed(false);
            dto.setOwned(false);
        }

        dto.setLive(course.getStatus() == CourseStatus.LIVE);

        return dto;
    }
}
