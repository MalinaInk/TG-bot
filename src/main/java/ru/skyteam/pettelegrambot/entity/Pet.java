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

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private Volunteer volunteer;

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

    @Column(name = "date_of_end_report")
    private LocalDate dateOfEndReport;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_of_adoption")
    private StatusOfAdoption statusOfAdoption;

    public Pet() {
    }

    public Pet(Long id, Parent parent, Volunteer volunteer, PetType petType, String name, Shelter shelter, List<Report> reports,
               LocalDate dateOfAdoption, LocalDate dateOfEndReport, StatusOfAdoption statusOfAdoption) {
        this.id = id;
        this.parent = parent;
        this.volunteer = volunteer;
        this.petType = petType;
        this.name = name;
        this.shelter = shelter;
        this.reports = reports;
        this.dateOfAdoption = dateOfAdoption;
        this.statusOfAdoption = StatusOfAdoption.WITHOUT_PARENT;
        this.dateOfEndReport = dateOfEndReport;
        }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
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

    public LocalDate getDateOfEndReport() {
        return dateOfEndReport;
    }

    public void setDateOfEndReport(LocalDate dateOfEndReport) {
        this.dateOfEndReport = dateOfEndReport;
    }

    public StatusOfAdoption getStatusOfAdoption() {
        return statusOfAdoption;
    }

    public void setStatusOfAdoption(StatusOfAdoption statusOfAdoption) {
        this.statusOfAdoption = statusOfAdoption;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", parent=" + parent +
                ", volunteer=" + volunteer +
                ", petType=" + petType +
                ", name='" + name + '\'' +
                ", shelter=" + shelter +
                ", reports=" + reports +
                ", dateOfAdoption=" + dateOfAdoption +
                ", dateOfEndReport=" + dateOfEndReport +
                ", statusOfAdoption=" + statusOfAdoption +
                '}';
    }
}

