package ru.skyteam.pettelegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skyteam.pettelegrambot.entity.Volunteer;
import ru.skyteam.pettelegrambot.repository.VolunteerRepository;

import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService{
    private final VolunteerRepository volunteerRepository;
    Logger logger = LoggerFactory.getLogger(VolunteerService.class);


    VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * create volunteer
     * {@link JpaRepository#save(Object)}
     * @param volunteer
     * @return object volunteer
     */
    public Volunteer create(Volunteer volunteer) {
        logger.info("method [create]");
        return volunteerRepository.save(volunteer);
    }

    /**
     * read volunteer
     * {@link JpaRepository#findById(Object)}
     * @param id
     * @return object volunteer
     */
    public Volunteer read(long id) {
        logger.info("method [read]");
        return volunteerRepository.getVolunteerById(id);
    }

    /**
     * update volunteer
     * {@link JpaRepository#save(Object)}
     * @param volunteer
     * @return object volunteer
     */
    public Volunteer update(Volunteer volunteer) {
        logger.info("method [update]");
        return volunteerRepository.save(volunteer);
    }

    /**
     * delete volunteer
     * {@link JpaRepository#delete(Object)}
     * @param id
     */
    public ResponseEntity<Volunteer> delete(long id) {
        logger.info("method [delete]");
        volunteerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all volunteers
     * {@link VolunteerRepository#getAll()}
     * @return List<Volunteer>
     */
    public List <Volunteer> getAll() {
        return volunteerRepository.getAll();
    }
}