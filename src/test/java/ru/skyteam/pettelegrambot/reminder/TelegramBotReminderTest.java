//package ru.skyteam.pettelegrambot.reminder;
//import static org.mockito.Mockito.verify;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.request.SendMessage;
//
//
//    public class TelegramReminderTest {
//
//        private static final int DAYS_TO_SEND = 30;
//
//        @Mock
//        private TelegramBot telegramBot;
//
//        private TelegramBotReminder.TelegramReminderScheduler scheduler;
//
//        @BeforeEach
//        public void setup() {
//            MockitoAnnotations.initMocks(this);
//            scheduler = new TelegramBotReminder.TelegramReminderScheduler();
//            scheduler.telegramBot = telegramBot;
//        }
//
//        @Test
//        public void testScheduleReminders() throws TelegramApiException {
//            LocalDate startDate = LocalDate.of(2023, 5, 1); // заданная дата старта
//            LocalDate endDate = startDate.plusDays(DAYS_TO_SEND); // дата окончания
//            LocalTime reminderTime = LocalTime.of(12, 0); // заданное время отправки
//
//            for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
//                LocalDateTime reminderDateTime = LocalDateTime.of(currentDate, reminderTime);
//                SendMessage message = new SendMessage();
//                message.setChatId("1234567890"); // заданный chatId
//                message.setText("Напоминание на " + currentDate.toString() + " в " + reminderTime.toString());
//
//                scheduler.scheduleReminders();
//
//                verify(telegramBot).execute(message);
//            }
//        }
//    }
//
////    В этом тесте мы проверяем, что каждое отправленное сообщение имеет правильный текст и отправляется в заданное время в течение 30 дней. Мы используем библиотеку Mockito для создания mock-объекта TelegramBot и проверки, что метод execute вызывается с правильными параметрами. Мы также используем цикл for для проверки каждого дня в периоде отправки напоминаний.
//
//}
