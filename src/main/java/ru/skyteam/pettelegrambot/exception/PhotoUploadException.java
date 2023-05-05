package ru.skyteam.pettelegrambot.exception;

import java.io.IOException;

public class PhotoUploadException extends IOException {
    public PhotoUploadException(String message) {
        super(message);
    }
}
