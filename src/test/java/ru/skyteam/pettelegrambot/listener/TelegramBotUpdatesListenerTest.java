package ru.skyteam.pettelegrambot.listener;

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
import org.springframework.test.context.junit4.SpringRunner;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.User;
import ru.skyteam.pettelegrambot.message.ButtonMenu;
import ru.skyteam.pettelegrambot.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    TelegramBot telegramBot;

    @InjectMocks
    TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Mock
    UserServiceImpl userService;

    @Mock
    ButtonMenu buttonMenu;

    @Test
    public void testOfCommandStart(){

        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        User user = new User();
        SendResponse response = mock(SendResponse.class);
        List<Update> updates = new ArrayList<>();
        updates.add(update);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(message.text()).thenReturn("/start");

        telegramBotUpdatesListener.process(updates);
        verify(buttonMenu).petMenu(chatId);
    }

    @Test
    public void testOfCommandStart_Negative(){
        String expected = "Неверный запрос";
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = new User();
        Chat chat = mock(Chat.class);
        SendResponse response = mock(SendResponse.class);;
        List<Update> updates = new ArrayList<>();
        updates.add(update);

        when(response.isOk()).thenReturn(true);

        when(chat.id()).thenReturn(123L);
        user.setLastAction(LastAction.DONE);
        when(userService.findUserByChatId(any())).thenReturn(user);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().id()).thenReturn(chatId);
        when(message.text()).thenReturn("/starrt");
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage messageArg = (SendMessage) arg;
            Assertions.assertThat(messageArg.getParameters().get("text")).isEqualTo(expected);
            return response;
        });
        telegramBotUpdatesListener.process(updates);
    }

}