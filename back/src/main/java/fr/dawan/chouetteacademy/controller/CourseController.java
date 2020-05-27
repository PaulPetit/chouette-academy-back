package fr.dawan.chouetteacademy.controller;

import fr.dawan.chouetteacademy.dto.PostCourseDTO;
import fr.dawan.chouetteacademy.dto.ResponseDTO;
import fr.dawan.chouetteacademy.dto.courses.*;
import fr.dawan.chouetteacademy.entity.Category;
import fr.dawan.chouetteacademy.entity.Course;
import fr.dawan.chouetteacademy.entity.User;
import fr.dawan.chouetteacademy.model.CourseStatus;
import fr.dawan.chouetteacademy.model.MessageStatus;
import fr.dawan.chouetteacademy.model.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CourseController extends BaseController {

    @GetMapping("/api/courses")
    public ResponseEntity<CourseListDTO> getCourses(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @RequestParam(name = "c", required = false) Long categoryId) {

        UserSession session = sessionService.getUserSession(httpTokenHash);

        CourseListDTO dto = new CourseListDTO();

        if (categoryId != null) {
            Category category = categoryService.findById(categoryId);
            if (category != null) {
                dto = courseService.findAllByCategoryDTO(category);
            } else {
                exceptionService.throwExceptionBadRequest();
            }
        } else {
            dto = courseService.findAllDTO();
        }

        return getResponse(dto, session);
    }

    @GetMapping("/api/courses/search")
    public ResponseEntity<CourseListDTO> getCoursesSearch(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @RequestParam(name = "q", required = true) String query) {

        UserSession session = sessionService.getUserSession(httpTokenHash);
        //CourseListDTO dto = new CourseListDTO();
        if (query == null) exceptionService.throwExceptionBadRequest();
        CourseListDTO dto = courseService.searchDTO(query, session);

        return getResponse(dto, session);
    }

    @GetMapping("/api/courses/upcoming")
    public ResponseEntity<CourseListDTO> getCoursesUpcoming(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSession(httpTokenHash);

        User user=null;
        if (session!=null) {
            user=session.getUser();
        }
        CourseListDTO dto = courseService.getCoursesUpcoming(user);
        return getResponse(dto, session);
    }

    @GetMapping("/api/courses/category/{id}")
    public ResponseEntity<CourseByCategoryDTO> getCoursesByCategory(
            @PathVariable(value = "id", required = true) Long id,
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSession(httpTokenHash);
        CourseByCategoryDTO dto = courseService.getCoursesByCategory(id, session);
        return getResponse(dto, session);
    }


    @GetMapping("/api/courses/live")
    public ResponseEntity<CourseListDTO> getCoursesLive(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSession(httpTokenHash);
        User user=null;
        if (session!=null) {
            user=session.getUser();
        }

        CourseListDTO dto = courseService.getCoursesLive(user);
        return getResponse(dto, session);
    }

    @GetMapping("/api/courses/owned")
    public ResponseEntity<CourseListDTO> getOwnedCourses(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        CourseListDTO dto = courseService.findAllByOwnerDTO(session.getUser());
        return getResponse(dto, session);
    }

    @GetMapping("/api/courses/subscribed")
    public ResponseEntity<CourseListDTO> getSubscribedCourses(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        CourseListDTO dto = courseService.findAllBySubscribedDTO(session.getUser());
        return getResponse(dto, session);
    }

    @PostMapping("/api/course/{id}/subscribe")
    public ResponseEntity<ResponseDTO> subscribeCourse(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @PathVariable(value = "id", required = true) Long id) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        courseService.subscribe(id, session.getUser());

        ResponseDTO dto = new ResponseDTO();
        dto.setMessage(MessageStatus.SUBSCRIBED.toString());
        dto.setStatus(HttpStatus.OK.value());
        return getResponse(dto, session);
    }

    @PostMapping("/api/course/{id}/unsubscribe")
    public ResponseEntity<ResponseDTO> unsubscribeCourse(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @PathVariable(value = "id", required = true) Long id) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        courseService.unsubscribe(id, session.getUser());

        ResponseDTO dto = new ResponseDTO();
        dto.setMessage(MessageStatus.UNSUBSCRIBED.toString());
        dto.setStatus(HttpStatus.OK.value());
        return getResponse(dto, session);
    }

    @GetMapping("/api/course/{id}")
    public ResponseEntity<CourseDTO> getCourse(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @PathVariable(value = "id") Long id) {

        UserSession session = sessionService.getUserSession(httpTokenHash);
        if (id == null) exceptionService.throwExceptionBadRequest();
        Course course = courseService.findById(id);
        if (course == null) exceptionService.throwExceptionNotFound();

        User user = null;
        if (session != null)
            user = session.getUser();
        User owner = course.getOwner();
        CourseStatus status = course.getStatus();

        if ((status == CourseStatus.DRAFT) && (user != owner)) {
            exceptionService.throwExceptionUnauthorized();
        }
        return getResponse(courseService.getDTO(course, user), session);
    }

    @PostMapping("/api/course/create")
    public ResponseEntity<CourseCreatedDTO> createCourse(
            @RequestBody PostCourseDTO dto,
            @RequestHeader(name = "Authorization", required = true) String httpTokenHash) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);
        CourseCreatedDTO createdDTO = courseService.createDTO(dto, session.getUser());
        return getResponse(createdDTO, session, HttpStatus.CREATED);
    }

    @PutMapping("/api/course/update")
    public ResponseEntity<ResponseDTO> updateCourse(
            @RequestBody CourseUpdateDTO dto,
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);
        if (courseService.updateDTO(dto, session.getUser())) {
            return getResponse(MessageStatus.COURSE_UPDATED, session, HttpStatus.OK);
        } else {
            return getResponse(MessageStatus.COURSE_UPDATED_ERROR, session, HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/api/course/golive/{id}")
    public ResponseEntity<CourseDTO> goLive(

            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @PathVariable(value = "id") Long id) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        if (id == null) exceptionService.throwExceptionBadRequest();

        CourseDTO courseDTO = courseService.goLive(id, session);

        return getResponse(courseDTO, session, HttpStatus.OK);
    }

    @PutMapping("/api/course/endlive/{id}")
    public ResponseEntity<CourseDTO> endLive(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @PathVariable(value = "id") Long id) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);

        if (id == null) exceptionService.throwExceptionBadRequest();

        CourseDTO courseDTO = courseService.endLive(id, session);

        return getResponse(courseDTO, session, HttpStatus.OK);
    }

    @PostMapping("/api/course/picture/{id}")
    public ResponseEntity<ResponseDTO> uploadFile(
            @RequestHeader(name = "Authorization", required = false) String httpTokenHash,
            @RequestParam("file") MultipartFile file,
            @PathVariable(value = "id") Long id) {

        UserSession session = sessionService.getUserSessionRequired(httpTokenHash);
        if (id == null) exceptionService.throwExceptionBadRequest();
        courseService.updateCourseImage(session.getUser(), id, file);
        return getResponse(MessageStatus.IMAGE_UPLOADED, session, HttpStatus.OK);
    }
}
