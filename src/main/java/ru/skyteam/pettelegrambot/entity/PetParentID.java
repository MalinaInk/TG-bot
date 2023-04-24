package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable

public class PetParentID implements Serializable {
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "pet_id")
    private Long petId;

    public PetParentID(Long parentId, Long petId) {
        this.parentId = parentId;
        this.petId = petId;
    }

    public PetParentID() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    @Override
    public String toString() {
        return "PetParentID{" +
                "parentId=" + parentId +
                ", petId=" + petId +
                '}';
    }
}
