package ru.skyteam.pettelegrambot.sendContact;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.User;
import ru.skyteam.pettelegrambot.service.ParentService;
import ru.skyteam.pettelegrambot.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SendContactHandler {


    private final Logger logger = LoggerFactory.getLogger(SendContactHandler.class);
    private final Pattern PATTERN = Pattern.compile(
            "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$");
    @Autowired
    TelegramBot telegramBot;
    @Autowired
    UserService userService;
    @Autowired
    ParentService parentService;

    public SendContactHandler() {
    }


    /**
     * * <b><u>Обработка апдейтов для отправки контактов пользователем</u></b>
     * <i> Получение текущего состояния пользователя<br>
     * обработка данных из соответствующих апдейтов</i>
     * сохранение пользователя с контактами как потенциального усыновителя</i>
     * <br>Использует {@link  #userService} и {@link #parentService}
     * <br> @see {@link #userService findUserByChatId}
     * @param update (user's updates to contact details)
     * @return obj parent
     */

    public void handleContact (Update update) {
        Long chatId = update.message().chat().id();
        User user = userService.findUserByChatId(chatId);
        Parent parent = new Parent();

        if (user.getLastAction() == null) {
            user.setLastAction(LastAction.START_CONTACT);
        }

        switch (user.getLastAction()) {
            case START_CONTACT: {
                sendMessage(chatId,
                        """
                        Введите Ваши Имя и Фамилию:
                        """);
                user.setLastAction(LastAction.WAITING_USER_FULL_NAME);
                break;
            }
            case WAITING_USER_FULL_NAME: {
                String fullName = update.message().text();
                parent.setChatId(chatId);
                parent.setFullName(fullName);

                sendMessage(chatId,
                        """
                                Введите Ваш номер телефона:
                                """);
                user.setLastAction(LastAction.WAITING_USER_PHONE);
                break;
            }
            case WAITING_USER_PHONE: {
                String phoneNumber = update.message().text();

                Matcher matcher = PATTERN.matcher(phoneNumber);
                if (matcher.matches()) {
                    parent.setPhoneNumber(phoneNumber);
                    parentService.save(parent);

                    user.setLastAction(LastAction.DONE_CONTACT);
                    sendMessage(chatId,
                            """
                Контактные данные успешно сохранены. Спасибо!
                """);
                } else {
                    sendMessage(chatId,
                            """
                    Убедитесь, что вводите номер телефона российского оператора связи, повторите попытку
                    """);
                    user.setLastAction(LastAction.WAITING_USER_PHONE);
                }
                break;
            }
        }

    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()) {
            logger.error("Ошибка отправки сообщения от бота: {}", response.description());
        }
    }
}