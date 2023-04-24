package ru.skyteam.pettelegrambot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Shelter;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
