package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report")

public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Parent parent;

    @Column(name = "pet_diet", columnDefinition = "TEXT")
    private String petDiet;

    @Column(name = "health", columnDefinition = "TEXT")
    private String health;

    @Column(name = "changing_habits", columnDefinition = "TEXT")
    private String changingHabits;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    public Report(Long id, LocalDate reportDate, Pet pet, Parent parent, String petDiet, String health, String changingHabits, Photo photo, Boolean isCorrect) {
        this.id = id;
        this.reportDate = reportDate;
        this.pet = pet;
        this.parent = parent;
        this.petDiet = petDiet;
        this.health = health;
        this.changingHabits = changingHabits;
        this.photo = photo;
        this.isCorrect = isCorrect;
    }

    public Report() {
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

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
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
                ", parent=" + parent +
                ", petDiet='" + petDiet + '\'' +
                ", health='" + health + '\'' +
                ", changingHabits='" + changingHabits + '\'' +
                ", photo=" + photo +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
