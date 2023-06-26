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

import static ru.skyteam.pettelegrambot.entity.LastAction.DONE_CONTACT;
import static ru.skyteam.pettelegrambot.entity.LastAction.WAITING_USER_PHONE;

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
     *
     * @param update (user's updates to contact details)
     * @return obj parent
     */

    public void handleContact(Update update) {
        Long chatId;
        if (update.message() != null) {
            chatId = update.message().chat().id();
        } else {
            chatId = update.callbackQuery().message().chat().id();
        }
        User user = userService.findUserByChatId(chatId);
        if (user == null) {
            user = new User();
            user.setChatId(chatId);
        }
        Parent parent = parentService.findParentByChatId(chatId);
        if (parent == null) {
            parent = new Parent();
            parent.setChatId(chatId);
        }
        if (user.getLastAction() == null) {
            user.setLastAction(LastAction.START_CONTACT);
        }
        switch (user.getLastAction()) {
            case START_CONTACT: {
                sendMessage(chatId, "Перед тем как взять питомца вы должны оставить свои контактные данные. "
                        + "Пожалуйста, укажите ваши Имя и Фамилию: ");

                user.setLastAction(LastAction.WAITING_USER_FULL_NAME);
                userService.save(user);
                break;
            }

            case WAITING_USER_FULL_NAME: {
                String fullName = update.message().text();

                parent.setFullName(fullName);
                parentService.save(parent);
                user.setLastAction(WAITING_USER_PHONE);
                userService.save(user);

                sendMessage(chatId, "Пожалуйста, введите номер телефона в формате +7 XXX XXX XX XX");
                break;
            }
            case WAITING_USER_PHONE: {
                String phoneNumber = update.message().text();

                Matcher matcher = PATTERN.matcher(phoneNumber);
                if (matcher.matches()) {
                    parent.setPhoneNumber(phoneNumber);
                    parentService.save(parent);
                    user.setLastAction(DONE_CONTACT);
                    userService.save(user);
                    sendMessage(chatId, "Контактные данные успешно сохранены. Спасибо! " +
                            "В случае, если Вы по ошибке ввели не свой номер телефона, Вы можете связаться с " +
                            "волонтером - клавиша в главном меню");
                } else {

                    sendMessage(chatId, "Убедитесь, что вводите номер телефона российского оператора связи, повторите попытку");
                    break;
                }
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