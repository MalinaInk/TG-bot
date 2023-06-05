package ru.skyteam.pettelegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.repository.ParentRepository;

import java.util.List;

@Service
public class ParentServiceImpl implements ParentService{
    private final ParentRepository parentRepository;
    Logger logger = LoggerFactory.getLogger(ParentServiceImpl.class);

    public ParentServiceImpl(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    /**
     * create parent
     * {@link JpaRepository#save(Object)}
     * @param parent
     * @return object parent
     */
    public Parent create(Parent parent) {
        logger.info("method [create]");
        return parentRepository.save(parent);
    }

    /**
     * read parent
     * {@link JpaRepository#findById(Object)}
     * @param id
     * @return object parent
     */
    public Parent read(long id) {
        logger.info("method [read]");
        return parentRepository.getParentById(id);
    }

    /**
     * update parent
     * {@link JpaRepository#save(Object)}
     * @param parent
     * @return object parent
     */
    public Parent update(Parent parent) {
        logger.info("method [update]");
        return parentRepository.save(parent);
    }

    /**
     * delete parent
     * {@link JpaRepository#delete(Object)}
     * @param id
     * @return object parent
     */
    public ResponseEntity<Parent> delete(long id) {
        logger.info("method [delete]");
        parentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * read all parents
     * {@link JpaRepository#findAll()}
     * @return List Parent
     */
    public List<Parent> readAll() {
        logger.info("method [update]");
        return parentRepository.findAll();
    }

    public Parent findParentByChatId(Long chatId) {
      return parentRepository.getParentByChatId(chatId);
    }
    public Parent save(Parent parent) {
        return parentRepository.save(parent);
    }


}