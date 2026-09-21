package com.mytube.service;

import com.mytube.model.Video;
import com.mytube.model.VideoCategory;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VideoUtils {

    public static String formatViews(long views) {
        if (views >= 1_000_000) {
            return String.format("%.1fM", views / 1_000_000.0);
        }
        if (views >= 1_000) {
            return String.format("%.1fK", views / 1_000.0);
        }
        return String.valueOf(views);
    }

    public static String timeAgo(LocalDate date, LocalDate now) {
        Period period = Period.between(date, now);

        if (period.getYears() > 0) {
            int years = period.getYears();
            return years + (years == 1 ? " year ago" : " years ago");
        }
        if (period.getMonths() > 0) {
            int months = period.getMonths();
            return months + (months == 1 ? " month ago" : " months ago");
        }
        if (period.getDays() > 0) {
            int days = period.getDays();
            return days + (days == 1 ? " day ago" : " days ago");
        }
        return "Today";
    }

    public static String timeAgo(LocalDate date) {
        return timeAgo(date, LocalDate.now());
    }

    public static List<Video> filterAndSortVideos(List<Video> videos, String search, String category, String sort) {
        return videos.stream()
                .filter(video -> {
                    boolean matchesSearch = search == null || search.isBlank() ||
                            video.getTitle().toLowerCase().contains(search.toLowerCase());

                    VideoCategory cat = VideoCategory.fromString(category);
                    boolean matchesCategory = category == null || category.equalsIgnoreCase("All") ||
                            video.getCategory() == cat;

                    return matchesSearch && matchesCategory;
                })
                .sorted((a, b) -> {
                    if (sort == null) return 0;
                    return switch (sort.toLowerCase()) {
                        case "newest" -> b.getUploadDate().compareTo(a.getUploadDate());
                        case "oldest" -> a.getUploadDate().compareTo(b.getUploadDate());
                        case "views-high" -> Long.compare(b.getViews(), a.getViews());
                        default -> 0;
                    };
                })
                .collect(Collectors.toList());
    }
}