package ru.skyteam.pettelegrambot.report;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.util.StringUtils;
import ru.skyteam.pettelegrambot.exception.PhotoUploadException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class PhotoHandler {
    //} else if (message.photo() != null){
//    private static TelegramBotUpdatesListener telegramBotUpdatesListener;

    private TelegramBot telegramBot;

    public PhotoHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    //    Пользователь высылает фото
    public String receivePhoto(Message message) throws PhotoUploadException {

        PhotoSize photoSize = message.photo()[message.photo().length - 1];

        GetFileResponse getFileResponse = telegramBot.execute(
                new GetFile(photoSize.fileId()));
        if (getFileResponse.isOk()) {
            try {
                String extension = StringUtils.getFilenameExtension(
                        getFileResponse.file().filePath());
                byte[] image = telegramBot.getFileContent(getFileResponse.file());
                String fileName = UUID.randomUUID().toString() + "." + extension;
                Files.write(Paths.get(fileName), image);
                return fileName;
            } catch (IOException e) {
                throw new PhotoUploadException("Произошла ошибка при получении фото от усыновителя");
            }
        }
        return null;
    }
}

