package ru.skyteam.pettelegrambot.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.User;
import ru.skyteam.pettelegrambot.repository.UserRepository;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * create user
     * {@link JpaRepository#save(Object)}
     * @param user
     * @return object user
     */
    public User create(User user) {
        logger.info("method [create]");
        return userRepository.save(user);
    }

    /**
     * read user
     * {@link JpaRepository#findById(Object)}
     * @param id
     * @return object user
     */
    public User read(long id) {
        logger.info("method [read]");
        return userRepository.getUserById(id);
    }

    /**
     * update user
     * {@link JpaRepository#save(Object)}
     * @param user
     * @return object user
     */
    public User update(User user) {
        logger.info("method [update]");
        return userRepository.save(user);
    }

    /**
     * delete user
     * {@link JpaRepository#delete(Object)}
     * @param id
     * @return object user
     */
    public ResponseEntity<User> delete(long id) {
        logger.info("method [delete]");
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * read all users
     * {@link JpaRepository#findAll()}
     * @return List User
     */
    public List<User> readAll() {
        logger.info("method [update]");
        return userRepository.findAll();
    }

    /**
     * find user by chatId
     * <br>Использует {@link  #userRepository}
     * @return obj User
     */
    public User findUserByChatId(Long chatId){
        logger.info("method [findUserByChatId]");
        return userRepository.getUserByChatId(chatId);
    }

    /**
     * сохраняем пользователя в базу
     * <br>Использует {@link  #userRepository}
     * @return obj User
     */
    public User save(User user) {
        logger.info("method [save]");
        return userRepository.save(user);
    }

    /**
     * находим пользователя по его последнему действию
     * <br>Использует {@link  #userRepository}
     * @return obj User
     */
    public User getUserByLastAction(LastAction lastAction){
        logger.info("method [getUserByLastAction]");
        return userRepository.getUserByLastAction(lastAction);
    }
}

