package com.dchprojects.mydictionaryrestapi.api;

import com.dchprojects.mydictionaryrestapi.api.path.Path;
import com.dchprojects.mydictionaryrestapi.domain.dto.CreateWordRequest;
import com.dchprojects.mydictionaryrestapi.domain.dto.UpdateWordRequest;
import com.dchprojects.mydictionaryrestapi.domain.dto.WordResponse;
import com.dchprojects.mydictionaryrestapi.domain.entity.role.RoleNameString;
import com.dchprojects.mydictionaryrestapi.service.WordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.annotation.security.RolesAllowed;
import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Word")
@RestController
@RequestMapping(Path.REQUEST_PATH_API_WORDS)
@RolesAllowed({RoleNameString.ROLE_USER, RoleNameString.ROLE_ADMIN})
@RequiredArgsConstructor
public class WordApi {

    private final WordService wordService;

    private static final String REQUEST_PATH_API_WORDS_INDIVIDUAL_USER = "/userId/{userId}";
    private static final String REQUEST_PATH_API_WORDS_INDIVIDUAL_COURSE = "/courseId/{courseId}";
    private static final String REQUEST_PATH_API_DELETE_WORD_INDIVIDUAL_USER_INDIVIDUAL_COURSE_INDIVIDUAL_WORD = "/userId/{userId}/courseId/{courseId}/wordId/{wordId}";

    @GetMapping(REQUEST_PATH_API_WORDS_INDIVIDUAL_COURSE)
    public ResponseEntity<List<WordResponse>> listByCourseId(@PathVariable Long courseId) {
        return new ResponseEntity<>(wordService.listAllByCourseId(courseId), HttpStatus.OK);
    }

    @GetMapping(REQUEST_PATH_API_WORDS_INDIVIDUAL_USER)
    public ResponseEntity<List<WordResponse>> listByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(wordService.listAllByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WordResponse> createWord(@RequestBody @Valid CreateWordRequest createWordRequest) {
        try {
            return new ResponseEntity<>(wordService.create(createWordRequest), HttpStatus.OK);
        } catch (ValidationException validationException) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<WordResponse> udpateWord(@RequestBody @Valid UpdateWordRequest updateWordRequest) {
        try {
            return new ResponseEntity<>(wordService.update(updateWordRequest), HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ValidationException validationException) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(REQUEST_PATH_API_DELETE_WORD_INDIVIDUAL_USER_INDIVIDUAL_COURSE_INDIVIDUAL_WORD)
    public ResponseEntity<?> deleteWord(@PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long wordId) {
        try {
            wordService.deleteByUserIdAndCourseIdAndWordId(userId, courseId, wordId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
