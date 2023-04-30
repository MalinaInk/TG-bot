package ru.skyteam.pettelegrambot.service;
import org.springframework.http.ResponseEntity;
import ru.skyteam.pettelegrambot.entity.Parent;
import java.util.List;

public interface ParentService {
    public Parent create (Parent parent);
    public Parent read (long id);
    public Parent update (Parent parent);
    public ResponseEntity <Parent> delete (long id);
    public List <Parent> readAll();
}
