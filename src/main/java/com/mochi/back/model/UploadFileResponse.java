package com.mochi.back.model;

public class UploadFileResponse {
    private String fileName;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileType, long size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public UploadFileResponse setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public UploadFileResponse setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public long getSize() {
        return size;
    }

    public UploadFileResponse setSize(long size) {
        this.size = size;
        return this;
    }
}
