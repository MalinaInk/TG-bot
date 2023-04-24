package ru.skyteam.pettelegrambot.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Parent;
import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Parent getParentByChatId(Long id);

    Parent getParentById(Long id);

    List<Parent> findAllByVolunteerId(Long volunteer_id);

    List<Parent> findAllByNumberOfReportDaysAfter(Integer numberOfReportDays);

}
