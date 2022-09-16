package com.ssafy.fullcourse.global.error;

import com.ssafy.fullcourse.domain.sharefullcourse.exception.AlreadyExistException;
import com.ssafy.fullcourse.domain.sharefullcourse.exception.CommentNotFoundException;
import com.ssafy.fullcourse.domain.sharefullcourse.exception.SharedFCNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

class ErrorMessage{
    public static final String SHAREDFC_NOT_FOUNDS = "공유 풀코스가 존재하지 않습니다.";
    public static final String COMMENT_NOT_FOUNDS = "댓글이 존재하지 않습니다.";
}

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ServerError.class})
    public ResponseEntity<Object> handleServerException(SharedFCNotFoundException ex){
        return ResponseEntity.status(500).body(ErrorResponseBody.of(500,ex.getMessage()));
    }

    @ExceptionHandler(value = {AlreadyExistException.class})
    public ResponseEntity<Object> handleSharedFCException(AlreadyExistException ex){
        return ResponseEntity.status(400).body(ErrorResponseBody.of(400,ex.getMessage()));
    }

    @ExceptionHandler(value = {SharedFCNotFoundException.class})
    public ResponseEntity<Object> handleSharedFCException(SharedFCNotFoundException ex){
        return ResponseEntity.status(400).body(ErrorResponseBody.of(400,ErrorMessage.SHAREDFC_NOT_FOUNDS));
    }

    @ExceptionHandler(value = {CommentNotFoundException.class})
    public ResponseEntity<Object> handleSharedFCCommentException(SharedFCNotFoundException ex){
        return ResponseEntity.status(400).body(ErrorResponseBody.of(400,ErrorMessage.COMMENT_NOT_FOUNDS));
    }

}
