
package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "photo")

public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "path_to_photo")
    private String pathToPhoto;

    public Photo(Long id, String pathToPhoto) {
        this.id = id;
        this.pathToPhoto = pathToPhoto;
    }

    public Photo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathToPhoto() {
        return pathToPhoto;
    }

    public void setPathToPhoto(String pathToPhoto) {
        this.pathToPhoto = pathToPhoto;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", pathToPhoto='" + pathToPhoto + '\'' +
                '}';
    }
}
