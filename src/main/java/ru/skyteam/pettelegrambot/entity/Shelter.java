package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "shelter_type")
    private boolean shelterType;

    @OneToMany
    @JoinColumn(name = "volunteer_id")
    private List<Volunteer> volunteers;

    @OneToMany
    @JoinColumn(name = "pet_id")
    private List<Pet> pets;

    public Shelter() {
    }

    public Shelter(Long id, boolean shelterType, List<Volunteer> volunteers, List<Pet> pets) {
        this.id = id;
        this.shelterType = shelterType;
        this.volunteers = volunteers;
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isShelterType() {
        return shelterType;
    }

    public void setShelterType(boolean shelterType) {
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
