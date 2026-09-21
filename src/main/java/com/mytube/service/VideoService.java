package com.mytube.service;

import com.mytube.model.Video;
import com.mytube.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> getVideos(String search, String category, String sort) {
        List<Video> allVideos = videoRepository.findAll();
        return VideoUtils.filterAndSortVideos(allVideos, search, category, sort);
    }

    public Optional<Video> getVideoById(String id) {
        return videoRepository.findById(id);
    }

    public List<Video> getSidebarVideos(String currentVideoId) {
        return videoRepository.findAll().stream()
                .filter(v -> !v.getId().equals(currentVideoId))
                .toList();
    }
}