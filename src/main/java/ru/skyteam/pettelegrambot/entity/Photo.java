package ru.skyteam.pettelegrambot.entity;

import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_id")
    private String fileId;


    public Photo(String fileId) {
        this.fileId = fileId;
    }

    public Photo() {
    }

    public Photo(Long id, String fileId) {
        this.id = id;
        this.fileId = fileId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}
