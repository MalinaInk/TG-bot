package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "pet")

public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type")
    private PetType petType;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "shelter_id", referencedColumnName = "id")
    private Shelter shelter;

    @OneToMany(mappedBy = "pet")
    private List<Report> reports;

    @Column(name = "date_of_adoption")
    private LocalDate dateOfAdoption;

    public Pet(Long id, PetType petType, String name, Shelter shelter, List<Report> reports, LocalDate dateOfAdoption) {
        this.id = id;
        this.petType = petType;
        this.name = name;
        this.shelter = shelter;
        this.reports = reports;
        this.dateOfAdoption = dateOfAdoption;
    }

    public Pet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
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
                ", petType=" + petType +
                ", name='" + name + '\'' +
                ", shelter=" + shelter +
                ", reports=" + reports +
                ", dateOfAdoption=" + dateOfAdoption +
                '}';
    }
}

