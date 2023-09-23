package ru.netology.cloudservicediplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudservicediplom.model.LogoutToken;

public interface LogoutRepository extends JpaRepository<LogoutToken, String> {

//    Optional<LogoutToken> findByToken(String token);
}
