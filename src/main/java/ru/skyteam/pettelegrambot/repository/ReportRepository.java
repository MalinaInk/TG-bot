package ru.skyteam.pettelegrambot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Report;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Report getReportById(Long id);

    List<Report> findAllByParentId(Long id);

    List<Report> findAllByIsCorrectIsNull();
}
