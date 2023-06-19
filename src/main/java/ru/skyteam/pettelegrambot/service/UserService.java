package ru.skyteam.pettelegrambot.service;

import org.springframework.http.ResponseEntity;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.User;

import java.util.List;

public interface UserService {
    public User create(User user);
    public User read(long id);
    public User update(User user);
    public ResponseEntity<User> delete(long id);
    public List<User> readAll();
    public User save(User user);
    public User findUserByChatId(Long chatId);
    public User getUserByLastAction(LastAction lastAction);

}
