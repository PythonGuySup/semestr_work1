package ru.kpfu.itis.translation.model.dto;

import java.util.List;

public record YandexResponse(List<Translation> translations) {
  public record Translation(String text, String detectedLanguageCode) {}
}