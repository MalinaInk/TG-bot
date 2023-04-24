package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "parent")

public class Parent {
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
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private Volunteer volunteer;

    @OneToMany(mappedBy = "parent")
    private List<ParentPet> parentPets;

    public Parent() {
    }

    public Parent(Long id, Long chatId, String fullName, String phoneNumber, Integer numberOfReportDays, Volunteer volunteer, List<ParentPet> parentPets) {
        this.id = id;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.numberOfReportDays = numberOfReportDays;
        this.volunteer = volunteer;
        this.parentPets = parentPets;
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

    public List<ParentPet> getParentPets() {
        return parentPets;
    }

    public void setParentPets(List<ParentPet> parentPets) {
        this.parentPets = parentPets;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", numberOfReportDays=" + numberOfReportDays +
                ", volunteer=" + volunteer +
                ", parentPets=" + parentPets +
                '}';
    }
}
