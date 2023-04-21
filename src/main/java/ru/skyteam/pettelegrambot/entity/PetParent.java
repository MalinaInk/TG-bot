package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pet_parent")
public class PetParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "number_of_report_days")
    private Integer numberOfReportDays;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public PetParent() {
    }

    public PetParent(Long id, Long chatId, String fullName, String phoneNumber,
                     Integer numberOfReportDays, Volunteer volunteer, Pet pet) {
        this.id = id;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.numberOfReportDays = numberOfReportDays;
        this.volunteer = volunteer;
        this.pet = pet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getNumberOfReportDays() {
        return numberOfReportDays;
    }

    public void setNumberOfReportDays(Integer numberOfReportDays) {
        this.numberOfReportDays = numberOfReportDays;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public String toString() {
        return "PetParent{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", numberOfReportDays=" + numberOfReportDays +
                ", volunteer=" + volunteer +
                ", pet=" + pet +
                '}';
    }
}
