package com.example.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SongRequest(@NotBlank String title, @NotBlank String composer, List<Performer> performers) {
}