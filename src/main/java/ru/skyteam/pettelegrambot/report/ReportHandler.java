package ru.skyteam.pettelegrambot.report;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.Report;
import ru.skyteam.pettelegrambot.exception.PhotoUploadException;
import ru.skyteam.pettelegrambot.service.ParentServiceImpl;
import ru.skyteam.pettelegrambot.service.PetServiceImpl;
import ru.skyteam.pettelegrambot.service.ReportServiceImpl;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReportHandler {
    private final Logger logger = LoggerFactory.getLogger(ReportHandler.class);
    @Autowired
    TelegramBot telegramBot;
    @Autowired
    PetServiceImpl petService;
    @Autowired
    ReportServiceImpl reportService;
    @Autowired
    ParentServiceImpl parentService;

    public ReportHandler() {
    }

    /**
     * <b><u>Получение списка животных усыновителя
     * <br>по полю</u></b> <i>"chatId"</i>
     * <br><i>
     * <br>Использует {@link #petService}
     * @param chatId (user's updates to report-form)
     * @return список питомцев (List &lt;Pet&gt;  )
     */
    private List<Pet> getPetsOfParent(Long chatId) {
        return petService.findAllByChatId(chatId);
    }

    /**
     * <b><u>Получение последнего/текущего отчета усыновителя</u></b>
     * <i> в отчете проставляем поля согласно статусу, <br>
     * обрабатывая данные из соответствующих апдейтов</i>
     * <br>Использует {@link  #reportService} и {@link #petService}
     * <br> @see {@link #reportService reportFindLastByParent}
     * @param chatId (user's chatId)
     * @return Report
     */
    public Report getReport(Long chatId) {
        return reportService.reportFindLastByParent(parentService.findParentByChatId(chatId));
    }

    /**
     * <b><u>Последовательная обработка апдейтов для заполнения отчета</u></b>
     * <i>Устанавливаем контекст ожидания, <br>
     * обрабатываем согласно контексту</i>
     * @throws PhotoUploadException <br> если произошел сбой при загрузке фото
     * @param update (user's updates to report-form)
     */
    public void handle(Update update) throws PhotoUploadException {

        Long chatId = update.message().chat().id();

        Report report = getReport(chatId);
        if (report.getLastAction() == null) {
            report.setLastAction(LastAction.START_REPORT);
        }
        switch (report.getLastAction()) {
            default: {
                List<Pet> pets = getPetsOfParent(chatId);
                if (pets == null || pets.isEmpty()) {
                    sendMessage(chatId,
                            """
                                    Мы не смогли найти вашего питомца                       
                                    """
                    );
                    break;
                } else if (pets.size() == 1) {
                    report.setPet(pets.get(0));
                    report.setReportDate(LocalDate.now());
                    report.setParent(pets.get(0).getParent());
                    report.setLastAction(LastAction.WAITING_PHOTO);
                    reportService.save(report);
                    sendMessage(chatId,
                            """
                                    Отправьте фото питомца
                                    """);
                    break;
                } else {
                    report.setParent(pets.get(0).getParent());
                    report.setReportDate(LocalDate.now());
                    report.setLastAction(LastAction.WAITING_PET_NAME);
                    reportService.save(report);
                    sendMessage(chatId,
                            """
                                    Введите имя питомца                         
                                      """);
                    break;
                }
            }
            case WAITING_PET_NAME: {
                String name = update.message().text();

                Pet pet = getPetsOfParent(chatId)
                        .stream().filter(p -> p.getName().equals(name))
                        .findFirst()
                        .orElse(null);
                if (pet == null) {
                    sendMessage(chatId, "Имя питомца не найдено");
                    break;
                }
                report.setPet(pet);
                report.setReportDate(LocalDate.now());
                report.setLastAction(LastAction.WAITING_PHOTO);
                reportService.save(report);
                sendMessage(chatId, "Пришлите фото животного.");
                break;
            }

            case WAITING_PHOTO: {
                PhotoHandler photoHandler = new PhotoHandler(telegramBot);
                String path = photoHandler.receivePhoto(update.message());
                if (path == null) {
                    sendMessage(chatId, "Что-то пошло не так. Пришлите фото питомца");
                    break;
                }
                report.setPathToPhoto(path);
                report.setLastAction(LastAction.WAITING_DIET_INFO);
                report.setReportDate(LocalDate.now());
                reportService.save(report);
                sendMessage(chatId, "Опишите рацион животного");
                break;
            }
            case WAITING_DIET_INFO: {
                String diet = update.message().text();
                report.setPetDiet(diet);
                report.setLastAction(LastAction.WAITING_HEALTH_INFO);
                report.setReportDate(LocalDate.now());
                reportService.save(report);
                sendMessage(chatId, "Опишите общее самочувствие животного и привыкание к новому месту");
                break;

            }
            case WAITING_HEALTH_INFO: {
                String health = update.message().text();
                report.setHealth(health);
                report.setLastAction(LastAction.WAITING_CHANGING_HABITS_INFO);
                report.setReportDate(LocalDate.now());
                reportService.save(report);
                sendMessage(chatId, "Опишите изменения в поведении: отказ от старых привычек, приобретение новых");
                break;

            }
            case WAITING_CHANGING_HABITS_INFO: {
                String habits = update.message().text();
                report.setChangingHabits(habits);
                report.setLastAction(LastAction.DONE);
                report.setReportDate(LocalDate.now());
                reportService.save(report);
                sendMessage(chatId, "Отчет успешно заполнен. Спасибо!");
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
