package ru.skyteam.pettelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.ParentPet;

@Repository
public interface ParentPetRepository extends JpaRepository<ParentPet, Long> {

}
