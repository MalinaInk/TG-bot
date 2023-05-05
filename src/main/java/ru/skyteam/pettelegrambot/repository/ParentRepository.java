package ru.skyteam.pettelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Parent;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Parent getParentByChatId(Long id);

    Parent getParentById(Long id);

    List<Parent> findAllByNumberOfReportDaysAfter(Integer numberOfReportDays);

}
