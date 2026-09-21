package com.mytube.controller;

import com.mytube.model.Video;
import com.mytube.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*") // Allows cross-origin calls from frontend apps
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // GET /api/videos?search=...&category=...&sort=...
    @GetMapping
    public ResponseEntity<List<Video>> getVideos(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "All") String category,
            @RequestParam(required = false, defaultValue = "newest") String sort) {
        return ResponseEntity.ok(videoService.getVideos(search, category, sort));
    }

    // GET /api/videos/1
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        return videoService.getVideoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/videos/sidebar?excludeId=1
    @GetMapping("/sidebar")
    public ResponseEntity<List<Video>> getSidebarVideos(@RequestParam String excludeId) {
        return ResponseEntity.ok(videoService.getSidebarVideos(excludeId));
    }
}