package ru.skyteam.pettelegrambot.sendContact;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
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


@ExtendWith(MockitoExtension.class)

public class SendContactHandlerTest {
    @Autowired
    TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private Parent parent;
    @Mock
    private UserService userService;
    @Mock
    private ParentService parentService;
    @InjectMocks
    private SendContactHandler sendContactHandler;


    @Test
    public void testHandleMessageStartContact() {

        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        User user = Mockito.mock(User.class);

        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(response);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456L);
        when(userService.findUserByChatId(123456L)).thenReturn(user);
        when(user.getLastAction()).thenReturn(LastAction.START_CONTACT);

        sendContactHandler.handleContact(update);
        sendContactHandler.sendMessage(123456L, """
                Введите Ваши Имя и Фамилию:
                """);
        verify(user).setLastAction(LastAction.WAITING_USER_FULL_NAME);
    }

    @Test
    void testHandleWaitingUserFullName() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        User user = Mockito.mock(User.class);
        String fullName = "Иванов Иван";

        SendResponse response = mock(SendResponse.class);
        when(response.isOk()).thenReturn(true);
        when(telegramBot.execute(any(SendMessage.class))).thenReturn(response);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123456L);
        when(userService.findUserByChatId(123456L)).thenReturn(user);
        when(user.getLastAction()).thenReturn(LastAction.WAITING_USER_FULL_NAME);
        when(update.message().text()).thenReturn(fullName);

        sendContactHandler.handleContact(update);
        sendContactHandler.sendMessage(123456L, """
                Введите Ваш номер телефона:
                """);

        parent.setFullName(fullName);
        when(parentService.save(parent)).thenReturn(parent);
        Parent savedParent = parentService.save(parent);
        verify(parentService, Mockito.times(1)).save(parent);
        assertEquals(parent, savedParent);

        verify(user).setLastAction(LastAction.WAITING_USER_PHONE);
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
                Контактные данные успешно сохранены. Спасибо!
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
        verify(user).setLastAction(LastAction.WAITING_USER_PHONE);
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