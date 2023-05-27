package ru.skyteam.pettelegrambot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Pet;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet getPetById(Long id);

    Collection<Pet> findAllByPetType(String type);
    List<Pet> findAllByParentId(Long id);

    List<Pet> findAllByDateOfEndReportAfter(LocalDate date);

    List<Pet> findAllByDateOfEndReportEquals(LocalDate date);


}