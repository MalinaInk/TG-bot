package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "volunteer")

public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "full_name")
    private String fullName;

    @OneToOne
    @Column(name = "shelter_type")
    private Shelter shelterType;

    public Volunteer() {
    }

    public Volunteer(Long id, Long chatId, String fullName, Shelter shelterType) {
        this.id = id;
        this.chatId = chatId;
        this.fullName = fullName;
        this.shelterType = shelterType;
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

    public Shelter getShelterType() {
        return shelterType;
    }

    public void setShelterType(Shelter shelterType) {
        this.shelterType = shelterType;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", fullName='" + fullName + '\'' +
                ", shelterType=" + shelterType +
                '}';
    }
}
