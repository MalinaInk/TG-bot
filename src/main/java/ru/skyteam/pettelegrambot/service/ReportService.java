package ru.skyteam.pettelegrambot.service;

import org.springframework.http.ResponseEntity;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.Report;

import java.util.List;

public interface ReportService {
    public Report create (Report report);
    public Report read (long id);
    public Report update (Pet pet);
    public ResponseEntity<Report> delete (long id);
    public List<Report> readAll();
    public Report save(Report report);
    public Report reportFindLastByParent(Parent parent);
}
