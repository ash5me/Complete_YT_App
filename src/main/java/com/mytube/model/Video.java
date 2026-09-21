package com.mytube.model;

import java.time.LocalDate;

public class Video {

    private String id;
    private String title;
    private String thumbnail;
    private String videoUrl;
    private String description;
    private long views;
    private LocalDate uploadDate;
    private VideoCategory category;

    public Video() {
    }

    public Video(String id, String title, String thumbnail, String videoUrl,
                 String description, long views, LocalDate uploadDate, VideoCategory category) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.videoUrl = videoUrl;
        this.description = description;
        this.views = views;
        this.uploadDate = uploadDate;
        this.category = category;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public VideoCategory getCategory() {
        return category;
    }

    public void setCategory(VideoCategory category) {
        this.category = category;
    }
}