package ru.kpfu.itis.translation.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.kpfu.itis.translation.model.dto.enums.LanguageCode;
import ru.kpfu.itis.translation.utils.SanitizationUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TranslationRequest(
    @NotBlank(message = "Text to translate cannot be blank") String textToTranslate,
    @NotNull(message = "Targeted language cannot be null") LanguageCode targetedLanguage) {

  public TranslationRequest toSanitizedRequest() {
    return new TranslationRequest(
        SanitizationUtils.getSanitizedString(textToTranslate), targetedLanguage);
  }
}
