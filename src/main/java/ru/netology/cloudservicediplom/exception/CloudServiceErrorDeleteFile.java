package ru.netology.cloudservicediplom.exception;

import static java.lang.String.format;

public class CloudServiceErrorDeleteFile  extends CloudServiceError {
    public CloudServiceErrorDeleteFile(String filename) {
        super(format("File with name:[%s] not delete.", filename));
    }
}
