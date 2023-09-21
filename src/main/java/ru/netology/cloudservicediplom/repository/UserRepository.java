package ru.netology.cloudservicediplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservicediplom.model.CloudUser;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<CloudUser, Long> {
    Optional<CloudUser> findByLogin(String login);
}
