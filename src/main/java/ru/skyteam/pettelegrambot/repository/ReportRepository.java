package ru.skyteam.pettelegrambot.repository;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skyteam.pettelegrambot.entity.Report;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Report getReportById(Long id);

    List<Report> findAllByParentId(Long id);

    List<Report> findAllByIsCorrectIsNull();


    @Query("select max(r.reportDate) from Report r where r.pet.id= :petId")
    LocalDate findLatestDateByPetId(@Parameter(name="petId") Long petId);


}
