package ru.skyteam.pettelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
//import jakarta.annotation.PostConstruct;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.Parent;
import ru.skyteam.pettelegrambot.entity.PetType;
import ru.skyteam.pettelegrambot.entity.User;
import ru.skyteam.pettelegrambot.exception.PhotoUploadException;
import ru.skyteam.pettelegrambot.message.BotMenu;
import ru.skyteam.pettelegrambot.message.BotReplayMessage;
import ru.skyteam.pettelegrambot.message.ButtonMenu;
import ru.skyteam.pettelegrambot.report.ReportHandler;
import ru.skyteam.pettelegrambot.sendContact.SendContactHandler;
import ru.skyteam.pettelegrambot.repository.UserRepository;
import ru.skyteam.pettelegrambot.service.UserService;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final ButtonMenu buttonMenu;
    private final TelegramBot telegramBot;
    private final ReportHandler reportHandler;
    private final SendContactHandler sendContactHandler;
    private UserRepository userRepository;
    private User user;
    @Autowired
    private UserService userService;


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles updates: {}", update);
                if (update.message() != null) {
                    Message message = update.message();
                    Long chatId = message.chat().id();
                    String text = message.text();
                    if ("/start".equals(text)) {
                        buttonMenu.petMenu(chatId);
                    }else if (text != null){
                        sendContactHandler.handleContact(update);
 //                       sendContact(update);
                    } else {
                        sendMessage(chatId, "Неверный запрос");
                    }


                } else if (update.callbackQuery() != null) {
                    Long chatId = update.callbackQuery().message().chat().id();
                    CallbackQuery callbackQuery = update.callbackQuery();
                    String data = callbackQuery.data();
                    String name = update.callbackQuery().message().chat().firstName();

                    switch (data) {
                        case (BotMenu.INFO_CAT):
                            userService.findUserByChatId(chatId);
                            if (user== null ) {
                                user = new User(chatId);
                            }
                            user.setPetType(PetType.CAT);
                            userService.save(user);
                            buttonMenu.meinMenu(chatId);
                            break;
                        case (BotMenu.INFO_DOG):
//                            user = new User(chatId);
                            userService.findUserByChatId(chatId);
                            if (user== null ) {
                                user = new User(chatId);
                            }
                            user.setPetType(PetType.DOG);
                            userService.save(user);
                            buttonMenu.meinMenu(chatId);
                            break;
                        case (BotMenu.CALL_VOLUNTEER):
                            sendMessage(chatId, BotReplayMessage.VOLUNTEER);
                            break;
                        case (BotMenu.INFO_SHELTER):
                            buttonMenu.adoptMenu(chatId);
                            break;
                        case (BotMenu.SECURITY_PASS):
                            sendMessage(chatId, BotReplayMessage.SECURITY_PASS);
                            break;
                        case (BotMenu.SEND_CONTACTS):
                            sendMessage(chatId, BotReplayMessage.CONTACT_INFO);
                            break;
                        case (BotMenu.PRECAUTION):
                            sendMessage(chatId, BotReplayMessage.INFO_PRECAUTION);
                            break;
                        case (BotMenu.GENERAL_INFO_SHELTER):
                            sendMessage(chatId, BotReplayMessage.INFO_SHELTER);
                            break;
                        case (BotMenu.IN_MAIN_MENU):
                            buttonMenu.meinMenu(chatId);
                            break;
                        case (BotMenu.IN_PET_MENU):
                            buttonMenu.petMenu(chatId);
                            break;
                        case (BotMenu.HOW_TO_ADOPT):
                            if (user.getPetType().equals(PetType.CAT)) {
                                buttonMenu.consultationMenuCat(chatId);
                            } else {
                                buttonMenu.consDogMenu(chatId);
                            }
                            break;
                        case (BotMenu.DATING_RULES):
                            sendMessage(chatId, BotReplayMessage.POTENTIAL_PET_PARENT_RULES);
                            break;
                        case (BotMenu.REQUIRED_DOCUMENTS):
                            sendMessage(chatId, BotReplayMessage.POTENTIAL_PET_PARENT_DOCUMENTS);
                            break;
                        case (BotMenu.PET_TRANSPORT):
                            sendMessage(chatId, BotReplayMessage.RECOMMENDATIONS_TRANSPORT);
                            break;
                        case (BotMenu.DISABILITIES_PET_HOUSE):
                            sendMessage(chatId, BotReplayMessage.RECOMMENDATIONS_FOR_PET_WITH_DISABILITIES);
                            break;
                        case (BotMenu.PUPPY_HOUSE):
                            sendMessage(chatId, BotReplayMessage.RECOMMENDATIONS_FOR_HOME);
                            break;
                        case (BotMenu.ADULT_DOG_HOUSE):
                            sendMessage(chatId, BotReplayMessage.RECOMMENDATIONS_FOR_HOME_BIG_DOG);
                            break;
                        case (BotMenu.ADVICE_CYNOLOGIST):
                            sendMessage(chatId, BotReplayMessage.ADVICE_CYNOLOGIST_COMMUNICATION_DOG);
                            break;
                        case (BotMenu.DOG_CYNOLOGIST):
                            sendMessage(chatId, BotReplayMessage.LIST_OF_CYNOLOGIST);
                            break;
                        case (BotMenu.CAT_HOUSE):
                            sendMessage(chatId, BotReplayMessage.RECOMMENDATIONS_FOR_CAT_HOME);
                            break;
                        case (BotMenu.SEND_REPORT):
                            sendMessage(chatId, BotReplayMessage.REPORT_FORM);
                            try {
                                reportHandler.handle(update);
                            } catch (PhotoUploadException e) {
                                throw new RuntimeException(e);
                            }
                            break;

                    }

                }
            });


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    public void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()) {
            logger.error("Ошибка отправки сообщения от бота: {}", response.description());
        }

    }

    //Метод для отправки пользователем своих контактных данных

//    public void sendContact(Update update){
//        Pattern pattern = Pattern.compile("[+7].\\d{10}");
////        String name = update.message().chat().firstName();
//        String text = update.message().text();
//        Long chatId = update.message().chat().id();
//        Matcher matcher = pattern.matcher(text);
//        if (matcher.matches()){
//        String phoneNumber = matcher.group(0);
//        user.setPhoneNumber(phoneNumber);
//        user.setChatId(chatId);
//        sendMessage(chatId, "Мы вам презвоним");
//        } else {
//            sendMessage(chatId, "Не корректо введен номер телефона");
//        }
//    }
}
