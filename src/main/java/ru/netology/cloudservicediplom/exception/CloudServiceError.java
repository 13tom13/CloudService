package ru.netology.cloudservicediplom.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CloudServiceError extends RuntimeException{

    public CloudServiceError(String message){
        super(message);
        log.error(message);
    }
}
