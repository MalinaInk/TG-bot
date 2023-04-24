package ru.skyteam.pettelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Pet;

import java.util.Collection;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet getPetById(Long id);

    Collection<Pet> findAllByPetType(String type);
}