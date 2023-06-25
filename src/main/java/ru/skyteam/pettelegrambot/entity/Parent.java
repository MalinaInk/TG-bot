package ru.skyteam.pettelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Pet> pets;

    public Parent(Long id, Long chatId, String fullName, String phoneNumber, List<Pet> pets) {
        this.id = id;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.pets = pets;
    }

    public Parent() {

    }

    public Parent(Long chatId, String fullName, String phoneNumber) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Parent(Long chatId, String fullName) {
        this.chatId = chatId;
        this.fullName = fullName;
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

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", pets=" + pets +
                '}';
    }
}
