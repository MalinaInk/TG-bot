package ru.skyteam.pettelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Volunteer;
import java.util.List;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Volunteer getVolunteerById(Long id);

    @Query(value = "select * from volunteer  ", nativeQuery = true)
    List<Volunteer> getAll();

}
