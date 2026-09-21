package com.mytube.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VideoCategory {
    TUTORIAL("Tutorial"),
    VLOG("Vlog"),
    REVIEW("Review");

    private final String label;

    VideoCategory(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    public static VideoCategory fromString(String text) {
        for (VideoCategory category : VideoCategory.values()) {
            if (category.label.equalsIgnoreCase(text)) {
                return category;
            }
        }
        return null;
    }
}