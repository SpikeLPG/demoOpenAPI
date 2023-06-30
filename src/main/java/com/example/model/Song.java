package com.example.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record Song(@NotBlank long id, @NotBlank String title, @NotBlank String composer, List<Performer> performers) {
}
