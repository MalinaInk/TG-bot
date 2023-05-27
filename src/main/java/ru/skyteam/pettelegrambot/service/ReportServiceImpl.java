package ru.skyteam.pettelegrambot.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.Report;
import ru.skyteam.pettelegrambot.repository.ReportRepository;
import java.time.LocalDate;
import java.util.List;
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final PetServiceImpl petService;

    public ReportServiceImpl(ReportRepository reportRepository, PetServiceImpl petService) {
        this.reportRepository = reportRepository;
        this.petService = petService;
    }

    @Override
    public Report create(Report report) {
        return null;
    }

    @Override
    public Report read(long id) {
        return null;
    }

    @Override
    public Report update(Pet pet) {
        return null;
    }

    @Override
    public ResponseEntity<Report> delete(long id) {
        return null;
    }

    @Override
    public List<Report> readAll() {
        return null;
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    public Report reportFindLastByParent(Parent parent) {
    return reportRepository.findAllByParentId(parent.getId())
            .stream()
            .filter(report -> report.getReportDate()
                    .equals(LocalDate.now())
                    &&!report.getLastAction()
                    .equals(LastAction.DONE))
            .findFirst()
            .orElse(new Report());
    }

    public LocalDate getLatestDateByPetId(Long id) {
        return reportRepository.findLatestDateByPetId(id);
    }


}
