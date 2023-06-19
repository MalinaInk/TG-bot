package ru.skyteam.pettelegrambot.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skyteam.pettelegrambot.entity.Shelter;
import ru.skyteam.pettelegrambot.repository.ShelterRepository;

import java.util.List;
@Service
public class ShelterServiceImpl implements ShelterService{

    private ShelterRepository shelterRepository;

    public ShelterServiceImpl( ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    @Override
    public Shelter create(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    @Override
    public Shelter read(long id) {
        return shelterRepository.getShelterById(id);
    }

    @Override
    public Shelter update(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    @Override
    public ResponseEntity<Shelter> delete(long id) {
        shelterRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<Shelter> readAll() {
        return shelterRepository.findAll();
    }
}
