package ru.skyteam.pettelegrambot.reminder;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skyteam.pettelegrambot.entity.*;
import ru.skyteam.pettelegrambot.listener.TelegramBotUpdatesListener;
import ru.skyteam.pettelegrambot.message.BotReplayMessage;
import ru.skyteam.pettelegrambot.repository.PetRepository;
import ru.skyteam.pettelegrambot.repository.ReportRepository;
import ru.skyteam.pettelegrambot.service.ParentService;
import ru.skyteam.pettelegrambot.service.ParentServiceImpl;
import ru.skyteam.pettelegrambot.service.PetServiceImpl;
import ru.skyteam.pettelegrambot.service.ReportServiceImpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;
import static ru.skyteam.pettelegrambot.entity.StatusOfAdoption.*;
import static ru.skyteam.pettelegrambot.message.BotReplayMessage.WARNING_TO_REPORT;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class TelegramBotReminderTest {

    @Mock
    TelegramBot telegramBot;

    @Autowired
    TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Captor
    private ArgumentCaptor<String> chatIdCaptor;
    @Captor
    private ArgumentCaptor<String> textCaptor;
    @InjectMocks
    private TelegramBotReminder telegramBotReminder;

    @Mock
    private PetServiceImpl petService;
    @Mock
    private ParentServiceImpl parentServiceImpl;
    @Mock
    private ReportServiceImpl reportServiceImpl;
    @Mock
    private BotReplayMessage botReplayMessage;

    static LocalDate startDate = LocalDate.of(2023, 05, 25);

    private static Pet getPet() {
        List<Report> reports = new ArrayList<>();

        Pet pet = new Pet(1L, PetType.DOG, "Sharik", null);
        pet.setDateOfAdoption(startDate);
        pet.setDateOfEndReport(LocalDate.of(2023, 06, 25));
        pet.setReports(reports);
        return pet;
    }

    @ParameterizedTest
    @MethodSource("sourceProviderForCheckActualStartPeriod")
    void getActualStartPeriodTest(Pet pet, LocalDate expectResult) {
        telegramBotReminder.getActualStartPeriod(pet);
    }

    static Stream<Arguments> sourceProviderForCheckActualStartPeriod() {
        Pet pet1 = getPet();
        Pet pet2 = getPet();
        Pet pet3 = getPet();

        pet1.setDateOfEndReport(startDate.plusDays(60));
        pet2.setDateOfEndReport(startDate.plusDays(30));
        pet3.setDateOfEndReport(startDate.plusDays(44));

        return Stream.of(
                Arguments.of(pet1, startDate.plusDays(30)),
                Arguments.of(pet2, startDate),
                Arguments.of(pet3, startDate.plusDays(30))
        );
    }


    private static List<Report> getListReportCorrectOnly() {
        List<Report> reports = new ArrayList<>();
        LocalDate date = LocalDate.of(2023, 05, 25);

        for (int i = 0; i < 30; i++) {
            Report report = new Report();
            report.setReportDate(date.plusDays(i));
            report.setCorrect(true);
            reports.add(report);
        }
        return reports;
    }

    private static List<Report> getListReportWith3Incorrect() {
        List<Report> reports = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            Report report = new Report();
            report.setReportDate(startDate.plusDays(i));
            if (i % 10 == 0) {
                report.setCorrect(false);
            } else {
                report.setCorrect(true);
            }
            reports.add(report);
        }
        return reports;
    }

    private static List<Report> getListReportWith3EmptyReports() {
        List<Report> reports = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            Report report = new Report();
            report.setReportDate(startDate.plusDays(i));
            if (i % 10 != 0) {
                report.setCorrect(true);
                reports.add(report);
            }
        }
        return reports;
    }

    @ParameterizedTest
    @MethodSource("sourceProviderForCheckQuantityBadReport")
    void getQuantityBadReportsTest(Pet pet, int diapason, int expectResult) {

        int Q = telegramBotReminder.getQuantityBadReports(startDate, startDate.plusDays(diapason - 1), pet);
        Assertions.assertThat(Q).isEqualTo(expectResult);
    }

    static Stream<Arguments> sourceProviderForCheckQuantityBadReport() {
        Pet pet1 = getPet();
        Pet pet2 = getPet();
        Pet pet3 = getPet();
        Pet pet4 = getPet();

        pet1.setReports(getListReportWith3EmptyReports());
        pet2.setReports(getListReportWith3Incorrect());
        pet3.setReports(getListReportCorrectOnly());
        List<Report> incorrectAndEmptyReport = new ArrayList<>();
        incorrectAndEmptyReport.addAll(getListReportWith3EmptyReports());
        incorrectAndEmptyReport.addAll(getListReportWith3Incorrect());
        pet4.setReports(incorrectAndEmptyReport);

        return Stream.of(
                Arguments.of(pet1, 30, 3),
                Arguments.of(pet2, 30, 3),
                Arguments.of(pet3, 30, 0),
                Arguments.of(pet4, 60, 6)
        );
    }


// САМ МЕТОД
//    public List<Long> getListOfParentsChatIdForReminding(List<Pet> pets) {
//        return pets.stream()
//                .filter(p -> p.getParent() != null)
//                .map(pet -> pet.getParent().getChatId())
//                .distinct()
//                .collect(Collectors.toList());
//    }

    @Test
    @DisplayName("Метод выдает верные уникальные chatId по списку питомцев")
    void getListOfParentsChatIdForRemindingTest_uniqueAndNotNull() {
        List<Long> actual = telegramBotReminder.getListOfParentsChatIdForReminding(TelegramBotReminderTest.getListOfPetForReminding());
        List<Long> expected = new ArrayList<>(Arrays.asList(123L, 124L, 125L));
        Assertions.assertThat(actual.equals(expected));
    }

    //Тестовые данные для теста выше
    static List<Pet> getListOfPetForReminding() {
        Pet pet1 = new Pet();
        Pet pet2 = new Pet();
        Pet pet3 = new Pet();
        Pet pet4 = new Pet();
        Pet pet5 = new Pet();
        Parent parent1 = new Parent(1L, 123L, "name_name1", "123", new ArrayList<>());
        Parent parent2 = new Parent(2L, 124L, "name_name2", "123", new ArrayList<>());
        Parent parent3 = new Parent(3L, 125L, "name_name3", "123", new ArrayList<>());
        pet1.setParent(parent1);
        pet2.setParent(parent2);
        pet3.setParent(parent3);
        pet4.setParent(parent3);
        List<Pet> pets = new ArrayList<>(Arrays.asList(pet1, pet2, pet3, pet4, pet5));
        return pets;
    }

    @Test
    public void scheduleRemindersOfReportTest() {
        SendResponse response = mock(SendResponse.class);
        Mockito.when(petService.listPetForReport()).thenReturn(getListOfPetForReminding());

        String expected = """
                Уважаемый усыновитель! 
                Это стандартное сообщение, напоминающее Вам о важности ежедневных отчетов.
                Если вы еще не отправили свой отчет по питомцу, не забудьте это сделать, заполнив все нужные поля.
                Через час мы соберем отчеты на проверку, но у вас есть время до 24.00, если не успеете вовремя.
                """;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotReminder.scheduleRemindersOfReport();
    }

    @Test
    public void scheduleWarningToReportTest() {
       Pet pet = new Pet();
//        ReportRepository reportRepository = mock(ReportRepository.class);
        pet.setId(1L);
        pet.setReports(getListReportWith3EmptyReportInTheEnd());
//        pet.setDateOfAdoption(startDate);
//        pet.setDateOfEndReport(startDate.plusDays(30));
        Parent parent = mock(Parent.class);
        parent.setId(2L);
        pet.setParent(parent);
//        Volunteer volunteer = mock(Volunteer.class);
//        pet.setVolunteer(volunteer);
//
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

//        ReportServiceImpl reportService = mock(ReportServiceImpl.class);
//        when(reportRepository.findLatestDateByPetId(1L)).thenReturn(LocalDate.now().minusDays(4));
        String expected = WARNING_TO_REPORT;
        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForReport()).thenReturn(pets);
        when(reportServiceImpl.getLatestDateByPetId(anyLong())).thenReturn(LocalDate.now().minusDays(2));
//            when(pet.getParent().getFullName()).thenReturn("Иванов Иван Иванович");
//            when(pet.getParent().getPhoneNumber()).thenReturn("8-888-88-88-888");
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotReminder.scheduleWarningToReport();
    }


    private static List<Report> getListReportWith3EmptyReportInTheEnd() {
        List<Report> reports = new ArrayList<>();

        for (int i = 4; i > 1; i--) {
            Report report = new Report();
            report.setReportDate(LocalDate.now().minusDays(i));
            report.setCorrect(false);
            reports.add(report);
        }
        return reports;
    }

    @Test
    public void scheduleToVolunteerTwoEmptyReportTest() {
        Pet pet = new Pet();
        pet.setReports(getListReportWith3EmptyReportInTheEnd());
        pet.setDateOfAdoption(startDate);
        pet.setDateOfEndReport(startDate.plusDays(30));
        Parent parent = mock(Parent.class);
        pet.setParent(parent);
        Volunteer volunteer = mock(Volunteer.class);
        pet.setVolunteer(volunteer);

        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        String expected = "Усыновитель Иванов Иван Иванович не присылал отчета уже 2 дня. " +
                "Свяжитесь с ним по номеру 8-888-88-88-888";
        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForReport()).thenReturn(pets);
        when(pet.getParent().getFullName()).thenReturn("Иванов Иван Иванович");
        when(pet.getParent().getPhoneNumber()).thenReturn("8-888-88-88-888");
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotReminder.scheduleToVolunteerTwoEmptyReport();
    }

    //волонтеру
    @Test
    public void scheduleCheckResultOTestPeriodToVolunteer_WILL_BE_RETURN() {
        Pet pet = new Pet();
        List<Report> reports = new ArrayList<>();
        pet.setReports(reports);
        pet.setDateOfAdoption(startDate);
        pet.setDateOfEndReport(startDate.plusDays(30));
        Parent parent = mock(Parent.class);
        pet.setParent(parent);
        Volunteer volunteer = mock(Volunteer.class);
        pet.setVolunteer(volunteer);

        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForEndingReport()).thenReturn(pets);
        when(pet.getParent().getFullName()).thenReturn("Иванов Иван Иванович");
        when(pet.getParent().getPhoneNumber()).thenReturn("8-888-88-88-888");
        String expected = "Усыновитель Иванов Иван Иванович"
                + "допустил более 3-х некорректных отчетов за тестовый период. "
                + "\nАвтоматически питомцу будет присвоен статус WILL_BE_RETURN (нужно вернуть), "
                + "\nа усыновителю отправлено сообщение о необходимости возврата питомца. "
                + "\nПри необходимости вы можете поменять статус, а также связаться с усыновителем по номеру: "
                + "8-888-88-88-888";
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            Assertions.assertThat(pet.getStatusOfAdoption()).isEqualTo(WILL_BE_RETURN);
            return response;
        });
        telegramBotReminder.scheduleCheckResultOTestPeriodToVolunteer();
    }


    @Test
    public void scheduleCheckResultOTestPeriodToVolunteer_IN_HOME() {
        Pet pet = new Pet();
        pet.setReports(getListReportWith3EmptyReports());
        pet.setDateOfAdoption(startDate);
        pet.setDateOfEndReport(startDate.plusDays(30));
        Parent parent = mock(Parent.class);
        pet.setParent(parent);
        Volunteer volunteer = mock(Volunteer.class);
        pet.setVolunteer(volunteer);

        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForEndingReport()).thenReturn(pets);
        when(pet.getParent().getFullName()).thenReturn("Иванов Иван Иванович");
        when(pet.getParent().getPhoneNumber()).thenReturn("8-888-88-88-888");
        String expected = "Усыновитель Иванов Иван Иванович"
                + "допустил 2-3 некорректных отчета за тестовый период. "
                + "\nАвтоматически питомцу будет подтвержден статус IN_PROCESS, "
                + "\nа усыновителю продлен испытательный срок на 14 дней и отправлено соответствующее уведомление. "
                + "\nПри необходимости вы можете увеличить испытательный срок на 30 дней или отказать в усыновлении, если имеются объективные причины. "
                + "\nСвязаться с усыновителем можно по номеру: "
                + "8-888-88-88-888";
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            Assertions.assertThat(pet.getStatusOfAdoption()).isEqualTo(IN_PROCESS);
            Assertions.assertThat(pet.getDateOfEndReport()).isEqualTo(startDate.plusDays(44));
            return response;
        });
        telegramBotReminder.scheduleCheckResultOTestPeriodToVolunteer();
    }

    @Test
    public void scheduleCheckResultOTestPeriodToVolunteer_IN_PROCESS() {
        Pet pet = new Pet();
        pet.setReports(getListReportCorrectOnly());
        pet.setDateOfAdoption(startDate);
        pet.setDateOfEndReport(startDate.plusDays(30));
        Parent parent = mock(Parent.class);
        pet.setParent(parent);
        Volunteer volunteer = mock(Volunteer.class);
        pet.setVolunteer(volunteer);

        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForEndingReport()).thenReturn(pets);
        when(pet.getParent().getFullName()).thenReturn("Иванов Иван Иванович");
        when(pet.getParent().getPhoneNumber()).thenReturn("8-888-88-88-888");
        String expected = "Усыновитель Иванов Иван Иванович"
                + "допустил не более 1-го некорректного отчета за тестовый период. "
                + "\nАвтоматически питомцу будет присвоен статус IN_HOME (усыновлен), "
                + "\nа усыновителю отправлено поздравление с усыновлением питомца. "
                + "\nПри необходимости вы можете поменять статус, а также связаться с усыновителем по номеру: "
                + "8-888-88-88-888";
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            Assertions.assertThat(pet.getStatusOfAdoption()).isEqualTo(IN_HOME);
            return response;
        });
        telegramBotReminder.scheduleCheckResultOTestPeriodToVolunteer();
    }

    //усыновителю
    @Test
    void scheduleRemindersOfEndingTestPeriodTest_PET_RETURN() {

        Pet pet = mock(Pet.class);
        Parent parent = mock(Parent.class);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForEndingReport()).thenReturn(pets);
        when(pet.getStatusOfAdoption()).thenReturn(WILL_BE_RETURN);
        when(pet.getParent()).thenReturn(parent);
        String expected = BotReplayMessage.PET_RETURN;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotReminder.scheduleRemindersOfEndingTestPeriod();
        verify(petService).listPetForEndingReport();
    }

    @Test
    void scheduleRemindersOfEndingTestPeriodTest_ADDITIONAL_TIME_14() {

        Pet pet = mock(Pet.class);
        Parent parent = mock(Parent.class);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForEndingReport()).thenReturn(pets);
        when(pet.getStatusOfAdoption()).thenReturn(IN_PROCESS);
        when(pet.getDateOfEndReport()).thenReturn(LocalDate.now().plusDays(14));
        when(pet.getParent()).thenReturn(parent);
        String expected = BotReplayMessage.ADDITIONAL_TIME_14;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotReminder.scheduleRemindersOfEndingTestPeriod();
        verify(petService).listPetForEndingReport();
    }

    @Test
    void scheduleRemindersOfEndingTestPeriodTest_ADDITIONAL_TIME_30() {

        Pet pet = mock(Pet.class);
        Parent parent = mock(Parent.class);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForEndingReport()).thenReturn(pets);
        when(pet.getStatusOfAdoption()).thenReturn(IN_PROCESS);
        when(pet.getDateOfEndReport()).thenReturn(LocalDate.now().plusDays(30));
        when(pet.getParent()).thenReturn(parent);
        String expected = BotReplayMessage.ADDITIONAL_TIME_30;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotReminder.scheduleRemindersOfEndingTestPeriod();
        verify(petService).listPetForEndingReport();
    }

    @Test
    void scheduleRemindersOfEndingTestPeriodTest_CONGRATULATIONS() {

        Pet pet = mock(Pet.class);
        Parent parent = mock(Parent.class);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        SendResponse response = mock(SendResponse.class);
        when(petService.listPetForEndingReport()).thenReturn(pets);
        when(pet.getStatusOfAdoption()).thenReturn(IN_HOME);
        when(pet.getParent()).thenReturn(parent);
        String expected = BotReplayMessage.CONGRATULATIONS;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotReminder.scheduleRemindersOfEndingTestPeriod();
        verify(petService).listPetForEndingReport();
    }


}