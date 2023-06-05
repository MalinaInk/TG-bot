package ru.skyteam.pettelegrambot.report;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
//@SpringBootTest
class PhotoHandlerTest {

    @MockBean
    private PhotoHandler photoHandler;

    @Test
    void receivePhotoTest_positive() throws URISyntaxException, IOException {

//        // Получение пути до фото в ресурсах
        String photoPath = "photo-keeper/Lynx2.jpg";
//
//        // Загрузка фото из ресурсов
        InputStream photoInputStream = getClass().getClassLoader().getResourceAsStream(photoPath);
        TelegramBot telegramBot = mock(TelegramBot.class);
        Message message = mock(Message.class);
        GetFileResponse getFileResponse = mock(GetFileResponse.class);

        PhotoSize photoSize = mock(PhotoSize.class);
        PhotoSize[] p = {photoSize};
        File file = mock(File.class);
        when(telegramBot.execute(any())).thenReturn(getFileResponse);
        when(telegramBot.getFileContent(any())).thenReturn(photoInputStream.readAllBytes());
        when(getFileResponse.isOk()).thenReturn(true);
        when(getFileResponse.file()).thenReturn(file);
        when(getFileResponse.file().filePath()).thenReturn("C:\\Users\\mo\\IdeaProjects\\Pet-Telegram-Bot\\src\\main\\resources\\photo-keeper\\Lynx2.jpg");
        when(message.photo()).thenReturn(p);

        photoHandler = new PhotoHandler(telegramBot);
        String result = photoHandler.receivePhoto(message);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void receivePhotoTest_negative() throws IOException {

        TelegramBot telegramBot = mock(TelegramBot.class);
        Message message = mock(Message.class);
        GetFileResponse getFileResponse = mock(GetFileResponse.class);

        PhotoSize photoSize = mock(PhotoSize.class);
        PhotoSize[] p = {photoSize};
        when(message.photo()).thenReturn(p);
        File file = mock(File.class);
        when(telegramBot.execute(any())).thenReturn(getFileResponse);
        when(getFileResponse.isOk()).thenReturn(true);
        when(getFileResponse.file()).thenReturn(file);
        when(getFileResponse.file().filePath()).then((invocation) -> {
            throw new IOException();
           });
        photoHandler = new PhotoHandler(telegramBot);

        Assertions.assertThatThrownBy(() -> {
                    photoHandler.receivePhoto(message);
                })
                .isInstanceOf(IOException.class)
                .hasMessage("Произошла ошибка при получении фото от усыновителя");
    }
}