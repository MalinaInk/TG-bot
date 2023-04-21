package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @OneToOne
    @JoinColumn(name = "pet_parent_id")
    private PetParent petParent;

    @Column(name = "pet_diet")
    private String petDiet;

    @Column(name = "health")
    private String health;

    @Column(name = "changing habits")
    private String changingHabits;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    public Report() {
    }

    public Report(Long id, LocalDate reportDate, Pet pet, PetParent petParent, String petDiet,
                  String health, String changingHabits, Photo photo, Boolean isCorrect) {
        this.id = id;
        this.reportDate = reportDate;
        this.pet = pet;
        this.petParent = petParent;
        this.petDiet = petDiet;
        this.health = health;
        this.changingHabits = changingHabits;
        this.photo = photo;
        this.isCorrect = isCorrect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public PetParent getPetParent() {
        return petParent;
    }

    public void setPetParent(PetParent petParent) {
        this.petParent = petParent;
    }

    public String getPetDiet() {
        return petDiet;
    }

    public void setPetDiet(String petDiet) {
        this.petDiet = petDiet;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getChangingHabits() {
        return changingHabits;
    }

    public void setChangingHabits(String changingHabits) {
        this.changingHabits = changingHabits;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reportDate=" + reportDate +
                ", pet=" + pet +
                ", petParent=" + petParent +
                ", petDiet='" + petDiet + '\'' +
                ", health='" + health + '\'' +
                ", changingHabits='" + changingHabits + '\'' +
                ", photo=" + photo +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
