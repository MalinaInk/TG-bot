package ru.skyteam.pettelegrambot.reminder;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.skyteam.pettelegrambot.entity.Pet;
import ru.skyteam.pettelegrambot.entity.Report;
import ru.skyteam.pettelegrambot.entity.StatusOfAdoption;
import ru.skyteam.pettelegrambot.service.PetServiceImpl;
import ru.skyteam.pettelegrambot.service.ReportServiceImpl;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;
import java.util.stream.Collectors;


import static ru.skyteam.pettelegrambot.message.BotReplayMessage.*;

@Component
public class TelegramBotReminder {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    PetServiceImpl petService;

    @Autowired
    ReportServiceImpl reportService;

    /**
     * <b><u> Получение списка chatId усыновителей для рассылки напоминаний</u></b>
     * <br>по списку животных <i>на испытательном сроке усыновления</i>
     * Используется в {@link TelegramBotReminder#scheduleRemindersOfReport()}
     * @param pets (List&lt;Pet&gt; ) - актуальный список животных
     * @return список уникальных chatId усыновителей(List &lt;Long&gt; )
     */

    public List<Long> getListOfParentsChatIdForReminding(List<Pet> pets) {
        return pets.stream().map(pet -> pet.getParent().getChatId())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * <b><u> Ежедневная рассылка напоминаний усыновителям</u></b>
     * <br>о необходимости отправить отчет <i>по отфильтрованным актуальным chatId</i>
     * <br>Использует {@link #petService} и его метод {@link PetServiceImpl#listPetForReport()}
     */
    @Scheduled(cron = "0 0 20 * * *") // напоминалка в 20.00
    public void scheduleRemindersOfReport() {
        List<Long> parentsChatIdForReminding = getListOfParentsChatIdForReminding(petService.listPetForReport());
        if (!parentsChatIdForReminding.isEmpty()) {
            for (int i = 0; i < parentsChatIdForReminding.size(); i++) {
                SendMessage message = new SendMessage(parentsChatIdForReminding.get(i), """
                        Уважаемый усыновитель! 
                        Это стандартное сообщение, напоминающее Вам о важности ежедневных отчетов.
                        Если вы еще не отправили свой отчет по питомцу, не забудьте это сделать, заполнив все нужные поля.
                        Через час мы соберем отчеты на проверку, но у вас есть время до 24.00, если не успеете вовремя.
                        """);

                try {
                    telegramBot.execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <b><u> Рассылка предупреждений усыновителям, которые пропустили</u></b>
     * <br> вчерашний отчет <i>согласно актуальному списку питомцев</i>
     * использует {@link #petService} и {@link #reportService}
     */
    @Scheduled(cron = "0 55 19 * * *") // если накануне не корректно - усыновителю
    public void scheduleWarningToReport() {
        List<Pet> pets = petService.listPetForReport();
        for (Pet pet : pets) {
            LocalDate lastReportDate = reportService.getLatestDateByPetId(pet.getId());
            if (lastReportDate.isBefore(LocalDate.now().minusDays(2))
                    || lastReportDate.equals(LocalDate.now().minusDays(2))) {
                SendMessage message = new SendMessage(pet.getParent().getId(), WARNING_TO_REPORT);
                try {
                    telegramBot.execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <b><u> Рассылка волонтерам контактов усыновителей</u></b>
     * <br>пропустивших или не корректно заполнивших 2 отчета подряд
     * <br><i> (+ не закрывших отчет по текущей дате) </i>
     * <br>использует {@link #petService}, а также метод,
     * <br>подсчитывающий число неправильных и пропущенных отчетов {@link #getQuantityBadReports}
     */
    @Scheduled(cron = "0 35 20 * * *") // если 2 дня подряд не корректно - волонтеру
    public void scheduleToVolunteerTwoEmptyReport() {
        List<Pet> pets = petService.listPetForReport();
        for (Pet pet : pets) {
            int n = getQuantityBadReports(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1), pet);
            if (n > 2 && (pet.getDateOfAdoption().isBefore(LocalDate.now().minusDays(3)))) {
                SendMessage message = new SendMessage(pet.getVolunteer().getId(),
                        "Усыновитель " + pet.getParent().getFullName() + " имя не присылал отчета уже 2 дня. " +
                                "Свяжитесь с ним по номеру " + pet.getParent().getPhoneNumber());
                try {
                    telegramBot.execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//
    /**
     * <b><u> Метод, подсчитывающий число
     * <br>неправильных и пропущенных отчетов
     * <br>за определенный период</u></b>
     * @param startPeriod (LocalDate) - начало периода.
     * @param endPeriod (LocalDate) - окончание периода.
     * @param pet (Pet) - питомец, по которому формируется статистика.
     *  <br>
     * @return Количество не корректных репортов (int)
     */
    public int getQuantityBadReports(LocalDate startPeriod, LocalDate endPeriod, Pet pet) {

        List<Report> reports = pet.getReports().stream()
                .filter(r -> r.getReportDate().isAfter(startPeriod) && r.getReportDate().isBefore(endPeriod))
                .toList();
        int quantityBadReports = 0;

        quantityBadReports = (int) (ChronoUnit.DAYS.between(startPeriod, endPeriod)) - reports.size();

        quantityBadReports += (int) (reports.stream()
                .filter(r -> !r.getCorrect()).count());

        return quantityBadReports;
    }

    /**
     * <b><u>Метод, необходимый для расчета актуального тестового периода,</u></b>
     * <br>учитывающий случай назначения дополнительного срока
     * <br>по окончанию основного.
     * @param pet
     * @return Дата, с которой начинается актуальная отчетность (LocalDate)
     */
    public LocalDate getActualStartPeriod(Pet pet) {
        LocalDate actualStartPeriod = pet.getDateOfEndReport().minusDays(30);
        long allAdoptionDays = ChronoUnit.DAYS.between(pet.getDateOfAdoption(), pet.getDateOfEndReport());
        if (allAdoptionDays > 30 && allAdoptionDays < 45) {
            actualStartPeriod = pet.getDateOfEndReport().minusDays(15);
        }
        return actualStartPeriod;
    }

    /**
     * <b><u>Рассылка для проверки волонтером статистики и результата испытательного срока.</u></b>
     * <br>Присвоение животному соответствующего статуса.
     * <br>использует {@link #petService} и его метод {@link PetServiceImpl#listPetForEndingReport()}
     */
    @Scheduled(cron = "0 30 20 * * *") /*проверка волонтером статистики и результата испытательного срока*/
    public void scheduleCheckResultOTestPeriodToVolunteer() {
        List<Pet> pets = petService.listPetForEndingReport();
        if (pets != null && !pets.isEmpty()) {
            for (Pet pet : pets) {
                SendMessage message;
                int badReports = getQuantityBadReports(getActualStartPeriod(pet), pet.getDateOfEndReport(), pet);
                if (badReports > 3) {
                    message = new SendMessage(pet.getVolunteer().getId(),
                            "Усыновитель " + pet.getParent().getFullName()
                                    + "допустил более 3-х некорректных отчетов за тестовый период. "
                                    + "\nАвтоматически животному будет присвоен статус WILL_BE_RETURN (нужно вернуть), "
                                    + "\nа усыновителю отправлено сообщение о необходимости возврата животного. "
                                    + "\nПри необходимости вы можете поменять статус, а также связаться с усыновителем по номеру: "
                                    + pet.getParent().getPhoneNumber());
                    pet.setStatusOfAdoption(StatusOfAdoption.WILL_BE_RETURN);
                } else if (badReports <= 1) {
                    message = new SendMessage(pet.getVolunteer().getId(),
                            "Усыновитель " + pet.getParent().getFullName()
                                    + "допустил не более 1-го некорректного отчета за тестовый период. "
                                    + "\nАвтоматически животному будет присвоен статус IN_HOME (усыновлен), "
                                    + "\nа усыновителю отправлено поздравление с усыновлением питомца. "
                                    + "\nПри необходимости вы можете поменять статус, а также связаться с усыновителем по номеру: "
                                    + pet.getParent().getPhoneNumber());
                    pet.setStatusOfAdoption(StatusOfAdoption.IN_HOME);

                } else {
                    message = new SendMessage(pet.getVolunteer().getId(),
                            "Усыновитель " + pet.getParent().getFullName()
                                    + "допустил 2-3 некорректных отчета за тестовый период. "
                                    + "\nАвтоматически животному будет подтвержден статус IN_PROCESS, "
                                    + "\nа усыновителю продлен испытательный срок на 14 дней и отправлено соответствующее уведомление. "
                                    + "\nПри необходимости вы можете увеличить испытательный срок на 30 дней или отказать в усыновлении, если имеются объективные причины. "
                                    + "\nСвязаться с усыновителем можно по номеру: "
                                    + pet.getParent().getPhoneNumber());
                    pet.setStatusOfAdoption(StatusOfAdoption.IN_PROCESS);
                    pet.setDateOfEndReport(pet.getDateOfEndReport().plusDays(14));
                }
                try {
                    telegramBot.execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <b><u>Рассылка усыновителям о результате тестового периода усыновления</u></b>
     * <br>в соответствии с присвоенным животному статусом.
     * <br>использует {@link #petService} и его метод {@link PetServiceImpl#listPetForEndingReport()}
     */

    @Scheduled(cron = "0 30 21 * * *") /*результат периода*/
    public void scheduleRemindersOfEndingTestPeriod() {
        List<Pet> pets = petService.listPetForEndingReport();
        if (pets != null && !pets.isEmpty()) {
            for (Pet pet : pets) {
                SendMessage message;

                if (pet.getStatusOfAdoption() == StatusOfAdoption.WILL_BE_RETURN) {
                    message = new SendMessage(pet.getParent().getId(), PET_RETURN);

                } else if (pet.getStatusOfAdoption() == StatusOfAdoption.IN_PROCESS
                        && pet.getDateOfEndReport() == LocalDate.now().plusDays(14)) {
                    message = new SendMessage(pet.getParent().getId(), ADDITIONAL_TIME_14);

                } else if (pet.getStatusOfAdoption() == StatusOfAdoption.IN_PROCESS
                        && pet.getDateOfEndReport() == LocalDate.now().plusDays(30)) {
                    message = new SendMessage(pet.getParent().getId(), ADDITIONAL_TIME_30);

                } else {
                    message = new SendMessage(pet.getParent().getId(), CONGRATULATIONS);
                }
                try {
                    telegramBot.execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}






