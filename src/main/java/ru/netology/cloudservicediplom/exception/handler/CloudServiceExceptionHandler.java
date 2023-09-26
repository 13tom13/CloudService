package ru.netology.cloudservicediplom.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.cloudservicediplom.exception.*;

@RestControllerAdvice
public class CloudServiceExceptionHandler {


    @ExceptionHandler(CloudServiceErrorInputData.class)
    public ResponseEntity<CloudServiceErrorInputData> ErrorInputDataHandler(CloudServiceErrorInputData e) {
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CloudServiceUnauthorizedError.class)
    public ResponseEntity<CloudServiceUnauthorizedError> UnauthorizedErrorHandler(CloudServiceUnauthorizedError e) {
        return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CloudServiceErrorDeleteFile.class)
    public ResponseEntity<CloudServiceErrorDeleteFile> ErrorDeleteFileHandler(CloudServiceErrorDeleteFile e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CloudServiceErrorUploadFile.class)
    public ResponseEntity<CloudServiceErrorUploadFile> ErrorUploadFileHandler(CloudServiceErrorUploadFile e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CloudServiceFileNotFoundException.class)
    public ResponseEntity<CloudServiceFileNotFoundException> ErrorUploadFileHandler(CloudServiceFileNotFoundException e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
    /login Bad credentials 400

    /file post Error input data 400
    /file post Unauthorized error 401

    /file delete Error input data 400
    /file delete Unauthorized error 401
    /file delete Error delete file 500

    /file put Error input data 400
    /file put Unauthorized error 401
    /file put Error delete file 500

    /file get Error input data 400
    /file get Unauthorized error 401
    /file get Error delete file 500

    /list Error input data 400
    /list Unauthorized error 401
    /list Error delete file 500





     */
}
