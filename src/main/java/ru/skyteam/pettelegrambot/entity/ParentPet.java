package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

@Entity
@Table(name="parent_pets")

public class ParentPet {
    @EmbeddedId
    private PetParentID petParentID;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Parent parent;

    public ParentPet(PetParentID petParentID, Pet pet, Parent parent) {
        this.petParentID = petParentID;
        this.pet = pet;
        this.parent = parent;
    }

    public ParentPet() {
    }

    public PetParentID getPetParentID() {
        return petParentID;
    }

    public void setPetParentID(PetParentID petParentID) {
        this.petParentID = petParentID;
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

    @Override
    public String toString() {
        return "ParentPet{" +
                "petParentID=" + petParentID +
                ", pet=" + pet +
                ", parent=" + parent +
                '}';
    }
}
