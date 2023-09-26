package ru.netology.cloudservicediplom.exception;

import static java.lang.String.format;

public class CloudServiceFileNotFoundException extends CloudServiceError {
    public CloudServiceFileNotFoundException(String filename) {
        super(format("File with name:[%s] not found.", filename));
    }
}
