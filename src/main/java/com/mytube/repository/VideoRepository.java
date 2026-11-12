package com.mytube.repository;

import com.mytube.model.Video;
import com.mytube.model.VideoCategory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class VideoRepository {

    private final List<Video> videos = new ArrayList<>(List.of(
        new Video(
            "1", 
            "Beach day vlog", 
            "/assets/images/photo-1507525428034-b723cf961d3e.jpg", 
            "assets/videos/waves.mp4", 
            "Spending a relaxing day down at the sunny beach! Relax and enjoy the sound of the ocean waves.", 
            154000L, 
            LocalDate.of(2023, 10, 1), 
            VideoCategory.TUTORIAL
        ),
        new Video(
            "2", 
            "Millionaire yatch vlog", 
            "/assets/images/photo-1567899378494-47b22a2ae96a.jpg", 
            "assets/videos/person.mp4", 
            "A full tour of a luxury yacht while exploring open waters. Behind the scenes of high life travel.", 
            89000L, 
            LocalDate.of(2023, 10, 15), 
            VideoCategory.VLOG
        ),
        new Video(
            "3", 
            "Skyscraper everyday fit", 
            "/assets/images/photo-1483985988355-763728e1935b.jpg", 
            "assets/videos/motorcycle.mp4", 
            "Reviewing urban streetwear and outfits suited for working in high-rise skyscraper offices.", 
            320000L, 
            LocalDate.of(2023, 11, 5), 
            VideoCategory.REVIEW
        ),
        new Video(
            "4", 
            "Life of an farmer", 
            "/assets/images/photo-1500382017468-9049fed747ef.jpg", 
            "assets/videos/honeybee.mp4", 
            "An early morning look into daily operations on a self-sustaining organic farm.", 
            45000L, 
            LocalDate.of(2023, 9, 20), 
            VideoCategory.TUTORIAL
        ),
        new Video(
            "5", 
            "Solo traveller guide", 
            "/assets/images/photo-1488646953014-85cb44e25828.jpg", 
            "assets/videos/squirrel.mp4", 
            "Essential safety and budget tips when traveling abroad completely on your own.", 
            112000L, 
            LocalDate.of(2023, 12, 1), 
            VideoCategory.VLOG
        ),
        new Video(
            "6", 
            "Dark forest alone in the dark?", 
            "/assets/images/photo-1448375240586-882707db888b.jpg", 
            "assets/videos/lamp.mp4", 
            "Nighttime gear test and review deep inside the quiet woods.", 
            67000L, 
            LocalDate.of(2023, 12, 10), 
            VideoCategory.REVIEW
        )
    ));

    public List<Video> findAll() {
        return videos;
    }

    public Optional<Video> findById(String id) {
        return videos.stream().filter(v -> v.getId().equals(id)).findFirst();
    }
}