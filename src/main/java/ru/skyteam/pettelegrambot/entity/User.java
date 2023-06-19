package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;
@Entity
@Table( name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "choice_of_shelter")
    private PetType petType;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_action")
    private LastAction lastAction;

    @Column(name = "phone_number")
    private String phoneNumber;

    public User(Long id, Long chatId, PetType petType, String phoneNumber) {
        this.id = id;
        this.chatId = chatId;
        this.petType = petType;
        this.phoneNumber = phoneNumber;
    }
    public User(Long id, Long chatId, PetType petType, LastAction lastAction) {
        this.id = id;
        this.chatId = chatId;
        this.petType = petType;
        this.lastAction = lastAction;
    }

    public User() {
    }

    public User(Long chatId) {
        this.chatId = chatId;
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

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public LastAction getLastAction() {
        return lastAction;
    }

    public void setLastAction(LastAction lastAction) {
        this.lastAction = lastAction;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", petType=" + petType +
                ", lastAction=" + lastAction +
                '}';
    }
}