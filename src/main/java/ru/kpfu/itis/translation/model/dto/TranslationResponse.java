package ru.kpfu.itis.translation.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.kpfu.itis.translation.model.dto.enums.LanguageCode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TranslationResponse(String translatedText, LanguageCode detectedLanguage) {}