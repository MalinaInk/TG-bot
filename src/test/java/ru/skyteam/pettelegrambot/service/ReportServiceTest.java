package ru.skyteam.pettelegrambot.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.Report;
import ru.skyteam.pettelegrambot.repository.ReportRepository;
import java.time.LocalDate;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void testReportFindLastByParent() {
        Parent parent = new Parent();
        parent.setId(1L);

        Report report1 = new Report();
        report1.setParent(parent);
        report1.setReportDate(LocalDate.now());
        report1.setLastAction(LastAction.WAITING_PET_NAME);

        Report report2 = new Report();
        report2.setParent(parent);
        report2.setReportDate(LocalDate.now().minusDays(1));
        report2.setLastAction(LastAction.DONE);

        Report report3 = new Report();
        report3.setParent(parent);
        report3.setReportDate(LocalDate.now());
        report3.setLastAction(LastAction.WAITING_PHOTO);

        Mockito.when(reportRepository.findAllByParentId(parent.getId()))
                .thenReturn(Arrays.asList(report1, report2, report3));
        Report result = reportService.reportFindLastByParent(parent);
        assertThat(result).isEqualTo(report1);
    }

    @Test
    void testGetLatestDateByPetId() {
        Long petId = 1L;
        LocalDate latestDate = LocalDate.now().minusDays(1);
        Mockito.when(reportRepository.findLatestDateByPetId(petId)).thenReturn(latestDate);
        LocalDate result = reportService.getLatestDateByPetId(petId);
        assertThat(result).isEqualTo(latestDate);
    }

}