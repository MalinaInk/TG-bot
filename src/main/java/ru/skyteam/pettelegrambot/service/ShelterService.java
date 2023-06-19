package ru.skyteam.pettelegrambot.service;

import org.springframework.http.ResponseEntity;
import ru.skyteam.pettelegrambot.entity.Shelter;


import java.util.List;

public interface ShelterService {
    public Shelter create(Shelter shelter);
    public Shelter read(long id);
    public Shelter update(Shelter shelter);
    public ResponseEntity<Shelter> delete(long id);
    public List<Shelter> readAll();
}
