package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "shelter_type")
    private boolean shelterType;

    @Column(name = "date_of_adoption")
    private LocalDate dateOfAdoption;

    public Pet() {
    }

    public Pet(Long id, String name, boolean shelterType, LocalDate dateOfAdoption) {
        this.id = id;
        this.name = name;
        this.shelterType = shelterType;
        this.dateOfAdoption = dateOfAdoption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShelterType() {
        return shelterType;
    }

    public void setShelterType(boolean shelterType) {
        this.shelterType = shelterType;
    }

    public LocalDate getDateOfAdoption() {
        return dateOfAdoption;
    }

    public void setDateOfAdoption(LocalDate dateOfAdoption) {
        this.dateOfAdoption = dateOfAdoption;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shelterType=" + shelterType +
                ", dateOfAdoption=" + dateOfAdoption +
                '}';
    }
}
