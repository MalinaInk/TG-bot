package ru.skyteam.pettelegrambot.message;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skyteam.pettelegrambot.listener.TelegramBotUpdatesListener;

;

@RequiredArgsConstructor
@Service
public class ButtonMenu {
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);



    //Меню для выбора приюта
    public void petMenu(Long chatId){
        logger.info("Вызвано меню выбора приюта: {} ", chatId);
        InlineKeyboardButton catButton = new InlineKeyboardButton(BotMenu.INFO_CAT);
        InlineKeyboardButton dogButton = new InlineKeyboardButton(BotMenu.INFO_DOG);
        catButton.callbackData(BotMenu.INFO_CAT);
        dogButton.callbackData(BotMenu.INFO_DOG);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(catButton, dogButton);

        sendButtonMessage(chatId, BotReplayMessage.START,keyboardMarkup);
    }

//Основное меню
    public void mainMenu(Long chatId){
        logger.info("Вызвано главное меню: {}", chatId);
        InlineKeyboardButton button1 = new InlineKeyboardButton(BotMenu.HOW_TO_ADOPT);
        InlineKeyboardButton button2 = new InlineKeyboardButton(BotMenu.CALL_VOLUNTEER);
        InlineKeyboardButton button3 = new InlineKeyboardButton(BotMenu.SEND_REPORT);
        InlineKeyboardButton button4 = new InlineKeyboardButton(BotMenu.INFO_SHELTER);
        InlineKeyboardButton button5 = new InlineKeyboardButton(BotMenu.IN_PET_MENU);
        button1.callbackData(BotMenu.HOW_TO_ADOPT);
        button2.callbackData(BotMenu.CALL_VOLUNTEER);
        button3.callbackData(BotMenu.SEND_REPORT);
        button4.callbackData(BotMenu.INFO_SHELTER);
        button5.callbackData(BotMenu.IN_PET_MENU);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(button1, button2);
        keyboardMarkup.addRow(button3);
        keyboardMarkup.addRow(button4);
        keyboardMarkup.addRow(button5);

        sendButtonMessage(chatId, BotReplayMessage.MAIN_MENU,keyboardMarkup);

    }
//Меню информации о приюте
    public void adoptMenu(Long chatId){
        logger.info("Вызвано меню информации о приюте: {}", chatId);
        InlineKeyboardButton button1 = new InlineKeyboardButton(BotMenu.SECURITY_PASS);
        InlineKeyboardButton button2 = new InlineKeyboardButton(BotMenu.SEND_CONTACTS);
        InlineKeyboardButton button3 = new InlineKeyboardButton(BotMenu.PRECAUTION);
        InlineKeyboardButton button4 = new InlineKeyboardButton(BotMenu.GENERAL_INFO_SHELTER);
        InlineKeyboardButton button5 = new InlineKeyboardButton(BotMenu.CALL_VOLUNTEER);
        InlineKeyboardButton button6 = new InlineKeyboardButton(BotMenu.IN_MAIN_MENU);

        button1.callbackData(BotMenu.SECURITY_PASS);
        button2.callbackData(BotMenu.SEND_CONTACTS);
        button3.callbackData(BotMenu.PRECAUTION);
        button4.callbackData(BotMenu.GENERAL_INFO_SHELTER);
        button5.callbackData(BotMenu.CALL_VOLUNTEER);
        button6.callbackData(BotMenu.IN_MAIN_MENU);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(button1, button2);
        keyboardMarkup.addRow(button4, button3);
        keyboardMarkup.addRow(button5, button6);

        sendButtonMessage(chatId, BotReplayMessage.ADOPT_MENU,keyboardMarkup);
    }

    //Меню консультации
    public void consDogMenu(Long chatId){
        logger.info("Вызвано меню конслуьтации с потенциальным хозяином: {}", chatId);
        InlineKeyboardButton button1 = new InlineKeyboardButton(BotMenu.DATING_RULES);
        InlineKeyboardButton button2 = new InlineKeyboardButton(BotMenu.REQUIRED_DOCUMENTS);
        InlineKeyboardButton button3 = new InlineKeyboardButton(BotMenu.PET_TRANSPORT);
        InlineKeyboardButton button4 = new InlineKeyboardButton(BotMenu.DISABILITIES_PET_HOUSE);
        InlineKeyboardButton button5 = new InlineKeyboardButton(BotMenu.PUPPY_HOUSE);
        InlineKeyboardButton button6 = new InlineKeyboardButton(BotMenu.ADULT_DOG_HOUSE);
        InlineKeyboardButton button7 = new InlineKeyboardButton(BotMenu.ADVICE_CYNOLOGIST);
        InlineKeyboardButton button8 = new InlineKeyboardButton(BotMenu.DOG_CYNOLOGIST);

        button1.callbackData(BotMenu.DATING_RULES);
        button2.callbackData(BotMenu.REQUIRED_DOCUMENTS);
        button3.callbackData(BotMenu.PET_TRANSPORT);
        button4.callbackData(BotMenu.DISABILITIES_PET_HOUSE);
        button5.callbackData(BotMenu.PUPPY_HOUSE);
        button6.callbackData(BotMenu.ADULT_DOG_HOUSE);
        button7.callbackData(BotMenu.ADVICE_CYNOLOGIST);
        button8.callbackData(BotMenu.DOG_CYNOLOGIST);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(button1);
        keyboardMarkup.addRow(button2);
        keyboardMarkup.addRow(button5, button6);
        keyboardMarkup.addRow(button8, button7);
        keyboardMarkup.addRow(button3, button4);

        sendButtonMessage(chatId, BotReplayMessage.POTENTIAL_PET_PARENT_HELLO, keyboardMarkup);
    }


    public void consultationMenuCat(Long chatId){
        logger.info("Вызвано меню консультации с потенциальным хозяином животного из приюта: {}", chatId);
        InlineKeyboardButton button1 = new InlineKeyboardButton(BotMenu.DATING_RULES);
        InlineKeyboardButton button2 = new InlineKeyboardButton(BotMenu.REQUIRED_DOCUMENTS);
        InlineKeyboardButton button3 = new InlineKeyboardButton(BotMenu.PET_TRANSPORT);
        InlineKeyboardButton button4 = new InlineKeyboardButton(BotMenu.DISABILITIES_PET_HOUSE);
        InlineKeyboardButton button7 = new InlineKeyboardButton(BotMenu.CAT_HOUSE);

        button1.callbackData(BotMenu.DATING_RULES);
        button2.callbackData(BotMenu.REQUIRED_DOCUMENTS);
        button3.callbackData(BotMenu.PET_TRANSPORT);
        button4.callbackData(BotMenu.DISABILITIES_PET_HOUSE);
        button7.callbackData(BotMenu.CAT_HOUSE);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(button1, button2);
        keyboardMarkup.addRow(button3, button4);
            keyboardMarkup.addRow(button7);

        sendButtonMessage(chatId, BotReplayMessage.POTENTIAL_PET_PARENT_HELLO,keyboardMarkup);


    }


//Шаблон для создания сообщений с кнопками
    private void sendButtonMessage(Long chatId, String textToSend, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage(chatId, textToSend);
        message.replyMarkup(inlineKeyboardMarkup);
        SendResponse response = telegramBot.execute(message);
        if (!response.isOk()){
            logger.error("Ошибка при отправке сообщения: {}", response.description());
        }
    }
}
