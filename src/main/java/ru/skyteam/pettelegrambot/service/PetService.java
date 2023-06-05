package ru.skyteam.pettelegrambot.service;

import org.springframework.http.ResponseEntity;
import ru.skyteam.pettelegrambot.entity.Pet;

import java.util.List;

public interface PetService {

    public Pet create (Pet pet);
    public Pet read (long id);
    public Pet update (Pet pet);
    public ResponseEntity<Pet> delete (long id);
    public List<Pet> readAll();


}
