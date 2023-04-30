package ru.skyteam.pettelegrambot.service;

import org.springframework.http.ResponseEntity;
import ru.skyteam.pettelegrambot.entity.Volunteer;

import java.util.List;

public interface VolunteerService {
    public Volunteer create(Volunteer volunteer);
    public Volunteer read(long id);
    public Volunteer update(Volunteer volunteer);
    public ResponseEntity<Volunteer> delete(long id);
    public List<Volunteer> getAll();
}
