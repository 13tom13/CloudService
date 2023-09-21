package ru.netology.cloudservicediplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudservicediplom.model.CloudFile;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<CloudFile, Long> {
    List<CloudFile> findByUsername(String username);

    Optional<CloudFile> findByUsernameAndFilename(String username, String filename);
}
