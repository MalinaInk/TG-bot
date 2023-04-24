package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name = "volunteer")

public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne
    @JoinColumn (name = "shelter_id", referencedColumnName = "id")
    private Shelter shelter;

    @OneToMany(mappedBy = "volunteer")
    private List<Parent> parents;

    public Volunteer(Long id, Long chatId, String fullName, Shelter shelter, List<Parent> parents) {
        this.id = id;
        this.chatId = chatId;
        this.fullName = fullName;
        this.shelter = shelter;
        this.parents = parents;
    }

    public Volunteer() {
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

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", fullName='" + fullName + '\'' +
                ", shelter=" + shelter +
                ", parents=" + parents +
                '}';
    }
}
