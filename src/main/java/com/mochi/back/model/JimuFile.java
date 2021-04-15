package com.mochi.back.model;

import java.time.LocalDateTime;

public class JimuFile {

    private String name;
    private LocalDateTime creationDate;
    private Long size;
    private String path;

    public JimuFile(String name, LocalDateTime creationDate, Long size) {
        this.name = name;
        this.creationDate = creationDate;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public JimuFile setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public JimuFile setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public JimuFile setSize(Long size) {
        this.size = size;
        return this;
    }
}
