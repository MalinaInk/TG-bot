package ru.skyteam.pettelegrambot.report;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.Report;
import ru.skyteam.pettelegrambot.exception.PhotoUploadException;
import ru.skyteam.pettelegrambot.listener.TelegramBotUpdatesListener;
import ru.skyteam.pettelegrambot.service.ParentServiceImpl;
import ru.skyteam.pettelegrambot.service.PetServiceImpl;
import ru.skyteam.pettelegrambot.service.ReportServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skyteam.pettelegrambot.entity.LastAction.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class ReportHandlerTest {


    @Mock
    TelegramBot telegramBot;

    @Autowired
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    @InjectMocks
    private ReportHandler reportHandler;
    @Mock
    private PetServiceImpl petService;
    @Mock
    private ReportServiceImpl reportService;
    @Mock
    private PhotoHandler photoHandler;

    @Mock
    private ParentServiceImpl parentService;

    @Test
    void getPetsOfParentTest() throws PhotoUploadException {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        String expected = """
                Мы не смогли найти вашего питомца
                """;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        reportHandler.handle(update);
        verify(petService).findAllByChatId(chatId);
    }

    @Test
    void ReportTestWaitingName_petsSize1() throws PhotoUploadException {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(petService.findAllByChatId(chatId)).thenReturn(List.of(pet));
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        String expected = """
                Отправьте фото питомца
                """;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        when(reportService.save(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            Report r = (Report) arg;
            Assertions.assertThat(r.getLastAction()).isEqualTo(WAITING_PHOTO);
            return r;
        });
        reportHandler.handle(update);
        verify(petService).findAllByChatId(chatId);
    }

    @Test
    void ReportTestWaitingName_petsSize2() throws PhotoUploadException {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        Pet pet2 = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(petService.findAllByChatId(chatId)).thenReturn(List.of(pet, pet2));
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        String expected = """
                Введите имя питомца                         
                  """;
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        when(reportService.save(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            Report r = (Report) arg;
            Assertions.assertThat(r.getLastAction()).isEqualTo(WAITING_PET_NAME);
            return r;
        });
        reportHandler.handle(update);
        verify(petService).findAllByChatId(chatId);
    }

    @Test
    void ReportTestWaitingName_notFound() throws PhotoUploadException {
        String expected = "Имя питомца не найдено";

        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setName("Lynx");
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        report.setLastAction(WAITING_PET_NAME);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        when(response.isOk()).thenReturn(true);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

//        when(report.getLastAction()).thenReturn(LastAction.WAITING_PET_NAME);
        when(petService.findAllByChatId(chatId)).thenReturn(pets);
        when(message.text()).thenReturn("Hank");

        reportHandler.handle(update);
    }

    @Test
    void ReportTestWaitingName_isConsist() throws PhotoUploadException {
        String expected = "Пришлите фото животного.";

        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setName("Lynx");
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        report.setLastAction(WAITING_PET_NAME);
        when(update.message()).thenReturn(message);

        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        when(response.isOk()).thenReturn(true);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        when(petService.findAllByChatId(chatId)).thenReturn(pets);
        when(message.text()).thenReturn("Lynx");

        when(reportService.save(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            Report r = (Report) arg;
            Assertions.assertThat(r.getLastAction()).isEqualTo(WAITING_PHOTO);
            return r;
        });

        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

        reportHandler.handle(update);
    }

     @Test
    void ReportTestWaitingPhoto_done() throws PhotoUploadException {
        String expected = "Опишите рацион животного";

        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);
//        PhotoHandler photoHandler = mock(PhotoHandler.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        report.setLastAction(WAITING_PHOTO);
        when(update.message()).thenReturn(message);
//        when(message.photo()).thenReturn(any());
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);

        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

        when(photoHandler.receivePhoto(any())).thenReturn("123");

        when(reportService.save(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            Report r = (Report) arg;
            Assertions.assertThat(r.getLastAction()).isEqualTo(WAITING_DIET_INFO);
            return r;
        });

        reportHandler.handle(update);
    }

    @Test
    void ReportTestWaitingPhoto_notFound() throws PhotoUploadException {
        String expected = "Что-то пошло не так. Пришлите фото питомца";


        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        report.setLastAction(WAITING_PHOTO);
        when(update.message()).thenReturn(message);
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);

        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

        when(photoHandler.receivePhoto(any())).thenReturn(null);
        reportHandler.handle(update);
    }

    @Test
    void ReportTestWaitingDietInfo() throws PhotoUploadException {
        String expected = "Опишите общее самочувствие животного и привыкание к новому месту";

        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        report.setLastAction(WAITING_DIET_INFO);
        when(update.message()).thenReturn(message);

        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

        when(reportService.save(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            Report r = (Report) arg;
            Assertions.assertThat(r.getLastAction()).isEqualTo(WAITING_HEALTH_INFO);
            return r;
        });

        reportHandler.handle(update);
    }

    @Test
    void ReportTestWaitingHealthInfo() throws PhotoUploadException {
        String expected = "Опишите изменения в поведении: отказ от старых привычек, приобретение новых";
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        report.setLastAction(WAITING_HEALTH_INFO);
        when(update.message()).thenReturn(message);

        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

        when(reportService.save(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            Report r = (Report) arg;
            Assertions.assertThat(r.getLastAction()).isEqualTo(WAITING_CHANGING_HABITS_INFO);
            return r;
        });

        reportHandler.handle(update);
    }


    @Test
    void ReportTestWaitingChangingHabitsInfo() throws PhotoUploadException {
        String expected = "Отчет успешно заполнен. Спасибо!";
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);
        Report report = new Report();
        Parent parent = new Parent();
        Pet pet = new Pet();
        pet.setParent(parent);
        pet.setReports(List.of(report));
        report.setPet(pet);
        report.setParent(parent);
        report.setLastAction(WAITING_CHANGING_HABITS_INFO);
        when(update.message()).thenReturn(message);

        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(reportService.reportFindLastByParent(any())).thenReturn(report);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

        when(reportService.save(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            Report r = (Report) arg;
            Assertions.assertThat(r.getLastAction()).isEqualTo(DONE);
            return r;
        });

        reportHandler.handle(update);
    }
}
