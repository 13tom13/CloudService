package ru.netology.cloudservicediplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservicediplom.model.MyUser;


@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    MyUser findByLogin(String login);
}
