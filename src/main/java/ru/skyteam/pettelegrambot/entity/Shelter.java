package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "shelter")

public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "shelter_type")
    private PetType shelterType;

    @OneToMany(mappedBy = "shelter")
    private List<Volunteer> volunteers;

    @OneToMany(mappedBy = "shelter")
    private List<Pet> pets;

    public Shelter(Long id, PetType shelterType, List<Volunteer> volunteers, List<Pet> pets) {
        this.id = id;
        this.shelterType = shelterType;
        this.volunteers = volunteers;
        this.pets = pets;
    }
    public Shelter(Long id, PetType shelterType) {
        this.id = id;
        this.shelterType = shelterType;
    }

    public Shelter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetType getShelterType() {
        return shelterType;
    }

    public void setShelterType(PetType shelterType) {
        this.shelterType = shelterType;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", shelterType=" + shelterType +
                ", volunteers=" + volunteers +
                ", pets=" + pets +
                '}';
    }
}
