package ru.netology.cloudservicediplom.service;

import ru.netology.cloudservicediplom.dto.AuthRequest;

public interface AuthenticationService {

    String getToken (AuthRequest authRequest);

    void logout (String token);
}
