package ru.skyteam.pettelegrambot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Long> {
    Photo getPhotoById(Long id);
}