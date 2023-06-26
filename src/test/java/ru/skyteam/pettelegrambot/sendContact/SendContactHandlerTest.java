package ru.skyteam.pettelegrambot.sendContact;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.User;
import ru.skyteam.pettelegrambot.listener.TelegramBotUpdatesListener;
import ru.skyteam.pettelegrambot.service.ParentService;
import ru.skyteam.pettelegrambot.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.skyteam.pettelegrambot.entity.LastAction.DONE;
import static ru.skyteam.pettelegrambot.entity.LastAction.WAITING_USER_FULL_NAME;


@ExtendWith(MockitoExtension.class)

public class SendContactHandlerTest {
    @Autowired
    TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private Parent parent;

    @Mock
    private User user;
    @Mock
    private UserService userService;
    @Mock
    private ParentService parentService;
    @InjectMocks
    private SendContactHandler sendContactHandler;


    @Test
    public void testHandleMessageStartContact() {
        String expected = "Перед тем как взять питомца вы должны оставить свои контактные данные. " +
                "Пожалуйста, укажите ваши Имя и Фамилию: ";
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456L);
        when(userService.findUserByChatId(123456L)).thenReturn(user);
        when(user.getLastAction()).thenReturn(LastAction.START_CONTACT);
//        when(update.message().text()).thenReturn(fullName);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });

        sendContactHandler.handleContact(update);
        verify(user).setLastAction(WAITING_USER_FULL_NAME);


    }

    @Test
    void testHandleMessageWaitingFullName() {
        String expected = "Пожалуйста, введите номер телефона в формате +7 XXX XXX XX XX";
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        String fullName = "Иванов Иван";

        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
//        when(telegramBot.execute(any(SendMessage.class))).thenReturn(response);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456L);
        when(userService.findUserByChatId(123456L)).thenReturn(user);
        when(user.getLastAction()).thenReturn(WAITING_USER_FULL_NAME);

        parent.setFullName(fullName);

        when(parentService.findParentByChatId(123456L)).thenReturn(parent);
        when(userService.save(user)).thenReturn(user);
        when(telegramBot.execute(any())).then((invocation) -> {
                    Object arg = invocation.getArgument(0);
                    SendMessage messageArg = (SendMessage) arg;
                    Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
                    return response;
                }
        );

        sendContactHandler.handleContact(update);
        verify(user).setLastAction(LastAction.WAITING_USER_PHONE);
        verify(parentService, Mockito.times(1)).save(parent);
    }

    @Test
    void testHandleWaitingUserPhoneValid() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        User user = Mockito.mock(User.class);
        String phoneNumber = "89123456789";

        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(response);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456L);
        when(userService.findUserByChatId(123456L)).thenReturn(user);
        when(user.getLastAction()).thenReturn(LastAction.WAITING_USER_PHONE);
        when(update.message().text()).thenReturn(phoneNumber);

        sendContactHandler.handleContact(update);
        sendContactHandler.sendMessage(123456L, """
                Контактные данные успешно сохранены. Спасибо! +
                В случае, если Вы по ошибке ввели не свой номер телефона, Вы можете связаться с +
                волонтером - клавиша в главном меню
                """);

        parent.setPhoneNumber(phoneNumber);
        when(parentService.save(parent)).thenReturn(parent);
        Parent savedParent = parentService.save(parent);
        Mockito.verify(parentService, Mockito.times(1)).save(parent);
        assertEquals(parent, savedParent);

        verify(user).setLastAction(LastAction.DONE_CONTACT);

    }

    @Test
    void testHandleWaitingUserPhoneInvalid() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        User user = Mockito.mock(User.class);
        String phoneNumber = "123";
        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(response);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456L);
        when(userService.findUserByChatId(123456L)).thenReturn(user);
        when(user.getLastAction()).thenReturn(LastAction.WAITING_USER_PHONE);
        when(update.message().text()).thenReturn(phoneNumber);

        sendContactHandler.handleContact(update);
        sendContactHandler.sendMessage(123456L, """
                Убедитесь, что вводите номер телефона российского оператора связи, повторите попытку
                """);
    }

    @Test
    public void testSendMessage() {

        SendMessage message = mock(SendMessage.class);
        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(response);
        sendContactHandler.sendMessage(123456L, "Test message");
        verify(telegramBot).execute(any(SendMessage.class));
    }
}